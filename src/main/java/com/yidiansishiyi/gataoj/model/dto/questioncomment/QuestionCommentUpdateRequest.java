package com.yidiansishiyi.gataoj.model.dto.questioncomment;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class QuestionCommentUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    /**
     * 内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}