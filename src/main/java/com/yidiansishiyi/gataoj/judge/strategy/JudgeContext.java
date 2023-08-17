package com.yidiansishiyi.gataoj.judge.strategy;

import com.yidiansishiyi.gataoj.model.dto.question.JudgeCase;
import com.yidiansishiyi.gataoj.model.dto.questionsubmit.JudgeInfo;
import com.yidiansishiyi.gataoj.model.entity.Question;
import com.yidiansishiyi.gataoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private Integer status;

    private List<String> inputList;

    private List<JudgeCase> judgeCaseList;

    private String message;

    private List<String> outputList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
