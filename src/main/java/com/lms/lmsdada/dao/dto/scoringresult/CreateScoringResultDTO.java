package com.lms.lmsdada.dao.dto.scoringresult;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评分结果
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "CreateScoringResultDTO对象", description = "评分结果")
public class CreateScoringResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @Positive(message = "id不合法")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 结果名称，如物流师
     */
    @NotNull(message = "结果名字不能为空")
    @NotBlank(message = "结果名字不能为空")
    @ApiModelProperty(value = "结果名称，如物流师")
    private String resultName;

    /**
     * 结果描述
     */
    @ApiModelProperty(value = "结果描述")
    private String resultDesc;

    /**
     * 结果图片
     */
    @ApiModelProperty(value = "结果图片")
    private String resultPicture;

    /**
     * 结果属性集合 JSON，如 [I,S,T,J]
     */
    @NotNull(message = "结果属性不能为空")
    @ApiModelProperty(value = "结果属性集合 JSON，如 [I,S,T,J]")
    private List<String> resultProp;

    /**
     * 结果得分范围，如 80，表示 80及以上的分数命中此结果
     */
    @ApiModelProperty(value = "结果得分范围，如 80，表示 80及以上的分数命中此结果")
    private Integer resultScoreRange;

    /**
     * 应用 id
     */
    @NotNull(message = "应用id不能为空")
    @Positive(message = "应用id不合法")
    @ApiModelProperty(value = "应用 id")
    private Long appId;


}
