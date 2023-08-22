package com.yidiansishiyi.gataoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yidiansishiyi.gataoj.common.BaseResponse;
import com.yidiansishiyi.gataoj.common.ErrorCode;
import com.yidiansishiyi.gataoj.common.ResultUtils;
import com.yidiansishiyi.gataoj.exception.BusinessException;
import com.yidiansishiyi.gataoj.exception.ThrowUtils;
import com.yidiansishiyi.gataoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yidiansishiyi.gataoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yidiansishiyi.gataoj.model.entity.QuestionSubmit;
import com.yidiansishiyi.gataoj.model.entity.User;
import com.yidiansishiyi.gataoj.model.vo.QuestionSubmitVO;
import com.yidiansishiyi.gataoj.service.QuestionSubmitService;
import com.yidiansishiyi.gataoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 题目提交接口
 *
 * @author sanqi
 *
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private UserService userService;

    @Profile({"dev", "local"})
    @GetMapping("/doSelect")
    public List<QuestionSubmit> doSelect() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        long count = questionSubmitService.count();
        for (int i = 0; i < 10; i++) {
            long start = i * (count / 100) + 1;
            long end = (i+1) * (count / 100);
            System.out.println(start + "->>" + end);
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
                questionSubmitQueryWrapper.last("LIMIT " + start + "," + end);
                questionSubmitService.list(questionSubmitQueryWrapper);
            }, threadPoolExecutor);
            futureList.add(voidCompletableFuture);
        }
        System.out.println(count + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
        return null;
    }

    @Profile({"dev", "local"})
    @GetMapping("/test")
    public BaseResponse doSave(){
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
        return null;
    }

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交记录的 id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listFavourQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
            HttpServletRequest request) {
        if (questionSubmitQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

}
