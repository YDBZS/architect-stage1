package org.architect.service;

import com.architect.pojo.Items;
import com.architect.pojo.ItemsImg;
import com.architect.pojo.ItemsParam;
import com.architect.pojo.ItemsSpec;
import com.architect.pojo.vo.CommentLevelCountVO;
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


}
