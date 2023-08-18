package com.yidiansishiyi.gataoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yidiansishiyi.gataoj.common.ErrorCode;
import com.yidiansishiyi.gataoj.constant.CommonConstant;
import com.yidiansishiyi.gataoj.exception.ThrowUtils;
import com.yidiansishiyi.gataoj.model.dto.questioncomment.QuestionCommentAddRequest;
import com.yidiansishiyi.gataoj.model.dto.questioncomment.QuestionCommentQueryRequest;
import com.yidiansishiyi.gataoj.model.entity.QuestionComment;
import com.yidiansishiyi.gataoj.model.entity.User;
import com.yidiansishiyi.gataoj.model.vo.CommentVO;
import com.yidiansishiyi.gataoj.service.QuestionCommentService;
import com.yidiansishiyi.gataoj.mapper.QuestionCommentMapper;
import com.yidiansishiyi.gataoj.service.UserService;
import com.yidiansishiyi.gataoj.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author zeroc
* @description 针对表【question_comment(题目评论)】的数据库操作Service实现
* @createDate 2023-08-17 19:58:37
*/
@Service
public class QuestionCommentServiceImpl extends ServiceImpl<QuestionCommentMapper, QuestionComment>
    implements QuestionCommentService {
    @Resource
    private UserService userService;

    /**
     * 获取查询包装类
     *
     * @param commentQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionComment> getQueryWrapper(QuestionCommentQueryRequest commentQueryRequest) {
        QueryWrapper<QuestionComment> queryWrapper = new QueryWrapper<>();
        if (commentQueryRequest == null) {
            return queryWrapper;
        }
        String searchText = commentQueryRequest.getSearchText();
        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();
        Long id = commentQueryRequest.getId();
        Long userId = commentQueryRequest.getUserId();
        Long notId = commentQueryRequest.getNotId();
        Long parentId = commentQueryRequest.getParentId();
        Long questionId = commentQueryRequest.getQuestionId();

        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("content", searchText);
        }

        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.isNull(ObjectUtils.isEmpty(parentId), "parentId");
        queryWrapper.eq(ObjectUtils.isNotEmpty(parentId), "parentId", parentId);

        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }


    @Override
    public Page<CommentVO> getCommentVOPage(Page<QuestionComment> commentPage) {
        List<QuestionComment> commentList = commentPage.getRecords();
        Page<CommentVO> commentVOPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        if (CollectionUtils.isEmpty(commentList)) {
            return commentVOPage;
        }

        // 提取评论中的用户 ID
        List<Long> userIds = commentList.stream()
                .map(QuestionComment::getUserId)
                .collect(Collectors.toList());

        // 查询用户信息
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.select(User::getId, User::getUserAvatar, User::getUserName)
                .in(User::getId, userIds);
        List<User> users = userService.list(userQueryWrapper);

        // 构建 CommentVO 列表并填充信息
        List<CommentVO> commentVOList = commentList.stream()
                .map(comment -> {
                    CommentVO commentVO = CommentVO.objToVo(comment);
                    Long userId = comment.getUserId();
                    // 用户过滤, 用户 id 等于评论 id 时候复制信息
                    User user = users.stream()
                            .filter(u -> u.getId().equals(userId))
                            .findFirst()
                            .orElse(null);
                    if (user != null) {
                        commentVO.setUserName(user.getUserName());
                        commentVO.setUserAvatar(user.getUserAvatar());
                    }
                    return commentVO;
                }).collect(Collectors.toList());

        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

    @Override
    public Long savaComment(QuestionCommentAddRequest commentAddRequest, HttpServletRequest request) {
        // 敏感词校验

        QuestionComment comment = new QuestionComment();
        BeanUtils.copyProperties(commentAddRequest, comment);
        comment.setUserId(userService.getLoginUser(request).getId());
        comment.setParentId(commentAddRequest.getParentId());
        comment.setQuestionId(commentAddRequest.getQuestionId());

        boolean result = this.save(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newCommentId = comment.getId();
        return newCommentId;
    }

    @Override
    public Page<CommentVO> getCommentVO(QuestionCommentQueryRequest commentQueryRequest) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 收集复合要求的评论
        Page<QuestionComment> commentPage = this.page(new Page<>(current, size), this.getQueryWrapper(commentQueryRequest));

        Page<CommentVO> commentVOPage = this.getCommentVOPage(commentPage);

        return commentVOPage;
    }
}




