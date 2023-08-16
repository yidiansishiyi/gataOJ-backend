package com.yidiansishiyi.gataOJ.judge.impl;

import com.yidiansishiyi.gataOJ.judge.CodeSandbox;
import com.yidiansishiyi.gataOJ.judge.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataOJ.judge.model.ExecuteCodeResponse;

import com.yidiansishiyi.gataOJ.model.dto.questionsubmit.JudgeInfo;
import com.yidiansishiyi.gataOJ.model.enums.JudgeInfoMessageEnum;
import com.yidiansishiyi.gataOJ.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return null;
    }
}
