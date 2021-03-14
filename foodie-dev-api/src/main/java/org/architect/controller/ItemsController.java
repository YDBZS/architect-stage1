package org.architect.controller;

import com.architect.pojo.Items;
import com.architect.pojo.ItemsImg;
import com.architect.pojo.ItemsParam;
import com.architect.pojo.ItemsSpec;
import com.architect.pojo.vo.ItemInfoVO;
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


}
