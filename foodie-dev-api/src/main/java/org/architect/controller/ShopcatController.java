package org.architect.controller;

import com.architect.pojo.bo.ShopcartBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.architect.util.ReturnResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试接口
 *
 * @author 多宝
 * @since 2021/3/7 16:34
 */
@Api(value = "购物车接口", tags = {"购物车相关接口"})
@RestController
@RequestMapping("shopcat")
@Slf4j
public class ShopcatController {

    @PostMapping("add")
    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    public ReturnResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBo shopcartBo,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(userId)) {
            return ReturnResult.errorMsg("");
        }
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存

        log.info("接收到的数据 --> {}", shopcartBo);

        return ReturnResult.ok();

    }

    @PostMapping("/del")
    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    public ReturnResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId
    ) {
        if (StringUtils.isBlank(userId) || StringUtils.isEmpty(itemSpecId)) {
            return ReturnResult.errorMsg("参数不能为空");
        }
        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品

        return ReturnResult.ok();

    }


}
