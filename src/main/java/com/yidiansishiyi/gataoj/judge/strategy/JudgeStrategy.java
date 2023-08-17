package com.yidiansishiyi.gataoj.judge.strategy;

import com.yidiansishiyi.gataoj.model.dto.questionsubmit.JudgeInfo;

public interface JudgeStrategy {
    /**
     * 判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
