package com.lms.lmsdada.dao.vo;

import com.lms.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录信息
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "AppVO对象", description = "用户登录信息")
public class LoginUserVO extends BaseVO {
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String username;




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
