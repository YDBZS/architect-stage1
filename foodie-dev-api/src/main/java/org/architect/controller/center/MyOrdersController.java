package org.architect.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.service.center.MyOrdersService;
import org.architect.util.CheckUserOrderUtil;
import org.architect.util.PagedGridResult;
import org.architect.util.ReturnResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 我的订单接口
 *
 * @author 多宝
 * @since 2021/6/3 21:24
 */
@Slf4j
@RestController
@RequestMapping("myorders")
@Api(value = "用户中心我的订单", tags = {"用户中心我的订单"})
public class MyOrdersController {

    @Resource
    private MyOrdersService myOrdersService;
    @Resource
    private CheckUserOrderUtil checkUserOrderUtil;

    @PostMapping("/query")
    @ApiOperation(value = "查询订单列表", httpMethod = Constant.INTERFACE_METHOD_POST)
    public ReturnResult query(
            @ApiParam(value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(value = "订单状态")
            @RequestParam Integer orderStatus,
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(userId)) {
            return ReturnResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = Constant.COMMENT_PAGE_SIZE;
        }
        PagedGridResult gridResult = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return ReturnResult.ok(gridResult);
    }

    /**
     * 商家发货没有后端，所以这个接口仅仅只是用于模拟
     */
    @ApiOperation(value = "商家发货", notes = "商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public ReturnResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        if (StringUtils.isBlank(orderId)) {
            return ReturnResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return ReturnResult.ok();
    }

    @PostMapping("/confirmReceive")
    @ApiOperation(value = "用户确认收货", httpMethod = Constant.INTERFACE_METHOD_POST)
    public ReturnResult confirmReceive(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId
    ) {
        ReturnResult order = checkUserOrderUtil.checkUserOrder(orderId, userId);
        if (order.getStatus() != HttpStatus.OK.value()) {
            return order;
        }
        Boolean result = myOrdersService.updateReceiveOrderStatus(orderId);

        if (!result) {
            return ReturnResult.errorMsg("订单确认收货失败！");
        }
        return ReturnResult.ok();
    }



    @PostMapping("/delete")
    @ApiOperation(value = "用户删除订单", httpMethod = Constant.INTERFACE_METHOD_POST)
    public ReturnResult delete(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId
    ) {
        if (checkUserOrderUtil.checkUserOrder(orderId, userId).getStatus() != HttpStatus.OK.value()) {
            return checkUserOrderUtil.checkUserOrder(orderId, userId);
        }
        Boolean result = myOrdersService.deleteOrder(userId, orderId);
        if (!result) {
            return ReturnResult.errorMsg("订单删除失败！");
        }

        return ReturnResult.ok();
    }


}
