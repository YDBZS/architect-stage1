package org.architect.controller.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.util.JsonUtils;
import org.architect.util.RedisOperator;
import org.architect.util.ReturnResult;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 用户会话信息拦截器
 *
 * @author 多宝
 * @since 2021/8/15 12:36
 */
public class UserTokenInterceptor implements HandlerInterceptor {

    @Resource
    private RedisOperator redisOperator;

    /**
     * 这是一个拦截的请求，就是说当用户的请求请求到接口了以后，那么在调用Controller之前，所需要去进行的一层处理。
     * <p>
     * 拦截请求，在访问Controller调用之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerUserToken = request.getHeader("headerUserToken");
        String userId = request.getHeader("headerUserId");

        if (StringUtils.isNotBlank(headerUserToken) && StringUtils.isNotBlank(userId)) {
            String uniqueToken = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(uniqueToken)) {
                returnErrorResponse(response, ReturnResult.errorMsg("请登录"));
                return false;
            } else {
                if (!uniqueToken.equals(headerUserToken)) {
                    returnErrorResponse(response, ReturnResult.errorMsg("账号在异地登录...."));
                    return false;
                }
            }

        } else {
            returnErrorResponse(response, ReturnResult.errorMsg("请登录"));
            return false;
        }


        /**
         * false：代表请求被拦截，被驳回，验证出现问题
         * true：请求在经过验证校验以后，是OK的，是可以放行的
         */
        return true;
    }

    /**
     * 设置拦截器拦截之后返回的错误信息
     *
     * @param response 结果返回
     * @param result   返回JSON信息
     * @author 多宝
     * @since 2021/8/15 13:08
     */
    public void returnErrorResponse(HttpServletResponse response, ReturnResult result) {
        OutputStream outputStream = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            outputStream = response.getOutputStream();
            outputStream.write(JsonUtils.objectToJson(result).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 请求访问Controller之后，渲染视图之前(因为是存在渲染视图的过程，渲染视图就是数据的渲染，渲染到页面上)，渲染之前是会进入到postHandle
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 请求访问Controller之后，渲染视图之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
