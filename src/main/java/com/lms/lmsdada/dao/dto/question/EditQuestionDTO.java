package com.lms.lmsdada.dao.dto.question;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;
/**
 * 题目编辑
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "EditQuestionDTO", description = "题目编辑")
public class EditQuestionDTO implements Serializable {

    /**
     * id
     */
    @NotNull(message = "题目id")
    @Positive(message = "id不合法")
    private Long id;

    /**
     * 题目内容（json格式）
     */
    @NotNull(message = "题目内容不能为空")
    private List<QuestionContentDTO> questionContent;

    private static final long serialVersionUID = 1L;
}
