package com.lms.lmsdada.dao.dto.useranswer;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
/**
 * 用户答案
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "DeleteUserAnswerDTO", description = "用户答案")
public class DeleteUserAnswerDTO implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @Positive(message = "id不合法")
    private Long id;

    private static final long serialVersionUID = 1L;
}
