package org.architect.sso.util;

import org.architect.pojo.Users;
import org.architect.pojo.vo.UsersVO;
import org.architect.constant.Constant;
import org.architect.util.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 分布式会话工具类
 *
 * @author 多宝
 * @since 2021/8/15 11:58
 */
@Component
public class    DistributedSessionUtil {

    @Resource
    private RedisOperator redisOperator;

    /**
     * 转换前端反参，存储用户会话
     *
     * @param user 数据库查询用户对象
     * @return com.architect.pojo.vo.UsersVO
     * @author 多宝
     * @since 2021/8/15 11:23
     */
    public UsersVO convertUsersBO(Users user) {
        // 实现用户的Redis会话。
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(Constant.REDIS_USER_TOKEN + ":" + user.getId(), uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        return usersVO;
    }

}
