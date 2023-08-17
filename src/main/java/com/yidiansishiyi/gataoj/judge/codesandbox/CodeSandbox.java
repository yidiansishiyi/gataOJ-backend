package com.yidiansishiyi.gataoj.judge.codesandbox;

import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱功能接口
 */
public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
