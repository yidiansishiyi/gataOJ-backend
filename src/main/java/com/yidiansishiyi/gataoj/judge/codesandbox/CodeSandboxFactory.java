package com.yidiansishiyi.gataoj.judge.codesandbox;

import com.yidiansishiyi.gataoj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.yidiansishiyi.gataoj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.yidiansishiyi.gataoj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂(静态根据传值判断选择工厂类型)
 */
public class CodeSandboxFactory {
    /**
     * 静态代码工厂,返回配置传递来的代码工厂类型
     *
     * @param type
     * @return
     */
    public static CodeSandbox newInstance(String type){
        switch(type){
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
