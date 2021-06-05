package com.architect.mapper;

import com.architect.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author G-Dragon
 */
@Repository
public interface ItemsCommentsMapperCustom {

    /**
     * 保存用户评论内容
     *
     * @param map  参数
     * @author 多宝
     * @since 2021/6/5 17:49
     */
    void saveComments(Map<String, Object> map);

    /**
     * 查询我的评价
     *
     * @param map 参数
     * @return java.util.List<com.architect.pojo.vo.MyCommentVO>
     * @author 多宝
     * @since 2021/6/5 17:53
     */
    List<MyCommentVO> queryMyComments(@Param("paramMap") Map<String, Object> map);
}