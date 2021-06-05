package org.architect.controller.center;

import com.architect.pojo.OrderItems;
import com.architect.pojo.Orders;
import com.architect.pojo.bo.OrderItemsCommentBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.architect.constant.Constant;
import org.architect.enums.YesOrNo;
import org.architect.service.center.MyCommentsService;
import org.architect.util.CheckUserOrderUtil;
import org.architect.util.PagedGridResult;
import org.architect.util.ReturnResult;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 我的订单接口
 *
 * @author 多宝
 * @since 2021/6/3 21:24
 */
@Slf4j
@RestController
@RequestMapping("mycomments")
@Api(value = "用户中心评价模块", tags = {"用户中心评价模块"})
public class MyCommentsController {

    @Resource
    private MyCommentsService myCommentsService;
    @Resource
    private CheckUserOrderUtil checkUserOrderUtil;

    @PostMapping("/pending")
    @ApiOperation(value = "查询订单列表", httpMethod = Constant.INTERFACE_METHOD_POST)
    public ReturnResult pending(
            @ApiParam(value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(value = "订单ID")
            @RequestParam String orderId
    ) {
        ReturnResult result = checkUserOrderUtil.checkUserOrder(orderId, userId);
        if (result.getStatus() != HttpStatus.OK.value()) {
            return result;
        }
        Orders orders = (Orders) result.getData();
        if (orders.getIsComment().equals(YesOrNo.YES.type)) {
            return ReturnResult.errorMsg("该笔订单已经评价");
        }

        List<OrderItems> list = myCommentsService.queryPendingComments(orderId);

        return ReturnResult.ok(list);
    }


    @PostMapping("/saveList")
    @ApiOperation(value = "保存评论列表", httpMethod = Constant.INTERFACE_METHOD_POST)
    public ReturnResult saveList(
            @ApiParam(value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(value = "订单ID")
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList
    ) {


        ReturnResult result = checkUserOrderUtil.checkUserOrder(orderId, userId);
        if (result.getStatus() != HttpStatus.OK.value()) {
            return result;
        }

        // 判断评论内容List不能为空
        if (CollectionUtils.isEmpty(commentList)) {
            return ReturnResult.errorMsg("评论内容不能为空！");
        }

        myCommentsService.saveComments(orderId, userId, commentList);
        return ReturnResult.ok();
    }

    @PostMapping("/query")
    @ApiOperation(value = "查询我的评价", httpMethod = Constant.INTERFACE_METHOD_POST)
    public ReturnResult query(
            @ApiParam(value = "用户id", required = true)
            @RequestParam String userId,
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(userId)) {
            return ReturnResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = Constant.COMMENT_PAGE_SIZE;
        }
        PagedGridResult gridResult = myCommentsService.queryMyComments(userId, page, pageSize);
        return ReturnResult.ok(gridResult);
    }


}
