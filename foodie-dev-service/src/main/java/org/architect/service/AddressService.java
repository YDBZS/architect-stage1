package org.architect.service;

import com.architect.pojo.UserAddress;

import java.util.List;

/**
 * 用户地址相关Service
 *
 * @author 多宝
 * @since 2021/5/16 15:18
 */
public interface AddressService {

    /**
     * 根据用户ID查询用户的收货地址列表
     *
     * @since 2021/5/16 15:21
     * @return java.util.List<com.architect.pojo.UserAddress>
     * @param userId    用户ID
     */
    List<UserAddress> queryAll(String userId);

}
