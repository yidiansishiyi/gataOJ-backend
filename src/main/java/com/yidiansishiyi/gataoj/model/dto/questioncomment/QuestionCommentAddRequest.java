package com.yidiansishiyi.gataoj.model.dto.questioncomment;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 */
@Data
public class QuestionCommentAddRequest implements Serializable {

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
    private Long questionId;

    /**
     * 评论内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}