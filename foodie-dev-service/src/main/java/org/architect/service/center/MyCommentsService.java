package org.architect.service.center;

import org.architect.pojo.OrderItems;
import org.architect.pojo.bo.OrderItemsCommentBO;
import org.architect.util.PagedGridResult;

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

    /**
     * 分页查询我的评价
     *
     * @param userId    用户ID
     * @param page      当前页
     * @param pageSize  每页条数
     * @return org.architect.util.PagedGridResult
     * @author 多宝
     * @since 2021/6/5 17:58
     */
    PagedGridResult queryMyComments(String userId,Integer page,Integer pageSize);

}
