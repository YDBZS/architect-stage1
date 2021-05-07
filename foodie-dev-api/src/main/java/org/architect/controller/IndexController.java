package org.architect.controller;

import com.architect.pojo.Carousel;
import com.architect.pojo.Category;
import com.architect.pojo.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.architect.enums.YesOrNo;
import org.architect.service.CarouselService;
import org.architect.service.CategoryService;
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

    @ApiOperation(value = "查询轮播图", httpMethod = "GET")
    @GetMapping("/carousel")
    public ReturnResult caroucel() {
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);
        return ReturnResult.ok(carousels);
    }

    /**
     * 首页分类展示需求：
     * 1、第一次刷新主页查询大分类，渲染展示到首页
     * 2、如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载(懒加载)
     */
    @ApiOperation(value = "查询一级大分类", httpMethod = "GET")
    @GetMapping("/cats")
    public ReturnResult category() {
        List<Category> list = categoryService.queryAllRootLevelCat();
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
        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
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
