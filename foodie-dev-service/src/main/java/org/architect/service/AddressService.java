package org.architect.service;

import com.architect.pojo.UserAddress;
import com.architect.pojo.bo.AddressBO;

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

    /**
     * 用户新增地址
     *
     * @since 2021/5/22 13:17
     * @param addressBO 地址BO
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 用户修改地址
     *
     * @since 2021/5/22 13:50
     * @param addressBO 地址BO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 删除用户收货地址
     *
     * @since 2021/5/22 14:00
     * @param userId        用户ID
     * @param addressId     用户地址ID
     */
    void deleteUserAddress(String userId,String addressId);

    /**
     * 修改默认地址
     *
     * @since 2021/5/22 14:08
     * @param userId    用户ID
     * @param addressId 地址ID
     */
    void updateUserAddressToBeDefault(String userId,String addressId);

    /**
     * 根据userId和addressId查询具体的用户地址
     *
     * @since 2021/5/23 14:46
     * @return com.architect.pojo.UserAddress
     * @param userId    用户ID
     * @param addressId 地址ID
     */
    UserAddress queryUserAddress(String userId,String addressId);
}
