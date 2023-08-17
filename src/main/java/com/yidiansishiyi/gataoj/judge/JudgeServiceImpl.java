package com.yidiansishiyi.gataoj.judge;
import com.yidiansishiyi.gataoj.judge.*;
import com.yidiansishiyi.gataoj.judge.codesandbox.CodeSandbox;
import com.yidiansishiyi.gataoj.judge.codesandbox.CodeSandboxFactory;
import com.yidiansishiyi.gataoj.judge.codesandbox.CodeSandboxProxy;
import com.yidiansishiyi.gataoj.judge.strategy.JudgeContext;
import com.yidiansishiyi.gataoj.model.dto.questionsubmit.JudgeInfo;

import cn.hutool.json.JSONUtil;
import com.yidiansishiyi.gataoj.common.ErrorCode;
import com.yidiansishiyi.gataoj.exception.BusinessException;
import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yidiansishiyi.gataoj.model.dto.question.JudgeCase;
import com.yidiansishiyi.gataoj.model.entity.Question;
import com.yidiansishiyi.gataoj.model.entity.QuestionSubmit;
import com.yidiansishiyi.gataoj.model.enums.QuestionSubmitStatusEnum;
import com.yidiansishiyi.gataoj.service.QuestionService;
import com.yidiansishiyi.gataoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题服务
 * 考虑到多种语言进行判题,保证代码整洁性,将判题逻辑单独抽调出来做做一个优化
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesandbox.type}")
    private String type;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManager judgeManager;


    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1. 查询信息
        QuestionSubmit judgeSubmit = questionSubmitService.getById(questionSubmitId);
        // 2. 进行校验(提交信息,题目信息,状态)
        if (judgeSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目提交信息不存在");
        }
        Long questionId = judgeSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目信息不存在");
        }
        if (!judgeSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目正在判题中");
        }
        // 3. 更改判题（题目提交）的状态为 “判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        String language = judgeSubmit.getLanguage();
        String code = judgeSubmit.getCode();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> userIputs = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        // 4. 调用沙箱(时间长, 后期引入沙箱后异步化) 沙箱调用为 调用工厂,工厂传递给代理使用代理来实现,活得返回值
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .language(language)
                .code(code)
                .inputList(userIputs)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);

        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(judgeInfo);
        judgeContext.setInputList(userIputs);
        judgeContext.setJudgeCaseList(judgeCases);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(judgeSubmit);
        // 5. 返回结果判题
        JudgeInfo doJudge = judgeManager.doJudge(judgeContext);


        // 6. 数据入库
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
        return questionSubmitResult;
    }
}
