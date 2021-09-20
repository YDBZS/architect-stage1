package org.architect.pojo.vo;

import lombok.Data;

/**
 * @author 多宝
 * @since 2021/5/16 11:39
 */
@Data
public class ShopcartVO {

    private String itemId;

    private String itemImgUrl;

    private String itemName;

    private String specId;

    private String specName;

    private String priceDiscount;

    private String priceNormal;

}
