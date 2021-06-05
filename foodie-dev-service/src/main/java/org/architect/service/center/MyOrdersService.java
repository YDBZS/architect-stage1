package org.architect.service.center;

import com.architect.pojo.Orders;
import com.architect.pojo.vo.OrderStatusCountsVO;
import org.architect.util.PagedGridResult;

/**
 * 个人中心
 *
 * @author 多宝
 * @since 2021/5/23 20:12
 */
public interface MyOrdersService {

    /**
     * 查询我的订单列表
     *
     * @param userId      用户ID
     * @param orderStatus 订单状态
     * @param page        当前页面
     * @param pageSize    每页条数
     * @return org.architect.util.PagedGridResult
     * @author 多宝
     * @since 2021/6/3 20:54
     */
    PagedGridResult queryMyOrders(String userId,
                                  Integer orderStatus,
                                  Integer page,
                                  Integer pageSize);


    /**
     * 模拟商家发货
     *
     * @param orderId 订单ID
     * @author 多宝
     * @since 2021/6/3 21:53
     */
    void updateDeliverOrderStatus(String orderId);

    /**
     * 查询我的订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return com.architect.pojo.Orders
     * @author 多宝
     * @since 2021/6/4 5:53
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 更新订单状态 -> 确认收货
     *
     * @param orderId 订单ID
     * @return java.lang.Boolean
     * @author 多宝
     * @since 2021/6/4 6:05
     */
    Boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单(逻辑删除)
     *
     * @param userId    用户ID
     * @param orderId   订单ID
     * @return java.lang.Boolean
     * @author 多宝
     * @since 2021/6/4 6:06
     */
    Boolean deleteOrder(String userId, String orderId);

    /**
     * 根据订单状态查询用户订单数
     *
     * @param userId    用户ID
     * @return java.lang.Integer
     * @author 多宝
     * @since 2021/6/5 19:02
     */
    OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 查询我的订单动向
     *
     * @param userId    用户ID
     * @param page      当前查询页面
     * @param pageSize  每页条数
     * @return org.architect.util.PagedGridResult
     * @author 多宝
     * @since 2021/6/5 19:30
     */
    PagedGridResult getOrdersTrend(String userId,
                                   Integer page,
                                   Integer pageSize);



}
