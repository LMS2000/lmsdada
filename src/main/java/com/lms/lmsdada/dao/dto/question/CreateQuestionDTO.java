package com.lms.lmsdada.dao.dto.question;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "CreateQuestionDTO对象", description = "题目")
public class CreateQuestionDTO implements Serializable {

    /**
     * 题目内容（json格式）
     */
    @NotNull(message = "题目内容不能为空")
    private List<QuestionContentDTO> questionContent;

    /**
     * 应用 id
     */
    @NotNull(message = "应用id不能为空")
    @Positive(message = "应用id不合法")
    private Long appId;

    private static final long serialVersionUID = 1L;

}
