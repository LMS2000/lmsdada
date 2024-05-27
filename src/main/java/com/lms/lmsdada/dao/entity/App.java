package com.lms.lmsdada.dao.entity;

import lombok.*;
import com.lms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 应用
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "app")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class App extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 应用名
     */
    private String appName;


    /**
     * 应用描述
     */
    private String appDesc;


    /**
     * 应用图标
     */
    private String appIcon;


    /**
     * 应用类型（0-得分类，1-测评类）
     */
    private Integer appType;


    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;


    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;


    /**
     * 审核信息
     */
    private String reviewMessage;


    /**
     * 审核人 id
     */
    private Long reviewerId;


    /**
     * 审核时间
     */
    private Date reviewTime;


    /**
     * 创建用户 id
     */
    private Long userId;


    /**
     * 是否删除
     */
    private Integer isDelete;


}
