package com.lms.lmsdada.dao.entity;

import lombok.*;
import com.lms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 评分结果
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "scoring_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoringResult extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 结果名称，如物流师
     */
    private String resultName;


    /**
     * 结果描述
     */
    private String resultDesc;


    /**
     * 结果图片
     */
    private String resultPicture;


    /**
     * 结果属性集合 JSON，如 [I,S,T,J]
     */
    private String resultProp;


    /**
     * 结果得分范围，如 80，表示 80及以上的分数命中此结果
     */
    private Integer resultScoreRange;


    /**
     * 应用 id
     */
    private Long appId;


    /**
     * 创建用户 id
     */
    private Long userId;


    /**
     * 是否删除
     */
    private Integer isDelete;


}
