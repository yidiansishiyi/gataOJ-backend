package com.yidiansishiyi.gataoj.judge.codesandbox.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExecuteCodeRequest {
    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 输入用例
     */
    private List<String> inputList;
}
