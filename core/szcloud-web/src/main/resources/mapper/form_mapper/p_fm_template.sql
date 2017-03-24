/*
Navicat MySQL Data Transfer

Source Server         : platformcloud
Source Server Version : 50619
Source Host           : 10.86.0.29:3306
Source Database       : platformcloud

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-11-12 11:25:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `p_fm_template`
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_template`;
CREATE TABLE `p_fm_template` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(32) DEFAULT NULL,
  `sys_id` bigint(32) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `file_location` varchar(500) DEFAULT NULL,
  `content` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_template
-- ----------------------------
