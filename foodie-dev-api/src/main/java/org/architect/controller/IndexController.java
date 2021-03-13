package org.architect.controller;

import com.architect.pojo.Carousel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.architect.enums.YesOrNo;
import org.architect.service.CarouselService;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.GetMapping;
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

    @ApiOperation(value = "查询轮播图",httpMethod = "POST")
    @GetMapping("/carousel")
    public ReturnResult caroucel() {
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);
        return ReturnResult.ok(carousels);
    }

    /**
     * 首页分类展示需求：
     * 1、第一次刷新主页查询大分类，渲染展示到首页
     * 2、如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载(懒加载)
     * */


}
