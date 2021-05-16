package org.architect.controller;

import com.architect.pojo.Users;
import com.architect.pojo.bo.UsersBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.architect.service.UserService;
import org.architect.util.CookieUtils;
import org.architect.util.JsonUtils;
import org.architect.util.MD5Utils;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        Users setNull = setNull(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(setNull), true);

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

        Users setNull = setNull(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(setNull), true);

        // TODO 生成用户Token，存入redis会话
        // TODO 同步购物车数据

        return ReturnResult.ok(setNull);
    }

    /**
     * 设置敏感信息
     *
     * @param users 用户信息
     * @return com.architect.pojo.Users
     * @since 2021/3/13 19:56
     */
    private Users setNull(Users users) {
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

        // TODO 用户推出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据

        return ReturnResult.ok();
    }

}
