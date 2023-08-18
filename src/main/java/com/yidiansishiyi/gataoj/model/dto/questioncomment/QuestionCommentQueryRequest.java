package com.yidiansishiyi.gataoj.model.dto.questioncomment;

import com.yidiansishiyi.gataoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionCommentQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 评论所属父级 id
     */
    private Long parentId;

    /**
     * 评论所属题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}