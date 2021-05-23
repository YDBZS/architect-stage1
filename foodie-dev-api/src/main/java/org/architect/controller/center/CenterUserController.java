package org.architect.controller.center;

import com.architect.pojo.Users;
import com.architect.pojo.bo.center.CenterUserBO;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
                                       String userId, @RequestBody CenterUserBO centerUserBO,
                               HttpServletRequest request, HttpServletResponse response) {
        Users userInfo = centerUserService.updateUserInfo(userId, centerUserBO);
        Users setNull = passprotController.setNull(userInfo);
        CookieUtils.setCookie(request, response,    "user", JsonUtils.objectToJson(setNull), true);

        // todo 后续要改，增加令牌tocken，会整合进redis，分布式会话

        return ReturnResult.ok();
    }

}
