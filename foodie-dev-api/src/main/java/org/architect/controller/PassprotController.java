package org.architect.controller;

import com.architect.pojo.Users;
import com.architect.pojo.bo.ShopcartBO;
import com.architect.pojo.bo.UsersBO;
import com.architect.pojo.vo.UsersVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.service.UserService;
import org.architect.util.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户校验接口
 *
 * @author 多宝
 * @since 2021/3/13 15:34
 */
@Api(value = "用户接口", tags = {"用户接口"})
@RestController
@RequestMapping("passport")
public class PassprotController {

    @Resource
    private UserService userService;
    @Resource
    private RedisOperator redisOperator;
    @Resource
    private DistributedSessionUtil distributedSessionUtil;

    @ApiOperation("校验用户名是否存在")
    @GetMapping("usernameIsExist")
    public ReturnResult usernameIsExist(@RequestParam String username) {
        // 判断入参不能为空
        if (StringUtils.isEmpty(username)) {
            return ReturnResult.errorMsg("用户名不能为空");
        }
        // 查找注册的用户名是否存在
        boolean isexist = userService.queryUserNameIsExist(username);
        if (isexist) {
            return ReturnResult.errorMsg("用户名已经存在");
        }
        return ReturnResult.ok();
    }

    @PostMapping("/regist")
    @ApiOperation("用户注册")
    public ReturnResult save(@RequestBody UsersBO usersBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        String username = usersBO.getUsername();
        String password = usersBO.getPassword();
        String confirmPassword = usersBO.getConfirmPassword();

        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)
        ) {
            return ReturnResult.errorMsg("用户名或者密码为空");
        }
        boolean isexist = userService.queryUserNameIsExist(username);
        if (isexist) {
            return ReturnResult.errorMsg("用户名已经存在");
        }
        if (password.length() < 6) {
            return ReturnResult.errorMsg("密码长度不能小于6");
        }
        if (!password.equals(confirmPassword)) {
            return ReturnResult.errorMsg("两次密码输入不一致");
        }
        Users user = userService.createUser(usersBO);

//        Users setNull = setNull(user);
        UsersVO usersVO = distributedSessionUtil.convertUsersBO(user);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        return ReturnResult.ok(user);
    }

    @ApiOperation(value = "登录", httpMethod = "POST")
    @PostMapping("/login")
    public ReturnResult login(@RequestBody UsersBO usersBO,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        if (StringUtils.isBlank(usersBO.getUsername()) ||
                StringUtils.isBlank(usersBO.getPassword())
        ) {
            return ReturnResult.errorMsg("用户名或者密码为空");
        }
        Users users = userService.queryUserForLogin(usersBO.getUsername(),
                MD5Utils.getMD5Str(usersBO.getPassword()));
        if (null == users) {
            return ReturnResult.errorMsg("用户名或者密码错误");
        }

        // 实现用户的Redis会话。
        UsersVO usersVO = distributedSessionUtil.convertUsersBO(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        // 同步购物车数据
        syncShopCartData(users.getId(), request, response);

        return ReturnResult.ok(usersVO);
    }

    /**
     * 注册登录成功后，同步Cookie中的购物车数据到Redis
     *
     * @author 多宝
     * @since 2021/7/18 20:39
     */
    public void syncShopCartData(String userId,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        /**
         * 1、Redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
         * 2、Redis中无数据，如果cookie中的购物车不为空，此时直接放入Redis中
         * 3、redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         * 4、redis中有数据，如果cookie中的购物车不为空，如果cookie中的某个商品在redis中存在，则以cookie为主，删除redis中的，把cookie中的商品直接覆盖redis中(参考京东)
         * 5、同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         */
        // 1、从Redis中获取购物车
        String shopCartJsonRedis = redisOperator.get(Constant.FOODIE_SHOPCART + ":" + userId);


        // 2、从cookie中获取购物车
        String shopCartStrCookie = CookieUtils.getCookieValue(request, Constant.FOODIE_SHOPCART, true);
        if (StringUtils.isBlank(shopCartJsonRedis)) {
            // redis为空，cookie不为空，直接把Coockie中的数据放入Redis
            if (StringUtils.isNotBlank(shopCartStrCookie)) {
                redisOperator.set(Constant.FOODIE_SHOPCART + ":" + userId, shopCartStrCookie);
            }
        } else {
            // redis不为空，cookie不为空，合并cookie和redis中的商品数据（同一商品则覆盖redis）
            if (StringUtils.isNotBlank(shopCartStrCookie)) {
                // 1、已经存在的，把Cookie中对应的数量，覆盖Redis中(参考京东)
                // 2、该项商品标记为待删除，统一放入一个待删除的list中，
                // 3、从cookie中清除所有待删除的list，
                // 4、合并Redis和Cookie中的数据
                // 5、更新到redis和cookie中
                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopCartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopCartStrCookie, ShopcartBO.class);

                // 定义一个待删除List
                List<ShopcartBO> pendingDeleteList = Lists.newArrayList();

                for (ShopcartBO redisShopcart : shopcartListRedis) {
                    String redisSpecId = redisShopcart.getSpecId();
                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
                        String cookieSpecId = cookieShopcart.getSpecId();
                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，参考京东
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            // 把cookieShopcart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShopcart);
                        }
                    }
                }
                // 从现有cookie中删除对应的覆盖过的商品数据
                shopcartListCookie.removeAll(pendingDeleteList);

                // 合并两个List
                shopcartListRedis.addAll(shopcartListCookie);
                // 更新到Redis和Cookie
                CookieUtils.setCookie(request, response, Constant.FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
                redisOperator.set(Constant.FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));
            } else {
                // redis不为空，cookie为空，直接把Redis覆盖cookie
                CookieUtils.setCookie(request, response, Constant.FOODIE_SHOPCART, shopCartJsonRedis, true);
            }
        }
    }


    /**
     * 设置敏感信息
     *
     * @param users 用户信息
     * @return com.architect.pojo.Users
     * @since 2021/3/13 19:56
     */
    public Users setNull(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
        return users;
    }

    @ApiOperation(value = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public ReturnResult logout(@RequestParam String userId,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        // 清除用户相关的cookie信息
        CookieUtils.deleteCookie(request, response, "user");

        // 用户推出登录，需要清空购物车
        // 清除前端存储购物车信息
        CookieUtils.deleteCookie(request, response, Constant.FOODIE_SHOPCART);
        // 清除后端购物车信息

        // 用户退出登录，需要清除分布式会话中的用户数据
        redisOperator.del(Constant.REDIS_USER_TOKEN + ":" + userId);

        return ReturnResult.ok();
    }


}
