package com.architect.pojo.vo;

import lombok.Data;

/**
 * 这是用于展示商品评价数的VO
 *
 * @author 多宝
 * @since 2021/3/14 12:05
 */
@Data
public class CommentLevelCountVO {

    private Integer totalCounts;

    private Integer goodCounts;

    private Integer normalCounts;

    private Integer badCounts;

}
