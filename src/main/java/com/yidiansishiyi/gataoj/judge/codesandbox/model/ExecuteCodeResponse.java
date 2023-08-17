package com.yidiansishiyi.gataoj.judge.codesandbox.model;

import com.yidiansishiyi.gataoj.model.dto.questionsubmit.JudgeInfo;
import lombok.Data;

import java.util.List;

@Data
public class ExecuteCodeResponse {

    /**
     * 判题信息（json 对象）
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 输出用例
     */
    private List<String> outputList;


}
