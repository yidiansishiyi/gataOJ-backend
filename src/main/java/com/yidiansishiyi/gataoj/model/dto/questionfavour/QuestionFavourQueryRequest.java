package com.yidiansishiyi.gataoj.model.dto.questionfavour;

import com.yidiansishiyi.gataoj.common.PageRequest;
import com.yidiansishiyi.gataoj.model.dto.post.PostQueryRequest;
import com.yidiansishiyi.gataoj.model.dto.question.QuestionQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 题目收藏查询请求
 *
 * @author  sanqi
 *  
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目查询请求
     */
    private QuestionQueryRequest questionQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}