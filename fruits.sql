/*
 Navicat Premium Data Transfer

 Source Server         : ly阿里云
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : rm-wz97860x6cxc103yizo.mysql.rds.aliyuncs.com:3306
 Source Schema         : fruits

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 08/03/2022 16:29:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category` tinyint(3) NOT NULL COMMENT '优惠券类型',
  `payload` json NOT NULL COMMENT '优惠券具体信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pay_money` int(10) UNSIGNED NOT NULL COMMENT '订单实际支付金额，单位分',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单描述',
  `state` tinyint(3) UNSIGNED NOT NULL COMMENT '订单状态：0下单；1：已支付；2：订单已关闭(未支付成功,支付失败); 3: 完成制作；4：已取餐',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '下单用户',
  `create_time` datetime NOT NULL COMMENT '订单创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pay
-- ----------------------------
DROP TABLE IF EXISTS `pay`;
CREATE TABLE `pay`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_transaction_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户系统的实体id',
  `merchant_transaction_type` tinyint(4) NOT NULL COMMENT '商户系统的实体类型 1:order表,2:充值记录',
  `out_trade_no` bigint(20) NOT NULL COMMENT '商户的订单号，采用雪花算法生成',
  `amount` int(10) UNSIGNED NOT NULL COMMENT '金额，单位分',
  `create_time` datetime NOT NULL COMMENT '订单创建时间',
  `refund_amount` int(11) NOT NULL DEFAULT 0 COMMENT '退款金额，单位分，最大值和amount一样,用于判断是否能多次退款，相等则不能',
  `state` tinyint(3) UNSIGNED NOT NULL COMMENT '订单状态；0下单，1已支付，2支付失败，3进入退款',
  `transaction_id` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信支付系统生成的订单号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_outTradeNo`(`out_trade_no`) USING BTREE COMMENT '也可以确认唯一记录'
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付系统' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for refund
-- ----------------------------
DROP TABLE IF EXISTS `refund`;
CREATE TABLE `refund`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pay_id` bigint(20) UNSIGNED NOT NULL COMMENT 'payid 支付表的id',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款原因',
  `amount` int(10) UNSIGNED NOT NULL COMMENT '退款金额，单位分',
  `state` tinyint(3) UNSIGNED NOT NULL COMMENT '0退款中，1退款成功,2退款异常，3退款关闭',
  `create_time` datetime NOT NULL COMMENT '申请退款时间',
  `out_refund_no` bigint(20) NOT NULL COMMENT '商户退款单号',
  `refund_id` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信支付退款单号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_outRefundNo`(`out_refund_no`) USING BTREE COMMENT '可以确定唯一记录'
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退款' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for specification
-- ----------------------------
DROP TABLE IF EXISTS `specification`;
CREATE TABLE `specification`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规格名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq-name`(`name`) USING BTREE COMMENT '规格名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规格表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for specification_spu
-- ----------------------------
DROP TABLE IF EXISTS `specification_spu`;
CREATE TABLE `specification_spu`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `spu_id` bigint(20) UNSIGNED NOT NULL COMMENT 'spu的id',
  `specification_id` bigint(20) UNSIGNED NOT NULL COMMENT '规格id',
  `required` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否必填: 0不是必填，1必填',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规格和spu之间的多对多关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for specification_value
-- ----------------------------
DROP TABLE IF EXISTS `specification_value`;
CREATE TABLE `specification_value`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `specification_id` bigint(20) UNSIGNED NOT NULL COMMENT '规格id',
  `value` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规格值',
  `money` int(10) UNSIGNED NOT NULL COMMENT '金额；单位分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '一个规格对应有多个值' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for spu
-- ----------------------------
DROP TABLE IF EXISTS `spu`;
CREATE TABLE `spu`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名字',
  `category_id` bigint(20) UNSIGNED NOT NULL COMMENT '商品分类id',
  `is_inventory` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否有货； 0没有货：1有货',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片地址',
  `money` int(11) NOT NULL COMMENT '金额，单位分',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'spu表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for spu_category
-- ----------------------------
DROP TABLE IF EXISTS `spu_category`;
CREATE TABLE `spu_category`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `phone` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq-phone`(`phone`) USING BTREE COMMENT '手机号确认唯一用户'
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_we_chat
-- ----------------------------
DROP TABLE IF EXISTS `user_we_chat`;
CREATE TABLE `user_we_chat`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT 'user表的id',
  `carrier` tinyint(4) NOT NULL COMMENT '微信体系的载体。0：小程序, 1: 公众号',
  `open_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信的openId',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_carrier_openId`(`carrier`, `open_id`) USING BTREE COMMENT '载体和openId可以确定唯一记录'
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信用户体系，和user是一对多关系' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
