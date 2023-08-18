package com.yidiansishiyi.gataoj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目评论
 * @TableName question_comment
 */
@TableName(value ="question_comment")
@Data
public class QuestionComment implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 实体类型
     */
    private Integer entityType;

    /**
     * 题名 id
     */
    private Long questionId;

    /**
     * 父级 id
     */
    private Long parentId;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}