/*
 Navicat Premium Dump SQL

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 90600 (9.6.0)
 Source Host           : localhost:3306
 Source Schema         : sani_civil_aid_db

 Target Server Type    : MySQL
 Target Server Version : 90600 (9.6.0)
 File Encoding         : 65001

 Date: 14/05/2026 20:40:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_civil_article
-- ----------------------------
DROP TABLE IF EXISTS `biz_civil_article`;
CREATE TABLE `biz_civil_article`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL COMMENT '所属分类ID',
  `article_num` int NOT NULL COMMENT '法条序号(第n条)',
  `content_cn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '汉文原文',
  `content_sani` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '民法典条文表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for biz_civil_category
-- ----------------------------
DROP TABLE IF EXISTS `biz_civil_category`;
CREATE TABLE `biz_civil_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NULL DEFAULT 0 COMMENT '上级分类ID',
  `name_cn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '汉文名称',
  `name_sani` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '民法典分类目录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for biz_civil_faq
-- ----------------------------
DROP TABLE IF EXISTS `biz_civil_faq`;
CREATE TABLE `biz_civil_faq`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'QA编号',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '领域',
  `question_cn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '中文提问',
  `question_sani` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `answer_cn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '中文回答',
  `answer_sani` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `article_refs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联法条',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for biz_consultation
-- ----------------------------
DROP TABLE IF EXISTS `biz_consultation`;
CREATE TABLE `biz_consultation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '提问用户ID(匿名可为空)',
  `question_cn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '汉文问题',
  `question_sani` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `answer_cn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '汉文答复',
  `answer_sani` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `sani_font` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态: 0未回答 1已回答',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '法律咨询记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for biz_legal_term
-- ----------------------------
DROP TABLE IF EXISTS `biz_legal_term`;
CREATE TABLE `biz_legal_term`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `frequency` int NULL DEFAULT 0 COMMENT '词频',
  `term_cn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '汉文术语',
  `term_sani` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '法律术语对照表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色代码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
