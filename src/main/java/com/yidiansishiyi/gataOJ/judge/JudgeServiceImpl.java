package com.yidiansishiyi.gataOJ.judge;

import com.yidiansishiyi.gataOJ.common.ErrorCode;
import com.yidiansishiyi.gataOJ.exception.BusinessException;
import com.yidiansishiyi.gataOJ.model.entity.Question;
import com.yidiansishiyi.gataOJ.model.entity.QuestionSubmit;
import com.yidiansishiyi.gataOJ.model.enums.QuestionSubmitStatusEnum;
import com.yidiansishiyi.gataOJ.service.QuestionService;
import com.yidiansishiyi.gataOJ.service.QuestionSubmitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 判题服务
 * 考虑到多种语言进行判题,保证代码整洁性,将判题逻辑单独抽调出来做做一个优化
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;


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
        // 3. 调用沙箱(时间长, 后期引入沙箱后异步化)
        // 4. 返回结果判题
        // 5. 数据入库
        return null;
    }
}
