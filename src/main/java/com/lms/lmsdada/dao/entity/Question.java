package com.lms.lmsdada.dao.entity;

import lombok.*;
import com.lms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 题目
 *
 * @author LMS2000
 * @since 2024-05-23
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 题目内容（json格式）
     */
    private String questionContent;


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
