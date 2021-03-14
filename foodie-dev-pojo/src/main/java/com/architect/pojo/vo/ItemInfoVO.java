package com.architect.pojo.vo;

import com.architect.pojo.Items;
import com.architect.pojo.ItemsImg;
import com.architect.pojo.ItemsParam;
import com.architect.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 * 商品详情VO
 *
 * @author 多宝
 * @since 2021/3/14 10:42
 */
@Data
public class ItemInfoVO {

    private Items item;

    private List<ItemsImg> itemImgList;

    private List<ItemsSpec> itemSpecList;

    private ItemsParam itemParams;

}
