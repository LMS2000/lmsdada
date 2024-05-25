package com.lms.lmsdada.dao.dto.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 题目内容
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "QuestionContentDTO", description = "题目内容")
public class QuestionContentDTO {
    /**
     * 题目标题
     */
    @ApiModelProperty(value = "题目标题")
    private String title;

    /**
     * 题目选项列表
     */
    @ApiModelProperty(value = "题目选项列表")
    private List<Option> options;

    /**
     * 题目选项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "Option", description = "题目选项")
    public static class Option {
        @ApiModelProperty(value = "题目答案")
        private String result;
        @ApiModelProperty(value = "题目分数")
        private Integer score;
        @ApiModelProperty(value = "value")
        private String value;
        @ApiModelProperty(value = "key")
        private String key;
    }
}
