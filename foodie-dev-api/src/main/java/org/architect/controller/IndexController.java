package org.architect.controller;

import org.architect.pojo.Carousel;
import org.architect.pojo.Category;
import org.architect.pojo.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.architect.enums.YesOrNo;
import org.architect.service.CarouselService;
import org.architect.service.CategoryService;
import org.architect.util.JsonUtils;
import org.architect.util.RedisOperator;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 首页接口
 *
 * @author 多宝
 * @since 2021/3/13 22:02
 */
@Api(value = "首页接口", tags = {"首页接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Resource
    private CarouselService carouselService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private RedisOperator redisOperator;

    @ApiOperation(value = "查询轮播图", httpMethod = "GET")
    @GetMapping("/carousel")
    public ReturnResult caroucel() {
        List<Carousel> carousels;

        String carouselStr = redisOperator.get("carousel");

        if (StringUtils.isBlank(carouselStr)) {
            carousels = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(carousels));
        } else {
            carousels = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }

        return ReturnResult.ok(carousels);
    }

    /**
     *  如果数据发生更改，如何重新刷新缓存数据呢？
     *  1、后台运营系统，一旦广告(轮播图)发生更改，就可以删除缓存，然后重置
     *  2、定时重置，如每天凌晨三点重置
     *  3、没个轮播图都有可能是一个广告，每一个广告都会有一个过期时间，过期了，再重置。
     */

    /**
     * 首页分类展示需求：
     * 1、第一次刷新主页查询大分类，渲染展示到首页
     * 2、如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载(懒加载)
     */
    @ApiOperation(value = "查询一级大分类", httpMethod = "GET")
    @GetMapping("/cats")
    public ReturnResult category() {
        List<Category> list;
        String catsStr = redisOperator.get("cats");
        if (StringUtils.isBlank(catsStr)) {
            list = categoryService.queryAllRootLevelCat();
            redisOperator.set("cats", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(catsStr, Category.class);
        }
        return ReturnResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public ReturnResult sbuCat(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable Integer rootCatId) {
        if (null == rootCatId) {
            return ReturnResult.errorMsg("");
        }
        List<CategoryVO> list;
        String catsStr = redisOperator.get("subCat:" + rootCatId);
        if (StringUtils.isBlank(catsStr)) {
            list = categoryService.getSubCatList(rootCatId);

            /**
             * 查询的key在redis中不存在，
             * 对应的id在数据库也不存在，
             * 此时被非法用户进行攻击，大量的请求会直接打在db上，
             * 造成宕机，从而影响整个系统，
             * 这种现象称之为缓存穿透。
             * 解决方案：把空的数据也缓存起来，比如空字符串，空对象，空数组或list
             */
            if (list != null && list.size() > 0) {
                redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(list));
            } else {
                // 为解决缓存穿透问题，如果是查询出来列表为空，也放入缓存，只不过设置过期时间五分钟。
                redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(list), 5*60);
            }
        } else {
            list = JsonUtils.jsonToList(catsStr, CategoryVO.class);
        }

        return ReturnResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public ReturnResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable Integer rootCatId) {
        if (null == rootCatId) {
            return ReturnResult.errorMsg("");
        }
        return ReturnResult.ok(categoryService.getSixNewItemsLazy(rootCatId));

    }
}
