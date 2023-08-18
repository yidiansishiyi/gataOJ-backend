package com.yidiansishiyi.gataoj.model.vo;


import com.yidiansishiyi.gataoj.model.entity.QuestionComment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName comment
 */
@Data
public class CommentVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 发帖用户 id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 帖子所属类型
     */
    private Integer entityType;

    /**
     * 评论所属父级 id
     */
    private Long parentId;

    /**
     * 评论所属帖子 id
     */
    private Long postId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 包装类转对象
     *
     * @param commentVO
     * @return
     */
    public static QuestionComment voToObj(CommentVO commentVO) {
        if (commentVO == null) {
            return null;
        }
        QuestionComment comment = new QuestionComment();
        BeanUtils.copyProperties(commentVO, comment);
        return comment;
    }

    /**
     * 对象转包装类
     *
     * @param comment
     * @return
     */
    public static CommentVO objToVo(QuestionComment comment) {
        if (comment == null) {
            return null;
        }
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        return commentVO;
    }

}