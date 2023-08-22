package com.yidiansishiyi.gataoj.mapper;
import java.util.ArrayList;

import com.yidiansishiyi.gataoj.model.entity.QuestionSubmit;
import com.yidiansishiyi.gataoj.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


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
        boolean b = questionSubmitService.saveBatchAsync(userList, INSERT_NUM * 3);// 模拟插入10万条数据
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
            }, threadPoolExecutor).join();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test
    public void doInsertAnse() throws InterruptedException {
        System.out.println("张三1");
        StopWatch stopWatch = new StopWatch(); // 用来监听执行时间
        stopWatch.start();
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run(){
                    System.out.println("张三");
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
                    boolean b = questionSubmitService.saveBatch(userList, INSERT_NUM);// 模拟插入10万条数据
                }
            });
            thread.setName("数据插入线程" );
            thread.start();
            thread.join();
        }
        stopWatch.stop();
        System.out.println("张三3");
        System.out.println("花费时间：" + stopWatch.getTotalTimeMillis());
    }

//    private ExecutorService executorService = new ThreadPoolExecutor(40, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000)); // 定义线程池


    @Test
    public void doConcurrencyInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 分十组
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        final int INSERT_NUM = 100000;
        for (int i = 0; i < 3; i++) {
            LinkedList<QuestionSubmit> userList = new LinkedList<>();
            for (int j = 0; j < INSERT_NUM; j++) {
                QuestionSubmit questionSubmit = new QuestionSubmit();
                questionSubmit.setLanguage("java");
                questionSubmit.setCode("1+3=5");
                questionSubmit.setJudgeInfo("{}");
                questionSubmit.setStatus(0);
                questionSubmit.setQuestionId(1689905663431520258L);
                questionSubmit.setUserId(1685290200697200642L);
                userList.add(questionSubmit);
            }
            // 异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                questionSubmitService.saveBatch(userList, INSERT_NUM);
            }, threadPoolExecutor);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


}