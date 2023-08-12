package com.yidiansishiyi.gataOJ.model.dto.questionsubmit;

import com.yidiansishiyi.gataOJ.model.dto.question.JudgeCase;
import com.yidiansishiyi.gataOJ.model.dto.question.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author  sanqi
 *  
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}