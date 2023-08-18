package com.yidiansishiyi.gataoj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yidiansishiyi.gataoj.model.entity.Question;
import com.yidiansishiyi.gataoj.model.entity.QuestionFavour;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yidiansishiyi.gataoj.model.entity.User;

/**
* @author zeroc
* @description 针对表【question_favour(题目收藏)】的数据库操作Service
* @createDate 2023-08-17 19:58:37
*/
public interface QuestionFavourService extends IService<QuestionFavour> {
    /**
     * 题目收藏
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doQuestionFavour(long postId, User loginUser);

    /**
     * 分页获取用户收藏的题目列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper,
                                            long favourUserId);

    /**
     * 题目收藏（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doQuestionFavourInner(long userId, long postId);
}
