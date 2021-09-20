package org.architect.service;

import org.architect.pojo.Items;
import org.architect.pojo.ItemsImg;
import org.architect.pojo.ItemsParam;
import org.architect.pojo.ItemsSpec;
import org.architect.pojo.vo.CommentLevelCountVO;
import org.architect.pojo.vo.ShopcartVO;
import org.architect.util.PagedGridResult;

import java.util.List;

/**
 * 商品
 *
 * @author 多宝
 * @since 2021/3/14 11:10
 */
public interface ItemService {

    /**
     * 根据商品ID查询详情
     *
     * @param itemId 商品ID
     * @return com.architect.pojo.Items
     * @since 2021/3/14 11:11
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品ID查询商品图片列表
     *
     * @param itemId 商品ID
     * @return java.util.List<com.architect.pojo.ItemsImg>
     * @since 2021/3/14 11:13
     */
    List<ItemsImg> queryItemImageList(String itemId);

    /**
     * 根据商品ID查询商品规格列表
     *
     * @param itemId 商品ID
     * @return java.util.List<com.architect.pojo.ItemsSpec>
     * @since 2021/3/14 11:14
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品ID查询商品参数
     *
     * @param itemId 商品ID
     * @return java.util.List<com.architect.pojo.ItemsParam>
     * @since 2021/3/14 11:16
     */
    ItemsParam queryItemParamList(String itemId);

    /**
     * 根据商品ID查询商品的评价等级数量
     *
     * @param itemId 商品ID
     * @since 2021/3/14 12:04
     */
    CommentLevelCountVO queryCommentCounts(String itemId);

    /**
     * 分页查询商品评价信息
     *
     * @param itemId 商品ID
     * @param level  评价等级
     * @return java.util.List<com.architect.pojo.vo.ItemCommentVO>
     * @since 2021/3/14 12:47
     */
    PagedGridResult queryPagedComments(String itemId,
                                       Integer level,
                                       Integer page,
                                       Integer pageSIze);


    /**
     * 查询商品列表
     *
     * @since 2021/5/8 2:45
     * @return org.architect.util.PagedGridResult
     * @param keywords 查询关键字
     * @param sort     排序规则
     * @param page     第几页
     * @param pageSize 每页条数
     */
    PagedGridResult searchItems(String keywords,
                                       String sort,
                                       Integer page,
                                       Integer pageSize);


    /**
     * 根据分类ID查询商品列表
     *
     * @since 2021/5/16 10:49
     * @return org.architect.util.PagedGridResult
     * @param catId  商品三级分类ID
     */
    PagedGridResult searchItems(Integer catId,
                                       String sort,
                                       Integer page,
                                       Integer pageSize);


    /**
     * 根据商品规格IDS查询最新的购物车中商品数据（用于刷新渲染购物车中的商品数据）
     *
     * @since 2021/5/16 14:04
     * @return java.util.List<com.architect.pojo.vo.ShopcartVO>
     * @param specIds   商品规格ID
     */
    List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据商品规格ID查询具体的商品规格
     *
     * @since 2021/5/23 15:03
     * @return com.architect.pojo.ItemsSpec
     * @param specId    商品规格ID
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品ID获取商品图片主图的URL
     *
     * @since 2021/5/23 15:12
     * @return java.lang.String
     * @param itemId    商品ID
     */
    String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     *
     * @since 2021/5/23 15:31
     * @param specId    商品规格ID
     * @param buyCounts 商品购买数量
     */
    void decreaseItemSpecStock(String specId,int buyCounts);
}
