package com.yidiansishiyi.gataoj.judge.codesandbox.impl;

import com.yidiansishiyi.gataoj.judge.codesandbox.CodeSandbox;
import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeResponse;

public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
