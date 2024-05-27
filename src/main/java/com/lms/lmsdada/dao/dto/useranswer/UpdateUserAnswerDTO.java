package com.lms.lmsdada.dao.dto.useranswer;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户答题记录
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "UpdateUserAnswerDTO对象", description = "用户答题记录")
public class UpdateUserAnswerDTO implements Serializable {


    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @Positive(message = "id不合法")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;

    private static final long serialVersionUID = 1L;

}
