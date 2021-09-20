package org.architect.sso.controller;

import org.architect.pojo.Users;
import org.architect.pojo.vo.UsersVO;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.service.UserService;
import org.architect.util.JsonUtils;
import org.architect.util.MD5Utils;
import org.architect.util.RedisOperator;
import org.architect.util.ReturnResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * SSO接口
 *
 * @author 多宝
 * @since 2021/8/15 14:54
 */
@Controller
public class SSOController {

    @Resource
    private UserService userService;
    @Resource
    private RedisOperator redisOperator;


    @GetMapping("login")
    public String login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        model.addAttribute("returnUrl", returnUrl);

        // 1、获取userTicket全局门票，如果cookie中能够获得，证明用户登陆过，此时签发一个一次性的临时票据，并且回跳
        String userTicket = getCookie(request, Constant.COOKIE_USER_TICKET);

        if (verifyUserTicket(userTicket)) {
            String tmpTicket = createTmpTicket();
            return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
        }

        // 2、用户从未登陆过，第一次进入则跳转到CAS的统一登陆页面

        return "login";
    }

    /**
     * 验证CAS全局用户门票
     *
     * @param userTicket 前端获取的全局用户门票
     * @return boolean
     * @author 多宝
     * @since 2021/8/15 18:06
     */
    private boolean verifyUserTicket(String userTicket) {
        // 0、验证CAS门票不能为空
        if (StringUtils.isBlank(userTicket)) {
            return false;
        }

        // 1、验证CAS门票是否有效
        String userId = redisOperator.get(Constant.REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        // 2、验证门票对应的user会话是否存在
        String userRedis = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)) {
            return false;
        }

        return true;
    }

    /**
     * CAS的统一登陆接口
     * 目的：
     * 1、登录后创建用户的全局会话           ->  uniqueToken
     * 2、创建用户全局门票                  ->  userTicket(目的就是为了表示当前这个用户是在CAS端是登录过的)
     * 3、创建用户的临时票据，用于回跳回传    ->  tmpTicket
     */
    @PostMapping("/doLogin")
    @SneakyThrows
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model) {
        model.addAttribute("returnUrl", returnUrl);
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)
        ) {
            model.addAttribute("errmsg", "用户名或者密码为空");
            return "login";
        }
        // 1、实现登录
        Users users = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));
        if (null == users) {
            model.addAttribute("errmsg", "用户名或者密码为空");
            return "login";
        }

        // 2、实现用户的Redis会话
        String uniqueToken = UUID.randomUUID().toString().trim();

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        redisOperator.set(Constant.REDIS_USER_TOKEN + ":" + users.getId(), JsonUtils.objectToJson(usersVO));

        // 3、生成ticket门票，全局门票，代表用户在CAS端登录过
        // trimapi可以保证字符串首尾两端是没有空格存在的
        String userTicket = UUID.randomUUID().toString().trim();

        // 3.1、用户全局门票需要放入CAS端的cookie中
//        CookieUtils.setCookie(request,response,Constant.COOKIE_USER_TICKET,userTicket);
        setCookie(Constant.COOKIE_USER_TICKET, userTicket, response);

        // 4、userTicket关联用户ID，并且放入到Redis中，代表这个用户有门票了，可以在各个系统无障碍操作，
        redisOperator.set(Constant.REDIS_USER_TICKET + ":" + userTicket, usersVO.getId());

        // 5、生成临时票据，回跳到调用端网址，是由CAS端所签发的一个一次性的临时ticket
        String tmpTicket = createTmpTicket();

        /**
         * userTicket：用于标识用户在CAS端登录状态：已经登录
         * tmpTicket：用于颁发给用户进行一次性的验证的票据，有失效性
         */

        /**
         * 举例：
         *      现在和女朋友去动物园玩耍，大门口买了一张统一的门票，这个就是CAS系统的全局门票和用户全局会话(userTicket)。
         *      动物园里有一些小的景点，需要凭你的门票去领取一次性的票据(tmpTicket)，有了这张票据以后就能去一些小的景点游玩了。
         *      这样一个个的小景点，其实就是我们这里所对应的一个个的网站(不同域名的网站)。
         *      当使用完毕这张临时票据了以后，就需要销毁。
         */

//        return "login";
        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }

    /**
     * 创建临时票据方法
     *
     * @return java.lang.String
     * @author 多宝
     * @since 2021/8/15 16:02
     */
    @SneakyThrows
    private String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();
        redisOperator.set(Constant.REDIS_TMP_TICKET + ":" + tmpTicket, MD5Utils.getMD5Str(tmpTicket), 600);
        return tmpTicket;
    }

    private void setCookie(String key,
                           String val,
                           HttpServletResponse response) {
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("verifyTmpTicket")
    @ResponseBody
    @SneakyThrows
    public ReturnResult verifyTmpTicket(String tmpTicket,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        // 使用一次性临时票据来验证用户是否登陆，如果登陆过，把用户会话信息返回给站点
        // 使用完毕后，需要销毁临时票据
        String tmpTicketValue = redisOperator.get(Constant.REDIS_TMP_TICKET + ":" + tmpTicket);
        if (StringUtils.isBlank(tmpTicketValue)) {
            return ReturnResult.errorUserTicket("用户的票据异常");
        }

        // 0、如果临时票据查询得到，则需要销毁，并且拿到CAS端cookie中的全局UserTicket，以此在获取用户会话
        if (!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))) {
            return ReturnResult.errorUserTicket("用户的票据异常");
        } else {
            // 销毁临时票据
            redisOperator.del(Constant.REDIS_TMP_TICKET + ":" + tmpTicket);
        }

        // 1、验证并且获取用户的userTicket
//        String userTicket = CookieUtils.getCookieValue(request, Constant.COOKIE_USER_TICKET);
        String userTicket = getCookie(request, Constant.COOKIE_USER_TICKET);
        String userId = redisOperator.get(Constant.REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return ReturnResult.errorUserTicket("用户票据异常");
        }

        // 2、验证门票对应的user会话是否存在
        String userRedis = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)) {
            return ReturnResult.errorUserTicket("用户票据异常");
        }

        // 验证成功，返回OK，携带用户会话

        return ReturnResult.ok(JsonUtils.jsonToPojo(userRedis, UsersVO.class));
    }


    @PostMapping("logout")
    @ResponseBody
    @SneakyThrows
    public ReturnResult logout(String userId,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        // 0、获取CAS中的用户门票
        String userTicket = getCookie(request, Constant.COOKIE_USER_TICKET);

        // 1、清除UserTicket票据，redis/cookie
        deleteCookie(Constant.COOKIE_USER_TICKET, response);
        redisOperator.del(Constant.REDIS_USER_TICKET + ":" + userTicket);

        // 2、清除用户全局会话(分布式会话)
        redisOperator.del(Constant.REDIS_USER_TOKEN + ":" + userId);

        return ReturnResult.ok();
    }

    private String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies || StringUtils.isBlank(key)) {
            return null;
        }

        String cookieValue = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                cookieValue = cookie.getValue();
                break;
            }
        }
        return cookieValue;
    }

    private void deleteCookie(String key,
                              HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

}
