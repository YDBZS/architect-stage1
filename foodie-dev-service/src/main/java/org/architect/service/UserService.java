package org.architect.service;

import com.architect.pojo.Users;
import com.architect.pojo.bo.UsersBO;

/**
 * 基于用户操作的Service
 *
 * @author 多宝
 * @since 2021/3/13 15:27
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     */
    boolean queryUserNameIsExist(String username);

    /**
     * 创建用户信息
     *
     * @since 2021/3/13 16:02
     * @return com.architect.pojo.Users
     * @param users 用户信息
     */
    Users createUser(UsersBO users);

}
