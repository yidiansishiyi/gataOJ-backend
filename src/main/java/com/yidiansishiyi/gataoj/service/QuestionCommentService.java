package com.yidiansishiyi.gataoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yidiansishiyi.gataoj.model.dto.questioncomment.QuestionCommentAddRequest;
import com.yidiansishiyi.gataoj.model.dto.questioncomment.QuestionCommentQueryRequest;
import com.yidiansishiyi.gataoj.model.entity.QuestionComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yidiansishiyi.gataoj.model.vo.CommentVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author zeroc
* @description 针对表【question_comment(题目评论)】的数据库操作Service
* @createDate 2023-08-17 19:58:37
*/
public interface QuestionCommentService extends IService<QuestionComment> {
    /**
     * 获取查询条件
     *
     * @param commentQueryRequest
     * @return
     */
    QueryWrapper<QuestionComment> getQueryWrapper(QuestionCommentQueryRequest commentQueryRequest);

    /**
     * 分页获取帖子封装
     *
     * @param commentPage
     * @param request
     * @return
     */
    Page<CommentVO> getCommentVOPage(Page<QuestionComment> commentPage);

    Long savaComment(QuestionCommentAddRequest commentAddRequest, HttpServletRequest request);

    Page<CommentVO> getCommentVO(QuestionCommentQueryRequest commentQueryRequest);
}
