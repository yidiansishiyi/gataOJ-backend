package com.yidiansishiyi.gataOJ.judge;

import com.yidiansishiyi.gataOJ.judge.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataOJ.judge.model.ExecuteCodeResponse;

/**
 * 代码沙箱功能接口
 */
public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
