package org.architect.service.center;

import org.architect.pojo.Users;
import org.architect.pojo.bo.center.CenterUserBO;

/**
 * 个人中心
 *
 * @author 多宝
 * @since 2021/5/23 20:12
 */
public interface CenterUserService {

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return com.architect.pojo.Users
     * @author 多宝
     * @since 2021/5/23 20:14
     */
    Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     *
     * @param userId       用户ID
     * @param centerUserBO 用户基本信息
     * @author 多宝
     * @since 2021/5/23 21:19
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 用户头像更新
     *
     * @param userId  用户ID
     * @param faceUrl 用户头像地址
     * @return com.architect.pojo.Users
     * @author 多宝
     * @since 2021/6/2 21:59
     */
    Users updateUserFace(String userId, String faceUrl);

}
