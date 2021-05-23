package com.architect.pojo.bo;

import lombok.Data;

/**
 * 用于创建订单的BO对象
 *
 * @author 多宝
 * @since 2021/5/22 15:00
 */
@Data
public class SubmitOrderBo {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
}
