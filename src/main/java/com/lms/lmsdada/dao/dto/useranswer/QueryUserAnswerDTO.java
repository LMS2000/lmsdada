package com.lms.lmsdada.dao.dto.useranswer;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import com.lms.lmsdada.common.PageRequest;

/**
 * 用户答题记录
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "QueryUserAnswerDTO对象", description = "用户答题记录")
public class QueryUserAnswerDTO extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 应用 id
     */
    @ApiModelProperty(value = "应用 id")
    private Long appId;

    /**
     * 应用类型（0-得分类，1-角色测评类）
     */
    @ApiModelProperty(value = "应用类型（0-得分类，1-角色测评类）")
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    @ApiModelProperty(value = "评分策略（0-自定义，1-AI）")
    private Integer scoringStrategy;

    /**
     * 用户答案（JSON 数组）
     */
    @ApiModelProperty(value = "用户答案（JSON 数组）")
    private String choices;

    /**
     * 评分结果 id
     */
    @ApiModelProperty(value = "评分结果 id")
    private Long resultId;

    /**
     * 结果名称，如物流师
     */
    @ApiModelProperty(value = "结果名称，如物流师")
    private String resultName;

    /**
     * 结果描述
     */
    @ApiModelProperty(value = "结果描述")
    private String resultDesc;

    /**
     * 结果图标
     */
    @ApiModelProperty(value = "结果图标")
    private String resultPicture;

    /**
     * 得分
     */
    @ApiModelProperty(value = "得分")
    private Integer resultScore;

    /**
     * 用户 id
     */
    @ApiModelProperty(value = "用户 id")
    private Long userId;

    /**
     * id
     */
    @ApiModelProperty(value = "notId")
    private Long notId;

    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    private String searchText;

}
