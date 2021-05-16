package org.architect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.architect.service.AddressService;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 地址相关接口
 *
 * @author 多宝
 * @since 2021/5/16 15:10
 */
@Api(value = "地址相关", tags = {"地址相关的接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Resource
    AddressService addressService;

    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1、查询用户的所有收货地址列表
     * 2、新增收货地址
     * 3、删除收货地址
     * 4、修改收货地址
     * 5、设置默认收货地址
     */

    @ApiOperation(value = "根据用户ID查询收货地址列表", notes = "根据用户ID查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public ReturnResult list(
            @RequestParam String userId
    ) {

        if (StringUtils.isBlank(userId)) {
            return ReturnResult.errorMsg("");
        }

        return ReturnResult.ok(addressService.queryAll(userId));
    }


}
