package com.lms.lmsdada.dao.vo;

import com.lms.lmsdada.dao.dto.question.QuestionContentDTO;
import lombok.Data;
import com.lms.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 题目
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "QuestionVO对象", description = "题目")
public class QuestionVO extends BaseVO {


    /**
     * 题目内容（json格式）
     */
    @ApiModelProperty(value = "题目内容")
    private List<QuestionContentDTO> questionContent;


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
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;


}
