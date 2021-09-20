package org.architect.service;

import org.architect.pojo.Carousel;

import java.util.List;

/**
 * 轮播图
 *
 * @author 多宝
 * @since 2021/3/13 21:56
 */
public interface CarouselService {

    /**
     * 查询所有轮播图列表
     *
     * @since 2021/3/13 21:58
     * @return java.util.List<com.architect.pojo.Carousel>
     * @param isShow    是否展示
     */
    List<Carousel> queryAll(Integer isShow);

}
