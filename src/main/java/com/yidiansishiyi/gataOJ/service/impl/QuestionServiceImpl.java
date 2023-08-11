package com.yidiansishiyi.gataOJ.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yidiansishiyi.gataOJ.model.entity.Question;
import com.yidiansishiyi.gataOJ.service.QuestionService;
import com.yidiansishiyi.gataOJ.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author zeroc
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2023-08-11 09:47:34
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




