package com.yidiansishiyi.gataoj.judge;

import com.yidiansishiyi.gataoj.judge.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataoj.judge.model.ExecuteCodeResponse;

/**
 * 代码沙箱功能接口
 */
public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
