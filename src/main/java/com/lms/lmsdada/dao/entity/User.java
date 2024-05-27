package com.lms.lmsdada.dao.entity;

import lombok.*;
import com.lms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 账号
     */
    private String username;


    /**
     * 密码
     */
    private String password;


    /**
     * 微信开放平台id
     */
    private String unionId;


    /**
     * 公众号openId
     */
    private String mpOpenId;


    /**
     * 用户昵称
     */
    private String nickname;


    /**
     * 用户头像
     */
    private String userAvatar;


    /**
     * 用户简介
     */
    private String userProfile;


    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;


    /**
     * 是否删除
     */
    private Integer isDelete;


}
