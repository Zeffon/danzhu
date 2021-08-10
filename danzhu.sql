/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 118.25.210.135:3306
 Source Schema         : danzhu

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 29/04/2021 17:19:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL COMMENT '收集者(管理员)id',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收集组标题',
  `summary` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收集组简介',
  `code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码，用于搜索',
  `valid_period` tinyint(3) NULL DEFAULT NULL COMMENT '有效期：1天 - 7天 ',
  `user_limit` tinyint(5) NULL DEFAULT NULL COMMENT '收集夹用户数上限值',
  `user_number` tinyint(4) NULL DEFAULT 40 COMMENT '收集夹的用户数',
  `end_time` datetime(3) NULL DEFAULT NULL COMMENT '收集组截止收集文件的时间(如果当前时间大于过去时间则不再接收文件)',
  `file_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '0.不限 1. 文档 2.图片 3. 视频（1-3可多选）以字符串方式，其中以英语逗号分隔',
  `group_id` int(10) NULL DEFAULT NULL COMMENT '有数值表示指定用户组（默认为null为不指定）',
  `remark` json NULL COMMENT '上传扩展字段（此字段只在选择用户组时生效，用于备注所有用户上传时的用户姓名之类限制）',
  `file_number` tinyint(2) NULL DEFAULT 1 COMMENT '提交文件的数量（默认1）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '用户申请加入时 0.不需要验证  1.表示需要验证  2.不可加入（默认为0）',
  `create_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (1, 43, '高级架构实训作业', '10周前收集完毕', 'CAA0574874', 10, 100, 50, '2020-10-15 18:26:43.750', '0', NULL, '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"姓名\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', 1, 1, '2020-10-05 18:26:43.559', '2020-11-08 20:38:56.292', NULL);
INSERT INTO `collect` VALUES (2, 73, '高级架构实训作业', '10周前收集完毕', 'CAA0591056', 10, 100, 50, '2020-10-15 18:32:12.914', '0', NULL, '[]', 1, 1, '2020-10-05 18:32:12.684', '2020-11-16 23:19:47.711', NULL);
INSERT INTO `collect` VALUES (3, 73, '17软件4班', '大数据作业收集', 'CAA2806310', 3, 50, 5, '2020-10-31 16:24:17.490', '0', NULL, '[]', 1, 0, '2020-10-28 16:25:07.109', '2020-11-16 23:19:52.014', NULL);
INSERT INTO `collect` VALUES (4, 73, '17软件4班', '实训作业收集', 'CAA2892285', 3, 50, 0, '2020-10-31 17:02:04.923', '0', NULL, '[]', 1, 0, '2020-10-28 17:02:13.059', '2020-11-04 15:35:03.628', NULL);
INSERT INTO `collect` VALUES (5, 73, '人工智能收集', '大家请及时提交', 'CAA2892237', 3, 50, 0, '2020-10-31 17:04:44.932', '0', NULL, '[]', 1, 0, '2020-10-28 17:04:48.150', '2020-11-04 15:35:01.079', NULL);
INSERT INTO `collect` VALUES (6, 73, '人工智能收集', '大家请及时提交', 'CAA2847991', 3, 50, 0, '2020-10-31 17:05:21.479', '0', NULL, '[]', 1, 0, '2020-10-28 17:05:21.449', '2020-11-04 15:34:59.552', NULL);
INSERT INTO `collect` VALUES (7, 73, '四班来测试', '就只是测试而已', 'CAA2827473', 3, 50, 8, '2020-10-31 19:01:55.286', '0', 5, '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', 1, 0, '2020-10-28 19:02:01.397', '2020-11-04 15:34:58.006', NULL);
INSERT INTO `collect` VALUES (8, 73, '再来测试一下', '', 'CAA2804043', 3, 50, 8, '2020-10-31 19:14:11.041', '0', 5, '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', 1, 0, '2020-10-28 19:14:11.417', '2020-11-04 17:24:03.428', NULL);
INSERT INTO `collect` VALUES (9, 73, '测试2轮', '', 'CAA2844530', 3, 50, 8, '2020-10-31 19:14:59.451', '0', 5, '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', 1, 0, '2020-10-28 19:14:59.735', '2020-11-04 17:23:53.114', NULL);

-- ----------------------------
-- Table structure for file_library
-- ----------------------------
DROP TABLE IF EXISTS `file_library`;
CREATE TABLE `file_library`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `collect_id` int(10) NULL DEFAULT NULL COMMENT '收集夹id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件url',
  `size` int(10) NULL DEFAULT NULL COMMENT '文件大小，单位是b（如 82.2kb = 84265b）',
  `online` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '文件是否公开，0表示私有，1表示公开',
  `category` tinyint(3) NULL DEFAULT NULL COMMENT '类型, 1 表示图片image， 2表示文件file',
  `create_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 123 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_library
