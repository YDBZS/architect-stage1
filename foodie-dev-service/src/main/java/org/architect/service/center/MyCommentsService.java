package org.architect.service.center;

import com.architect.pojo.OrderItems;
import com.architect.pojo.bo.OrderItemsCommentBO;

import java.util.List;

/**
 * 用户中心评论Service
 *
 * @author 多宝
 * @since 2021/5/23 20:12
 */
public interface MyCommentsService {

    /**
     * 根据订单ID查询关联的商品
     *
     * @param orderId 订单ID
     * @return java.util.List<com.architect.pojo.OrderItems>
     * @author 多宝
     * @since 2021/6/4 6:40
     */
    List<OrderItems> queryPendingComments(String orderId);

    /**
     * 保存用户的评论
     *
     * @param orderId     订单ID
     * @param userId      用户ID
     * @param commentList 评价集合
     * @author 多宝
     * @since 2021/6/4 7:33
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);

}
