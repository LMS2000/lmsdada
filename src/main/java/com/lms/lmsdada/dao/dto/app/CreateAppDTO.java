package com.lms.lmsdada.dao.dto.app;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 应用
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "CreateAppDTO对象", description = "应用")
public class CreateAppDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用名
     */
    @NotBlank(message = "应用名不能为空")
    @NotNull(message = "应用名不能为空")
    @Max(value = 80,message = "应用名长度必须小于80")
    @ApiModelProperty(value = "应用名")
    private String appName;

    /**
     * 应用描述
     */
    @NotBlank(message = "应用描述不能为空")
    @NotNull(message = "应用描述不能为空")
    @ApiModelProperty(value = "应用描述")
    private String appDesc;

    /**
     * 应用图标
     */
    @ApiModelProperty(value = "应用图标")
    private String appIcon;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    @NotNull(message = "应用类型不能为空")
    @ApiModelProperty(value = "应用类型（0-得分类，1-测评类）")
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    @NotNull(message = "评分策略不能为空")
    @ApiModelProperty(value = "评分策略（0-自定义，1-AI）")
    private Integer scoringStrategy;
}
