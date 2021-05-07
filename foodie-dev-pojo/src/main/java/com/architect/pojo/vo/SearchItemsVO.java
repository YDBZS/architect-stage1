package com.architect.pojo.vo;

import lombok.Data;

/**
 * 查询商品列表的展示层对象
 *
 * @author 多宝
 * @since 2021/5/8 2:24
 */
@Data
public class SearchItemsVO {

    private String itemId;

    private String itemName;

    private Integer sellCounts;

    private String imgUrl;

    private Integer price;
}
