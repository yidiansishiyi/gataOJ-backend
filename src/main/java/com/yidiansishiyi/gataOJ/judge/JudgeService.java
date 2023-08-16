package com.yidiansishiyi.gataOJ.judge;

import com.yidiansishiyi.gataOJ.model.entity.QuestionSubmit;

public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
