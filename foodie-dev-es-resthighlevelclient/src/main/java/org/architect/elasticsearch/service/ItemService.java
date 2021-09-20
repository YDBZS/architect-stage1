package org.architect.elasticsearch.service;

import org.architect.util.PagedGridResult;

/**
 * ES商品Service
 *
 * @author 多宝
 * @since 2021/9/20 14:56
 */
public interface ItemService {

    /**
     * 查询ES的商品数据
     *
     * @param keywords  查询关键字
     * @param sort      排序字段
     * @param page      当前页面
     * @param pageSize  每页条数
     * @return org.architect.elasticsearch.pojo.Item
     * @author 多宝
     * @since 2021/9/20 14:58
     */
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);


}
