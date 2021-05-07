package com.architect.mapper;


import com.architect.pojo.vo.ItemCommentVO;
import com.architect.pojo.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    /**
     * 查询商品评价
     *
     * @since 2021/5/8 2:51
     * @return java.util.List<com.architect.pojo.vo.ItemCommentVO>
     * @param map  参数
     */
    List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    /**
     * 查询商品列表
     *
     * @param map 参数Map
     * @return java.util.List<com.architect.pojo.vo.SearchItemsVO>
     * @since 2021/5/8 2:50
     */
    List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);
}