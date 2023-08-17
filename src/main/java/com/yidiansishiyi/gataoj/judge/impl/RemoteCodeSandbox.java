package com.yidiansishiyi.gataoj.judge.impl;

import com.yidiansishiyi.gataoj.judge.CodeSandbox;
import com.yidiansishiyi.gataoj.judge.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataoj.judge.model.ExecuteCodeResponse;

public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("本地代码沙箱");
        return null;
    }
}
