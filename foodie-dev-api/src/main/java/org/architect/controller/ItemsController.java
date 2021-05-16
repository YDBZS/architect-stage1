package org.architect.controller;

import com.architect.pojo.Items;
import com.architect.pojo.ItemsImg;
import com.architect.pojo.ItemsParam;
import com.architect.pojo.ItemsSpec;
import com.architect.pojo.vo.ItemInfoVO;
import com.architect.pojo.vo.ShopcartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.service.ItemService;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品街口
 *
 * @author 多宝
 * @since 2021/3/14 11:25
 */
@Api(value = "商品相关接口", tags = {"商品相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController {

    @Resource
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public ReturnResult info(
            @ApiParam(value = "商品ID", required = true)
            @PathVariable String itemId
    ) {
        if (null == itemId) {
            return ReturnResult.errorMsg("");
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImageList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParamList(itemId);
        ItemInfoVO infoVO = new ItemInfoVO();
        infoVO.setItem(items);
        infoVO.setItemParams(itemsParam);
        infoVO.setItemImgList(itemsImgs);
        infoVO.setItemSpecList(itemsSpecs);
        return ReturnResult.ok(infoVO);
    }

    @ApiOperation(value = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("commentLevel")
    public ReturnResult commentLevel(
            @ApiParam(name = "itemId", required = true)
            @RequestParam String itemId
    ) {
        if (StringUtils.isBlank(itemId)) {
            return ReturnResult.errorMsg("");
        }
        return ReturnResult.ok(itemService.queryCommentCounts(itemId));
    }


    @ApiOperation(value = "查询商品评论", httpMethod = "GET")
    @GetMapping("comments")
    public ReturnResult comments(
            @ApiParam(name = "itemId", required = true)
            @RequestParam String itemId,
            @ApiParam("level")
            @RequestParam Integer level,
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(itemId)) {
            return ReturnResult.errorMsg("");
        }
        if (null == page) {
            page = 1;
        }
        if (null == pageSize) {
            pageSize = Constant.COMMENT_PAGE_SIZE;
        }

        return ReturnResult.ok(itemService.queryPagedComments(itemId, level, page, pageSize));
    }


    @ApiOperation(value = "搜索商品列表", httpMethod = "GET")
    @GetMapping("search")
    public ReturnResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(value = "排序", required = false)
            @RequestParam String sort,
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(keywords)) {
            return ReturnResult.errorMsg("");
        }
        if (null == page) {
            page = 1;
        }
        if (null == pageSize) {
            pageSize = Constant.PAGE_SIZE;
        }

        return ReturnResult.ok(itemService.searchItems(keywords, sort, page, pageSize));
    }


    @ApiOperation(value = "通过分类ID搜索商品列表", httpMethod = "GET")
    @GetMapping("catItems")
    public ReturnResult catItems(
            @ApiParam(name = "catId", value = "商品三级分类ID", required = true)
            @RequestParam Integer catId,
            @ApiParam(value = "排序", required = false)
            @RequestParam String sort,
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        if (null == catId) {
            return ReturnResult.errorMsg(null);
        }
        if (null == page) {
            page = 1;
        }
        if (null == pageSize) {
            pageSize = Constant.PAGE_SIZE;
        }

        return ReturnResult.ok(itemService.searchItems(catId, sort, page, pageSize));
    }

    @ApiOperation(value = "根据商品规格IDS查找最新的商品数据(渲染购物车)", notes = "根据商品规格IDS查找最新的商品数据(渲染购物车)")
    @GetMapping("refresh")
    public ReturnResult q(
            @ApiParam(value = "拼接的商品规格IDS")
            @RequestParam String itemSpecIds
    ) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return ReturnResult.ok();
        }

        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);

        return ReturnResult.ok(list);
    }


}
