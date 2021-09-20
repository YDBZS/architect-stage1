package org.architect.service.impl;

import org.architect.mapper.UserAddressMapper;
import org.architect.pojo.UserAddress;
import org.architect.pojo.bo.AddressBO;
import org.architect.enums.YesOrNo;
import org.architect.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 多宝
 * @since 2021/5/16 15:19
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private UserAddressMapper userAddressMapper;
    @Resource
    private Sid sid;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserAddress> queryAll(String userId) {
        UserAddress ua = new UserAddress();
        ua.setUserId(userId);
        return userAddressMapper.select(ua);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewUserAddress(AddressBO addressBO) {
        // 1、判断当前用户是否存在地址，如果没有，则新增为默认地址
        int isDefault = 0;
        List<UserAddress> addressList = queryAll(addressBO.getUserId());
        if (CollectionUtils.isEmpty(addressList)) {
            isDefault = 1;
        }

        String addressId = sid.nextShort();

        // 2、保存地址到数据库
        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(addressBO, address);
        address.setId(addressId);
        address.setIsDefault(isDefault);
        address.setCreatedTime(new Date());
        address.setUpdatedTime(new Date());

        userAddressMapper.insert(address);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();

        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(addressBO, address);

        address.setId(addressId);
        address.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(address);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserAddress(String userId, String addressId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        userAddressMapper.delete(userAddress);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        // 1、查找默认地址，设置为不默认
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> addressList = userAddressMapper.select(userAddress);
        addressList.forEach(address -> {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        });
        // 2、根据地址ID修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        return userAddressMapper.selectOne(userAddress);
    }
}
