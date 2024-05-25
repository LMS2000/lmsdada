package com.lms;

import com.lms.generate.LmsCodeGenerator;
import org.junit.jupiter.api.Test;

public class generateTest {


    @Test
    public void test(){
        String sql="CREATE TABLE `user` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
                "  `username` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',\n" +
                "  `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',\n" +
                "  `union_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开放平台id',\n" +
                "  `mp_open_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公众号openId',\n" +
                "  `nickname` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',\n" +
                "  `user_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',\n" +
                "  `user_profile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',\n" +
                "  `user_role` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',\n" +
                "  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',\n" +
                "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `idx_unionId` (`union_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';" +
                "CREATE TABLE `user_answer` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
                "  `app_id` bigint(20) NOT NULL COMMENT '应用 id',\n" +
                "  `app_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '应用类型（0-得分类，1-角色测评类）',\n" +
                "  `scoring_strategy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '评分策略（0-自定义，1-AI）',\n" +
                "  `choices` text COLLATE utf8mb4_unicode_ci COMMENT '用户答案（JSON 数组）',\n" +
                "  `result_id` bigint(20) DEFAULT NULL COMMENT '评分结果 id',\n" +
                "  `result_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '结果名称，如物流师',\n" +
                "  `result_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '结果描述',\n" +
                "  `result_picture` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '结果图标',\n" +
                "  `result_score` int(11) DEFAULT NULL COMMENT '得分',\n" +
                "  `user_id` bigint(20) NOT NULL COMMENT '用户 id',\n" +
                "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `idx_appId` (`app_id`),\n" +
                "  KEY `idx_userId` (`user_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户答题记录';" +
                "CREATE TABLE `scoring_result` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
                "  `result_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '结果名称，如物流师',\n" +
                "  `result_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '结果描述',\n" +
                "  `result_picture` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '结果图片',\n" +
                "  `result_prop` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '结果属性集合 JSON，如 [I,S,T,J]',\n" +
                "  `result_score_range` int(11) DEFAULT NULL COMMENT '结果得分范围，如 80，表示 80及以上的分数命中此结果',\n" +
                "  `app_id` bigint(20) NOT NULL COMMENT '应用 id',\n" +
                "  `user_id` bigint(20) NOT NULL COMMENT '创建用户 id',\n" +
                "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `idx_appId` (`app_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评分结果';" +
                "CREATE TABLE `question` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
                "  `question_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '题目内容（json格式）',\n" +
                "  `app_id` bigint(20) NOT NULL COMMENT '应用 id',\n" +
                "  `user_id` bigint(20) NOT NULL COMMENT '创建用户 id',\n" +
                "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `idx_appId` (`app_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目';" +
                "CREATE TABLE `app` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
                "  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '应用名',\n" +
                "  `app_desc` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用描述',\n" +
                "  `app_icon` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用图标',\n" +
                "  `app_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '应用类型（0-得分类，1-测评类）',\n" +
                "  `scoring_strategy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '评分策略（0-自定义，1-AI）',\n" +
                "  `review_status` int(11) NOT NULL DEFAULT '0' COMMENT '审核状态：0-待审核, 1-通过, 2-拒绝',\n" +
                "  `review_message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核信息',\n" +
                "  `reviewer_id` bigint(20) DEFAULT NULL COMMENT '审核人 id',\n" +
                "  `review_time` datetime DEFAULT NULL COMMENT '审核时间',\n" +
                "  `user_id` bigint(20) NOT NULL COMMENT '创建用户 id',\n" +
                "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `idx_appName` (`app_name`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用';";

        LmsCodeGenerator.doGenerate("com.lms",sql,System.getProperty("user.dir")+"/generated");
    }
}
