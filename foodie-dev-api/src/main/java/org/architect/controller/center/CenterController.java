package org.architect.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.architect.constant.Constant;
import org.architect.service.center.CenterUserService;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 个人中心接口
 *
 * @author 多宝
 * @since 2021/5/23 20:05
 */
@Slf4j
@RestController
@RequestMapping("center")
@Api(value = "用户中心接口", tags = {"用户中心接口"})
public class CenterController {

    @Resource
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", httpMethod = Constant.INTERFACE_METHOD_GET)
    @GetMapping("/userInfo")
    public ReturnResult userInfo(@RequestParam
         @ApiParam(name = "userId", value = "用户ID", required = true)
         String userId) {
        return ReturnResult.ok(centerUserService.queryUserInfo(userId));
    }

}
