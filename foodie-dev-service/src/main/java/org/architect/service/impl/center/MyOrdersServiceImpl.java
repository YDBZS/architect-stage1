package org.architect.service.impl.center;

import com.architect.mapper.OrderStatusMapper;
import com.architect.mapper.OrdersMapper;
import com.architect.mapper.OrdersMapperCustom;
import com.architect.pojo.OrderStatus;
import com.architect.pojo.Orders;
import com.architect.pojo.vo.MyOrdersVO;
import com.architect.pojo.vo.OrderStatusCountsVO;
import com.architect.util.PageUtil;
import com.github.pagehelper.PageHelper;
import org.architect.enums.OrderStatusEnum;
import org.architect.enums.YesOrNo;
import org.architect.service.center.MyOrdersService;
import org.architect.util.PagedGridResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 多宝
 * @since 2021/6/3 20:52
 */
@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    @Resource
    private OrdersMapperCustom ordersMapperCustom;
    @Resource
    private OrderStatusMapper orderStatusMapper;
    @Resource
    private OrdersMapper ordersMapper;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("userId", userId);
        if (null != orderStatus) {
            map.put("orderStatus", orderStatus);
        }
        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(map);
        return PageUtil.setterPagedGrid(list, page);
    }

    @Override
    public void updateDeliverOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.NO.type);

        return ordersMapper.selectOne(orders);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateReceiveOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        updateOrder.setSuccessTime(new Date());
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);

        int result = orderStatusMapper.updateByExampleSelective(updateOrder, example);
        return result == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean deleteOrder(String userId, String orderId) {
        Orders updateOrder = new Orders();
        updateOrder.setIsDelete(YesOrNo.YES.type);
        updateOrder.setUpdatedTime(new Date());
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);
        criteria.andEqualTo("userId", userId);

        int result = ordersMapper.updateByExampleSelective(updateOrder, example);
        return result == 1;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {
        Map<String, Object> map = new HashMap<>(8);
        map.put("userId", userId);
        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);

        Integer waitPayCounts = ordersMapperCustom.getMyorderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        Integer waitDeliverCounts = ordersMapperCustom.getMyorderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        Integer waitRecieveCounts = ordersMapperCustom.getMyorderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNo.NO.type);
        Integer waitCommentCounts = ordersMapperCustom.getMyorderStatusCounts(map);

        return OrderStatusCountsVO.builder()
                .waitPayCounts(waitPayCounts)
                .waitDeliverCounts(waitDeliverCounts)
                .waitReceiveCounts(waitRecieveCounts)
                .waitCommentCounts(waitCommentCounts)
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);

        List<OrderStatus> list = ordersMapperCustom.getMyOrderTrend(map);
        return PageUtil.setterPagedGrid(list, page);
    }
}
