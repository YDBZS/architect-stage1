package org.architect.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 最新商品VO
 *
 * @author 多宝
 * @since 2021/3/14 9:46
 */
@Data
public class NewItemsVO {

    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;

    private List<SimpleItemVO> simpleItemList;

}
