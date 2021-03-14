package org.architect.service;

import com.architect.pojo.Category;
import com.architect.pojo.vo.CategoryVO;
import com.architect.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * 商品分类
 *
 * @author 多宝
 * @since 2021/3/14 9:12
 */
public interface CategoryService {

    /**
     * 查询所有一级分类
     *
     * @since 2021/3/14 9:14
     * @return java.util.List<com.architect.pojo.Category>
     */
    List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类ID查询子分类
     *
     * @since 2021/3/14 9:57
     * @return java.util.List<com.architect.pojo.vo.CategoryVO>
     * @param rootCatId 一级分类ID
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页每个一级分类下的六条最新商品数据
     *
     * @since 2021/3/14 10:30_
     * @return java.util.List
     * @param rootCatId 一级分类ID
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
