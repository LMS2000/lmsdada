package com.lms.lmsdada.dao.dto.app;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
/**
 * 审核应用
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "ReviewAppDTO", description = "审核应用")
public class ReviewAppDTO implements Serializable {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @Positive(message = "id不合法")
    private Long id;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    @NotNull(message = "审核状态不能为空")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;


    private static final long serialVersionUID = 1L;
}
