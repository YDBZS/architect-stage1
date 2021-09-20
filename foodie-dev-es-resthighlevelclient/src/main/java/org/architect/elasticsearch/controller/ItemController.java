package org.architect.elasticsearch.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.architect.elasticsearch.common.Constant;
import org.architect.elasticsearch.service.ItemService;
import org.architect.util.ReturnResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品搜索接口
 *
 * @author 多宝
 * @since 2021/9/20 14:15
 */
@Slf4j
@RestController
@RequestMapping("items")
@Api(value = "商品搜索接口", tags = {"商品搜索接口"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/hello")
    @ApiOperation(value = "测试接口", httpMethod = Constant.INTERFACE_METHOD_GET)
    public String hello() {
        return "Hello World";
    }


    @ApiOperation(value = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/es/search")
    public ReturnResult search(
            String keywords,
            String sort,
            Integer page,
            Integer pageSize
    ) {
        if (StringUtils.isBlank(keywords)) {
            return ReturnResult.errorMsg("");
        }
        if (null == page) {
            page = 1;
        }
        if (null == pageSize) {
            pageSize = org.architect.constant.Constant.PAGE_SIZE;
        }

        //ES的分页是从0开始的，所以这里进行累减
        page--;

        return ReturnResult.ok(itemService.searchItems(keywords, sort, page, pageSize));
    }


}
