package org.architect.service.impl.center;

import org.architect.mapper.ItemsCommentsMapperCustom;
import org.architect.mapper.OrderItemsMapper;
import org.architect.mapper.OrderStatusMapper;
import org.architect.mapper.OrdersMapper;
import org.architect.pojo.OrderItems;
import org.architect.pojo.OrderStatus;
import org.architect.pojo.Orders;
import org.architect.pojo.bo.OrderItemsCommentBO;
import org.architect.pojo.vo.MyCommentVO;
import org.architect.util.PageUtil;
import com.github.pagehelper.PageHelper;
import org.architect.enums.YesOrNo;
import org.architect.service.center.MyCommentsService;
import org.architect.util.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 多宝
 * @since 2021/6/4 6:38
 */
@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    @Resource
    private OrderItemsMapper orderItemsMapper;
    @Resource
    private Sid sid;
    @Resource
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrderStatusMapper orderStatusMapper;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public List<OrderItems> queryPendingComments(String orderId) {
        OrderItems query = new OrderItems();
        query.setOrderId(orderId);
        return orderItemsMapper.select(query);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {
        // 1、保存评价 items_comments
        commentList.forEach(item -> {
            item.setCommentId(sid.nextShort());
        });
        Map<String, Object> map = new HashMap<>(9);
        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapperCustom.saveComments(map);

        // 2、修改订单表改为已评价 orders
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);

        // 3、修改订单状态表的留言时间   order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapperCustom.queryMyComments(map);
        return PageUtil.setterPagedGrid(list, page);
    }
}
