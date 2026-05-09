/*
 Navicat Premium Dump SQL

 Source Server         : nuosu_civil_aid_db
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : nuosu_civil_aid_db

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 08/05/2026 20:40:09
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
  `content_nuosu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '规范彝文原文',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '民法典条文表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_civil_article
-- ----------------------------
INSERT INTO `biz_civil_article` VALUES (1, 1, 1, '为了保护民事主体的合法权益，调整民事关系，维护社会和经济秩序，尊重社会主义核心价值观，根据宪法，制定本法。', 'ꍏꉸꈲꈎꏓꂱꈄꏍꀋꄜꀋꄉꐥꌠꄜꅉꀋꄜꇬꄜꇅꇈꀐꄻꄉꐥ', '2026-05-08 20:08:17', '2026-05-08 20:08:17', 0);
INSERT INTO `biz_civil_article` VALUES (2, 1, 2, '民法调整平等主体的自然人、法人和非法人组织之间的人身关系和财产关系。', 'ꏓꂱꈄꏍꌠꆈꌠꐛꄧꐚꇉꄜꐛꇈꑌꄜꄉꐥꄜꅉꀋꄜ', '2026-05-08 20:08:17', '2026-05-08 20:08:17', 0);
INSERT INTO `biz_civil_article` VALUES (3, 3, 464, '合同是民事主体之间设立、变更、终止民事法律关系的协议。', 'ꄧꄉꌠꏓꂱꊈꌠꐛꄧꐚꇉꄜꐛꇈꑌꄜꄉꐥ', '2026-05-08 20:17:20', '2026-05-08 20:17:20', 0);
INSERT INTO `biz_civil_article` VALUES (4, 4, 1040, '本编调整因婚姻家庭产生的民事关系。', 'ꊇꇅꇉꄧꑼꂠꌠꏓꂱꈄꏍꀋꄜꀋꄉꐥꌠꄜꅉꀋꄜ', '2026-05-08 20:17:20', '2026-05-08 20:17:20', 0);

-- ----------------------------
-- Table structure for biz_civil_category
-- ----------------------------
DROP TABLE IF EXISTS `biz_civil_category`;
CREATE TABLE `biz_civil_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NULL DEFAULT 0 COMMENT '上级分类ID',
  `name_cn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '汉文名称',
  `name_nuosu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规范彝文名称',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '民法典分类目录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_civil_category
-- ----------------------------
INSERT INTO `biz_civil_category` VALUES (1, 0, '总则编', 'ꏓꂱꈄꏍꑼꂠ', 1, '2026-05-08 20:08:17', 0);
INSERT INTO `biz_civil_category` VALUES (2, 0, '物权编', 'ꏇꃅꑼꂠ', 2, '2026-05-08 20:08:17', 0);
INSERT INTO `biz_civil_category` VALUES (3, 0, '合同编', 'ꄧꄉꑼꂠ', 3, '2026-05-08 20:17:20', 0);
INSERT INTO `biz_civil_category` VALUES (4, 0, '婚姻家庭', 'ꊇꇅꇉꄧ', 4, '2026-05-08 20:17:20', 0);

-- ----------------------------
-- Table structure for biz_consultation
-- ----------------------------
DROP TABLE IF EXISTS `biz_consultation`;
CREATE TABLE `biz_consultation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '提问用户ID(匿名可为空)',
  `question_cn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '汉文问题',
  `question_nuosu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '彝文问题',
  `answer_cn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '汉文答复',
  `answer_nuosu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '彝文答复',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态: 0未回答 1已回答',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '法律咨询记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of biz_consultation
-- ----------------------------
INSERT INTO `biz_consultation` VALUES (1, 1, '什么是民法典？', 'ꏓꂱꈄꏍꌠꄜꀋꄜꀋ?', '民法典是新中国第一部以法典命名的法律。', 'ꏓꂱꈄꏍꌠꍏꉸꈲꈎꏓꂱꈄꏍꀋꄜꀋꄉꐥꌠꄜꅉꀋꄜꇬꄜꇅꇈꀐꄻꄉꐥ.', 1, '2026-05-08 18:58:05', 0);

-- ----------------------------
-- Table structure for biz_legal_term
-- ----------------------------
DROP TABLE IF EXISTS `biz_legal_term`;
CREATE TABLE `biz_legal_term`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `term_cn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '汉文术语',
  `term_nuosu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '彝文术语',
  `definition_cn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '汉文解释',
  `definition_nuosu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '彝文解释',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '法律术语对照表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_legal_term
-- ----------------------------
INSERT INTO `biz_legal_term` VALUES (1, '法人', 'ꏓꂱꊈꌠ', '法人是具有民事权利能力和民事行为能力的组织。', 'ꏓꂱꊈꌠꌠꏓꂱꈄꏍꀋꄜꀋꄉꐥꌠꄜꅉꀋꄜꇬꄜꇅꇈꀐꄻꄉꐥ', '2026-05-08 20:20:26', 0);
INSERT INTO `biz_legal_term` VALUES (2, '民事权利', 'ꏓꂱꈄꏍꉠꄜ', '民事主体依法享有的人身权利、财产权利以及其他合法权益。', 'ꆈꌠꐛꄧꐚꇉꄜꐛꇈꑌꄜꄉꐥꄜꅉꀋꄜ', '2026-05-08 20:20:26', 0);

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
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'admin', '系统超级管理员', '2026-05-08 18:58:05', 0);
INSERT INTO `sys_role` VALUES (2, '普通用户', 'user', '普通查询用户', '2026-05-08 18:58:05', 0);

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

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$2jzFvFSwdQlXO0KF1hngDO/HpmlfPm2PUihwKGfAiUNhpXy/Crq/y', '13800000000', 1, '2026-05-08 18:58:05', '2026-05-08 20:03:03', 0);

SET FOREIGN_KEY_CHECKS = 1;
