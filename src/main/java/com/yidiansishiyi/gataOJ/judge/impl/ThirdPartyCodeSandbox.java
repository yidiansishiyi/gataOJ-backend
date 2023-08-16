package com.yidiansishiyi.gataOJ.judge.impl;

import com.yidiansishiyi.gataOJ.judge.CodeSandbox;
import com.yidiansishiyi.gataOJ.judge.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataOJ.judge.model.ExecuteCodeResponse;

public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
