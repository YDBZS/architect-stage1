package org.architect.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 二级分类的VO
 *
 * @author 多宝
 * @since 2021/3/14 9:42
 */
@Data
public class CategoryVO {

    private Integer id;

    private String name;

    private String type;

    private Integer fatherId;

    // 三级分类VOList
    private List<SubCategoryVO> subCatList;
}
