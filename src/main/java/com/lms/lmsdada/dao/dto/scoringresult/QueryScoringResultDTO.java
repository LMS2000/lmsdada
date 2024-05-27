package com.lms.lmsdada.dao.dto.scoringresult;

import com.lms.lmsdada.common.PageRequest;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 评分结果
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@Data
@ApiModel(value = "QueryScoringResultDTO对象", description = "评分结果")
public class QueryScoringResultDTO extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 结果名称，如物流师
     */
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
    @ApiModelProperty(value = "结果属性集合 JSON，如 [I,S,T,J]")
    private String resultProp;

    /**
     * 结果得分范围，如 80，表示 80及以上的分数命中此结果
     */
    @ApiModelProperty(value = "结果得分范围，如 80，表示 80及以上的分数命中此结果")
    private Integer resultScoreRange;

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
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;



}
