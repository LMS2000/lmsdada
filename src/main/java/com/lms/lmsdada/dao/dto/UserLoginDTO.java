package com.lms.lmsdada.dao.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * 用户
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "UserLoginDTO", description = "用户登录")
public class UserLoginDTO  implements Serializable {
    @NotNull(message = "用户名不能为空")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String username;
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
    @NotNull(message = "图片验证码不能为空")
    @NotBlank(message = "图片验证码不能为空")
    @ApiModelProperty(value = "图片验证码")
    private String code;

}
