package com.lms.lmsdada.dao.dto.question;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
/**
 * AI生成题目
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "AiGenerateQuestionDTO", description = "AI生成题目")
public class AiGenerateQuestionDTO implements Serializable {
    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 题目数
     */
    int questionNumber = 10;

    /**
     * 选项数
     */
    int optionNumber = 2;

    private static final long serialVersionUID = 1L;
}
