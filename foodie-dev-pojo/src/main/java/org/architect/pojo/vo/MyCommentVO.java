package org.architect.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 我的评价查询结果
 * @author G-Dragon
 */
@Data
public class MyCommentVO {

    private String commentId;
    private String content;
    private Date createdTime;
    private String itemId;
    private String itemName;
    private String specName;
    private String itemImg;

}