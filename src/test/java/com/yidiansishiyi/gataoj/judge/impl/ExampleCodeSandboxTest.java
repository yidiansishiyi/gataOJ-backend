package com.yidiansishiyi.gataoj.judge.impl;

import com.yidiansishiyi.gataoj.judge.codesandbox.CodeSandbox;
import com.yidiansishiyi.gataoj.judge.codesandbox.CodeSandboxFactory;
import com.yidiansishiyi.gataoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yidiansishiyi.gataoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ExampleCodeSandboxTest {

    @Value("${codesandbox.type:example}")
    private String type;


    @Test
    public void testFailure(){
        System.out.println(type);
        String code = "int main() { }";
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest build = ExecuteCodeRequest.builder()
                .code(code)
                .language(QuestionSubmitLanguageEnum.JAVA.getValue())
                .inputList(inputList)
                .build();
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox.executeCode(build);
    }

    @Test
    public void testP(){
        System.out.println(type);
        String code = "int main() { }";
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest build = ExecuteCodeRequest.builder()
                .code(code)
                .language(QuestionSubmitLanguageEnum.JAVA.getValue())
                .inputList(inputList)
                .build();
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox.executeCode(build);
    }

}