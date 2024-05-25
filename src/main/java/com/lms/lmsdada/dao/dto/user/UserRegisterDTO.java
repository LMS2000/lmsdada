package com.lms.lmsdada.dao.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "RegisterUserDTO", description = "用户注册")
public class UserRegisterDTO {

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @NotBlank(message = "用户名不能为空")
    @Max(value = 16,message = "用户名过长")
    @ApiModelProperty(value = "用户昵称")
    private String nickname;
    /**
     * 账号
     */
    @NotNull(message = "账号不能为空")
    @NotBlank(message = "账号不能为空")
    @Min(value = 4,message = "账号过短")
    @ApiModelProperty(value = "账号")
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @Min(value = 8,message = "密码过短")
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    @NotBlank(message = "确认密码不能为空")
    @Min(value = 8,message = "密码过短")
    @ApiModelProperty(value = "确认密码")
    private String checkPassword;

}
