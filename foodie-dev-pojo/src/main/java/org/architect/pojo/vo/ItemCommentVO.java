package org.architect.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 商品评论信息VO
 *
 * @author 多宝
 * @since 2021/3/14 12:41
 */
@Data
public class ItemCommentVO {

    private Integer commentLevel;

    private String content;

    private String specName;

    private Date createdTime;

    private String userFace;

    private String nickname;

}
