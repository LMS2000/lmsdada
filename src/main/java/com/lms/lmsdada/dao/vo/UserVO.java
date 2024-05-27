package com.lms.lmsdada.dao.vo;

import lombok.Data;
import com.lms.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

/**
 * 用户
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "UserVO对象", description = "用户")
public class UserVO extends BaseVO {


    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String username;


    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;


    /**
     * 微信开放平台id
     */
    @ApiModelProperty(value = "微信开放平台id")
    private String unionId;


    /**
     * 公众号openId
     */
    @ApiModelProperty(value = "公众号openId")
    private String mpOpenId;


    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickname;


    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatar;


    /**
     * 用户简介
     */
    @ApiModelProperty(value = "用户简介")
    private String userProfile;


    /**
     * 用户角色：user/admin/ban
     */
    @ApiModelProperty(value = "用户角色：user/admin/ban")
    private String userRole;


    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;


}
