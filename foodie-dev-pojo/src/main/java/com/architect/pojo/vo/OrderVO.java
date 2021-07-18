package com.architect.pojo.vo;

import com.architect.pojo.bo.ShopcartBO;
import lombok.Data;

import java.util.List;

/**
 * @author 多宝
 * @since 2021/5/23 16:48
 */
@Data
public class OrderVO {

    private String orderId;

    private MerchantOrdersVO merchantOrdersVO;

    private List<ShopcartBO> toBeRemovedShopcartList;

}
