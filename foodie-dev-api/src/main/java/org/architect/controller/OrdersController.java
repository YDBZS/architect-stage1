package org.architect.controller;

import com.architect.pojo.bo.ShopcartBO;
import com.architect.pojo.bo.SubmitOrderBo;
import com.architect.pojo.vo.MerchantOrdersVO;
import com.architect.pojo.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.enums.OrderStatusEnum;
import org.architect.enums.PayMethod;
import org.architect.service.OrdersService;
import org.architect.util.CookieUtils;
import org.architect.util.JsonUtils;
import org.architect.util.RedisOperator;
import org.architect.util.ReturnResult;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单接口
 *
 * @author 多宝
 * @since 2021/5/22 14:57
 */
@Slf4j
@Api(value = "订单", tags = {"订单"})
@RestController
@RequestMapping("orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public ReturnResult list(
            @RequestBody SubmitOrderBo submitOrderBo,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (null == PayMethod.getValueByType(submitOrderBo.getPayMethod())) {
            return ReturnResult.errorMsg("支付方式不支持！");
        }

        String shopCartJson = redisOperator.get(Constant.FOODIE_SHOPCART + ":" + submitOrderBo.getUserId());
        if (StringUtils.isBlank(shopCartJson)) {
            return ReturnResult.errorMsg("购物车数据不正确");
        }
        List<ShopcartBO> list = JsonUtils.jsonToList(shopCartJson, ShopcartBO.class);


        // 1、创建订单
        OrderVO orderVO = ordersService.createOrder(list, submitOrderBo);
        String orderId = orderVO.getOrderId();

        // 2、创建订单以后，移除购物车中已结算(已提交)的商品
        // 清理覆盖现有的redis中的购物车数据
        list.removeAll(orderVO.getToBeRemovedShopcartList());
        redisOperator.set(Constant.FOODIE_SHOPCART + ":" + submitOrderBo.getUserId(), JsonUtils.objectToJson(list));
        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, Constant.FOODIE_SHOPCART, JsonUtils.objectToJson(list), true);


        // 3、将商户订单的数据向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(Constant.PAY_RETURN_URL);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("imoocUserId", "imooc");
        httpHeaders.add("password", "imooc");

        // 调用外部接口
        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, httpHeaders);

        ResponseEntity<ReturnResult> responseEntity = restTemplate.postForEntity(Constant.PAYMENTURL, entity, ReturnResult.class);
        ReturnResult result = responseEntity.getBody();
        assert null != result;
        if (result.getStatus() != HttpStatus.OK.value()) {
            return ReturnResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }
        return ReturnResult.ok(orderId);

    }

    @PostMapping("notifyMerchantOrderPaid")
    @ApiOperation(value = "支付回调通知接口", httpMethod = Constant.INTERFACE_METHOD_POST)
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        ordersService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "轮询查询订单支付状态", httpMethod = Constant.INTERFACE_METHOD_POST)
    @PostMapping("/getPaidInfo")
    public ReturnResult getPaidInfo(String orderId) {
        return ReturnResult.ok(ordersService.queryOrderStatusInfo(orderId));
    }


}
