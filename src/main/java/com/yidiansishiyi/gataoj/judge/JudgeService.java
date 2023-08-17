package com.yidiansishiyi.gataoj.judge;

import com.yidiansishiyi.gataoj.model.entity.QuestionSubmit;

public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
