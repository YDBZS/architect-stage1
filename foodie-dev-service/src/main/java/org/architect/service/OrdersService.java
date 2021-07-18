package org.architect.service;

import com.architect.pojo.OrderStatus;
import com.architect.pojo.bo.ShopcartBO;
import com.architect.pojo.bo.SubmitOrderBo;
import com.architect.pojo.vo.OrderVO;

import java.util.List;

/**
 * 订单接口
 *
 * @author 多宝
 * @since 2021/5/22 15:13
 */
public interface OrdersService {

    /**
     * 创建订单
     *
     * @param submitOrderBo 订单信息
     * @since 2021/5/22 15:15
     */
    OrderVO createOrder(List<ShopcartBO> list, SubmitOrderBo submitOrderBo);

    /**
     * 修改订单状态
     *
     * @param orderId     订单ID
     * @param orderStatus 订单状态
     * @since 2021/5/23 16:27
     */
    void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 轮询查询订单状态
     *
     * @param orderId 订单ID
     * @return com.architect.pojo.OrderStatus
     * @author 多宝
     * @since 2021/5/23 18:25
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     *
     * @author 多宝
     * @since 2021/5/23 19:28
     */
    void closeOrder();

}
