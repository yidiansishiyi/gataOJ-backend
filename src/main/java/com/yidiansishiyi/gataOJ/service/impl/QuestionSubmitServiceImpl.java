package com.yidiansishiyi.gataOJ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yidiansishiyi.gataOJ.common.ErrorCode;
import com.yidiansishiyi.gataOJ.constant.CommonConstant;
import com.yidiansishiyi.gataOJ.exception.BusinessException;
import com.yidiansishiyi.gataOJ.mapper.QuestionSubmitMapper;
import com.yidiansishiyi.gataOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yidiansishiyi.gataOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yidiansishiyi.gataOJ.model.entity.*;
import com.yidiansishiyi.gataOJ.model.enums.QuestionSubmitLanguageEnum;
import com.yidiansishiyi.gataOJ.model.enums.QuestionSubmitStatusEnum;
import com.yidiansishiyi.gataOJ.model.vo.QuestionSubmitVO;
import com.yidiansishiyi.gataOJ.model.vo.QuestionVO;
import com.yidiansishiyi.gataOJ.service.QuestionService;
import com.yidiansishiyi.gataOJ.service.QuestionSubmitService;
import com.yidiansishiyi.gataOJ.service.UserService;
import com.yidiansishiyi.gataOJ.utils.SqlUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
* @author zeroc
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2023-08-11 09:47:34
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        // 拼接查询条件
        String language = questionSubmitQueryRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        queryWrapper.eq(enumByValue!=null,"language",enumByValue);
        Integer status = questionSubmitQueryRequest.getStatus();
        QuestionSubmitStatusEnum statusEnum = QuestionSubmitStatusEnum.getEnumByValue(status);
        queryWrapper.eq(statusEnum!=null,"status",status);
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId),"questionId",questionId);
        Long userId = questionSubmitQueryRequest.getUserId();
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"userId",userId);
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        if (!questionSubmit.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage =
                new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }

        HashSet<Long> questionSubmitIds = new HashSet<>();
        HashSet<Long> userIds = new HashSet<>();
        questionSubmitList.forEach(res -> {
            questionSubmitIds.add(res.getQuestionId());
            userIds.add(res.getUserId());
        });

        Map<Long, List<Question>> questionListMap = questionService.listByIds(questionSubmitIds)
                .stream()
                .collect(Collectors.groupingBy(Question::getId));
        Map<Long, List<User>> userListMap = userService.listByIds(userIds).stream()
                .collect(Collectors.groupingBy(User::getId));

        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> {
                    QuestionSubmitVO questionSubmitVO = getQuestionSubmitVO(questionSubmit, loginUser);
                    Long questionId = questionSubmitVO.getQuestionId();
                    Question question = null;
                    if (questionListMap.containsKey(questionId)) {
                        question = questionListMap.get(questionId).get(0);
                    }
                    questionSubmitVO.setQuestionVO(QuestionVO.objToVo(question));
                    Long usersId = questionSubmitVO.getUserId();
                    User user = null;
                    if (userListMap.containsKey(usersId)) {
                        user = userListMap.get(usersId).get(0);
                    }
                    questionSubmitVO.setUserVO(userService.getUserVO(user));
                    return questionSubmitVO;
                }).collect(Collectors.toList());

        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        QuestionSubmit questionSubmit = new QuestionSubmit();

        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setQuestionId(questionSubmitAddRequest.getQuestionId());
        questionSubmit.setUserId(loginUser.getId());

        boolean save = this.save(questionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        return questionSubmit.getId();
    }

}




