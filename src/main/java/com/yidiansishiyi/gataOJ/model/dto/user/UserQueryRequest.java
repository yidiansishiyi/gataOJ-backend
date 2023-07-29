package com.yidiansishiyi.gataOJ.model.dto.user;

import com.yidiansishiyi.gataOJ.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求
 *
 * @author  sanqi
 *  
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 开放平台id
     */
    private String unionId;

    /**
     * 公众号openId
     */
    private String mpOpenId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;

     /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 用户邮箱
     */
    private String userMailbox;

    private static final long serialVersionUID = 1L;
}