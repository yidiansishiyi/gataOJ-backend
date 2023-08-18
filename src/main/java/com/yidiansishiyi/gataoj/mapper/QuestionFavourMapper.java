package com.yidiansishiyi.gataoj.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yidiansishiyi.gataoj.model.entity.Question;
import com.yidiansishiyi.gataoj.model.entity.QuestionFavour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

/**
* @author zeroc
* @description 针对表【question_favour(帖子收藏)】的数据库操作Mapper
* @createDate 2023-08-17 19:58:37
* @Entity com.yidiansishiyi.gataoj.model.entity.QuestionFavour
*/
public interface QuestionFavourMapper extends BaseMapper<QuestionFavour> {
    /**
     * 分页查询收藏帖子列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listFavourQuestionByPage(IPage<Question> page, @Param(Constants.WRAPPER) Wrapper<Question> queryWrapper,
                                    long favourUserId);
}




