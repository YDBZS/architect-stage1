package org.architect.controller.center;

import com.architect.pojo.Users;
import com.architect.pojo.bo.center.CenterUserBO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.architect.constant.Constant;
import org.architect.controller.PassprotController;
import org.architect.service.center.CenterUserService;
import org.architect.util.CookieUtils;
import org.architect.util.JsonUtils;
import org.architect.util.ReturnResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户中心接口
 *
 * @author 多宝
 * @since 2021/5/23 20:46
 */
@Slf4j
@RestController
@RequestMapping("userInfo")
@Api(value = "用户信息接口", tags = {"用户信息接口"})
public class CenterUserController {

    @Resource
    private CenterUserService centerUserService;
    @Resource
    private PassprotController passprotController;

    @PostMapping("/update")
    @ApiOperation(value = "用户修改账户信息", httpMethod = Constant.INTERFACE_METHOD_POST)
    public ReturnResult update(@RequestParam
                               @ApiParam(name = "userId", value = "用户ID", required = true)
                                       String userId, @RequestBody @Valid CenterUserBO centerUserBO,
                               BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {

        // 判断BindingResult是否包含错误的验证信息，如果有则直接return就可以了
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return ReturnResult.errorMap(map);
        }

        Users userInfo = centerUserService.updateUserInfo(userId, centerUserBO);
        Users setNull = passprotController.setNull(userInfo);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(setNull), true);

        // todo 后续要改，增加令牌tocken，会整合进redis，分布式会话

        return ReturnResult.ok();
    }

    /**
     * 处理validator错误信息
     *
     * @param result 信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 多宝
     * @since 2021/5/29 19:52
     */
    private Map<String, String> getErrors(BindingResult result) {

        Map<String, String> map = Maps.newHashMap();

        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors) {
            // 发生验证错误所对应的某一个属性
            String field = error.getField();
            // 验证错误信息
            String message = error.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }

}
