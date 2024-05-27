package com.lms.lmsdada.dao.dto.question;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import com.lms.lmsdada.common.PageRequest;

/**
 * 题目
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "QueryQuestionDTO对象", description = "题目")
public class QueryQuestionDTO extends PageRequest implements Serializable {

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
     * 题目内容（json格式）
     */
    @ApiModelProperty(value = "题目内容（json格式）")
    private String questionContent;

    /**
     * 应用 id
     */
    @ApiModelProperty(value = "应用 id")
    private Long appId;

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
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

}
