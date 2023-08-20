package com.yidiansishiyi.gataoj.mapper;
import java.util.Date;

import com.yidiansishiyi.gataoj.model.entity.QuestionSubmit;
import com.yidiansishiyi.gataoj.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;


@Slf4j
@SpringBootTest
class QuestionSubmitMapperTest {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch(); // 用来监听执行时间
        stopWatch.start();
        final int INSERT_NUM = 100000;
        LinkedList<QuestionSubmit> userList = new LinkedList<>();
        for (int i = 0; i < INSERT_NUM*3; i++) {
            QuestionSubmit questionSubmit = new QuestionSubmit();
            questionSubmit.setLanguage("java");
            questionSubmit.setCode("1+3=5");
            questionSubmit.setJudgeInfo("{}");
            questionSubmit.setStatus(0);
            questionSubmit.setQuestionId(1689905663431520258L);
            questionSubmit.setUserId(1685290200697200642L);
            userList.add(questionSubmit);
        }
        questionSubmitService.saveBatch(userList, INSERT_NUM*3); // 模拟插入10万条数据
        stopWatch.stop();
        System.out.println("花费时间：" + stopWatch.getTotalTimeMillis());
    }


    @Test
    public void test3(){
        doInsertAnseUsers();
        doInsertAnseUsers();
        doInsertAnseUsers();
    }

    @Test
    public void doInsertAnseUsers() {
        System.out.println("张三1");
        try {
            CompletableFuture.runAsync(() -> {
                System.out.println("张三");
                StopWatch stopWatch = new StopWatch(); // 用来监听执行时间
                stopWatch.start();
                final int INSERT_NUM = 100000;
                LinkedList<QuestionSubmit> userList = new LinkedList<>();
                for (int i = 0; i < INSERT_NUM; i++) {
                    QuestionSubmit questionSubmit = new QuestionSubmit();
                    questionSubmit.setLanguage("java");
                    questionSubmit.setCode("1+3=5");
                    questionSubmit.setJudgeInfo("{}");
                    questionSubmit.setStatus(0);
                    questionSubmit.setQuestionId(1689905663431520258L);
                    questionSubmit.setUserId(1685290200697200642L);
                    userList.add(questionSubmit);
                }
                System.out.println("插入");
                try {
                    questionSubmitService.saveBatch(userList, INSERT_NUM); // 模拟插入10万条数据
                } catch (Exception e) {
                    log.error("异步化插入数据失败");
                    throw new RuntimeException(e);
                }
                stopWatch.stop();
                System.out.println("张三3");
                System.out.println("花费时间：" + stopWatch.getTotalTimeMillis());
            }, threadPoolExecutor);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}