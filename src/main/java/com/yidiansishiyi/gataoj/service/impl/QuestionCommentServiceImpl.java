package com.yidiansishiyi.gataoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yidiansishiyi.gataoj.model.entity.QuestionComment;
import com.yidiansishiyi.gataoj.service.QuestionCommentService;
import com.yidiansishiyi.gataoj.mapper.QuestionCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author zeroc
* @description 针对表【question_comment(题目评论)】的数据库操作Service实现
* @createDate 2023-08-17 19:58:37
*/
@Service
public class QuestionCommentServiceImpl extends ServiceImpl<QuestionCommentMapper, QuestionComment>
    implements QuestionCommentService{

}




