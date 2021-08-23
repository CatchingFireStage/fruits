/*
 Navicat Premium Data Transfer

 Source Server         : ly百度云
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : 112.32.23.22:3306
 Source Schema         : fruits

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 23/08/2021 16:33:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint unsigned NOT NULL,
  `pay_money` int unsigned NOT NULL COMMENT '订单实际支付金额，单位分',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单描述',
  `state` tinyint unsigned NOT NULL COMMENT '订单状态：0下单；1：已支付；2：订单已关闭(未支付成功,支付失败); 3: 完成制作；4：已取餐',
  `user_id` bigint unsigned NOT NULL COMMENT '下单用户',
  `create_time` datetime(0) NOT NULL COMMENT '订单创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay
-- ----------------------------
DROP TABLE IF EXISTS `pay`;
CREATE TABLE `pay`  (
  `id` int unsigned NOT NULL,
  `orders_id` int unsigned NOT NULL COMMENT '商户系统的订单号',
  `description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品描述',
  `time_expire` datetime(0) NOT NULL COMMENT '订单失效时间',
  `amount` int unsigned NOT NULL COMMENT '金额，单位分',
  `create_time` datetime(0) NOT NULL COMMENT '订单创建时间',
  `state` tinyint unsigned NOT NULL COMMENT '订单状态；0下单，1已支付，2支付失败',
  `transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信支付系统生成的订单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付系统' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for refund
-- ----------------------------
DROP TABLE IF EXISTS `refund`;
CREATE TABLE `refund`  (
  `id` int unsigned NOT NULL,
  `pay_id` int unsigned NOT NULL COMMENT 'payid 支付表的id',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款原因',
  `amount` int unsigned NOT NULL COMMENT '退款金额，单位分',
  `state` tinyint unsigned NOT NULL COMMENT '0退款中，1退款成功',
  `create_time` datetime(0) NOT NULL COMMENT '申请退款时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退款' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for specification
-- ----------------------------
DROP TABLE IF EXISTS `specification`;
CREATE TABLE `specification`  (
  `id` bigint unsigned NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规格名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq-name`(`name`) USING BTREE COMMENT '规格名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规格表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for specification_spu
-- ----------------------------
DROP TABLE IF EXISTS `specification_spu`;
CREATE TABLE `specification_spu`  (
  `id` bigint unsigned NOT NULL,
  `spu_id` bigint unsigned NOT NULL COMMENT 'spu的id',
  `specification_id` bigint unsigned NOT NULL COMMENT '规格id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规格和spu之间的多对多关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for specification_value
-- ----------------------------
DROP TABLE IF EXISTS `specification_value`;
CREATE TABLE `specification_value`  (
  `id` bigint unsigned NOT NULL,
  `specification_id` bigint unsigned NOT NULL COMMENT '规格id',
  `value` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规格值',
  `money` int unsigned NOT NULL COMMENT '金额；单位分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '一个规格对应有多个值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu
-- ----------------------------
DROP TABLE IF EXISTS `spu`;
CREATE TABLE `spu`  (
  `id` bigint unsigned NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名字',
  `category_id` bigint unsigned NOT NULL COMMENT '商品分类id',
  `is_inventory` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否有货； 0没有货：1有货',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片地址',
  `money` int(0) NOT NULL COMMENT '金额，单位分',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'spu表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu_category
-- ----------------------------
DROP TABLE IF EXISTS `spu_category`;
CREATE TABLE `spu_category`  (
  `id` bigint unsigned NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
