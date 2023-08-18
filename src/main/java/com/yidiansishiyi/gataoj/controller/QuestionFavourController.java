package com.yidiansishiyi.gataoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yidiansishiyi.gataoj.common.BaseResponse;
import com.yidiansishiyi.gataoj.common.ErrorCode;
import com.yidiansishiyi.gataoj.common.ResultUtils;
import com.yidiansishiyi.gataoj.exception.BusinessException;
import com.yidiansishiyi.gataoj.exception.ThrowUtils;
import com.yidiansishiyi.gataoj.model.dto.question.QuestionQueryRequest;
import com.yidiansishiyi.gataoj.model.dto.questionfavour.QuestionFavourAddRequest;
import com.yidiansishiyi.gataoj.model.dto.questionfavour.QuestionFavourQueryRequest;
import com.yidiansishiyi.gataoj.model.entity.Question;
import com.yidiansishiyi.gataoj.model.entity.User;
import com.yidiansishiyi.gataoj.model.vo.QuestionVO;
import com.yidiansishiyi.gataoj.service.QuestionFavourService;
import com.yidiansishiyi.gataoj.service.QuestionService;
import com.yidiansishiyi.gataoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子收藏接口
 *
 * @author  sanqi
 *  
 */
@RestController
@RequestMapping("/question_favour")
@Slf4j
public class QuestionFavourController {

    @Resource
    private QuestionFavourService questionFavourService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    /**
     * 收藏 / 取消收藏
     *
     * @param questionFavourAddRequest
     * @param request
     * @return resultNum 收藏变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doQuestionFavour(@RequestBody QuestionFavourAddRequest questionFavourAddRequest,
            HttpServletRequest request) {
        if (questionFavourAddRequest == null || questionFavourAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long questionId = questionFavourAddRequest.getQuestionId();
        int result = questionFavourService.doQuestionFavour(questionId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的帖子列表
     *
     * @param questionQueryRequest
     * @param request
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<QuestionVO>> listMyFavourQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
            HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest), loginUser.getId());
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param questionFavourQueryRequest
     * @param request
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionVO>> listFavourQuestionByPage(@RequestBody QuestionFavourQueryRequest questionFavourQueryRequest,
            HttpServletRequest request) {
        if (questionFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = questionFavourQueryRequest.getCurrent();
        long size = questionFavourQueryRequest.getPageSize();
        Long userId = questionFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size),
                questionService.getQueryWrapper(questionFavourQueryRequest.getQuestionQueryRequest()), userId);
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }
}
