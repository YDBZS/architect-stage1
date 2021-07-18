package com.architect.pojo.bo;

import lombok.Data;

/**
 * @author 多宝
 * @since 2021/5/16 11:39
 */
@Data
public class ShopcartBO {

    private String itemId;

    private String itemImgUrl;

    private String itemName;

    private String specId;

    private String specName;

    private Integer buyCounts;

    private String priceDiscount;

    private String priceNormal;

}