-- ----------------------------

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL COMMENT '创建者（管理员）id',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码，用于搜索到该用户组',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户组的名称',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户组的简介',
  `remark` json NULL COMMENT '用户组备注基本信息',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户申请加入时 0.不需要验证  1.表示需要验证  2.不可加入（默认为0）',
  `user_number` int(4) NULL DEFAULT 0 COMMENT '用户组的用户数',
  `create_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE COMMENT 'code唯一索引',
  INDEX `idx_id_user`(`id`, `user_id`, `delete_time`) USING BTREE COMMENT 'id与user_id的联合索引'
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of groups
-- ----------------------------
INSERT INTO `groups` VALUES (1, 43, 'GAA0475681', '王一', '', '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"姓名\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', 0, 0, '2020-10-04 20:09:16.051', '2020-10-31 10:20:40.967', NULL);
INSERT INTO `groups` VALUES (2, 43, 'GAA0475682', '王二', '', '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"姓名\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', 1, 1, '2020-10-04 20:09:16.051', '2020-10-31 10:20:42.519', NULL);
INSERT INTO `groups` VALUES (3, 43, 'GAA0475683', '王三', '', '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"姓名\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', 2, 0, '2020-10-04 20:09:16.051', '2020-10-31 10:20:43.810', NULL);

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `collect_id` int(11) NULL DEFAULT NULL COMMENT '收集组id',
  `user_id` int(10) NULL DEFAULT NULL COMMENT '管理员id',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'code唯一编号',
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'url',
  `password_type` tinyint(1) NULL DEFAULT 0 COMMENT '密码的类型：0. 随机 1. 自定义',
  `password` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接的提取码（4位 字母+数字）',
  `valid_period` tinyint(1) UNSIGNED NULL DEFAULT 7 COMMENT '有效期：0. 永久 1. 1天  7. 7天 ',
  `end_time` datetime(3) NULL DEFAULT NULL COMMENT '链接设置有效期的结束分享时间（用于给用户展示）',
  `create_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of link
-- ----------------------------
INSERT INTO `link` VALUES (1, 23, 73, 'LAB1519525', 'https://www.zeffon.cn/link?s=LAB1519525', 0, 'mnjf', 7, '2020-11-22 13:41:05.180', '2020-11-15 13:41:06.298', '2021-03-31 08:26:29.531', NULL);
INSERT INTO `link` VALUES (2, 23, 73, 'LAB1524111', 'https://www.zeffon.cn/link?s=LAB1524111', 0, 'qp86', 7, '2020-11-22 13:44:49.230', '2020-11-15 13:44:50.331', '2021-03-31 08:26:33.944', NULL);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `online` tinyint(3) UNSIGNED NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 'share-btn', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `openid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码，用于搜索',
  `scope` tinyint(3) UNSIGNED NULL DEFAULT 1,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `wx_info` json NULL COMMENT '微信用户信息',
  `create_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (32, 'oDkfx5ChZ-n5Hyd012fdg22rffg4fdg3', 'UAA1800401', 1, NULL, NULL, NULL, NULL, '{\"city\": \"Guangzhou\", \"gender\": 1, \"country\": \"China\", \"language\": \"zh_CN\", \"nickName\": \"路人乙\", \"province\": \"Guangdong\", \"avatarUrl\": \"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLN2SEhW7fSdcI6XiaJSiaJ5n29L4YjVquViaP4PEgaxP1KtN1WUYJr8PZcFXDHKMGqDg6AVVjfzPibsw/132\"}', '2020-10-18 10:08:23.003', '2020-10-24 12:52:10.044', NULL);
