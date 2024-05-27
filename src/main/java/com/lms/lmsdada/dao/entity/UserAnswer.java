package com.lms.lmsdada.dao.entity;

import lombok.*;
import com.lms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户答题记录
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user_answer")
@Data
public class UserAnswer extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 应用 id
     */
    private Long appId;


    /**
     * 应用类型（0-得分类，1-角色测评类）
     */
    private Integer appType;


    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;


    /**
     * 用户答案（JSON 数组）
     */
    private String choices;


    /**
     * 评分结果 id
     */
    private Long resultId;


    /**
     * 结果名称，如物流师
     */
    private String resultName;


    /**
     * 结果描述
     */
    private String resultDesc;


    /**
     * 结果图标
     */
    private String resultPicture;


    /**
     * 得分
     */
    private Integer resultScore;


    /**
     * 用户 id
     */
    private Long userId;


    /**
     * 是否删除
     */
    private Integer isDelete;


}
