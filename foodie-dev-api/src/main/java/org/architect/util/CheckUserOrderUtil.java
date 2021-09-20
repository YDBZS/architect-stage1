package org.architect.util;

import org.architect.pojo.Orders;
import org.architect.service.center.MyOrdersService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 校验用户订单工具类
 *
 * @author 多宝
 * @since 2021/6/4 6:49
 */
@Component
public class CheckUserOrderUtil {

    @Resource
    private MyOrdersService myOrdersService;

    /**
     * 用于校验用户和订单是否有关联关系，避免非法用户调用
     *
     * @param orderId 订单ID
     * @param userId  用户ID
     * @return org.architect.util.ReturnResult
     * @author 多宝
     * @since 2021/6/4 5:57
     */
    public ReturnResult checkUserOrder(String orderId, String userId) {
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            return ReturnResult.errorMsg("订单不存在");
        }
        return ReturnResult.ok(orders);
    }

}
