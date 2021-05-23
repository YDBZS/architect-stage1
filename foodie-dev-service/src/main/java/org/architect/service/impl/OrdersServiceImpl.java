package org.architect.service.impl;

import com.architect.mapper.OrderItemsMapper;
import com.architect.mapper.OrderStatusMapper;
import com.architect.mapper.OrdersMapper;
import com.architect.pojo.*;
import com.architect.pojo.bo.SubmitOrderBo;
import com.architect.pojo.vo.MerchantOrdersVO;
import com.architect.pojo.vo.OrderVO;
import org.architect.constant.Constant;
import org.architect.enums.OrderStatusEnum;
import org.architect.enums.YesOrNo;
import org.architect.service.AddressService;
import org.architect.service.ItemService;
import org.architect.service.OrdersService;
import org.architect.util.DateUtil;
import org.architect.util.collection.ArchitectCollectionUtil;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 多宝
 * @since 2021/5/22 15:13
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    @Resource
    Sid sid;
    @Resource
    AddressService addressService;
    @Resource
    ItemService itemService;
    @Resource
    OrderItemsMapper orderItemsMapper;
    @Resource
    OrdersMapper ordersMapper;
    @Resource
    OrderStatusMapper orderStatusMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVO createOrder(SubmitOrderBo submitOrderBo) {
        String userId = submitOrderBo.getUserId();
        String addressId = submitOrderBo.getAddressId();
        String itemSpecIds = submitOrderBo.getItemSpecIds();
        Integer payMethod = submitOrderBo.getPayMethod();
        String leftMsg = submitOrderBo.getLeftMsg();

        // 包邮费用设置为0
        int postAmount = 0;
        String orderId = sid.nextShort();
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);

        // 1、新订单数据保存
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getProvince() + userAddress.getCity()
                + userAddress.getDistrict() + userAddress.getDetail());
//        orders.setTotalAmount();
//        orders.setRealPayAmount();
        orders.setPostAmount(postAmount);
        orders.setPayMethod(payMethod);
        orders.setLeftMsg(leftMsg);
        orders.setIsComment(YesOrNo.NO.type);
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());

        // 2、循环根据itemSpecIds保存订单商品信息
        String[] specIdList = itemSpecIds.split(",");
        int totalAmount = 0;    // 商品原价累计
        int realPayAmount = 0;  // 优惠后的实际支付价格累计
        for (String specId : specIdList) {
            // 2.1 根据规格ID，查询规格的具体信息，主要获取价格
            // todo 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;

            ItemsSpec itemsSpec = itemService.queryItemSpecById(specId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品ID获取商品信息以及商品图片
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            // 2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(items.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(specId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 2.4 在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(specId, buyCounts);
        }

        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(orders);

        // 3、保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        // 4、构建商户订单，用于传给支付中心
        MerchantOrdersVO ordersVO = new MerchantOrdersVO();
        ordersVO.setMerchantOrderId(orderId);
        ordersVO.setMerchantUserId(userId);
        ordersVO.setAmount(realPayAmount + postAmount);
        ordersVO.setPayMethod(payMethod);
        ordersVO.setReturnUrl(Constant.PAY_RETURN_URL);

        // 5、构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(ordersVO);

        return orderVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void closeOrder() {
        // 1、查询所有未付款订单，判断时间是否超时(1天)，超时则关闭交易
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrder);
        if (ArchitectCollectionUtil.isNotEmpty(list)) {
            list.forEach(os -> {
                // 获取订单创建时间
                Date createdTime = os.getCreatedTime();
                // 和当前时间进行对比
                int days = DateUtil.daysBetween(createdTime, new Date());
                if (days >= 1) {
                    // 超过一天，关闭订单
                    doCloseOrder(os.getOrderId());
                }
            });
        }
    }

    /**
     * 操作关闭订单
     *
     * @param orderId 订单ID
     * @author 多宝
     * @since 2021/5/23 19:35
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void doCloseOrder(String orderId) {
        OrderStatus status = new OrderStatus();
        status.setOrderId(orderId);
        status.setOrderStatus(OrderStatusEnum.CLOSE.type);
        status.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(status);
    }


}
