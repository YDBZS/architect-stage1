package com.architect.mapper;

import com.architect.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 自定义订单Mapper
 *
 * @author 多宝
 * @since 2021/6/3 20:36
 */
public interface OrdersMapperCustom {

    /**
     * 查询我的订单信息
     *
     * @param map 参数信息
     * @return java.util.List<com.architect.pojo.vo.MyOrdersVO>
     * @author 多宝
     * @since 2021/6/3 20:51
     */
    List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

}
