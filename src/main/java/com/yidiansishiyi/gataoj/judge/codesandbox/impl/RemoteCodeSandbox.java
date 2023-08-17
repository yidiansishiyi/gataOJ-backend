package com.yidiansishiyi.gataoj.judge.codesandbox.impl;

import com.yidiansishiyi.gataoj.judge.codesandbox.CodeSandbox;
import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeResponse;

public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("本地代码沙箱");
        return null;
    }
}
