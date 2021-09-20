package org.architect.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户VO
 *
 * @author 多宝
 * @since 2021/8/15 11:04
 */
@Data
public class UsersVO {

    private String id;

    private String username;


    private String nickname;


    private String face;


    private Integer sex;

    /**
     * 用户会话Token
     */
    private String userUniqueToken;

}
