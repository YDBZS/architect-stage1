package org.architect.service.impl;

import org.architect.mapper.UsersMapper;
import org.architect.pojo.Users;
import org.architect.pojo.bo.UsersBO;
import org.architect.constant.Constant;
import org.architect.enums.Sex;
import org.architect.service.UserService;
import org.architect.util.DateUtil;
import org.architect.util.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 多宝
 * @since 2021/3/13 15:29
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UsersMapper usersMapper;
    @Resource
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String username) {

        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        Users users = usersMapper.selectOneByExample(userExample);
        return users != null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UsersBO users) {
        Users user = new Users();
        String userId = sid.nextShort();
        user.setId(userId);
        user.setUsername(users.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(users.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        user.setNickname(users.getUsername());
        user.setFace(Constant.USER_FACE);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.SECRIT.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        usersMapper.insert(user);
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password",password);
        return usersMapper.selectOneByExample(userExample);
    }
}