INSERT INTO `user` VALUES (33, 'oDkfx5ChZ-n5Hyd012fdg2fg22g33g', 'UAA1800402', 1, NULL, NULL, NULL, NULL, '{\"city\": \"Guangzhou\", \"gender\": 1, \"country\": \"China\", \"language\": \"zh_CN\", \"nickName\": \"小白\", \"province\": \"Guangdong\", \"avatarUrl\": \"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLN2SEhW7fSdcI6XiaJSiaJ5n29L4YjVquViaP4PEgaxP1KtN1WUYJr8PZcFXDHKMGqDg6AVVjfzPibsw/132\"}', '2020-10-18 10:08:23.003', '2020-10-24 12:51:50.966', NULL);

-- ----------------------------
-- Table structure for user_collect
-- ----------------------------
DROP TABLE IF EXISTS `user_collect`;
CREATE TABLE `user_collect`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `collect_id` int(10) NOT NULL COMMENT '收集组id',
  `remark` json NULL COMMENT '收集夹里用户备注信息',
  `status` tinyint(3) UNSIGNED NULL DEFAULT 0 COMMENT '状态值：0.表示非用户组可能想上传 1.表示非用户组已上传. 2.表示用户组未上传 3.用户组已上传',
  `create_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_collect
-- ----------------------------
INSERT INTO `user_collect` VALUES (36, 74, 23, '[{\"key\": \"姓名\", \"value\": \"\"}]', 1, '2020-11-05 13:19:28.505', '2020-11-28 10:51:25.836', '2020-11-28 10:51:25.196');
INSERT INTO `user_collect` VALUES (37, 32, 23, '[{\"key\": \"姓名\", \"value\": \"\"}]', 1, '2020-11-05 13:19:28.505', '2020-11-28 09:12:34.895', NULL);

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `group_id` int(10) NOT NULL COMMENT '用户组id',
  `remark` json NULL COMMENT '用户组要求用户备注信息',
  `create_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_group`(`user_id`, `group_id`, `delete_time`) USING BTREE COMMENT 'user_id和group_id联合索引'
) ENGINE = InnoDB AUTO_INCREMENT = 124 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_group
-- ----------------------------
INSERT INTO `user_group` VALUES (1, 33, 5, '[{\"key\": \"学号\", \"value\": \"20170407430439\"}, {\"key\": \"姓名\", \"value\": \"诸神之战12rfboedbc gvdfbcscdohobsduvbosu3\"}, {\"key\": \"班级\", \"value\": \"黄昏来临123\"}, {\"key\": \"学号\", \"value\": \"20170407430439\"}, {\"key\": \"姓名\", \"value\": \"诸神之战12rfboedbcgvdfbcscdohobsduvbosu3\"}, {\"key\": \"班级\", \"value\": \"黄昏来临123\"}]', '2020-10-05 01:06:54.519', '2020-10-27 22:55:19.085', '2020-10-27 22:55:17.366');

-- ----------------------------
-- Table structure for user_group_apply
-- ----------------------------
DROP TABLE IF EXISTS `user_group_apply`;
CREATE TABLE `user_group_apply`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `group_id` int(10) NOT NULL COMMENT '用户组id',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '申请状态： 0.等待  1. 通过  2.拒绝',
  `remark` json NULL COMMENT '用户组要求用户备注信息',
  `create_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `delete_time` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_group`(`user_id`, `group_id`, `delete_time`) USING BTREE COMMENT 'user_id和group_id联合索引'
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_group_apply
-- ----------------------------
INSERT INTO `user_group_apply` VALUES (47, 32, 5, 1, '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"姓名\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', '2020-10-25 13:19:58.708', '2020-10-28 19:58:06.687', NULL);
INSERT INTO `user_group_apply` VALUES (48, 33, 5, 1, '[{\"key\": \"学号\", \"value\": \"\"}, {\"key\": \"姓名\", \"value\": \"\"}, {\"key\": \"班级\", \"value\": \"\"}]', '2020-10-25 13:19:58.708', '2020-10-27 22:08:00.722', NULL);

SET FOREIGN_KEY_CHECKS = 1;
