package com.lms.lmsdada.dao.dto.app;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import com.lms.lmsdada.common.PageRequest;

/**
 * 应用
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "QueryAppDTO对象", description = "应用")
public class QueryAppDTO extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * notId
     */
    @ApiModelProperty(value = "notId")
    private Long notId;
    /**
     * 应用名
     */
    @ApiModelProperty(value = "应用名")
    private String appName;

    /**
     * 应用描述
     */
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
    @ApiModelProperty(value = "应用类型（0-得分类，1-测评类）")
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    @ApiModelProperty(value = "评分策略（0-自定义，1-AI）")
    private Integer scoringStrategy;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    @ApiModelProperty(value = "审核状态：0-待审核, 1-通过, 2-拒绝")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @ApiModelProperty(value = "审核信息")
    private String reviewMessage;

    /**
     * 审核人 id
     */
    @ApiModelProperty(value = "审核人 id")
    private Long reviewerId;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private Date reviewTime;

    /**
     * 创建用户 id
     */
    @ApiModelProperty(value = "创建用户 id")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    /**
     * 搜索词
     */
    private String searchText;
}
