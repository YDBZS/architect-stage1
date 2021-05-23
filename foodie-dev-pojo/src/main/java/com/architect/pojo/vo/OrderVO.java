package com.architect.pojo.vo;

import lombok.Data;

/**
 * @author 多宝
 * @since 2021/5/23 16:48
 */
@Data
public class OrderVO {

    private String orderId;

    private MerchantOrdersVO merchantOrdersVO;

}
