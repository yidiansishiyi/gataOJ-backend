package com.yidiansishiyi.gataoj.judge;
import com.yidiansishiyi.gataoj.judge.strategy.DefaultJudgeStrategy;
import com.yidiansishiyi.gataoj.judge.strategy.JavaLanguageJudgeStrategy;
import com.yidiansishiyi.gataoj.judge.strategy.JudgeContext;
import com.yidiansishiyi.gataoj.judge.strategy.JudgeStrategy;
import com.yidiansishiyi.gataoj.model.dto.questionsubmit.JudgeInfo;
import com.yidiansishiyi.gataoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题逻辑 抽出简化代码
 */
@Service
public class JudgeManager {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
