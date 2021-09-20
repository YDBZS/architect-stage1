package org.architect.pojo.vo;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author G-Dragon
 * 订单状态概览数量VO 
 */
@Data
@SuperBuilder
public class OrderStatusCountsVO {

    private Integer waitPayCounts;
    private Integer waitDeliverCounts;
    private Integer waitReceiveCounts;
    private Integer waitCommentCounts;

}