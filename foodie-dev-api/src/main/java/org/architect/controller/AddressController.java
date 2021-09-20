package org.architect.controller;

import org.architect.pojo.bo.AddressBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.service.AddressService;
import org.architect.util.MobileEmailUtils;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public ReturnResult add(
            @RequestBody AddressBO addressBO
    ) {
        ReturnResult checkAddress = checkAddress(addressBO);
        if (!checkAddress.getStatus().equals(Constant.STATUS_OK)) {
            return checkAddress;
        }
        addressService.addNewUserAddress(addressBO);
        return ReturnResult.ok();
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public ReturnResult upodate(
            @RequestBody AddressBO addressBO
    ) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return ReturnResult.errorMsg("地址ID不能为空");
        }

        ReturnResult checkAddress = checkAddress(addressBO);
        if (!checkAddress.getStatus().equals(Constant.STATUS_OK)) {
            return checkAddress;
        }
        addressService.updateUserAddress(addressBO);
        return ReturnResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public ReturnResult delete(
            @RequestParam String userId,
            @RequestParam String addressId
    ) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ReturnResult.errorMsg("");
        }
        addressService.deleteUserAddress(userId, addressId);
        return ReturnResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefault")
    public ReturnResult setDefault(
            @RequestParam String userId,
            @RequestParam String addressId
    ) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ReturnResult.errorMsg("");
        }
        addressService.updateUserAddressToBeDefault(userId, addressId);
        return ReturnResult.ok();
    }

    private ReturnResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return ReturnResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return ReturnResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return ReturnResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return ReturnResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return ReturnResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return ReturnResult.errorMsg("收货地址信息不能为空");
        }

        return ReturnResult.ok();
    }

}
