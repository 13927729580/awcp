/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : awcp

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2017-12-19 23:47:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for fw_mm_datasourcemanager
-- ----------------------------
DROP TABLE IF EXISTS `fw_mm_datasourcemanager`;
CREATE TABLE `fw_mm_datasourcemanager` (
  `ID` char(36) NOT NULL COMMENT '主键',
  `NAME` varchar(100) DEFAULT NULL COMMENT '数据源名称',
  `SOURCE_TYPE` varchar(50) DEFAULT NULL COMMENT '数据源类型',
  `SOURCE_URL` varchar(200) DEFAULT NULL COMMENT '数据源url地址',
  `SOURCE_DRIVER` varchar(100) DEFAULT NULL COMMENT '数据源驱动',
  `SOURCE_ALIAS` varchar(100) DEFAULT NULL COMMENT '数据源别名',
  `USERNAME` varchar(100) DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(100) DEFAULT NULL COMMENT '密码',
  `MAXIMUM_ACTIVE_TIME` int(11) DEFAULT NULL,
  `PROTOTYPE_COUNT` int(11) DEFAULT NULL,
  `MAXIMUM_CONNECTION_COUNT` int(11) DEFAULT NULL,
  `MINIMUM_CONNECTION_COUNT` int(11) DEFAULT NULL,
  `SIMULTANEOUS_BUILD_THROTTLE` int(11) DEFAULT NULL,
  `TRACE` tinyint(1) DEFAULT NULL,
  `DOMAIN` varchar(20) DEFAULT NULL COMMENT '数据源来源（内部、外部）',
  `CHECKOUT_USER` varchar(20) DEFAULT NULL COMMENT '迁出人',
  `CHECKOUT` tinyint(1) DEFAULT NULL COMMENT '是否是迁出状态',
  `VERSION` bigint(11) DEFAULT NULL COMMENT '版本',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(200) DEFAULT NULL COMMENT '创建人',
  `LAST_MODIFY_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `LAST_MODIFIER` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fw_mm_datasourcemanager
-- ----------------------------

-- ----------------------------
-- Table structure for fw_mm_metamodel
-- ----------------------------
DROP TABLE IF EXISTS `fw_mm_metamodel`;
CREATE TABLE `fw_mm_metamodel` (
  `id` char(36) COLLATE utf8_bin NOT NULL,
  `modelClassId` char(36) COLLATE utf8_bin DEFAULT NULL,
  `modelName` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `modelCode` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `modelDesc` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `tableName` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `projectName` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `modelType` int(11) DEFAULT NULL,
  `modelSynchronization` int(11) DEFAULT NULL,
  `modelValid` int(11) DEFAULT NULL,
  `SYSTEM_ID` bigint(20) DEFAULT NULL COMMENT '系统ID',
  `DATASOURCE_ID` bigint(20) DEFAULT '25',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `modelName` (`modelName`) USING BTREE,
  KEY `seq` (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of fw_mm_metamodel
-- ----------------------------
INSERT INTO `fw_mm_metamodel` VALUES ('1f3d0087-b160-4a1f-85cd-adc94bf68b32', null, 'p_fm_api_describe', 'p_fm_api_describe', null, 'p_fm_api_describe', null, null, null, null, '110', null, null, '67');
INSERT INTO `fw_mm_metamodel` VALUES ('2739', null, 'p_un_suggestion', 'p_un_suggestion', null, 'p_un_suggestion', null, null, null, null, '110', null, null, '1');
INSERT INTO `fw_mm_metamodel` VALUES ('2746', null, 'p_fm_modular', 'p_fm_modular', null, 'p_fm_modular', null, null, null, null, '110', null, null, '12');
INSERT INTO `fw_mm_metamodel` VALUES ('2778', null, 'p_fm_dynamicpage', 'p_fm_dynamicpage', null, 'p_fm_dynamicpage', null, null, null, null, '110', null, null, '5');
INSERT INTO `fw_mm_metamodel` VALUES ('2791', null, 'p_un_user_base_info', 'p_un_user_base_info', null, 'p_un_user_base_info', null, null, null, null, '110', null, null, '6');
INSERT INTO `fw_mm_metamodel` VALUES ('2798', null, 'p_un_i18n', 'p_un_i18n', null, 'p_un_i18n', null, null, null, null, '110', null, null, '7');
INSERT INTO `fw_mm_metamodel` VALUES ('2803', null, 'p_data_dictionary', 'p_data_dictionary', null, 'p_data_dictionary', null, null, null, null, '110', null, null, '8');
INSERT INTO `fw_mm_metamodel` VALUES ('2813', null, 'p_un_role_info', 'p_un_role_info', null, 'p_un_role_info', null, null, null, null, '110', null, null, '9');
INSERT INTO `fw_mm_metamodel` VALUES ('2819', null, 'p_un_notification', 'p_un_notification', null, 'p_un_notification', null, null, null, null, '110', null, null, '10');
INSERT INTO `fw_mm_metamodel` VALUES ('32dde3c5-59ff-4edd-b399-a4249927d45a', null, 'p_attr_city', 'p_attr_city', null, 'p_attr_city', null, null, null, null, '110', null, null, '16');
INSERT INTO `fw_mm_metamodel` VALUES ('9bd67bd4-3967-4152-90d1-507d7fe75caf', null, 'p_fm_api_privilege', 'p_fm_api_privilege', null, 'p_fm_api_privilege', null, null, null, null, '110', null, null, '68');
INSERT INTO `fw_mm_metamodel` VALUES ('b61b4445-242c-405b-9873-a6c2848c8a9a', null, 'p_fm_api', 'p_fm_api', null, 'p_fm_api', null, null, null, null, '110', null, null, '66');
INSERT INTO `fw_mm_metamodel` VALUES ('cbf8baef-0ec7-4327-9a7f-0d324ee6d043', null, 'p_attr_province', 'p_attr_province', null, 'p_attr_province', null, null, null, null, '110', null, null, '17');
INSERT INTO `fw_mm_metamodel` VALUES ('d57df080-b4c4-4485-8ab0-16a5f96b3622', null, 'p_attr_area', 'p_attr_area', null, 'p_attr_area', null, null, null, null, '110', null, null, '15');

-- ----------------------------
-- Table structure for fw_mm_metamodelclass
-- ----------------------------
DROP TABLE IF EXISTS `fw_mm_metamodelclass`;
CREATE TABLE `fw_mm_metamodelclass` (
  `id` char(36) COLLATE utf8_bin NOT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `classCode` varchar(50) COLLATE utf8_bin NOT NULL,
  `sysId` bigint(20) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of fw_mm_metamodelclass
-- ----------------------------

-- ----------------------------
-- Table structure for fw_mm_metamodelitems
-- ----------------------------
DROP TABLE IF EXISTS `fw_mm_metamodelitems`;
CREATE TABLE `fw_mm_metamodelitems` (
  `id` char(36) COLLATE utf8_bin NOT NULL,
  `modelId` char(36) COLLATE utf8_bin DEFAULT NULL,
  `itemName` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `itemCode` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `itemType` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `itemLength` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `usePrimaryKey` int(11) DEFAULT NULL,
  `useIndex` int(11) DEFAULT NULL,
  `useNull` int(11) DEFAULT NULL,
  `itemValid` int(11) DEFAULT '0',
  `defaultValue` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tttt_idx` (`modelId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of fw_mm_metamodelitems
-- ----------------------------
INSERT INTO `fw_mm_metamodelitems` VALUES ('03f69796-9fc2-41e1-aec1-c07c3169e122', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_NAME', 'API_NAME', 'varchar', '255', null, '1', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('092cf366-32ae-4c26-8426-d1ca341ef87f', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_TABLE', 'API_TABLE', 'varchar', '50', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('0b0c8267-d306-44a8-9369-a666bf0eed10', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_ID', 'API_ID', 'char', '36', '1', '-1', '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('0e02834f-85bf-411c-ae5d-8be670471088', '2803', 'DATA_ORDER', 'DATA_ORDER', 'int', '11', null, '0', '0', '1', null, '顺序', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('12daefcb-269f-406f-94ba-eb8e6de3c247', '1f3d0087-b160-4a1f-85cd-adc94bf68b32', 'ISNECESSARY', 'ISNECESSARY', 'varchar', '5', null, '0', '0', '1', null, '是否必须，1=是，0=否', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19133', '2746', 'modularName', 'modularName', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19134', '2746', 'ID', 'ID', 'BIGINT', '19', '1', null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19208', '2739', 'LinkName', 'LinkName', 'VARCHAR', '30', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19209', '2739', 'Orders', 'Orders', 'INT', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19210', '2739', 'PersonName', 'PersonName', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19211', '2739', 'Dept', 'Dept', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19212', '2739', 'isLeader', 'isLeader', 'CHAR', '2', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19213', '2739', 'remark', 'remark', 'VARCHAR', '1000', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19214', '2739', 'Conment', 'Conment', 'VARCHAR', '1000', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19215', '2739', 'type', 'type', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19216', '2739', 'Flag', 'Flag', 'INT', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19217', '2739', 'Date', 'Date', 'DATETIME', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19218', '2739', 'GROUP_ID', 'GROUP_ID', 'INT', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19219', '2739', 'sendTime', 'sendTime', 'DATETIME', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19220', '2739', 'DeptName', 'DeptName', 'VARCHAR', '100', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19221', '2739', 'ID', 'ID', 'VARCHAR', '40', '1', null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19222', '2739', 'deadline', 'deadline', 'FLOAT', '12', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19223', '2739', 'Person', 'Person', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19224', '2739', 'BusinessId', 'BusinessId', 'VARCHAR', '40', null, null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19225', '2739', 'Link', 'Link', 'VARCHAR', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('19226', '2739', 'status', 'status', 'VARCHAR', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('1d3d3b7d-6ca8-4d62-847d-9b60f5a8db14', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_STATE', 'API_STATE', 'int', '3', null, '0', '0', '1', null, '0否1是', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('1dc30d08-9833-4b90-8b30-6027c56d9150', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_REMARK', 'API_REMARK', 'varchar', '255', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20685', '2798', 'en', 'en', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20686', '2798', 'remark', 'remark', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20687', '2798', 'ID', 'ID', 'INT', '10', '1', null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20688', '2798', 'pageId', 'pageId', 'INT', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20689', '2798', 'key', 'key', 'VARCHAR', '200', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20690', '2798', 'zh', 'zh', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20763', '2778', 'is_log', 'is_log', 'VARCHAR', '4', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20764', '2778', 'css', 'css', 'VARCHAR', '1024', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20765', '2778', 'disabled_script', 'disabled_script', 'BLOB', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20766', '2778', 'page_type', 'page_type', 'INT', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20767', '2778', 'preload_script', 'preload_script', 'BLOB', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20768', '2778', 'description', 'description', 'VARCHAR', '1020', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20769', '2778', 'LINE_HEIGHT', 'LINE_HEIGHT', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20770', '2778', 'SYSTEM_ID', 'SYSTEM_ID', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20771', '2778', 'REVERSESORTORD', 'REVERSESORTORD', 'VARCHAR', '4', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20772', '2778', 'templateContext', 'templateContext', 'LONGTEXT', '2147483647', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20773', '2778', 'STORES', 'STORES', 'LONGTEXT', '2147483647', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20774', '2778', 'IS_CHECKOUT', 'IS_CHECKOUT', 'INT', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20775', '2778', 'id', 'id', 'BIGINT', '19', '1', null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20776', '2778', 'UPDATED_USER', 'UPDATED_USER', 'VARCHAR', '250', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20777', '2778', 'REVERSENUMMODE', 'REVERSENUMMODE', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20778', '2778', 'PDF_TEMPLATE_PAGE', 'PDF_TEMPLATE_PAGE', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20779', '2778', 'afterload_script', 'afterload_script', 'BLOB', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20780', '2778', 'modular', 'modular', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20781', '2778', 'created', 'created', 'DATETIME', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20782', '2778', 'MAX_LINE_COUNT', 'MAX_LINE_COUNT', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20783', '2778', 'SHOWTOTALCOUNT', 'SHOWTOTALCOUNT', 'VARCHAR', '4', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20784', '2778', 'CREATED_USER', 'CREATED_USER', 'VARCHAR', '250', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20785', '2778', 'ordernum', 'ordernum', 'INT', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20786', '2778', 'MIN_LINE_COUNT', 'MIN_LINE_COUNT', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20787', '2778', 'model_xml', 'model_xml', 'BLOB', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20788', '2778', 'version', 'version', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20789', '2778', 'GROUP_ID', 'GROUP_ID', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20790', '2778', 'readonly_script', 'readonly_script', 'BLOB', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20791', '2778', 'hidden_script', 'hidden_script', 'BLOB', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20792', '2778', 'STYLE_ID', 'STYLE_ID', 'VARCHAR', '36', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20793', '2778', 'CHECKOUT_USER', 'CHECKOUT_USER', 'VARCHAR', '250', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20794', '2778', 'SHOWREVERSENUM', 'SHOWREVERSENUM', 'VARCHAR', '4', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20795', '2778', 'name', 'name', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20796', '2778', 'IS_LIMITPAGE', 'IS_LIMITPAGE', 'VARCHAR', '4', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20797', '2778', 'LINE_HEIGHT_TYPE', 'LINE_HEIGHT_TYPE', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20798', '2778', 'WORKFLOW_NODE_INFO', 'WORKFLOW_NODE_INFO', 'BLOB', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20799', '2778', 'style', 'style', 'VARCHAR', '1020', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20800', '2778', 'template_id', 'template_id', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20801', '2778', 'updated', 'updated', 'DATETIME', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20802', '2778', 'PAGESIZE', 'PAGESIZE', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20803', '2791', 'DEPT_NAME', 'DEPT_NAME', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20804', '2791', 'EMPLOYEE_ID', 'EMPLOYEE_ID', 'VARCHAR', '30', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20805', '2791', 'USER_STATUS', 'USER_STATUS', 'CHAR', '2', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20806', '2791', 'USER_EMAIL', 'USER_EMAIL', 'VARCHAR', '128', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20807', '2791', 'USER_BIRTH_PLACE', 'USER_BIRTH_PLACE', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20808', '2791', 'USER_ID', 'USER_ID', 'BIGINT', '19', '1', null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20809', '2791', 'USER_DOSSIER_NUMBER', 'USER_DOSSIER_NUMBER', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20810', '2791', 'USER_HEAD_IMG', 'USER_HEAD_IMG', 'VARCHAR', '200', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20811', '2791', 'LAST_EDIT_TIME', 'LAST_EDIT_TIME', 'DATETIME', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20812', '2791', 'USER_FAX', 'USER_FAX', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20813', '2791', 'DEPT_ID', 'DEPT_ID', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20814', '2791', 'NUMBER', 'NUMBER', 'INT', '10', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20815', '2791', 'USER_TITLE', 'USER_TITLE', 'VARCHAR', '30', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20816', '2791', 'USER_OFFICE_NUM', 'USER_OFFICE_NUM', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20817', '2791', 'USER_OFFICE_PHONE', 'USER_OFFICE_PHONE', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20818', '2791', 'USER_NAME', 'USER_NAME', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20819', '2791', 'GROUP_ID', 'GROUP_ID', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20820', '2791', 'NAME', 'NAME', 'VARCHAR', '30', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20821', '2791', 'USER_HOUSE_PHONE', 'USER_HOUSE_PHONE', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20822', '2791', 'USER_DOMICILE', 'USER_DOMICILE', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20823', '2791', 'USER_ID_CARD_NUMBER', 'USER_ID_CARD_NUMBER', 'VARCHAR', '100', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20824', '2791', 'ORG_CODE', 'ORG_CODE', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20825', '2791', 'SIGNATURE_IMG', 'SIGNATURE_IMG', 'VARCHAR', '50', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20826', '2791', 'USER_HOUSEHOLD_REGIST', 'USER_HOUSEHOLD_REGIST', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20827', '2791', 'USER_PWD', 'USER_PWD', 'VARCHAR', '255', null, null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('20828', '2791', 'MOBILE', 'MOBILE', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21445', '2813', 'SYS_ID', 'SYS_ID', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21446', '2813', 'DICT_REMARK', 'DICT_REMARK', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21447', '2813', 'ROLE_ID', 'ROLE_ID', 'BIGINT', '19', '1', null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21448', '2813', 'ROLE_NAME', 'ROLE_NAME', 'VARCHAR', '20', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21449', '2813', 'GROUP_ID', 'GROUP_ID', 'BIGINT', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21791', '2819', 'NOTICESTATE', 'NOTICESTATE', 'VARCHAR', '2', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21792', '2819', 'CREATOR', 'CREATOR', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21793', '2819', 'MSG_URL', 'MSG_URL', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21794', '2819', 'TITLE', 'TITLE', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21795', '2819', 'CONTENT', 'CONTENT', 'TEXT', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21796', '2819', 'CREATE_TIME', 'CREATE_TIME', 'DATETIME', '19', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21797', '2819', 'ICON', 'ICON', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21798', '2819', 'ID', 'ID', 'CHAR', '36', '1', null, '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21799', '2819', 'BODY', 'BODY', 'TEXT', '65535', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21800', '2819', 'REMARK', 'REMARK', 'VARCHAR', '255', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('21801', '2819', 'TYPE', 'TYPE', 'CHAR', '2', null, null, '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('2a41fc52-9736-46d7-955e-91b1239c0392', '9bd67bd4-3967-4152-90d1-507d7fe75caf', 'REMARK', 'REMARK', 'varchar', '500', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('2c106bdc-9153-4acc-967f-2d485638f280', 'cbf8baef-0ec7-4327-9a7f-0d324ee6d043', 'code', 'code', 'varchar', '6', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('38038879-17dd-44f9-9da9-84bad97024a4', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_CREATETIME', 'API_CREATETIME', 'timestamp', null, null, '0', '0', '1', 'CURRENT_TIMESTAMP', null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('38fa6370-9966-4a7c-a45e-90558ece1921', '1f3d0087-b160-4a1f-85cd-adc94bf68b32', 'TYPE', 'TYPE', 'varchar', '20', null, '0', '0', '1', null, '类型', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('4375f2d7-5a40-4593-891e-f9aee89d3a66', '9bd67bd4-3967-4152-90d1-507d7fe75caf', 'DESCRIBES', 'DESCRIBES', 'varchar', '1000', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('4991ccb8-98c9-452b-895f-c8bbd2bffa6c', '9bd67bd4-3967-4152-90d1-507d7fe75caf', 'MESSAGE', 'MESSAGE', 'varchar', '255', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('4b8c05c1-f10f-4163-9b57-6d7d557f3f79', '32dde3c5-59ff-4edd-b399-a4249927d45a', 'beiyong', 'beiyong', 'varchar', '255', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('4df5194e-0c6c-4a66-9985-4f7918caab09', '2803', 'CODE', 'CODE', 'varchar', '255', null, '2', '0', '1', null, '编码', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('4f8ad189-916d-41d5-9672-9b55ba75f064', '32dde3c5-59ff-4edd-b399-a4249927d45a', 'id', 'id', 'int', '11', '1', '-1', '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('5a964b49-a795-43f1-91e3-ed3c78dfa87d', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_DESC', 'API_DESC', 'varchar', '500', null, '0', '0', '1', null, '描述，手机上可以以此为标题', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('5d3257a3-cd48-4069-9db0-dc62ff46fa28', '2803', 'DICT_REMARK', 'DICT_REMARK', 'varchar', '255', null, '0', '0', '1', null, '字典备注', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('5f754b9e-2e59-4010-9bf3-b0e0054c2bd4', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_IS_LOGIN', 'API_IS_LOGIN', 'int', '3', null, '0', '0', '1', null, '是否需要登录，0不需要，1需要', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('62d5885f-85aa-41f7-af22-215dc16db8ea', '2803', 'DICT_STATUS', 'DICT_STATUS', 'varchar', '2', null, '0', '0', '1', null, '状态', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('646891a3-3984-4f33-ba2c-c9f8758d0b7d', 'd57df080-b4c4-4485-8ab0-16a5f96b3622', 'id', 'id', 'int', '11', '1', '-1', '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('68aab4be-d624-4fd5-b596-d4bf4012a04f', 'd57df080-b4c4-4485-8ab0-16a5f96b3622', 'name', 'name', 'varchar', '60', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('6975977c-508a-42a6-9957-b78f7f8b7bb3', '9bd67bd4-3967-4152-90d1-507d7fe75caf', 'NAME', 'NAME', 'varchar', '100', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('727424b1-f098-4b3a-9d92-be128ddbad91', '9bd67bd4-3967-4152-90d1-507d7fe75caf', 'TYPE', 'TYPE', 'char', '4', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('76054c9d-e04c-420c-bbf3-6c246fb97c29', '1f3d0087-b160-4a1f-85cd-adc94bf68b32', 'ID', 'ID', 'varchar', '40', '1', '-1', '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('86383a3a-fc62-49e1-b47f-f5fafe98e6ba', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_METHOD', 'API_METHOD', 'varchar', '4', null, '0', '0', '1', null, '请求方式', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('8c2c97b8-74f7-4816-8d3b-0295005ea147', '9bd67bd4-3967-4152-90d1-507d7fe75caf', 'RULES', 'RULES', 'varchar', '5000', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('8c45884c-0c77-4c90-81f6-0dc104e55ba3', 'd57df080-b4c4-4485-8ab0-16a5f96b3622', 'PID', 'PID', 'varchar', '50', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('8c82147d-4a4c-41e8-be4e-1f1b8a4a167c', '1f3d0087-b160-4a1f-85cd-adc94bf68b32', 'DESCRIBES', 'DESCRIBES', 'text', null, null, '0', '0', '1', null, '描述', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('9478ebae-e194-4873-b156-17f0f0d0d696', '2803', 'DATA_VALUE', 'DATA_VALUE', 'varchar', '255', null, '2', '0', '1', null, 'DATA_VALUE', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('96624976-f316-4921-9456-50a2d85c2f62', '9bd67bd4-3967-4152-90d1-507d7fe75caf', 'ID', 'ID', 'int', '10', '1', '-1', '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('9bed384f-d5c9-46a2-850d-3a4736429ed5', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_ISCACHE', 'API_ISCACHE', 'varchar', '2', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('9ff4e477-8d97-49e2-a1fc-322b54f21cbf', '32dde3c5-59ff-4edd-b399-a4249927d45a', 'code', 'code', 'varchar', '6', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('a43fdcef-855d-4f8a-8170-bcf75f57d835', '2803', 'DATA_KEY', 'DATA_KEY', 'varchar', '50', null, '0', '0', '1', null, 'DATA_KEY', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('a569a861-df71-4d63-b567-8df47e48931b', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_TYPE', 'API_TYPE', 'int', '3', null, '0', '0', '1', null, '0,禁用，1增加，2修改，3get，4删除，5excute，6query，7page', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('a8b3bc55-2ed0-4177-b890-939fe44e1709', '1f3d0087-b160-4a1f-85cd-adc94bf68b32', 'NAME', 'NAME', 'varchar', '255', null, '0', '0', '1', null, '名称', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('a92c9e15-896e-4667-87d3-e3a8b239f928', '1f3d0087-b160-4a1f-85cd-adc94bf68b32', 'APIID', 'APIID', 'char', '36', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('a9595c86-dfb0-4394-9047-72cfb28f88df', '32dde3c5-59ff-4edd-b399-a4249927d45a', 'name', 'name', 'varchar', '50', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('ad70c522-ea05-4aac-9564-25d2515708da', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_CREATOR', 'API_CREATOR', 'varchar', '40', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('b688598b-f435-40a9-b7fe-a6ef36eda138', '2803', 'PID', 'PID', 'varchar', '36', null, '0', '0', '1', null, '父类ID', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('bf13a5d0-d0ff-4c4b-ab71-573d47165580', '1f3d0087-b160-4a1f-85cd-adc94bf68b32', 'PARAM_PLACE', 'PARAM_PLACE', 'varchar', '255', null, '0', '0', '1', null, '参数位置', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('c4942da6-5b32-4f20-9fff-288fc2a723d8', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_OUTPUT', 'API_OUTPUT', 'varchar', '5000', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('c6132dbc-89e6-49bd-b03b-50e02e3fc24c', 'b61b4445-242c-405b-9873-a6c2848c8a9a', 'API_SQL', 'API_SQL', 'varchar', '5000', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('c8ecfa94-326a-49ba-9515-bdb488147c4d', 'cbf8baef-0ec7-4327-9a7f-0d324ee6d043', 'id', 'id', 'int', '11', null, '0', '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('c92c01f1-b20e-46e9-a827-99edf77b4b8d', '32dde3c5-59ff-4edd-b399-a4249927d45a', 'provinceId', 'provinceId', 'varchar', '6', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('d315d66d-a5d4-4474-a605-af92acde791f', 'd57df080-b4c4-4485-8ab0-16a5f96b3622', 'cityId', 'cityId', 'varchar', '6', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('d86ec27c-06e6-4a4b-a828-5c4339cb1084', 'd57df080-b4c4-4485-8ab0-16a5f96b3622', 'code', 'code', 'varchar', '50', null, '0', '0', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('d9043990-3670-465e-83f2-bc3b94f495fe', '1f3d0087-b160-4a1f-85cd-adc94bf68b32', 'DEFAULTVAL', 'DEFAULTVAL', 'varchar', '255', null, '0', '0', '1', null, '默认值', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('eedab7cb-2532-4b14-a808-4ad256333bc9', '2803', 'LEVEL', 'LEVEL', 'bigint', '11', null, '0', '0', '1', null, '枚举层数', null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('f2d722aa-245e-45e8-a047-a6fd38e95d19', '2803', 'ID', 'ID', 'varchar', '36', '1', '-1', '1', '1', null, null, null);
INSERT INTO `fw_mm_metamodelitems` VALUES ('fa3694b1-3499-48ae-86bc-65032334bbdd', 'cbf8baef-0ec7-4327-9a7f-0d324ee6d043', 'name', 'name', 'varchar', '40', null, '0', '0', '1', null, null, null);

-- ----------------------------
-- Table structure for fw_mm_metamodeltype
-- ----------------------------
DROP TABLE IF EXISTS `fw_mm_metamodeltype`;
CREATE TABLE `fw_mm_metamodeltype` (
  `id` char(36) NOT NULL,
  `modelId` char(36) DEFAULT NULL,
  `relationModel` char(36) DEFAULT NULL,
  `params` varchar(5000) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fw_mm_metamodeltype
-- ----------------------------

-- ----------------------------
-- Table structure for fw_sys_datasource
-- ----------------------------
DROP TABLE IF EXISTS `fw_sys_datasource`;
CREATE TABLE `fw_sys_datasource` (
  `ID` char(36) NOT NULL COMMENT '主键',
  `SYSTEM_ID` bigint(20) DEFAULT NULL COMMENT '系统ID',
  `DATASOURCE_ID` char(36) DEFAULT NULL COMMENT '数据源ID',
  `ISDEFAULT` bit(1) DEFAULT NULL COMMENT '是否默认数据源',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fw_sys_datasource
-- ----------------------------
INSERT INTO `fw_sys_datasource` VALUES ('136', '111', '32', '', '1');
INSERT INTO `fw_sys_datasource` VALUES ('141', '110', '32', '\0', null);

-- ----------------------------
-- Table structure for port_depttype
-- ----------------------------
DROP TABLE IF EXISTS `port_depttype`;
CREATE TABLE `port_depttype` (
  `No` varchar(2) NOT NULL,
  `Name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `Port_DeptTypeID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of port_depttype
-- ----------------------------

-- ----------------------------
-- Table structure for proc
-- ----------------------------
DROP TABLE IF EXISTS `proc`;
CREATE TABLE `proc` (
  `db` char(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `name` char(64) NOT NULL DEFAULT '',
  `type` enum('FUNCTION','PROCEDURE') NOT NULL,
  `specific_name` char(64) NOT NULL DEFAULT '',
  `language` enum('SQL') NOT NULL DEFAULT 'SQL',
  `sql_data_access` enum('CONTAINS_SQL','NO_SQL','READS_SQL_DATA','MODIFIES_SQL_DATA') NOT NULL DEFAULT 'CONTAINS_SQL',
  `is_deterministic` enum('YES','NO') NOT NULL DEFAULT 'NO',
  `security_type` enum('INVOKER','DEFINER') NOT NULL DEFAULT 'DEFINER',
  `param_list` blob NOT NULL,
  `returns` char(64) NOT NULL DEFAULT '',
  `body` longblob NOT NULL,
  `definer` char(77) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sql_mode` set('REAL_AS_FLOAT','PIPES_AS_CONCAT','ANSI_QUOTES','IGNORE_SPACE','NOT_USED','ONLY_FULL_GROUP_BY','NO_UNSIGNED_SUBTRACTION','NO_DIR_IN_CREATE','POSTGRESQL','ORACLE','MSSQL','DB2','MAXDB','NO_KEY_OPTIONS','NO_TABLE_OPTIONS','NO_FIELD_OPTIONS','MYSQL323','MYSQL40','ANSI','NO_AUTO_VALUE_ON_ZERO','NO_BACKSLASH_ESCAPES','STRICT_TRANS_TABLES','STRICT_ALL_TABLES','NO_ZERO_IN_DATE','NO_ZERO_DATE','INVALID_DATES','ERROR_FOR_DIVISION_BY_ZERO','TRADITIONAL','NO_AUTO_CREATE_USER','HIGH_NOT_PRECEDENCE') NOT NULL DEFAULT '',
  `comment` text CHARACTER SET tis620 NOT NULL,
  PRIMARY KEY (`db`,`name`,`type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Stored Procedures';

-- ----------------------------
-- Records of proc
-- ----------------------------

-- ----------------------------
-- Table structure for project_expert_match
-- ----------------------------
DROP TABLE IF EXISTS `project_expert_match`;
CREATE TABLE `project_expert_match` (
  `ID` varchar(36) NOT NULL,
  `PROJECT_GROUP_NAME` varchar(36) DEFAULT NULL,
  `EXPERT_GROUP_ID` varchar(36) DEFAULT NULL COMMENT '专家ID',
  `CREATE_USER` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `REVIEW_START_TIME` datetime DEFAULT NULL,
  `REVIEW_END_TIME` datetime DEFAULT NULL,
  `REVIEW_TABLE_NAME` varchar(200) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `SYSTEM_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project_expert_match
-- ----------------------------

-- ----------------------------
-- Table structure for project_group_info
-- ----------------------------
DROP TABLE IF EXISTS `project_group_info`;
CREATE TABLE `project_group_info` (
  `GROUP_NAME` varchar(200) DEFAULT NULL COMMENT '组名',
  `PROJECT_ID` varchar(36) NOT NULL DEFAULT '' COMMENT '专家ID',
  `COMPANY_BASE_INFO_ID` varchar(36) DEFAULT NULL,
  `COMPANY_NAME` varchar(255) DEFAULT NULL,
  `CREATE_USER` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `REVIEW_TIME` datetime DEFAULT NULL COMMENT '评审时间',
  `PROJECT_TYPE` varchar(100) DEFAULT NULL COMMENT '项目类别',
  `SUB_HIGH_TECH_FIELD` varchar(100) DEFAULT NULL COMMENT '所属高新技术子领域',
  `STATUS` varchar(5) DEFAULT NULL COMMENT '状态',
  `SYSTEM_ID` bigint(20) DEFAULT NULL,
  `HIGH_TECH_FIELD` varchar(100) DEFAULT NULL COMMENT '高新技术领域',
  `GROUP_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`PROJECT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project_group_info
-- ----------------------------

-- ----------------------------
-- Table structure for pub_nd
-- ----------------------------
DROP TABLE IF EXISTS `pub_nd`;
CREATE TABLE `pub_nd` (
  `No` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `Name` varchar(60) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of pub_nd
-- ----------------------------

-- ----------------------------
-- Table structure for pub_ny
-- ----------------------------
DROP TABLE IF EXISTS `pub_ny`;
CREATE TABLE `pub_ny` (
  `No` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `Name` varchar(60) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of pub_ny
-- ----------------------------

-- ----------------------------
-- Table structure for pub_yf
-- ----------------------------
DROP TABLE IF EXISTS `pub_yf`;
CREATE TABLE `pub_yf` (
  `No` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `Name` varchar(60) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of pub_yf
-- ----------------------------

-- ----------------------------
-- Table structure for p_attr_area
-- ----------------------------
DROP TABLE IF EXISTS `p_attr_area`;
CREATE TABLE `p_attr_area` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `cityId` varchar(6) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PID` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_attr_area
-- ----------------------------

-- ----------------------------
-- Table structure for p_attr_city
-- ----------------------------
DROP TABLE IF EXISTS `p_attr_city`;
CREATE TABLE `p_attr_city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(6) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `provinceId` varchar(6) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `beiyong` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_attr_city
-- ----------------------------

-- ----------------------------
-- Table structure for p_attr_province
-- ----------------------------
DROP TABLE IF EXISTS `p_attr_province`;
CREATE TABLE `p_attr_province` (
  `id` int(11) NOT NULL,
  `code` varchar(6) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_attr_province
-- ----------------------------

-- ----------------------------
-- Table structure for p_data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `p_data_dictionary`;
CREATE TABLE `p_data_dictionary` (
  `ID` varchar(36) NOT NULL,
  `PID` varchar(36) DEFAULT NULL COMMENT '父类ID',
  `CODE` varchar(255) DEFAULT NULL COMMENT '编码',
  `DATA_KEY` varchar(50) DEFAULT NULL COMMENT 'DATA_KEY',
  `DATA_VALUE` varchar(255) DEFAULT NULL COMMENT 'DATA_VALUE',
  `DATA_ORDER` int(11) DEFAULT NULL COMMENT '顺序',
  `DICT_STATUS` varchar(2) DEFAULT NULL COMMENT '状态',
  `LEVEL` bigint(11) DEFAULT NULL COMMENT '枚举层数',
  `DICT_REMARK` varchar(255) DEFAULT NULL COMMENT '字典备注',
  PRIMARY KEY (`ID`),
  KEY `IX_env_p_data_dictionary_code` (`CODE`) USING BTREE,
  KEY `index_dic_data_value` (`DATA_VALUE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典';

-- ----------------------------
-- Records of p_data_dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_api
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_api`;
CREATE TABLE `p_fm_api` (
  `API_ID` char(36) COLLATE utf8_bin NOT NULL,
  `API_SQL` varchar(5000) COLLATE utf8_bin DEFAULT NULL,
  `API_NAME` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `API_STATE` int(3) DEFAULT NULL COMMENT '0否1是',
  `API_TYPE` int(3) DEFAULT NULL COMMENT '0,禁用，1增加，2修改，3get，4删除，5excute，6query，7page',
  `API_TABLE` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `API_DESC` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '描述，手机上可以以此为标题',
  `API_IS_LOGIN` int(3) DEFAULT NULL COMMENT '是否需要登录，0不需要，1需要',
  `API_METHOD` varchar(4) COLLATE utf8_bin DEFAULT NULL COMMENT '请求方式',
  `API_OUTPUT` varchar(5000) COLLATE utf8_bin DEFAULT NULL,
  `API_REMARK` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `API_ISCACHE` varchar(2) COLLATE utf8_bin DEFAULT NULL,
  `API_CREATOR` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  `API_CREATETIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`API_ID`),
  UNIQUE KEY `unique_name` (`API_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of p_fm_api
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_api_describe
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_api_describe`;
CREATE TABLE `p_fm_api_describe` (
  `ID` varchar(40) COLLATE utf8_bin NOT NULL,
  `APIID` char(36) COLLATE utf8_bin DEFAULT NULL,
  `NAME` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `TYPE` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `ISNECESSARY` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '是否必须，1=是，0=否',
  `DESCRIBES` text COLLATE utf8_bin COMMENT '描述',
  `DEFAULTVAL` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '默认值',
  `PARAM_PLACE` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '参数位置',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of p_fm_api_describe
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_api_privilege
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_api_privilege`;
CREATE TABLE `p_fm_api_privilege` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `TYPE` char(4) COLLATE utf8mb4_bin DEFAULT NULL,
  `RULES` varchar(5000) COLLATE utf8mb4_bin DEFAULT NULL,
  `DESCRIBES` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL,
  `REMARK` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `MESSAGE` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of p_fm_api_privilege
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_api_privilege_relation
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_api_privilege_relation`;
CREATE TABLE `p_fm_api_privilege_relation` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `API_ID` char(36) COLLATE utf8mb4_bin DEFAULT NULL,
  `PRI_ID` char(36) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of p_fm_api_privilege_relation
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_attachment
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_attachment`;
CREATE TABLE `p_fm_attachment` (
  `ID` char(36) NOT NULL,
  `STORAGE_ID` varchar(500) DEFAULT NULL COMMENT '文件存储路径',
  `FILE_NAME` varchar(300) DEFAULT NULL COMMENT '文件名称',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户Id',
  `USER_NAME` varchar(200) DEFAULT NULL COMMENT '用户名',
  `CONTENT_TYPE` varchar(200) DEFAULT NULL COMMENT '文件类型',
  `SYSTEM_ID` bigint(20) DEFAULT NULL COMMENT '系统ID',
  `size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `TYPE` int(2) DEFAULT NULL COMMENT '存储类型0:mongodb1:文件夹2:ftp',
  `CREATOR` bigint(20) DEFAULT NULL COMMENT '上传人',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_authoritygroup
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_authoritygroup`;
CREATE TABLE `p_fm_authoritygroup` (
  `ID` varchar(40) NOT NULL DEFAULT '',
  `NAME` varchar(255) DEFAULT NULL,
  `DYNAMICPAGE_ID` bigint(11) DEFAULT NULL,
  `SYSTEM_ID` bigint(11) DEFAULT NULL,
  `CREATER` bigint(11) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LASTUPDATER` bigint(11) DEFAULT NULL,
  `LASTUPDATE_TIME` datetime DEFAULT NULL,
  `ORDER` varchar(30) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `T_ORDER` varchar(30) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_authoritygroup
-- ----------------------------
INSERT INTO `p_fm_authoritygroup` VALUES ('7064a520-3c57-4ad4-93be-aa56932405f5', '1', '1996', '110', '719751', '2017-03-24 09:36:31', '719751', '2017-03-24 09:36:31', null, null, '1', '1');

-- ----------------------------
-- Table structure for p_fm_authoritygroup_component
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_authoritygroup_component`;
CREATE TABLE `p_fm_authoritygroup_component` (
  `ID` varchar(40) DEFAULT NULL,
  `AUTHORITY_GROUP_ID` varchar(150) DEFAULT NULL,
  `COMPONENT_ID` varchar(150) DEFAULT NULL,
  `AUTHORITY_VALUE` int(11) DEFAULT NULL,
  `CREATER` bigint(11) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LASTUPDATE_TIME` datetime DEFAULT NULL,
  `LASTUPDATER` bigint(11) DEFAULT NULL,
  `INCLUDE_COMPONENT` varchar(150) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_authoritygroup_component
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_authoritygroup_workflow_node
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_authoritygroup_workflow_node`;
CREATE TABLE `p_fm_authoritygroup_workflow_node` (
  `ID` varchar(40) DEFAULT NULL,
  `DYNAMICPAGE_ID` varchar(50) DEFAULT NULL,
  `AUTHORITY_GROUP_ID` varchar(40) DEFAULT NULL,
  `WORKFLOW_NODE_ID` varchar(40) DEFAULT NULL,
  `CREATER` bigint(11) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LASTUPDATE_TIME` datetime DEFAULT NULL,
  `LASTUPDATER` bigint(11) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_authoritygroup_workflow_node
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_cc_flow
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_cc_flow`;
CREATE TABLE `p_fm_cc_flow` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAGE_ID` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '页面ID',
  `JSON` text COLLATE utf8_bin COMMENT '选择人员',
  `FLOW_ID` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '流程编号',
  `CREATOR` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `REMARK` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PAGE_ID` (`PAGE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of p_fm_cc_flow
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_document
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_document`;
CREATE TABLE `p_fm_document` (
  `ID` varchar(40) NOT NULL,
  `LASTMODIFIED` datetime DEFAULT NULL COMMENT '最后编辑时间',
  `DYNAMICPAGE_NAME` varchar(200) DEFAULT NULL COMMENT '动态表单名称',
  `DYNAMICPAGE_ID` varchar(36) DEFAULT NULL COMMENT '动态表单ID',
  `AUTHOR_ID` bigint(20) DEFAULT NULL,
  `AUTHOR_DEPT_INDEX` varchar(200) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL COMMENT '创建时间',
  `ISTMP` bit(1) DEFAULT NULL,
  `VERSIONS` int(11) DEFAULT '0' COMMENT '修改次数',
  `PARENT` varchar(40) DEFAULT NULL,
  `SORTID` varchar(200) DEFAULT NULL,
  `STATELABEL` varchar(200) DEFAULT NULL,
  `INITIATOR` varchar(200) DEFAULT NULL COMMENT '发起人/创建人',
  `AUDITDATE` datetime DEFAULT NULL COMMENT '审批时间',
  `AUDITUSER` varchar(200) DEFAULT NULL COMMENT '审批人',
  `AUDITORNAMES` mediumtext,
  `STATE` varchar(200) DEFAULT NULL COMMENT '数据状态',
  `STATEINT` int(11) DEFAULT NULL,
  `LASTMODIFIER` varchar(200) DEFAULT NULL COMMENT '最后编辑人',
  `AUDITORLIST` mediumtext,
  `RECORD_ID` varchar(36) DEFAULT NULL COMMENT '数据库记录ID',
  `INSTANCE_ID` varchar(40) DEFAULT NULL COMMENT '流程实例ID',
  `TABLE_NAME` varchar(200) DEFAULT NULL COMMENT '数据库表的名称',
  `NODE_ID` varchar(40) DEFAULT NULL COMMENT '流程节点ID',
  `WORKFLOW_ID` varchar(40) DEFAULT NULL COMMENT '流程ID',
  `TASK_ID` varchar(40) DEFAULT NULL COMMENT '任务ID',
  `WORKITEM_ID` varchar(40) DEFAULT NULL,
  `ENTRY_ID` varchar(40) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DYNAMICPAGE_ID_2` (`DYNAMICPAGE_ID`,`RECORD_ID`,`WORKITEM_ID`),
  KEY `WORKITEM_ID` (`WORKITEM_ID`),
  KEY `RECORD_ID` (`RECORD_ID`),
  KEY `DYNAMICPAGE_ID` (`DYNAMICPAGE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_document
-- ----------------------------
INSERT INTO `p_fm_document` VALUES ('98ed15ad-04cc-4bc7-be49-19db18aa0a36', null, 'API管理表单', '2', null, null, '2017-12-18 19:56:53', '\0', null, null, null, null, null, null, null, null, null, '0', null, null, null, null, 'p_fm_api', null, null, null, null, null, null);
INSERT INTO `p_fm_document` VALUES ('e0f841c4-cb12-4db2-b299-cccdc52a8ca7', null, 'API管理表单', '2', null, null, '2017-12-18 19:41:08', '\0', null, null, null, null, null, null, null, null, null, '0', null, null, null, null, 'p_fm_api', null, null, null, null, null, null);
INSERT INTO `p_fm_document` VALUES ('f2fe8083-d2f7-4872-94d3-71f072beacd1', null, 'API管理表单', '2', null, null, '2017-12-18 19:52:27', '\0', null, null, null, null, null, null, null, null, null, '0', null, null, null, null, 'p_fm_api', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for p_fm_dynamicpage
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_dynamicpage`;
CREATE TABLE `p_fm_dynamicpage` (
  `id` char(36) NOT NULL,
  `modular` bigint(20) DEFAULT NULL,
  `version` varchar(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(1020) DEFAULT NULL,
  `page_type` int(11) DEFAULT NULL,
  `ordernum` int(20) DEFAULT NULL,
  `css` varchar(1024) DEFAULT NULL,
  `style` varchar(1020) DEFAULT NULL,
  `hidden_script` text,
  `disabled_script` text,
  `readonly_script` text,
  `model_xml` text,
  `is_log` varchar(4) DEFAULT NULL,
  `preload_script` text,
  `afterload_script` text,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `WORKFLOW_NODE_INFO` text COMMENT '流程结点信息',
  `STYLE_ID` varchar(36) DEFAULT NULL,
  `templateContext` longtext,
  `template_id` char(36) DEFAULT NULL,
  `SYSTEM_ID` bigint(20) DEFAULT NULL COMMENT '系统ID',
  `LINE_HEIGHT` varchar(20) DEFAULT NULL,
  `MIN_LINE_COUNT` varchar(20) DEFAULT NULL,
  `MAX_LINE_COUNT` varchar(20) DEFAULT NULL,
  `PDF_TEMPLATE_PAGE` bigint(20) DEFAULT NULL,
  `LINE_HEIGHT_TYPE` varchar(20) DEFAULT NULL,
  `STORES` longtext,
  `IS_CHECKOUT` int(11) DEFAULT NULL,
  `CHECKOUT_USER` varchar(250) DEFAULT NULL,
  `CREATED_USER` varchar(250) DEFAULT NULL,
  `UPDATED_USER` varchar(250) DEFAULT NULL,
  `SHOWTOTALCOUNT` varchar(4) DEFAULT NULL,
  `IS_LIMITPAGE` varchar(4) DEFAULT NULL,
  `PAGESIZE` bigint(20) DEFAULT NULL,
  `SHOWREVERSENUM` varchar(4) DEFAULT NULL,
  `REVERSENUMMODE` varchar(20) DEFAULT NULL,
  `REVERSESORTORD` varchar(4) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SYSTEM_ID0000` (`SYSTEM_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_dynamicpage
-- ----------------------------
INSERT INTO `p_fm_dynamicpage` VALUES ('1', '23', null, 'API管理列表', null, '1003', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;28e59625-77ac-23fa-a4da-6c647fd8f994&quot;,&quot;name&quot;:&quot;p_fm_api&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;1&quot;,&quot;limitCount&quot;:&quot;20&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var name=request.getParameter(\\&quot;name\\&quot;);\\r\\nvar desc=request.getParameter(\\&quot;desc\\&quot;);\\r\\nvar id=request.getParameter(\\&quot;id\\&quot;);\\r\\nvar where =\\&quot;\\&quot;;\\r\\nif(name!=null &amp;&amp; name!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_NAME LIKE \'%\\&quot;+name+\\&quot;%\'\\&quot;;\\r\\n}\\r\\nif(desc!=null &amp;&amp; desc!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_DESC LIKE \'%\\&quot;+desc+\\&quot;%\'\\&quot;;\\r\\n}\\r\\nif(id!=null &amp;&amp; id!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_ID LIKE \'%\\&quot;+id+\\&quot;%\'\\&quot;;\\r\\n}\\r\\nprint(where +\\&quot;AAAAAAAAAAAAAA\\&quot;);\\r\\n\\&quot;SELECT a.API_ID AS IDS, a.API_SQL,a.API_DESC,a.API_METHOD,case a.API_IS_LOGIN when \'1\' then \'需登录\' else \'不需登录\' end API_IS_LOGIN ,case a.API_STATE when \'0\' then \'禁用\' else \'启用\' end API_STATE,a.API_NAME,a.API_ID as ID,a.API_TABLE,case a.API_TYPE when \'0\' then \'forbidden\' when \'1\' then \'add\' when \'2\' then \'update\' when \'3\' then \'get\' when \'4\' then \'delete\' when \'5\' then \'excute\' when \'6\' then \'query\' when \'7\' then \'page\' when \'8\' then \'executeScript\' end API_TYPE FROM  p_fm_api a where 1=1 \\&quot; +where +\\&quot; order by a.API_CREATETIME desc \\&quot;;\\r\\n&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_api&quot;,&quot;modelItemCodes&quot;:&quot;API_SQL,API_METHOD,API_TYPE,API_IS_LOGIN,API_ID,API_STATE,API_TABLE,API_DESC,API_NAME&quot;}]', '0', null, '$(&quot;.table tbody tr&quot;).each(function(i,e){\r\n	var id=$(this).children(&quot;td&quot;).eq(0).find(&quot;input&quot;).val();\r\n	var seq=$(this).children(&quot;td&quot;).eq(2)\r\n	seq.html(&quot;&lt;a href=\'&quot;+baseUrl+&quot;document/view.do?dynamicPageId=2&amp;id=&quot;+id+&quot;\'&gt;&quot;+seq.text()+&quot;&lt;/a&gt;&quot;)\r\n})', '2017-03-26 17:02:10', '2017-12-18 15:31:20', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_api_list?? && p_fm_api_list?size gt 0>\r\n             <#assign p_fm_api = p_fm_api_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head lang=\"en\">\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-table.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/select2/select2.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">  \r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n    <script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n		for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n		cssFile=null;\r\n	</script>\r\n</head>\r\n<body>\r\n	<!-- Main content -->\r\n  <section class=\"content\">	\r\n		<div class=\"opeBtnGrop\">\r\n		<button data-valid=\"flase\" id=\'7dcba478-7377-4fbe-adf5-f59c0b00bb3e\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'7dcba478-7377-4fbe-adf5-f59c0b00bb3e\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'7dcba478-7377-4fbe-adf5-f59c0b00bb3e\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'2\'\r\n					<#if (pageActStatus[\'7dcba478-7377-4fbe-adf5-f59c0b00bb3e\'][\'hidden\'])?? && pageActStatus[\'7dcba478-7377-4fbe-adf5-f59c0b00bb3e\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-plus\'></i>\r\n			新增</button>\r\n		<button data-valid=\"flase\" id=\'dea500a0-6caf-4e32-adb0-ef9e8a2816d8\' title=\"\"\r\n				class=\" btn   btn-success\"\r\n					<#if (pageActStatus[\'dea500a0-6caf-4e32-adb0-ef9e8a2816d8\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'dea500a0-6caf-4e32-adb0-ef9e8a2816d8\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'2\'\r\n					<#if (pageActStatus[\'dea500a0-6caf-4e32-adb0-ef9e8a2816d8\'][\'hidden\'])?? && pageActStatus[\'dea500a0-6caf-4e32-adb0-ef9e8a2816d8\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-edit\'></i>\r\n			修改</button>\r\n		<button data-valid=\"flase\" id=\'f95066ec-c892-469e-9e53-9f8161fd1506\' title=\"\"\r\n				class=\" btn   btn-danger\"\r\n					<#if (pageActStatus[\'f95066ec-c892-469e-9e53-9f8161fd1506\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'f95066ec-c892-469e-9e53-9f8161fd1506\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'f95066ec-c892-469e-9e53-9f8161fd1506\'][\'hidden\'])?? && pageActStatus[\'f95066ec-c892-469e-9e53-9f8161fd1506\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-trash\'></i>\r\n			删除</button>\r\n		<button data-valid=\"flase\" id=\'bae5bf70-94e9-4b3d-b8fc-3c3c16ef5abf\' title=\"\"\r\n				class=\" btn   btn-info\"\r\n					<#if (pageActStatus[\'bae5bf70-94e9-4b3d-b8fc-3c3c16ef5abf\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'bae5bf70-94e9-4b3d-b8fc-3c3c16ef5abf\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'bae5bf70-94e9-4b3d-b8fc-3c3c16ef5abf\'][\'hidden\'])?? && pageActStatus[\'bae5bf70-94e9-4b3d-b8fc-3c3c16ef5abf\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-edit\'></i>\r\n			API权限编辑</button>\r\n		<button data-valid=\"flase\" id=\'bc10ac5b-2ab7-477a-a960-1f5bb4a4077a\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'bc10ac5b-2ab7-477a-a960-1f5bb4a4077a\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'bc10ac5b-2ab7-477a-a960-1f5bb4a4077a\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'bc10ac5b-2ab7-477a-a960-1f5bb4a4077a\'][\'hidden\'])?? && pageActStatus[\'bc10ac5b-2ab7-477a-a960-1f5bb4a4077a\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-plus\'></i>\r\n			新增关联接口</button>\r\n		<button data-valid=\"flase\" id=\'1a619d8b-456d-4e8b-bb3e-0646acebc6ef\' title=\"\"\r\n				class=\" btn   btn-danger\"\r\n					<#if (pageActStatus[\'1a619d8b-456d-4e8b-bb3e-0646acebc6ef\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'1a619d8b-456d-4e8b-bb3e-0646acebc6ef\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'1a619d8b-456d-4e8b-bb3e-0646acebc6ef\'][\'hidden\'])?? && pageActStatus[\'1a619d8b-456d-4e8b-bb3e-0646acebc6ef\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-off\'></i>\r\n			取消关联</button>\r\n		<button data-valid=\"flase\" id=\'1cd11161-04b4-4ff1-8eb2-832120e96b44\' title=\"\"\r\n				class=\" btn   btn-warning\"\r\n					<#if (pageActStatus[\'1cd11161-04b4-4ff1-8eb2-832120e96b44\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'1cd11161-04b4-4ff1-8eb2-832120e96b44\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'1cd11161-04b4-4ff1-8eb2-832120e96b44\'][\'hidden\'])?? && pageActStatus[\'1cd11161-04b4-4ff1-8eb2-832120e96b44\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-remove\'></i>\r\n			关闭</button>\r\n		<button data-valid=\"flase\" id=\'9f234c39-26ce-4760-9fec-a1f262f22297\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'9f234c39-26ce-4760-9fec-a1f262f22297\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'9f234c39-26ce-4760-9fec-a1f262f22297\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'9f234c39-26ce-4760-9fec-a1f262f22297\'][\'hidden\'])?? && pageActStatus[\'9f234c39-26ce-4760-9fec-a1f262f22297\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-save\'></i>\r\n			确定关联</button>\r\n		<button data-valid=\"flase\" id=\'92a764f2-4d0e-45de-8c0a-0f5a79da8eca\' title=\"\"\r\n				class=\" btn   btn-warning\"\r\n					<#if (pageActStatus[\'92a764f2-4d0e-45de-8c0a-0f5a79da8eca\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'92a764f2-4d0e-45de-8c0a-0f5a79da8eca\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'92a764f2-4d0e-45de-8c0a-0f5a79da8eca\'][\'hidden\'])?? && pageActStatus[\'92a764f2-4d0e-45de-8c0a-0f5a79da8eca\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			返回</button>\r\n		<button data-valid=\"flase\" id=\'9e3226b5-27ab-4dce-9935-3ed1ab992240\' title=\"\"\r\n				class=\" btn   btn-success\"\r\n					<#if (pageActStatus[\'9e3226b5-27ab-4dce-9935-3ed1ab992240\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'9e3226b5-27ab-4dce-9935-3ed1ab992240\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'9e3226b5-27ab-4dce-9935-3ed1ab992240\'][\'hidden\'])?? && pageActStatus[\'9e3226b5-27ab-4dce-9935-3ed1ab992240\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-search\'></i>\r\n			搜索</button>\r\n		</div>\r\n		<div class=\"row\">\r\n		<div class=\"col-md-12\">\r\n			<div class=\"box box-info\">\r\n				<div class=\"box-body\">					\r\n						<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n							<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"${p_fm_api_list_paginator.getLimit()}\">\r\n							<input type=\"hidden\" id=\"totalCount\" name=\"totalCount\" value=\"${p_fm_api_list_paginator.getTotalCount()}\">\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"orderBy\" value=\"${ (vo.orderBy)!\"\"}\" id=\"orderBy\"/>\r\n							<input type=\"hidden\" name=\"allowOrderBy\" id=\"allowOrderBy\" value=\"${ (vo.allowOrderBy)!\"\"}\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n		<div class=\"search_main row\">\r\n			<input type=\"hidden\" value=\"\r\n								\" id=\"selectOption\">\r\n			<input type=\"hidden\" value=\"\" id=\"selectLabel\">\r\n			<input type=\"hidden\" value=\"\" id=\"selectName\">\r\n			<input type=\"hidden\" value=\"ID@API名称@描述\" id=\"textLabel\">\r\n			<input type=\"hidden\" value=\"id@name@desc\" id=\"textName\">\r\n			<input type=\"hidden\" value=\"\" id=\"dateSelectLabel\">\r\n			<input type=\"hidden\" value=\"\" id=\"dateSelectName\">\r\n		</div>\r\n							<table id=\"bootstrapTable\" class=\"table table-hover\">\r\n								<thead>\r\n									<tr>\r\n										<th class=\"hidden\"></th>\r\n										<th data-width=\"\" data-field=\"\" data-checkbox=\"true\"></th>\r\n										<th data-width=\"50\">序号</th>\r\n		<th \r\n			data-width=\"5%\"\r\n		<#if (status[\'d2397b0000095917c02870a461c09a61\'][\'hidden\'])?? && status[\'d2397b0000095917c02870a461c09a61\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.IDS\"\r\n		>\r\n			ID\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"15%\"\r\n		<#if (status[\'28620aebdc7e1645b9cbf2f0a60901b9\'][\'hidden\'])?? && status[\'28620aebdc7e1645b9cbf2f0a60901b9\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.API_NAME\"\r\n		>\r\n			API名称\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"5%\"\r\n		<#if (status[\'2e4ebaa7b3b59912bef217445242f707\'][\'hidden\'])?? && status[\'2e4ebaa7b3b59912bef217445242f707\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.API_STATE\"\r\n		>\r\n			API状态\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"10%\"\r\n		<#if (status[\'32dd835f45e8c3340011b8f1a94ea90c\'][\'hidden\'])?? && status[\'32dd835f45e8c3340011b8f1a94ea90c\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.API_TYPE\"\r\n		>\r\n			类型\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"7%\"\r\n		<#if (status[\'41d99837c5c89caa1f485ec90e91a972\'][\'hidden\'])?? && status[\'41d99837c5c89caa1f485ec90e91a972\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.API_METHOD\"\r\n		>\r\n			请求方式\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"8%\"\r\n		<#if (status[\'761bb2191e0dbd7b064a5caa9885309a\'][\'hidden\'])?? && status[\'761bb2191e0dbd7b064a5caa9885309a\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.API_IS_LOGIN\"\r\n		>\r\n			是否登录\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'56cdde60383ab88f136b75266a8dac2c\'][\'hidden\'])?? && status[\'56cdde60383ab88f136b75266a8dac2c\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.API_DESC\"\r\n		>\r\n			描述\r\n		\r\n		</th>\r\n									</tr>\r\n								</thead>\r\n								<tbody>\r\n									<#if p_fm_api_list?? && p_fm_api_list?size gte 1>	\r\n										<#list\r\n											p_fm_api_list as p_fm_api>\r\n										<tr>\r\n											<td class=\"hidden formData\">\r\n												<input type=\"hidden\" value=\"${p_fm_api.ID!\'\'}\">\r\n											</td>\r\n											<td></td>\r\n									<td>${(p_fm_api_list_paginator.getPage() * p_fm_api_list_paginator.getLimit() - p_fm_api_list_paginator.getLimit() + p_fm_api_index + 1)}\r\n									</td>\r\n		<td <#if (status[\'d2397b0000095917c02870a461c09a61\'][\'hidden\'])?? && status[\'d2397b0000095917c02870a461c09a61\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.IDS!\'\'}\r\n		</td>\r\n		<td <#if (status[\'28620aebdc7e1645b9cbf2f0a60901b9\'][\'hidden\'])?? && status[\'28620aebdc7e1645b9cbf2f0a60901b9\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.API_NAME!\'\'}\r\n		</td>\r\n		<td <#if (status[\'2e4ebaa7b3b59912bef217445242f707\'][\'hidden\'])?? && status[\'2e4ebaa7b3b59912bef217445242f707\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.API_STATE!\'\'}\r\n		</td>\r\n		<td <#if (status[\'32dd835f45e8c3340011b8f1a94ea90c\'][\'hidden\'])?? && status[\'32dd835f45e8c3340011b8f1a94ea90c\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.API_TYPE!\'\'}\r\n		</td>\r\n		<td <#if (status[\'41d99837c5c89caa1f485ec90e91a972\'][\'hidden\'])?? && status[\'41d99837c5c89caa1f485ec90e91a972\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.API_METHOD!\'\'}\r\n		</td>\r\n		<td <#if (status[\'761bb2191e0dbd7b064a5caa9885309a\'][\'hidden\'])?? && status[\'761bb2191e0dbd7b064a5caa9885309a\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.API_IS_LOGIN!\'\'}\r\n		</td>\r\n		<td <#if (status[\'56cdde60383ab88f136b75266a8dac2c\'][\'hidden\'])?? && status[\'56cdde60383ab88f136b75266a8dac2c\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.API_DESC!\'\'}\r\n		</td>\r\n										</tr>\r\n										</#list>\r\n									</#if>\r\n								</tbody>\r\n							</table>\r\n						</form>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</section>\r\n	<script src=\"${basePath}template/AdminLTE/js/jquery.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/editTable.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/bootstrap-table.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/select2.full.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js\"></script>\r\n	<script src=\'${basePath}venson/js/common.js\'></script>\r\n	<script src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script>\r\n		for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n		scriptFile=null;\r\n		var basePath = \'${basePath}\';\r\n		var count=0;//默认选择行数为0\r\n	$(document).ready(function(){\r\n		$(document).keyup(function(event) {\r\n	    	if (event.keyCode == 13) {\r\n	    		$(\"#groupForm\").submit();\r\n	    	}\r\n	    });\r\n		 $(\"#bootstrapTable\").bootstrapTable({\r\n        	 pageSize:parseInt($(\"#pageSize\").val()),\r\n        	 pageNumber:parseInt($(\"#currentPage\").val()),\r\n        	 totalRows:parseInt($(\"#totalCount\").val()),\r\n        	 sidePagination:\"server\",\r\n        	 pagination:true,\r\n        	 onPageChange:function(number, size){\r\n        		$(\"#pageSize\").val(size);\r\n        		$(\"#currentPage\").val(number);\r\n        		$(\"#groupForm\").submit();\r\n        	 },\r\n        	 onClickRow:function(row,$element,field){\r\n        	  	  var $checkbox=$element.find(\":checkbox\").eq(0);\r\n        	  	  if($checkbox.get(0).checked){\r\n					  $checkbox.get(0).checked=false;\r\n					  $element.find(\"input[type=\'hidden\']\").removeAttr(\"name\",\"_selects\");\r\n        	  	  }else{\r\n					  $checkbox.get(0).checked=true;\r\n					  $element.find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n        	  	  }\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onCheck: function(row,$element){  \r\n				  $element.closest(\"tr\").find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onUncheck:function(row,$element){ \r\n        		 $element.closest(\"tr\").find(\"input[type=\'hidden\']\").removeAttr(\"name\");\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n        	 }, \r\n        	 onCheckAll: function (rows) {\r\n        		 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").attr(\"name\",\"_selects\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n             onUncheckAll: function (rows) {          	 \r\n            	 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").removeAttr(\"name\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n			 onSort:function(name, order){\r\n				 $(\"#allowOrderBy\").val(\"1\");\r\n				 var orderBy =$(\"#orderBy\").val();\r\n				 console.log(orderBy,name)\r\n				 if(orderBy.indexOf(name)!=-1&&orderBy.indexOf(\"desc\")!=-1){\r\n					 $(\"#orderBy\").val(name+\" asc\");\r\n				 }else{\r\n					 $(\"#orderBy\").val(name+\" desc\");\r\n				 }\r\n        		$(\"#groupForm\").submit();\r\n			 }\r\n         });							\r\n	\r\n	new addSearch();\r\n		\r\n	$(\"#7dcba478-7377-4fbe-adf5-f59c0b00bb3e\").click(function(){\r\n				location.href= basePath + \"document/view.do?id=&dynamicPageId=2\";\r\n	});\r\n	$(\"#dea500a0-6caf-4e32-adb0-ef9e8a2816d8\").click(function(){\r\n				if(count!=1){\r\n	alert(\"请选择一项进行操作\");\r\n	return false;\r\n}\r\nvar id = $(\"input[name=\'_selects\']\").val();\r\nlocation.href = basePath + \"document/view.do?dynamicPageId=2&id=\"+id;\r\n	});\r\n	$(\"#f95066ec-c892-469e-9e53-9f8161fd1506\").click(function(){\r\n				if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nif(!confirm(\"是否确认删除?\")){\r\n	return false;\r\n}\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize(),\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"删除成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=1\";\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n	$(\"#bae5bf70-94e9-4b3d-b8fc-3c3c16ef5abf\").click(function(){\r\n				location.href = basePath + \"document/view.do?dynamicPageId=20&edit=true\";\r\n	});\r\n	$(\"#bc10ac5b-2ab7-477a-a960-1f5bb4a4077a\").click(function(){\r\n				var priid=Comm.getUrlParam(\"priid\");\r\nlocation.href = basePath+\"document/view.do?dynamicPageId=1&relation=true&priid=\"+priid;\r\n	});\r\n	$(\"#1a619d8b-456d-4e8b-bb3e-0646acebc6ef\").click(function(){\r\n				if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nvar priid=Comm.getUrlParam(\"priid\");\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize()+\"&priid=\"+priid,\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"取消关联成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=1&pri=true&priid=\"+priid;\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n	$(\"#1cd11161-04b4-4ff1-8eb2-832120e96b44\").click(function(){\r\n				top.dialog({id : window.name}).close();\r\n	});\r\n	$(\"#9f234c39-26ce-4760-9fec-a1f262f22297\").click(function(){\r\n				if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nvar priid=Comm.getUrlParam(\"priid\")\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize()+\"&priid=\"+priid,\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"关联成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=1&pri=true&priid=\"+priid;\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n	$(\"#92a764f2-4d0e-45de-8c0a-0f5a79da8eca\").click(function(){\r\n				var priid=Comm.getUrlParam(\"priid\");\r\nlocation.href=basePath+\"document/view.do?dynamicPageId=1&pri=true&priid=\"+priid;\r\n	});\r\n	$(\"#9e3226b5-27ab-4dce-9935-3ed1ab992240\").click(function(){\r\n				$(\"#groupForm\").submit();\r\n	});\r\n        \r\n				$(\".table tbody tr\").each(function(i,e){\r\n	var id=$(this).children(\"td\").eq(0).find(\"input\").val();\r\n	var seq=$(this).children(\"td\").eq(2)\r\n	seq.html(\"<a href=\'\"+baseUrl+\"document/view.do?dynamicPageId=2&id=\"+id+\"\'>\"+seq.text()+\"</a>\")\r\n})\r\n		});\r\n	</script>\r\n</body>\r\n</html>', '1', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '1', '50', '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('10', null, null, '模块添加页面', null, '1003', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;3ccee183-ed30-840a-b167-10b2858650a1&quot;,&quot;name&quot;:&quot;p_fm_dynamicpage&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;1&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;\\&quot;SELECT  a.id as ID,a.modular,a.name,b.modularName as mname FROM  p_fm_dynamicpage a left join p_fm_modular b on a.modular=b.id\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_dynamicpage&quot;,&quot;modelItemCodes&quot;:&quot;id,modular,name&quot;}]', '0', null, null, '2017-08-11 17:26:21', '2017-12-18 20:00:34', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_dynamicpage_list?? && p_fm_dynamicpage_list?size gt 0>\r\n             <#assign p_fm_dynamicpage = p_fm_dynamicpage_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head lang=\"en\">\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-table.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/select2/select2.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">  \r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n    <script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n		for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n		cssFile=null;\r\n	</script>\r\n</head>\r\n<body>\r\n	<!-- Main content -->\r\n  <section class=\"content\">	\r\n		<div class=\"opeBtnGrop\">\r\n		<button data-valid=\"flase\" id=\'855d3397-bb88-4679-b340-841110988987\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'855d3397-bb88-4679-b340-841110988987\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'855d3397-bb88-4679-b340-841110988987\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'855d3397-bb88-4679-b340-841110988987\'][\'hidden\'])?? && pageActStatus[\'855d3397-bb88-4679-b340-841110988987\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-save\'></i>\r\n			确定更改</button>\r\n		<button data-valid=\"flase\" id=\'d670486d-e040-4fec-b162-782748cb8afb\' title=\"\"\r\n				class=\" btn   btn-warning\"\r\n					<#if (pageActStatus[\'d670486d-e040-4fec-b162-782748cb8afb\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'d670486d-e040-4fec-b162-782748cb8afb\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'d670486d-e040-4fec-b162-782748cb8afb\'][\'hidden\'])?? && pageActStatus[\'d670486d-e040-4fec-b162-782748cb8afb\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-remove\'></i>\r\n			关闭</button>\r\n		</div>\r\n		<div class=\"row\">\r\n		<div class=\"col-md-12\">\r\n			<div class=\"box box-info\">\r\n				<div class=\"box-body\">					\r\n						<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n							<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"${p_fm_dynamicpage_list_paginator.getLimit()}\">\r\n							<input type=\"hidden\" id=\"totalCount\" name=\"totalCount\" value=\"${p_fm_dynamicpage_list_paginator.getTotalCount()}\">\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"orderBy\" value=\"${ (vo.orderBy)!\"\"}\" id=\"orderBy\"/>\r\n							<input type=\"hidden\" name=\"allowOrderBy\" id=\"allowOrderBy\" value=\"${ (vo.allowOrderBy)!\"\"}\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							<table id=\"bootstrapTable\" class=\"table table-hover\">\r\n								<thead>\r\n									<tr>\r\n										<th class=\"hidden\"></th>\r\n										<th data-width=\"\" data-field=\"\" data-checkbox=\"true\"></th>\r\n										<th data-width=\"50\">序号</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'24c4d0ab3a5d01a59ef342970ea5397a\'][\'hidden\'])?? && status[\'24c4d0ab3a5d01a59ef342970ea5397a\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_dynamicpage.ID\"\r\n		>\r\n			pageId\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'13689a2c2062ed8d9844d0c32fdbec0b\'][\'hidden\'])?? && status[\'13689a2c2062ed8d9844d0c32fdbec0b\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_dynamicpage.name\"\r\n		>\r\n			名称\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'f05d0e802a5f89e32324012de9ee74a6\'][\'hidden\'])?? && status[\'f05d0e802a5f89e32324012de9ee74a6\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_dynamicpage.mname\"\r\n		>\r\n			目前所属模块\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'0a00b1952547bc9e5ee04c96124062d9\'][\'hidden\'])?? && status[\'0a00b1952547bc9e5ee04c96124062d9\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_dynamicpage.modular\"\r\n		>\r\n			模块id\r\n		\r\n		</th>\r\n									</tr>\r\n								</thead>\r\n								<tbody>\r\n									<#if p_fm_dynamicpage_list?? && p_fm_dynamicpage_list?size gte 1>	\r\n										<#list\r\n											p_fm_dynamicpage_list as p_fm_dynamicpage>\r\n										<tr>\r\n											<td class=\"hidden formData\">\r\n												<input type=\"hidden\" value=\"${p_fm_dynamicpage.ID!\'\'}\">\r\n											</td>\r\n											<td></td>\r\n									<td>${(p_fm_dynamicpage_list_paginator.getPage() * p_fm_dynamicpage_list_paginator.getLimit() - p_fm_dynamicpage_list_paginator.getLimit() + p_fm_dynamicpage_index + 1)}\r\n									</td>\r\n		<td <#if (status[\'24c4d0ab3a5d01a59ef342970ea5397a\'][\'hidden\'])?? && status[\'24c4d0ab3a5d01a59ef342970ea5397a\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_dynamicpage.ID!\'\'}\r\n		</td>\r\n		<td <#if (status[\'13689a2c2062ed8d9844d0c32fdbec0b\'][\'hidden\'])?? && status[\'13689a2c2062ed8d9844d0c32fdbec0b\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_dynamicpage.name!\'\'}\r\n		</td>\r\n		<td <#if (status[\'f05d0e802a5f89e32324012de9ee74a6\'][\'hidden\'])?? && status[\'f05d0e802a5f89e32324012de9ee74a6\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_dynamicpage.mname!\'\'}\r\n		</td>\r\n		<td <#if (status[\'0a00b1952547bc9e5ee04c96124062d9\'][\'hidden\'])?? && status[\'0a00b1952547bc9e5ee04c96124062d9\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_dynamicpage.modular!\'\'}\r\n		</td>\r\n										</tr>\r\n										</#list>\r\n									</#if>\r\n								</tbody>\r\n							</table>\r\n						</form>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</section>\r\n	<script src=\"${basePath}template/AdminLTE/js/jquery.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/editTable.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/bootstrap-table.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/select2.full.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js\"></script>\r\n	<script src=\'${basePath}venson/js/common.js\'></script>\r\n	<script src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script>\r\n		for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n		scriptFile=null;\r\n		var basePath = \'${basePath}\';\r\n		var count=0;//默认选择行数为0\r\n	$(document).ready(function(){\r\n		$(document).keyup(function(event) {\r\n	    	if (event.keyCode == 13) {\r\n	    		$(\"#groupForm\").submit();\r\n	    	}\r\n	    });\r\n		 $(\"#bootstrapTable\").bootstrapTable({\r\n        	 pageSize:parseInt($(\"#pageSize\").val()),\r\n        	 pageNumber:parseInt($(\"#currentPage\").val()),\r\n        	 totalRows:parseInt($(\"#totalCount\").val()),\r\n        	 sidePagination:\"server\",\r\n        	 pagination:true,\r\n        	 onPageChange:function(number, size){\r\n        		$(\"#pageSize\").val(size);\r\n        		$(\"#currentPage\").val(number);\r\n        		$(\"#groupForm\").submit();\r\n        	 },\r\n        	 onClickRow:function(row,$element,field){\r\n        	  	  var $checkbox=$element.find(\":checkbox\").eq(0);\r\n        	  	  if($checkbox.get(0).checked){\r\n					  $checkbox.get(0).checked=false;\r\n					  $element.find(\"input[type=\'hidden\']\").removeAttr(\"name\",\"_selects\");\r\n        	  	  }else{\r\n					  $checkbox.get(0).checked=true;\r\n					  $element.find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n        	  	  }\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onCheck: function(row,$element){  \r\n				  $element.closest(\"tr\").find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onUncheck:function(row,$element){ \r\n        		 $element.closest(\"tr\").find(\"input[type=\'hidden\']\").removeAttr(\"name\");\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n        	 }, \r\n        	 onCheckAll: function (rows) {\r\n        		 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").attr(\"name\",\"_selects\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n             onUncheckAll: function (rows) {          	 \r\n            	 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").removeAttr(\"name\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n			 onSort:function(name, order){\r\n				 $(\"#allowOrderBy\").val(\"1\");\r\n				 var orderBy =$(\"#orderBy\").val();\r\n				 console.log(orderBy,name)\r\n				 if(orderBy.indexOf(name)!=-1&&orderBy.indexOf(\"desc\")!=-1){\r\n					 $(\"#orderBy\").val(name+\" asc\");\r\n				 }else{\r\n					 $(\"#orderBy\").val(name+\" desc\");\r\n				 }\r\n        		$(\"#groupForm\").submit();\r\n			 }\r\n         });							\r\n		\r\n	$(\"#855d3397-bb88-4679-b340-841110988987\").click(function(){\r\n				if(count==0){\r\nalert(\"请至少选择一项\");\r\nreturn false;\r\n}\r\nvar mid=Comm.getUrlParam(\"818bb1c3-bf87-4861-a9b7-88b605f1a2e3\");\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize()+\"&mid=\"+mid,\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"关联成功\");\r\ntop.dialog({id : window.name}).close(\"Y\");\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n	$(\"#d670486d-e040-4fec-b162-782748cb8afb\").click(function(){\r\n				top.dialog({id : window.name}).close();\r\n	});\r\n        \r\n		});\r\n	</script>\r\n</body>\r\n</html>', '1', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', null);
INSERT INTO `p_fm_dynamicpage` VALUES ('13', null, null, '意见组件（PC）', 'APP', '1002', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;800220eb-365c-8300-3a75-65d3bbbc2dc6&quot;,&quot;name&quot;:&quot;oa_suggestion&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id = DocumentUtils.getDataItem(\\&quot;notices_list\\&quot;,\\&quot;ID\\&quot;);\\r\\n\\&quot;SELECT  Flag,remark,Dept,Conment,Orders,LinkName,Link,BusinessId,Date,ID,Person,GROUP_ID,PersonName,status,sendTime,isLeader,type,DeptName,deadline  FROM  oa_suggestion where flag = 0 and BusinessId = \'\\&quot; + id + \\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;oa_suggestion&quot;,&quot;modelItemCodes&quot;:&quot;GROUP_ID,Flag,PersonName,status,remark,Dept,sendTime,Conment,isLeader,Orders,type,LinkName,DeptName,Link,BusinessId,Date,ID,deadline,Person&quot;},{&quot;id&quot;:&quot;dde5a6dd-71df-5950-d886-1bc71f0db29a&quot;,&quot;name&quot;:&quot;oa_comment&quot;,&quot;isSingle&quot;:&quot;0&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id = DocumentUtils.getDataItem(\\&quot;notices_list\\&quot;,\\&quot;ID\\&quot;);\\r\\n\\&quot;SELECT  Flag,remark,Dept,Conment,Orders,LinkName,Link,BusinessId,Date,ID,Person,GROUP_ID,PersonName,status,sendTime,isLeader,type,DeptName,deadline  FROM  oa_suggestion where flag =1 and BusinessId = \'\\&quot; + id + \\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;oa_suggestion&quot;,&quot;modelItemCodes&quot;:&quot;GROUP_ID,Flag,PersonName,status,remark,Dept,sendTime,Conment,isLeader,Orders,type,LinkName,DeptName,Link,BusinessId,Date,ID,deadline,Person&quot;}]', '0', null, null, '2016-12-08 09:10:03', '2016-12-19 12:56:30', null, null, '\r\n		<--MYLayoutAndCom-->\r\n		\r\n		         <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >	\r\n			 </table>\r\n		<--MYLayoutAndCom/-->\r\n		\r\n			<--MYpageJScript-->\r\n			\r\n			\r\n\r\n			<--MYpageJScript/-->\r\n		\r\n			<--MYvalidate-->\r\n			\r\n			<--MYvalidate/-->\r\n		\r\n		<--MYpageActScript-->\r\n		\r\n		<--MYpageActScript/-->\r\n', '95', '110', null, null, null, null, null, null, '1', '9999', '9999', null, '0', '0', null, '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('14', null, null, '意见列表片段（手机）', null, '1003', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;7b6278ee-dde4-3fa0-a3ba-5d423aa574cc&quot;,&quot;name&quot;:&quot;oa_suggestion&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id = DocumentUtils.getDataItem(\\&quot;notices_list\\&quot;,\\&quot;ID\\&quot;);\\r\\n\\&quot;SELECT  Flag,remark,Dept,Conment,Orders,LinkName,Link,BusinessId,Date,ID,Person,GROUP_ID,PersonName,status,sendTime,isLeader,type,DeptName,deadline  FROM  oa_suggestion where flag = 0 and BusinessId = \'\\&quot; + id + \\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;oa_suggestion&quot;,&quot;modelItemCodes&quot;:&quot;GROUP_ID,Flag,PersonName,status,remark,Dept,sendTime,Conment,isLeader,Orders,type,LinkName,DeptName,Link,BusinessId,Date,ID,deadline,Person&quot;}]', '0', null, null, '2016-12-20 16:12:58', '2016-12-20 16:13:02', null, null, '\r\n\r\n<--MYLayoutAndCom--> \r\n		<div class=\"form-group\">\r\n			<div class=\"col-xs-12\">\r\n			</div>\r\n		</div>\r\n	\r\n		\r\n		<div class=\"form-group\">\r\n		<div class=\"col-xs-12\">\r\n		<div  id=\"datatable\">\r\n				\r\n					<table class=\"table datatable table-bordered\" style=\"table-layout:fixed;width:100%\">\r\n						<thead>\r\n							<tr>\r\n								<th  class=\"hidden\" style=\"text-align:center;\"></th>\r\n							</tr>\r\n						</thead>\r\n						<tbody id=\"includeList3\">\r\n		            	<#if oa_suggestion_list?? >	\r\n		            	 	<#list\r\n		            	 		oa_suggestion_list as oa_suggestion>\r\n								<tr>\r\n								\r\n								</tr>\r\n							</#list>\r\n						</#if>\r\n					</tbody>\r\n				</table>\r\n			</div>\r\n			</div>\r\n		</div>\r\n<--MYLayoutAndCom/-->\r\n\r\n<--MYpageJScript-->\r\n<--MYpageJScript/-->\r\n<--MYvalidate-->\r\n<--MYvalidate/-->\r\n\r\n<--MYpageActScript-->\r\n<--MYpageActScript/--> \r\n', '110', '110', null, null, null, null, null, null, '1', '9999', '9999', null, '0', '0', null, '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('1411', null, null, '意见组件（手机）', 'APP', '1002', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;800220eb-365c-8300-3a75-65d3bbbc2dc6&quot;,&quot;name&quot;:&quot;oa_suggestion&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id = DocumentUtils.getDataItem(\\&quot;notices_list\\&quot;,\\&quot;ID\\&quot;);\\r\\n\\&quot;SELECT  Flag,remark,Dept,Conment,Orders,LinkName,Link,BusinessId,Date,ID,Person,GROUP_ID,PersonName,status,sendTime,isLeader,type,DeptName,deadline  FROM  oa_suggestion where flag = 0 and BusinessId = \'\\&quot; + id + \\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;oa_suggestion&quot;,&quot;modelItemCodes&quot;:&quot;GROUP_ID,Flag,PersonName,status,remark,Dept,sendTime,Conment,isLeader,Orders,type,LinkName,DeptName,Link,BusinessId,Date,ID,deadline,Person&quot;},{&quot;id&quot;:&quot;dde5a6dd-71df-5950-d886-1bc71f0db29a&quot;,&quot;name&quot;:&quot;oa_comment&quot;,&quot;isSingle&quot;:&quot;0&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id = DocumentUtils.getDataItem(\\&quot;notices_list\\&quot;,\\&quot;ID\\&quot;);\\r\\n\\&quot;SELECT  Flag,remark,Dept,Conment,Orders,LinkName,Link,BusinessId,Date,ID,Person,GROUP_ID,PersonName,status,sendTime,isLeader,type,DeptName,deadline  FROM  oa_suggestion where flag =1 and BusinessId = \'\\&quot; + id + \\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;oa_suggestion&quot;,&quot;modelItemCodes&quot;:&quot;GROUP_ID,Flag,PersonName,status,remark,Dept,sendTime,Conment,isLeader,Orders,type,LinkName,DeptName,Link,BusinessId,Date,ID,deadline,Person&quot;}]', '0', null, null, '2015-08-31 11:09:43', '2016-11-25 16:49:14', null, null, '\r\n		<--MYLayoutAndCom-->\r\n		\r\n		         <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >	\r\n			 </table>\r\n		<--MYLayoutAndCom/-->\r\n		\r\n			<--MYpageJScript-->\r\n			\r\n			\r\n			\r\n			if(window.KindEditor){ //富文本编辑框\r\n		  		KindEditor.ready(function(K){\r\n		  			var options ={\r\n			            allowFileManager : true,\r\n			            allowUpload : true,\r\n			            uploadJson : basePath+\'common/file/uploadImg.do\',\r\n			            //readonlyMode:true,\r\n			            bodyClass : \'article-content\',\r\n			            afterBlur: function(){this.sync();$(this.container[0]).removeClass(\'focus\');\r\n			            },\r\n			            afterFocus: function(){$(this.container[0]).addClass(\'focus\');},\r\n			            afterCreate : function(){\r\n			                var doc = this.edit.doc; \r\n			                var cmd = this.edit.cmd; \r\n			                if(K.WEBKIT){\r\n			                    $(doc.body).bind(\'paste\', function(ev){\r\n			                        var $this = $(this);\r\n			                        var original =  ev.originalEvent;\r\n			                        var file =  original.clipboardData.items[0].getAsFile();\r\n			                        var reader = new FileReader();\r\n			                        reader.onload = function (evt){\r\n			                            var result = evt.target.result;\r\n			                            var arr = result.split(\",\");\r\n			                            var data = arr[1]; // raw base64\r\n			                            var contentType = arr[0].split(\";\")[0].split(\":\")[1];\r\n			                            //html = \'<img src=\"\' + result + \'\" alt=\"\" />\';\r\n			                            //cmd.inserthtml(html);\r\n			                            //post本地图片到服务器并返回服务器存放地址\r\n										$.ajax({\r\n			                            	type:\"post\",\r\n			                            	url: basePath+\"common/file/generateImageByBase64Str.do\",\r\n			                            	dataType:\"html\",\r\n			                            	data:{imgStr:result},\r\n			                            	success:function(data){\r\n			                            	var html = \'<img src=\"\'+basePath+\'/attached/image/\' +data + \'\" alt=\"\" />\';\r\n			                            	cmd.inserthtml(html)\r\n			                            	}\r\n			                            });\r\n			                            //$.post(\'<%=basePath%>resources/demo/pages/ajaxPasteImage.html\', {editor: html}, function(data){cmd.inserthtml(data);});\r\n			                        };\r\n			                        reader.readAsDataURL(file);\r\n			                    });\r\n			                }\r\n			                if(K.GECKO){\r\n			                    K(doc.body).bind(\'paste\', function(ev){\r\n			                        setTimeout(function(){\r\n			                            var html = K(doc.body).html();\r\n			                            if(html.search(/<img src=\"data:.+;base64,/) > -1)\r\n			                            {\r\n			                                $.post(basePath+\'resources/demo/pages/ajaxPasteImage.html\', {editor: html}, function(data){K(doc.body).html(data);});\r\n			                            }\r\n			                        }, 80);\r\n			                    });\r\n			                }\r\n			            }\r\n			        };\r\n		  		    \r\n				});\r\n	  		};\r\n\r\n			<--MYpageJScript/-->\r\n		\r\n			<--MYvalidate-->\r\n			\r\n			<--MYvalidate/-->\r\n		\r\n		<--MYpageActScript-->\r\n		\r\n		<--MYpageActScript/-->\r\n', '109', '110', null, null, null, null, null, null, '1', '9999', '9999', null, '0', '0', null, '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('1504', null, null, '意见组件（电脑）', null, '1002', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;f57bb481-737a-f286-2d80-23388b8acdd0&quot;,&quot;name&quot;:&quot;oa_suggestion&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id = DocumentUtils.getDataItem(\\&quot;notices_list\\&quot;,\\&quot;ID\\&quot;);\\r\\n\\&quot;SELECT  Flag,remark,Dept,Conment,Orders,LinkName,Link,BusinessId,Date,ID,Person,GROUP_ID,PersonName,status,sendTime,isLeader,type,DeptName,deadline  FROM  oa_suggestion where flag = 0 and BusinessId = \'\\&quot; + id + \\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;oa_suggestion&quot;,&quot;modelItemCodes&quot;:&quot;Flag,remark,Dept,Conment,Orders,LinkName,Link,BusinessId,Date,ID,Person&quot;},{&quot;id&quot;:&quot;7731a2e5-88d5-fe98-dca9-9d10ebf3e104&quot;,&quot;name&quot;:&quot;oa_comment&quot;,&quot;isSingle&quot;:&quot;0&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id = DocumentUtils.getDataItem(\\&quot;notices_list\\&quot;,\\&quot;ID\\&quot;);\\r\\n\\&quot;SELECT  Flag,remark,Dept,Conment,Orders,LinkName,Link,BusinessId,Date,ID,Person,GROUP_ID,PersonName,status,sendTime,isLeader,type,DeptName,deadline  FROM  oa_suggestion where flag =1 and BusinessId = \'\\&quot; + id + \\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;oa_suggestion&quot;,&quot;modelItemCodes&quot;:&quot;GROUP_ID,Flag,PersonName,status,remark,Dept,sendTime,Conment,isLeader,Orders,type,LinkName,DeptName,Link,BusinessId,Date,ID,deadline,Person&quot;}]', '0', null, null, '2015-11-04 12:32:07', '2016-12-16 15:21:37', null, null, '\r\n		<--MYLayoutAndCom-->\r\n		\r\n		         <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >	\r\n			 </table>\r\n		<--MYLayoutAndCom/-->\r\n		\r\n			<--MYpageJScript-->\r\n			\r\n			\r\n\r\n			<--MYpageJScript/-->\r\n		\r\n			<--MYvalidate-->\r\n			\r\n			<--MYvalidate/-->\r\n		\r\n		<--MYpageActScript-->\r\n		\r\n		<--MYpageActScript/-->\r\n', '95', '110', null, null, null, null, null, null, '1', '9999', '9999', null, '0', '0', null, '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('17', null, null, '系统属性字典配置表单', null, '1002', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;9b42bdb1-5d97-dea8-9600-c34300576e32&quot;,&quot;name&quot;:&quot;p_data_dictionary&quot;,&quot;isSingle&quot;:&quot;0&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id=request.getParameter(\\&quot;id\\&quot;);\\r\\nvar pid=DocumentUtils.queryObjectById(\\&quot;p_data_dictionary\\&quot;,\\&quot;PID\\&quot;,\\&quot;ID\\&quot;,id);\\r\\nroot.put(\\&quot;suggestion\\&quot;,pid);\\r\\n\\&quot;SELECT  CODE,DICT_REMARK,DATA_KEY,DICT_STATUS,PID,ID,LEVEL,DATA_ORDER,DATA_VALUE  FROM  p_data_dictionary where ID=\'\\&quot;+id+\\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_data_dictionary&quot;,&quot;modelItemCodes&quot;:&quot;CODE,DICT_REMARK,DATA_KEY,DICT_STATUS,PID,ID,LEVEL,DATA_VALUE,DATA_ORDER&quot;}]', '0', null, 'var a=$(&quot;#83519b5a-2576-4c70-8136-267ccab815c2&quot;).val();\r\nif(a==\'1\'){\r\n$(&quot;#b8e6dc9f-4eb4-4c31-a315-0963c09466d1&quot;).parent().hide();\r\n$(&quot;#133db9dd-fcba-4c88-ad74-b18b803c1e55&quot;).parent().hide();\r\n$(&quot;#133db9dd-fcba-4c88-ad74-b18b803c1e55&quot;).attr(&quot;disabled&quot;,true);\r\n}', '2017-09-09 15:26:00', '2017-09-11 11:23:56', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_data_dictionary_list?? && p_data_dictionary_list?size gt 0>\r\n             <#assign p_data_dictionary = p_data_dictionary_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/font-awesome.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrapValidator.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n	<script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n	for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n	cssFile=null;\r\n	</script>\r\n	\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n        \r\n	<![endif]-->\r\n	<script>\r\n		\r\n    </script>\r\n</head>\r\n<body>\r\n    <div class=\"content\"> \r\n		<div class=\"row opeBtnGrop\" id=\"buttons\">\r\n			<div class=\"col-md-12\">\r\n		<button data-valid=\"true\" id=\'d6db3231-d736-431f-b89f-fb66945e33df\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'d6db3231-d736-431f-b89f-fb66945e33df\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'d6db3231-d736-431f-b89f-fb66945e33df\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'d6db3231-d736-431f-b89f-fb66945e33df\'][\'hidden\'])?? && pageActStatus[\'d6db3231-d736-431f-b89f-fb66945e33df\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-save\'></i>\r\n			保存</button>\r\n		<button data-valid=\"flase\" id=\'54736121-b385-43a3-a6ad-187fd1ce9d19\' title=\"\"\r\n				class=\" btn   btn-warning\"\r\n					<#if (pageActStatus[\'54736121-b385-43a3-a6ad-187fd1ce9d19\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'54736121-b385-43a3-a6ad-187fd1ce9d19\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'54736121-b385-43a3-a6ad-187fd1ce9d19\'][\'hidden\'])?? && pageActStatus[\'54736121-b385-43a3-a6ad-187fd1ce9d19\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			返回</button>\r\n			</div>\r\n		</div>\r\n		<div class=\"row\" id=\"newTitle\"></div>\r\n		<div class=\"row\">\r\n			<div class=\"col-md-12\">\r\n				<div class=\"box box-info\">\r\n					<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}workflow/wf/excute.do\" role=\"customForm\" method=\"post\">\r\n						<div class=\"box-body\">\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div  data-required=\"0\" data-placeholder=\"\" style=\"display:none;\">\r\n		<input class=\"dataItemCode\" type=\'hidden\'\r\n			name=\'a878a3375bd47a2f773f1d10c6c92ee7\'\r\n				value=\"${(p_data_dictionary.ID)!\'\'}\"\r\n		id=\'218da08a-1906-41b2-8b28-da3d0dc3c74e\'\r\n		/>\r\n	</div>\r\n	<div class=\"customGroup\"  data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'53a520bae96818822a3783f4de3f34f6\'][\'hidden\'])?? && status[\'53a520bae96818822a3783f4de3f34f6\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_53a520bae96818822a3783f4de3f34f6\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'53a520bae96818822a3783f4de3f34f6\'\r\n		<#if (status[\'53a520bae96818822a3783f4de3f34f6\'][\'disabled\'])?? && status[\'53a520bae96818822a3783f4de3f34f6\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'53a520bae96818822a3783f4de3f34f6\'][\'readonly\'])?? && status[\'53a520bae96818822a3783f4de3f34f6\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_data_dictionary.DATA_ORDER)!\'\'}\"\r\n		id=\'ad999fa4-33f1-40b9-ad1a-4fc2828b3387\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\" data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'be2298e3da03834143b3ca069c743bfc\'][\'hidden\'])?? && status[\'be2298e3da03834143b3ca069c743bfc\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_be2298e3da03834143b3ca069c743bfc\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'be2298e3da03834143b3ca069c743bfc\'][\'disabled\'])?? && status[\'be2298e3da03834143b3ca069c743bfc\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'be2298e3da03834143b3ca069c743bfc\'][\'readonly\'])?? && status[\'be2298e3da03834143b3ca069c743bfc\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'be2298e3da03834143b3ca069c743bfc\'\r\n\r\n				value=\"${(p_data_dictionary.LEVEL)!\'\'}\"\r\n\r\n		id=\'83519b5a-2576-4c70-8136-267ccab815c2\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'be2298e3da03834143b3ca069c743bfc\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'11eda505319a6e269f3c4067f21c5f40\'][\'hidden\'])?? && status[\'11eda505319a6e269f3c4067f21c5f40\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_11eda505319a6e269f3c4067f21c5f40\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'11eda505319a6e269f3c4067f21c5f40\'][\'disabled\'])?? && status[\'11eda505319a6e269f3c4067f21c5f40\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'11eda505319a6e269f3c4067f21c5f40\'][\'readonly\'])?? && status[\'11eda505319a6e269f3c4067f21c5f40\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'11eda505319a6e269f3c4067f21c5f40\'\r\n\r\n				value=\"${(p_data_dictionary.PID)!\'\'}\"\r\n\r\n		id=\'133db9dd-fcba-4c88-ad74-b18b803c1e55\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'11eda505319a6e269f3c4067f21c5f40\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\"  data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'2f97a429f8b6eb2cae13cbbc8bea86f8\'][\'hidden\'])?? && status[\'2f97a429f8b6eb2cae13cbbc8bea86f8\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_2f97a429f8b6eb2cae13cbbc8bea86f8\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'2f97a429f8b6eb2cae13cbbc8bea86f8\'\r\n		<#if (status[\'2f97a429f8b6eb2cae13cbbc8bea86f8\'][\'disabled\'])?? && status[\'2f97a429f8b6eb2cae13cbbc8bea86f8\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'2f97a429f8b6eb2cae13cbbc8bea86f8\'][\'readonly\'])?? && status[\'2f97a429f8b6eb2cae13cbbc8bea86f8\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_data_dictionary.DATA_KEY)!\'\'}\"\r\n		id=\'f2068ebf-3b49-4917-ac87-da85d470f9f5\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\"  data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'102dfe19c1bbe2b7a96b93f97676e79c\'][\'hidden\'])?? && status[\'102dfe19c1bbe2b7a96b93f97676e79c\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_102dfe19c1bbe2b7a96b93f97676e79c\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'102dfe19c1bbe2b7a96b93f97676e79c\'\r\n		<#if (status[\'102dfe19c1bbe2b7a96b93f97676e79c\'][\'disabled\'])?? && status[\'102dfe19c1bbe2b7a96b93f97676e79c\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'102dfe19c1bbe2b7a96b93f97676e79c\'][\'readonly\'])?? && status[\'102dfe19c1bbe2b7a96b93f97676e79c\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_data_dictionary.DATA_VALUE)!\'\'}\"\r\n		id=\'3ef7340a-815a-485d-a89c-a2c14a56a9ca\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'f9574122a2d617f157e422a04dc3f0d7\'][\'hidden\'])?? && status[\'f9574122a2d617f157e422a04dc3f0d7\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_f9574122a2d617f157e422a04dc3f0d7\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'f9574122a2d617f157e422a04dc3f0d7\'][\'hidden\'])?? && status[\'f9574122a2d617f157e422a04dc3f0d7\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'f9574122a2d617f157e422a04dc3f0d7\'][\'disabled\'])?? && status[\'f9574122a2d617f157e422a04dc3f0d7\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'f9574122a2d617f157e422a04dc3f0d7\'][\'readonly\'])?? && status[\'f9574122a2d617f157e422a04dc3f0d7\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'f9574122a2d617f157e422a04dc3f0d7\'\r\n			rows=\'3\'\r\n		id=\'e92c560e-9be0-4987-9793-a7619dcf53bb\'\r\n			title=\'\'\r\n		>${(p_data_dictionary.DICT_REMARK)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n			</div>\r\n				\r\n		</div>	\r\n						</div>\r\n					</form>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script>\r\n	//引入组件脚本\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n\r\n\r\n	for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n	scriptFile=null;\r\n	</script>\r\n	<script>\r\n\r\n\r\n\r\n\r\n	(function(){\r\n			$(\'#133db9dd-fcba-4c88-ad74-b18b803c1e55\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#133db9dd-fcba-4c88-ad74-b18b803c1e55\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n\r\n\r\n(function(){\r\n		var that=$(\'#ad999fa4-33f1-40b9-ad1a-4fc2828b3387\');\r\n			\r\n})();\r\n\r\n(function(){\r\n		var that=$(\'#3ef7340a-815a-485d-a89c-a2c14a56a9ca\');\r\n			\r\n})();\r\n\r\n(function(){\r\n		var that=$(\'#f2068ebf-3b49-4917-ac87-da85d470f9f5\');\r\n			\r\n})();\r\n\r\n	(function(){\r\n			$(\'#83519b5a-2576-4c70-8136-267ccab815c2\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#83519b5a-2576-4c70-8136-267ccab815c2\').on(\"change\",function(){\r\n				var a=$(\"#83519b5a-2576-4c70-8136-267ccab815c2\").val();\r\nif(a==\'1\'){\r\n$(\"#b8e6dc9f-4eb4-4c31-a315-0963c09466d1\").parent().hide();\r\n$(\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\").parent().hide();\r\n$(\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\").attr(\"disabled\",true);\r\n}else{\r\n$(\"#b8e6dc9f-4eb4-4c31-a315-0963c09466d1\").parent().show();\r\n$(\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\").parent().show();\r\n$(\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\").attr(\"disabled\",false);\r\n}\r\n$.get(basePath+\"api/executeAPI.do?APIId=getPID\",{ level: this.value},function(data){\r\nvar a=$(\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\")[0].options;\r\nvar b=a.length;\r\nfor(var i=0;i<b;i++){\r\n	a[0].remove();\r\n}\r\nvar c=data.data.length;\r\nfor(var i=0;i<c;i++){\r\n	var e=new Option(data.data[i].DATA_KEY,data.data[i].ID);\r\n	a.add(e);\r\n}\r\n});\r\n			});\r\n	})();\r\n		\r\n		\r\n					var a=$(\"#83519b5a-2576-4c70-8136-267ccab815c2\").val();\r\nif(a==\'1\'){\r\n$(\"#b8e6dc9f-4eb4-4c31-a315-0963c09466d1\").parent().hide();\r\n$(\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\").parent().hide();\r\n$(\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\").attr(\"disabled\",true);\r\n}\r\n			\r\n			\r\n	$(\"#d6db3231-d736-431f-b89f-fb66945e33df\").click(function(){\r\n			if(!Comm.validForm())\r\n				return;\r\n				var actId = $(this).attr(\"id\");\r\nvar pid=Comm.getUrlParam(\"pid\");\r\nif(pid==null){\r\npid=\"\";\r\n}\r\n$(\"#actId\").val(actId);\r\n	$.ajax({\r\n	   type: \"POST\",\r\n	   url: basePath + \"document/excuteOnly.do\",\r\n	   data:$(\"#groupForm\").serialize(),\r\n	   async : false,\r\n	   success: function(data){\r\n			if(data==1){\r\n				alert(\"保存成功\");\r\n				location.href= basePath + \"document/view.do?dynamicPageId=18&pid=\"+pid;\r\n			}\r\n			else{\r\n				alert(data);\r\n			}\r\n	   }\r\n	});\r\n	});\r\n	$(\"#54736121-b385-43a3-a6ad-187fd1ce9d19\").click(function(){\r\n				location.href = basePath + \"document/view.do?dynamicPageId=18\";\r\n	});\r\n			\r\n	</script>\r\n</body>\r\n</html>', '2', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', null);
INSERT INTO `p_fm_dynamicpage` VALUES ('18', null, null, '系统属性字典配置列表', null, '1003', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;f9662975-0b32-a12f-4a08-c24f5a4fd1ab&quot;,&quot;name&quot;:&quot;p_data_dictionary&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;1&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var pid=request.getParameter(\\&quot;pid\\&quot;);\\r\\nvar name=request.getParameter(\\&quot;name\\&quot;);\\r\\nvar level=request.getParameter(\\&quot;level\\&quot;);\\r\\nvar where=\\&quot; where 1=1 \\&quot;;\\r\\nif(pid!=null &amp;&amp; pid!=\\&quot;\\&quot;){\\r\\nwhere +=\\&quot; and a.PID=\'\\&quot;+pid+\\&quot;\' \\&quot;;\\r\\n}\\r\\nif(name!=null &amp;&amp; name!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.DATA_KEY like \'%\\&quot;+name+\\&quot;%\' \\&quot;;\\r\\n}\\r\\nif(level!=null &amp;&amp; level!=\\&quot;\\&quot;){\\r\\nwhere +=\\&quot; and a.LEVEL=\'\\&quot;+level+\\&quot;\' \\&quot;;\\r\\n}else{\\r\\nif((pid==null || pid==\\&quot;\\&quot;) &amp;&amp; (name==null || name==\\&quot;\\&quot;)){\\r\\nwhere +=\\&quot; and a.level=\'1\' \\&quot;;\\r\\n}\\r\\n}\\r\\n\\&quot;SELECT distinct a.ID, b.DATA_KEY AS PKEY,a.CODE,a.DICT_REMARK,a.DATA_KEY,a.DICT_STATUS,a.PID,a.LEVEL,a.DATA_ORDER, a.DATA_VALUE  FROM  p_data_dictionary a left join p_data_dictionary b on b.ID=a.PID  \\&quot;+where+\\&quot;  ORDER BY a.DATA_ORDER\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_data_dictionary&quot;,&quot;modelItemCodes&quot;:&quot;CODE,DICT_REMARK,DATA_KEY,DICT_STATUS,PID,ID,LEVEL,DATA_VALUE,DATA_ORDER&quot;}]', '0', null, 'updateAction([&quot;pid&quot;]);', '2017-09-09 15:26:07', '2017-12-18 20:14:47', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_data_dictionary_list?? && p_data_dictionary_list?size gt 0>\r\n             <#assign p_data_dictionary = p_data_dictionary_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head lang=\"en\">\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-table.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/select2/select2.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">  \r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n    <script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n		for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n		cssFile=null;\r\n	</script>\r\n</head>\r\n<body>\r\n	<!-- Main content -->\r\n  <section class=\"content\">	\r\n		<div class=\"opeBtnGrop\">\r\n		<button data-valid=\"flase\" id=\'083bc58d-6490-49de-af15-c1dbff097fd5\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'083bc58d-6490-49de-af15-c1dbff097fd5\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'083bc58d-6490-49de-af15-c1dbff097fd5\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'083bc58d-6490-49de-af15-c1dbff097fd5\'][\'hidden\'])?? && pageActStatus[\'083bc58d-6490-49de-af15-c1dbff097fd5\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-plus\'></i>\r\n			新增</button>\r\n		<button data-valid=\"flase\" id=\'dcd83cde-c798-4e89-aa75-439e71b2c3aa\' title=\"\"\r\n				class=\" btn   btn-success\"\r\n					<#if (pageActStatus[\'dcd83cde-c798-4e89-aa75-439e71b2c3aa\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'dcd83cde-c798-4e89-aa75-439e71b2c3aa\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'dcd83cde-c798-4e89-aa75-439e71b2c3aa\'][\'hidden\'])?? && pageActStatus[\'dcd83cde-c798-4e89-aa75-439e71b2c3aa\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-edit\'></i>\r\n			编辑</button>\r\n		<button data-valid=\"flase\" id=\'65635ee7-9495-4d50-937c-2343f5e1f5b9\' title=\"\"\r\n				class=\" btn   btn-info\"\r\n					<#if (pageActStatus[\'65635ee7-9495-4d50-937c-2343f5e1f5b9\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'65635ee7-9495-4d50-937c-2343f5e1f5b9\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'65635ee7-9495-4d50-937c-2343f5e1f5b9\'][\'hidden\'])?? && pageActStatus[\'65635ee7-9495-4d50-937c-2343f5e1f5b9\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-tasks\'></i>\r\n			启用</button>\r\n		<button data-valid=\"flase\" id=\'02940dbe-7c48-4fc8-97f1-b65cb7204326\' title=\"\"\r\n				class=\" btn   btn-warning\"\r\n					<#if (pageActStatus[\'02940dbe-7c48-4fc8-97f1-b65cb7204326\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'02940dbe-7c48-4fc8-97f1-b65cb7204326\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'02940dbe-7c48-4fc8-97f1-b65cb7204326\'][\'hidden\'])?? && pageActStatus[\'02940dbe-7c48-4fc8-97f1-b65cb7204326\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-off\'></i>\r\n			禁用</button>\r\n		<button data-valid=\"flase\" id=\'2d3d7992-7daa-4781-ac30-c233da644e18\' title=\"\"\r\n				class=\" btn   btn-danger\"\r\n					<#if (pageActStatus[\'2d3d7992-7daa-4781-ac30-c233da644e18\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'2d3d7992-7daa-4781-ac30-c233da644e18\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'2d3d7992-7daa-4781-ac30-c233da644e18\'][\'hidden\'])?? && pageActStatus[\'2d3d7992-7daa-4781-ac30-c233da644e18\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-trash\'></i>\r\n			删除</button>\r\n		<button data-valid=\"flase\" id=\'9cb9f74c-1fd3-40ae-9802-786c70b2725b\' title=\"\"\r\n				class=\" btn   btn-info\"\r\n					<#if (pageActStatus[\'9cb9f74c-1fd3-40ae-9802-786c70b2725b\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'9cb9f74c-1fd3-40ae-9802-786c70b2725b\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'9cb9f74c-1fd3-40ae-9802-786c70b2725b\'][\'hidden\'])?? && pageActStatus[\'9cb9f74c-1fd3-40ae-9802-786c70b2725b\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			返回上一级</button>\r\n		<button data-valid=\"flase\" id=\'e2a34cd1-54e4-4104-8872-07f7df7eacc3\' title=\"\"\r\n				class=\" btn   btn-info\"\r\n					<#if (pageActStatus[\'e2a34cd1-54e4-4104-8872-07f7df7eacc3\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'e2a34cd1-54e4-4104-8872-07f7df7eacc3\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'e2a34cd1-54e4-4104-8872-07f7df7eacc3\'][\'hidden\'])?? && pageActStatus[\'e2a34cd1-54e4-4104-8872-07f7df7eacc3\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-search\'></i>\r\n			搜索</button>\r\n		</div>\r\n		<div class=\"row\">\r\n		<div class=\"col-md-12\">\r\n			<div class=\"box box-info\">\r\n				<div class=\"box-body\">					\r\n						<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n							<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"${p_data_dictionary_list_paginator.getLimit()}\">\r\n							<input type=\"hidden\" id=\"totalCount\" name=\"totalCount\" value=\"${p_data_dictionary_list_paginator.getTotalCount()}\">\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"orderBy\" value=\"${ (vo.orderBy)!\"\"}\" id=\"orderBy\"/>\r\n							<input type=\"hidden\" name=\"allowOrderBy\" id=\"allowOrderBy\" value=\"${ (vo.allowOrderBy)!\"\"}\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n		<div class=\"search_main row\">\r\n			<input type=\"hidden\" value=\"1=1;2=2;3=3\" id=\"selectOption\">\r\n			<input type=\"hidden\" value=\"级层\" id=\"selectLabel\">\r\n			<input type=\"hidden\" value=\"level\" id=\"selectName\">\r\n			<input type=\"hidden\" value=\"名称\" id=\"textLabel\">\r\n			<input type=\"hidden\" value=\"name\" id=\"textName\">\r\n			<input type=\"hidden\" value=\"\" id=\"dateSelectLabel\">\r\n			<input type=\"hidden\" value=\"\" id=\"dateSelectName\">\r\n		</div>\r\n							<table id=\"bootstrapTable\" class=\"table table-hover\">\r\n								<thead>\r\n									<tr>\r\n										<th class=\"hidden\"></th>\r\n										<th data-width=\"\" data-field=\"\" data-checkbox=\"true\"></th>\r\n										<th data-width=\"50\">序号</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'4745599bc0b79f03f3e88b67dcec024d\'][\'hidden\'])?? && status[\'4745599bc0b79f03f3e88b67dcec024d\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_data_dictionary.PKEY\"\r\n		>\r\n			父级KEY值\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'53477f984f5893e8b7ffce3e495f85bb\'][\'hidden\'])?? && status[\'53477f984f5893e8b7ffce3e495f85bb\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_data_dictionary.DATA_KEY\"\r\n		>\r\n			KEY值\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'ee6276ed9076aa7921cc5a6a41fcd8c3\'][\'hidden\'])?? && status[\'ee6276ed9076aa7921cc5a6a41fcd8c3\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_data_dictionary.DATA_VALUE\"\r\n		>\r\n			VALUE值\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'a206c2cf59badfd8b15a5880f70ae485\'][\'hidden\'])?? && status[\'a206c2cf59badfd8b15a5880f70ae485\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_data_dictionary.PID\"\r\n		>\r\n			PID\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'69988767ca27a6854dedc3aaf4d530af\'][\'hidden\'])?? && status[\'69988767ca27a6854dedc3aaf4d530af\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_data_dictionary.DICT_REMARK\"\r\n		>\r\n			描述\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'a4b80c4c842973475007ed824320a043\'][\'hidden\'])?? && status[\'a4b80c4c842973475007ed824320a043\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_data_dictionary.DATA_ORDER\"\r\n		>\r\n			排序\r\n		\r\n		</th>\r\n									</tr>\r\n								</thead>\r\n								<tbody>\r\n									<#if p_data_dictionary_list?? && p_data_dictionary_list?size gte 1>	\r\n										<#list\r\n											p_data_dictionary_list as p_data_dictionary>\r\n										<tr>\r\n											<td class=\"hidden formData\">\r\n												<input type=\"hidden\" value=\"${p_data_dictionary.ID!\'\'}\">\r\n											</td>\r\n											<td></td>\r\n									<td>${(p_data_dictionary_list_paginator.getPage() * p_data_dictionary_list_paginator.getLimit() - p_data_dictionary_list_paginator.getLimit() + p_data_dictionary_index + 1)}\r\n									</td>\r\n		<td <#if (status[\'4745599bc0b79f03f3e88b67dcec024d\'][\'hidden\'])?? && status[\'4745599bc0b79f03f3e88b67dcec024d\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_data_dictionary.PKEY!\'\'}\r\n		</td>\r\n		<td <#if (status[\'53477f984f5893e8b7ffce3e495f85bb\'][\'hidden\'])?? && status[\'53477f984f5893e8b7ffce3e495f85bb\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_data_dictionary.DATA_KEY!\'\'}\r\n		</td>\r\n		<td <#if (status[\'ee6276ed9076aa7921cc5a6a41fcd8c3\'][\'hidden\'])?? && status[\'ee6276ed9076aa7921cc5a6a41fcd8c3\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_data_dictionary.DATA_VALUE!\'\'}\r\n		</td>\r\n		<td <#if (status[\'a206c2cf59badfd8b15a5880f70ae485\'][\'hidden\'])?? && status[\'a206c2cf59badfd8b15a5880f70ae485\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_data_dictionary.PID!\'\'}\r\n		</td>\r\n		<td <#if (status[\'69988767ca27a6854dedc3aaf4d530af\'][\'hidden\'])?? && status[\'69988767ca27a6854dedc3aaf4d530af\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_data_dictionary.DICT_REMARK!\'\'}\r\n		</td>\r\n		<td <#if (status[\'a4b80c4c842973475007ed824320a043\'][\'hidden\'])?? && status[\'a4b80c4c842973475007ed824320a043\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_data_dictionary.DATA_ORDER!\'\'}\r\n		</td>\r\n										</tr>\r\n										</#list>\r\n									</#if>\r\n								</tbody>\r\n							</table>\r\n						</form>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</section>\r\n	<script src=\"${basePath}template/AdminLTE/js/jquery.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/editTable.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/bootstrap-table.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/select2.full.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js\"></script>\r\n	<script src=\'${basePath}venson/js/common.js\'></script>\r\n	<script src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script>\r\n		for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n		scriptFile=null;\r\n		var basePath = \'${basePath}\';\r\n		var count=0;//默认选择行数为0\r\n	$(document).ready(function(){\r\n		$(document).keyup(function(event) {\r\n	    	if (event.keyCode == 13) {\r\n	    		$(\"#groupForm\").submit();\r\n	    	}\r\n	    });\r\n		 $(\"#bootstrapTable\").bootstrapTable({\r\n        	 pageSize:parseInt($(\"#pageSize\").val()),\r\n        	 pageNumber:parseInt($(\"#currentPage\").val()),\r\n        	 totalRows:parseInt($(\"#totalCount\").val()),\r\n        	 sidePagination:\"server\",\r\n        	 pagination:true,\r\n        	 onPageChange:function(number, size){\r\n        		$(\"#pageSize\").val(size);\r\n        		$(\"#currentPage\").val(number);\r\n        		$(\"#groupForm\").submit();\r\n        	 },\r\n        	 onClickRow:function(row,$element,field){\r\n        	  	  var $checkbox=$element.find(\":checkbox\").eq(0);\r\n        	  	  if($checkbox.get(0).checked){\r\n					  $checkbox.get(0).checked=false;\r\n					  $element.find(\"input[type=\'hidden\']\").removeAttr(\"name\",\"_selects\");\r\n        	  	  }else{\r\n					  $checkbox.get(0).checked=true;\r\n					  $element.find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n        	  	  }\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onCheck: function(row,$element){  \r\n				  $element.closest(\"tr\").find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onUncheck:function(row,$element){ \r\n        		 $element.closest(\"tr\").find(\"input[type=\'hidden\']\").removeAttr(\"name\");\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n        	 }, \r\n        	 onCheckAll: function (rows) {\r\n        		 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").attr(\"name\",\"_selects\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n             onUncheckAll: function (rows) {          	 \r\n            	 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").removeAttr(\"name\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n			 onSort:function(name, order){\r\n				 $(\"#allowOrderBy\").val(\"1\");\r\n				 var orderBy =$(\"#orderBy\").val();\r\n				 console.log(orderBy,name)\r\n				 if(orderBy.indexOf(name)!=-1&&orderBy.indexOf(\"desc\")!=-1){\r\n					 $(\"#orderBy\").val(name+\" asc\");\r\n				 }else{\r\n					 $(\"#orderBy\").val(name+\" desc\");\r\n				 }\r\n        		$(\"#groupForm\").submit();\r\n			 }\r\n         });							\r\n	\r\n	new addSearch();\r\n		\r\n	$(\"#083bc58d-6490-49de-af15-c1dbff097fd5\").click(function(){\r\n				if(count==1){\r\nvar pid = $(\"input[name=\'_selects\']\").val();\r\nlocation.href = basePath + \"document/view.do?dynamicPageId=17&pid=\"+pid;//如果选中某行点新增，就直接添加这行下属项\r\n}else{\r\nvar pid=Comm.getUrlParam(\"pid\");\r\nif(pid==null){\r\npid=\"\";\r\n}\r\nlocation.href = basePath + \"document/view.do?dynamicPageId=17&pid=\"+pid;\r\n}\r\n	});\r\n	$(\"#dcd83cde-c798-4e89-aa75-439e71b2c3aa\").click(function(){\r\n				if(count!=1){\r\n	alert(\"请选择一项进行操作\");\r\n	return false;\r\n}\r\nvar id = $(\"input[name=\'_selects\']\").val();\r\nlocation.href = basePath + \"document/view.do?dynamicPageId=17&id=\" + id;\r\n	});\r\n	$(\"#65635ee7-9495-4d50-937c-2343f5e1f5b9\").click(function(){\r\n				if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nvar pid=Comm.getUrlParam(\"pid\");\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize(),\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"启用成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=18&pid=\"+pid;\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n	$(\"#02940dbe-7c48-4fc8-97f1-b65cb7204326\").click(function(){\r\n				if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nvar pid=Comm.getUrlParam(\"pid\");\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize(),\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"禁用成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=18&pid=\"+pid;\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n	$(\"#2d3d7992-7daa-4781-ac30-c233da644e18\").click(function(){\r\n				if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nvar pid=Comm.getUrlParam(\"pid\");\r\nif(pid==null){\r\npid=\"\";\r\n}\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize(),\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"删除成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=18&pid=\"+pid;\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n	$(\"#9cb9f74c-1fd3-40ae-9802-786c70b2725b\").click(function(){\r\n				var pid=Comm.getUrlParam(\"pid\");\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize()+\"&pid=\"+pid,\r\n   async : false,\r\n   success: function(data){\r\n		if(data.length==36 ){\r\n			location.href=basePath + \"document/view.do?dynamicPageId=18&pid=\"+data;\r\n		}else if(data==\'null\'){\r\n			location.href=basePath + \"document/view.do?dynamicPageId=18\";\r\n		}else{\r\n			Comm.alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n	$(\"#e2a34cd1-54e4-4104-8872-07f7df7eacc3\").click(function(){\r\n				$(\"#groupForm\").submit()\r\n	});\r\n        \r\n				updateAction([\"pid\"]);\r\n		});\r\n	</script>\r\n</body>\r\n</html>', '1', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', null);
INSERT INTO `p_fm_dynamicpage` VALUES ('19', '23', null, 'API权限编辑控制表单', null, '1002', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;471935a4-a838-a55b-bef8-fdea52600940&quot;,&quot;name&quot;:&quot;p_fm_api_privilege&quot;,&quot;isSingle&quot;:&quot;0&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id=request.getParameter(\\&quot;id\\&quot;);\\r\\n\\&quot;SELECT  DESCRIBES,ID,RULES,REMARK,TYPE,NAME,MESSAGE  FROM  p_fm_api_privilege where ID=\'\\&quot;+id+\\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_api_privilege&quot;,&quot;modelItemCodes&quot;:&quot;MESSAGE,DESCRIBES,ID,RULES,REMARK,TYPE,NAME&quot;}]', '0', null, null, '2017-09-19 19:44:38', '2017-12-18 15:32:36', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_api_privilege_list?? && p_fm_api_privilege_list?size gt 0>\r\n             <#assign p_fm_api_privilege = p_fm_api_privilege_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/font-awesome.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrapValidator.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n	<script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n	for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n	cssFile=null;\r\n	</script>\r\n	\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n        \r\n	<![endif]-->\r\n	<script>\r\n		\r\n    </script>\r\n</head>\r\n<body>\r\n    <div class=\"content\"> \r\n		<div class=\"row opeBtnGrop\" id=\"buttons\">\r\n			<div class=\"col-md-12\">\r\n		<button data-valid=\"flase\" id=\'bf766420-6706-4691-bf4c-03796ebead73\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'bf766420-6706-4691-bf4c-03796ebead73\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'bf766420-6706-4691-bf4c-03796ebead73\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'bf766420-6706-4691-bf4c-03796ebead73\'][\'hidden\'])?? && pageActStatus[\'bf766420-6706-4691-bf4c-03796ebead73\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-save\'></i>\r\n			保存</button>\r\n		<button data-valid=\"flase\" id=\'5d14b424-37c8-4e09-b14c-440b99104712\' title=\"\"\r\n				class=\" btn   btn-warning\"\r\n					<#if (pageActStatus[\'5d14b424-37c8-4e09-b14c-440b99104712\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'5d14b424-37c8-4e09-b14c-440b99104712\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'5d14b424-37c8-4e09-b14c-440b99104712\'][\'hidden\'])?? && pageActStatus[\'5d14b424-37c8-4e09-b14c-440b99104712\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			返回</button>\r\n			</div>\r\n		</div>\r\n		<div class=\"row\" id=\"newTitle\"></div>\r\n		<div class=\"row\">\r\n			<div class=\"col-md-12\">\r\n				<div class=\"box box-info\">\r\n					<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}workflow/wf/excute.do\" role=\"customForm\" method=\"post\">\r\n						<div class=\"box-body\">\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-9 col-md-9 col-md-9\">\r\n	<div  data-required=\"0\" data-placeholder=\"\" style=\"display:none;\">\r\n		<input class=\"dataItemCode\" type=\'hidden\'\r\n			name=\'ce8ad88d98557614a3929baaaa7ecf2a\'\r\n				value=\"${(p_fm_api_privilege.ID)!\'\'}\"\r\n		id=\'8c1d6fcb-6d26-4fc0-93b6-6335db015d98\'\r\n		/>\r\n	</div>\r\n	<div class=\"customGroup\"  data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'c5d2f388a8a682b440436f6f247a41f0\'][\'hidden\'])?? && status[\'c5d2f388a8a682b440436f6f247a41f0\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_c5d2f388a8a682b440436f6f247a41f0\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'c5d2f388a8a682b440436f6f247a41f0\'\r\n		<#if (status[\'c5d2f388a8a682b440436f6f247a41f0\'][\'disabled\'])?? && status[\'c5d2f388a8a682b440436f6f247a41f0\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'c5d2f388a8a682b440436f6f247a41f0\'][\'readonly\'])?? && status[\'c5d2f388a8a682b440436f6f247a41f0\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_fm_api_privilege.NAME)!\'\'}\"\r\n		id=\'f2b2807c-881e-4d92-b999-1e70e3a846e2\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div class=\"customGroup\" data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'882c4f948d0b6f97bba05653ab4ef094\'][\'hidden\'])?? && status[\'882c4f948d0b6f97bba05653ab4ef094\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_882c4f948d0b6f97bba05653ab4ef094\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'882c4f948d0b6f97bba05653ab4ef094\'][\'disabled\'])?? && status[\'882c4f948d0b6f97bba05653ab4ef094\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'882c4f948d0b6f97bba05653ab4ef094\'][\'readonly\'])?? && status[\'882c4f948d0b6f97bba05653ab4ef094\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'882c4f948d0b6f97bba05653ab4ef094\'\r\n\r\n				value=\"${(p_fm_api_privilege.TYPE)!\'\'}\"\r\n\r\n		id=\'9d479de4-e5a6-4924-98fc-69371e880128\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'882c4f948d0b6f97bba05653ab4ef094\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'05ea582e6e23c0d83aee97fff763d878\'][\'hidden\'])?? && status[\'05ea582e6e23c0d83aee97fff763d878\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_05ea582e6e23c0d83aee97fff763d878\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'05ea582e6e23c0d83aee97fff763d878\'][\'hidden\'])?? && status[\'05ea582e6e23c0d83aee97fff763d878\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'05ea582e6e23c0d83aee97fff763d878\'][\'disabled\'])?? && status[\'05ea582e6e23c0d83aee97fff763d878\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'05ea582e6e23c0d83aee97fff763d878\'][\'readonly\'])?? && status[\'05ea582e6e23c0d83aee97fff763d878\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'05ea582e6e23c0d83aee97fff763d878\'\r\n			rows=\'4\'\r\n		id=\'6bb39ce0-7de2-4666-a68c-e94a4fe43a1f\'\r\n			title=\'\'\r\n		>${(p_fm_api_privilege.RULES)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'65b4ab87288acd632b87cf5402a753fd\'][\'hidden\'])?? && status[\'65b4ab87288acd632b87cf5402a753fd\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_65b4ab87288acd632b87cf5402a753fd\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'65b4ab87288acd632b87cf5402a753fd\'][\'hidden\'])?? && status[\'65b4ab87288acd632b87cf5402a753fd\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'65b4ab87288acd632b87cf5402a753fd\'][\'disabled\'])?? && status[\'65b4ab87288acd632b87cf5402a753fd\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'65b4ab87288acd632b87cf5402a753fd\'][\'readonly\'])?? && status[\'65b4ab87288acd632b87cf5402a753fd\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'65b4ab87288acd632b87cf5402a753fd\'\r\n			rows=\'2\'\r\n		id=\'37e55e1e-5f61-487e-b232-929df1075676\'\r\n			title=\'\'\r\n		>${(p_fm_api_privilege.MESSAGE)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'6e354f2a3cdba2d79af87123cb96efad\'][\'hidden\'])?? && status[\'6e354f2a3cdba2d79af87123cb96efad\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_6e354f2a3cdba2d79af87123cb96efad\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'6e354f2a3cdba2d79af87123cb96efad\'][\'hidden\'])?? && status[\'6e354f2a3cdba2d79af87123cb96efad\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'6e354f2a3cdba2d79af87123cb96efad\'][\'disabled\'])?? && status[\'6e354f2a3cdba2d79af87123cb96efad\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'6e354f2a3cdba2d79af87123cb96efad\'][\'readonly\'])?? && status[\'6e354f2a3cdba2d79af87123cb96efad\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'6e354f2a3cdba2d79af87123cb96efad\'\r\n			rows=\'3\'\r\n		id=\'7759ea99-88f2-4dd2-a5fd-8784e0d0c3ce\'\r\n			title=\'\'\r\n		>${(p_fm_api_privilege.DESCRIBES)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'3e56b9fc74b6ad988e248bf0b53a7f92\'][\'hidden\'])?? && status[\'3e56b9fc74b6ad988e248bf0b53a7f92\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_3e56b9fc74b6ad988e248bf0b53a7f92\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'3e56b9fc74b6ad988e248bf0b53a7f92\'][\'hidden\'])?? && status[\'3e56b9fc74b6ad988e248bf0b53a7f92\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'3e56b9fc74b6ad988e248bf0b53a7f92\'][\'disabled\'])?? && status[\'3e56b9fc74b6ad988e248bf0b53a7f92\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'3e56b9fc74b6ad988e248bf0b53a7f92\'][\'readonly\'])?? && status[\'3e56b9fc74b6ad988e248bf0b53a7f92\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'3e56b9fc74b6ad988e248bf0b53a7f92\'\r\n			rows=\'3\'\r\n		id=\'ad36fbe4-de5e-4ed8-86be-2422e3480b1f\'\r\n			title=\'\'\r\n		>${(p_fm_api_privilege.REMARK)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n				\r\n		</div>	\r\n						</div>\r\n					</form>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script>\r\n	//引入组件脚本\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n\r\n\r\n	for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n	scriptFile=null;\r\n	</script>\r\n	<script>\r\n\r\n	(function(){\r\n			$(\'#9d479de4-e5a6-4924-98fc-69371e880128\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#9d479de4-e5a6-4924-98fc-69371e880128\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n(function(){\r\n		var that=$(\'#f2b2807c-881e-4d92-b999-1e70e3a846e2\');\r\n			\r\n})();\r\n\r\n\r\n\r\n		\r\n		\r\n			\r\n			\r\n	$(\"#bf766420-6706-4691-bf4c-03796ebead73\").click(function(){\r\n				var actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);\r\n	$.ajax({\r\n	   type: \"POST\",\r\n	   url: basePath + \"document/excuteOnly.do\",\r\n	   data:$(\"#groupForm\").serialize(),\r\n	   async : false,\r\n	   success: function(data){\r\n			if(data==1){\r\n				alert(\"保存成功\");\r\n				location.href= basePath + \"document/view.do?dynamicPageId=20&edit=true\";\r\n			}\r\n			else{\r\n				alert(data);\r\n			}\r\n	   }\r\n	});\r\n	});\r\n	$(\"#5d14b424-37c8-4e09-b14c-440b99104712\").click(function(){\r\n				location.href= basePath + \"document/view.do?dynamicPageId=20&edit=true\";\r\n	});\r\n			\r\n	</script>\r\n</body>\r\n</html>', '2', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', null);
INSERT INTO `p_fm_dynamicpage` VALUES ('2', '23', null, 'API管理表单', null, '1002', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;4a20d2eb-af01-40fd-f75b-cca55b6b517a&quot;,&quot;name&quot;:&quot;p_fm_api&quot;,&quot;isSingle&quot;:&quot;0&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id =request.getParameter(\\&quot;id\\&quot;);\\r\\nvar reader=request.getParameter(\\&quot;reader\\&quot;);\\r\\nroot.put(\\&quot;suggestion\\&quot;,reader);\\r\\n\\&quot;SELECT  API_METHOD,API_SQL,API_IS_LOGIN,API_TYPE,API_ID,API_OUTPUT,API_STATE,API_TABLE,API_DESC,API_NAME,API_REMARK,API_CREATETIME,API_CREATOR,API_ISCACHE  FROM  p_fm_api where API_ID=\'\\&quot;+id+\\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_api&quot;,&quot;modelItemCodes&quot;:&quot;API_METHOD,API_SQL,API_IS_LOGIN,API_TYPE,API_REMARK,API_CREATETIME,API_ID,API_OUTPUT,API_CREATOR,API_STATE,API_TABLE,API_ISCACHE,API_DESC,API_NAME&quot;},{&quot;id&quot;:&quot;e31a27e5-0163-9329-9352-cd4f91c07ef5&quot;,&quot;name&quot;:&quot;p_fm_api_describe&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;50&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var apiid=request.getParameter(\\&quot;apiid\\&quot;);\\r\\n\\&quot;SELECT  DEFAULTVAL,PARAM_PLACE,NAME,ID,APIID,TYPE,case ISNECESSARY when \'0\' then \'否\' else \'是\' end ISNECESSARY,DESCRIBES  FROM  p_fm_api_describe where  APIID=\'\\&quot;+apiid+\\&quot;\' order by ISNECESSARY desc\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_api_describe&quot;,&quot;modelItemCodes&quot;:&quot;DESCRIBES,PARAM_PLACE,ID,APIID,ISNECESSARY,DEFAULTVAL,TYPE,NAME&quot;}]', '0', null, 'var api_name=$(&quot;#c0a27a2a-4430-4795-814b-7585e4a4308d&quot;).val();\r\n$(&quot;#7547d92a-a8a3-4780-8930-ddae6e9a0e46&quot;).text(&quot;接口地址：baseUrl+api/execute/&quot;+api_name);\r\nvar reader=$(&quot;#suggestion&quot;).val();\r\nif(reader==1){\r\n$(&quot;#9e45b738-f5c9-4ef6-862c-bb74da985c2b&quot;).parent().parent().hide();\r\n$(&quot;#9e45b738-f5c9-4ef6-862c-bb74da985c2b&quot;).parent().parent().next().hide();\r\n$(&quot;#9e45b738-f5c9-4ef6-862c-bb74da985c2b&quot;).parent().parent().next().next().next().hide();\r\n}\r\nvar id=$(&quot;#818bb1c3-bf87-4861-a9b7-88b605f1a2e3&quot;).val();\r\nif(id==null || id==&quot;&quot; || reader==1){\r\n$(&quot;#tb_8555626c-6361-4380-ab2a-4084596937ed&quot;).hide();\r\n}\r\n$(&quot;textarea&quot;).each(function(){\r\n$(this).css({\'height\':\'auto\',\'overflow-y\':\'hidden\'}).height(this.scrollHeight)\r\n})', '2017-03-26 17:05:55', '2017-12-18 17:57:00', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_api_list?? && p_fm_api_list?size gt 0>\r\n             <#assign p_fm_api = p_fm_api_list[0]!\'\'>\r\n        </#if>\r\n	<#if p_fm_api_describe_list?? && p_fm_api_describe_list?size gt 0>\r\n             <#assign p_fm_api_describe = p_fm_api_describe_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/font-awesome.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrapValidator.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n	<script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n			cssFile[\"${basePath}template/easyui/css/easyui.css\"]=null;\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n	for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n	cssFile=null;\r\n	</script>\r\n	\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n        \r\n	<![endif]-->\r\n	<script>\r\n		\r\n    </script>\r\n</head>\r\n<body>\r\n    <div class=\"content\"> \r\n		<div class=\"row opeBtnGrop\" id=\"buttons\">\r\n			<div class=\"col-md-12\">\r\n		<button data-valid=\"flase\" id=\'5d41e4d1-7bba-487b-9f98-b9e19172c2a6\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'5d41e4d1-7bba-487b-9f98-b9e19172c2a6\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'5d41e4d1-7bba-487b-9f98-b9e19172c2a6\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'5d41e4d1-7bba-487b-9f98-b9e19172c2a6\'][\'hidden\'])?? && pageActStatus[\'5d41e4d1-7bba-487b-9f98-b9e19172c2a6\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-save\'></i>\r\n			保存</button>\r\n		<button data-valid=\"flase\" id=\'0e68706c-4700-4767-ad34-41a17b9ee052\' title=\"\"\r\n				class=\" btn   btn-warning\"\r\n					<#if (pageActStatus[\'0e68706c-4700-4767-ad34-41a17b9ee052\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'0e68706c-4700-4767-ad34-41a17b9ee052\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'1\'\r\n					<#if (pageActStatus[\'0e68706c-4700-4767-ad34-41a17b9ee052\'][\'hidden\'])?? && pageActStatus[\'0e68706c-4700-4767-ad34-41a17b9ee052\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			返回</button>\r\n			</div>\r\n		</div>\r\n		<div class=\"row\" id=\"newTitle\"></div>\r\n		<div class=\"row\">\r\n			<div class=\"col-md-12\">\r\n				<div class=\"box box-info\">\r\n					<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}workflow/wf/excute.do\" role=\"customForm\" method=\"post\">\r\n						<div class=\"box-body\">\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div  data-required=\"0\" data-placeholder=\"\" style=\"display:none;\">\r\n		<input class=\"dataItemCode\" type=\'hidden\'\r\n			name=\'379a5cbf0dea0c7cd3eb6b6d9f2ae9d7\'\r\n				value=\"${(p_fm_api.API_CREATOR)!\'\'}\"\r\n		id=\'206538cd-c22b-43db-ab43-3eb0dd1e1da1\'\r\n		/>\r\n	</div>\r\n	<div  data-required=\"0\" data-placeholder=\"\" style=\"display:none;\">\r\n		<input class=\"dataItemCode\" type=\'hidden\'\r\n			name=\'apiid\'\r\n				value=\"${(p_fm_api.API_ID)!\'\'}\"\r\n		id=\'818bb1c3-bf87-4861-a9b7-88b605f1a2e3\'\r\n		/>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-6 col-md-6 col-md-6\">\r\n	<label  class=\"control-label <#if (status[\'f864c9d106a4235def3c3ce2edbb55bf\'][\'hidden\'])?? && status[\'f864c9d106a4235def3c3ce2edbb55bf\'][\'hidden\'] == \'true\'>hidden</#if> 	\r\n			rightLabel\r\n	 \"\r\n			style=\'\'\r\n\r\n		id=\"7547d92a-a8a3-4780-8930-ddae6e9a0e46\"\r\n\r\n		name=\'f864c9d106a4235def3c3ce2edbb55bf\'\r\n		\r\n		lang=\'\'\r\n		\r\n	>\r\n\r\n		接口地址\r\n	</label>\r\n			</div>\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div class=\"customGroup\"  data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'723d082b1c163b525b0cdfe1b5fab8a6\'][\'hidden\'])?? && status[\'723d082b1c163b525b0cdfe1b5fab8a6\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_723d082b1c163b525b0cdfe1b5fab8a6\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'723d082b1c163b525b0cdfe1b5fab8a6\'\r\n		<#if (status[\'723d082b1c163b525b0cdfe1b5fab8a6\'][\'disabled\'])?? && status[\'723d082b1c163b525b0cdfe1b5fab8a6\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'723d082b1c163b525b0cdfe1b5fab8a6\'][\'readonly\'])?? && status[\'723d082b1c163b525b0cdfe1b5fab8a6\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_fm_api.API_NAME)!\'\'}\"\r\n		id=\'c0a27a2a-4430-4795-814b-7585e4a4308d\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div class=\"customGroup\"  data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'ceed34cb2cbad40f6035ca4f93284db6\'][\'hidden\'])?? && status[\'ceed34cb2cbad40f6035ca4f93284db6\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_ceed34cb2cbad40f6035ca4f93284db6\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'ceed34cb2cbad40f6035ca4f93284db6\'\r\n		<#if (status[\'ceed34cb2cbad40f6035ca4f93284db6\'][\'disabled\'])?? && status[\'ceed34cb2cbad40f6035ca4f93284db6\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'ceed34cb2cbad40f6035ca4f93284db6\'][\'readonly\'])?? && status[\'ceed34cb2cbad40f6035ca4f93284db6\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_fm_api.API_TABLE)!\'\'}\"\r\n		id=\'26ffc23b-cb46-4656-aac1-6b5f85e08dd1\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div class=\"customGroup\" data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'3bc7eeb21ba41cc1baf7c4560da8b3ca\'][\'hidden\'])?? && status[\'3bc7eeb21ba41cc1baf7c4560da8b3ca\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_3bc7eeb21ba41cc1baf7c4560da8b3ca\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'3bc7eeb21ba41cc1baf7c4560da8b3ca\'][\'disabled\'])?? && status[\'3bc7eeb21ba41cc1baf7c4560da8b3ca\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'3bc7eeb21ba41cc1baf7c4560da8b3ca\'][\'readonly\'])?? && status[\'3bc7eeb21ba41cc1baf7c4560da8b3ca\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'3bc7eeb21ba41cc1baf7c4560da8b3ca\'\r\n\r\n				value=\"${(p_fm_api.API_STATE)!\'\'}\"\r\n\r\n		id=\'4de3dd38-a1e2-42ce-a7ec-6f657216e226\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'3bc7eeb21ba41cc1baf7c4560da8b3ca\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'e410be02030e3a635ff6ba1f0d9383f2\'][\'hidden\'])?? && status[\'e410be02030e3a635ff6ba1f0d9383f2\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_e410be02030e3a635ff6ba1f0d9383f2\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'e410be02030e3a635ff6ba1f0d9383f2\'][\'disabled\'])?? && status[\'e410be02030e3a635ff6ba1f0d9383f2\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'e410be02030e3a635ff6ba1f0d9383f2\'][\'readonly\'])?? && status[\'e410be02030e3a635ff6ba1f0d9383f2\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'e410be02030e3a635ff6ba1f0d9383f2\'\r\n\r\n				value=\"${(p_fm_api.API_TYPE)!\'\'}\"\r\n\r\n		id=\'500377e9-7464-4b0d-aff2-0fd057c4f637\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'e410be02030e3a635ff6ba1f0d9383f2\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div class=\"customGroup\" data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'dbe7659d0468e35f17bf2aec91681b5c\'][\'hidden\'])?? && status[\'dbe7659d0468e35f17bf2aec91681b5c\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_dbe7659d0468e35f17bf2aec91681b5c\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'dbe7659d0468e35f17bf2aec91681b5c\'][\'disabled\'])?? && status[\'dbe7659d0468e35f17bf2aec91681b5c\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'dbe7659d0468e35f17bf2aec91681b5c\'][\'readonly\'])?? && status[\'dbe7659d0468e35f17bf2aec91681b5c\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'dbe7659d0468e35f17bf2aec91681b5c\'\r\n\r\n				value=\"${(p_fm_api.API_METHOD)!\'\'}\"\r\n\r\n		id=\'9934c176-a78a-45c9-9fce-b5bc01f1ba7a\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'dbe7659d0468e35f17bf2aec91681b5c\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div class=\"customGroup\" data-required=\"1\" data-placeholder=\"\" style=\"<#if (status[\'d5098699ff59528ffbb64e0c974d0b22\'][\'hidden\'])?? && status[\'d5098699ff59528ffbb64e0c974d0b22\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_d5098699ff59528ffbb64e0c974d0b22\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'d5098699ff59528ffbb64e0c974d0b22\'][\'disabled\'])?? && status[\'d5098699ff59528ffbb64e0c974d0b22\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'d5098699ff59528ffbb64e0c974d0b22\'][\'readonly\'])?? && status[\'d5098699ff59528ffbb64e0c974d0b22\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'d5098699ff59528ffbb64e0c974d0b22\'\r\n\r\n				value=\"${(p_fm_api.API_IS_LOGIN)!\'\'}\"\r\n\r\n		id=\'22490515-7014-4954-a947-b3a79ccf7d43\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'d5098699ff59528ffbb64e0c974d0b22\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'1e3e7efca290c9bb4c2e2f490c965d34\'][\'hidden\'])?? && status[\'1e3e7efca290c9bb4c2e2f490c965d34\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_1e3e7efca290c9bb4c2e2f490c965d34\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'1e3e7efca290c9bb4c2e2f490c965d34\'][\'disabled\'])?? && status[\'1e3e7efca290c9bb4c2e2f490c965d34\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'1e3e7efca290c9bb4c2e2f490c965d34\'][\'readonly\'])?? && status[\'1e3e7efca290c9bb4c2e2f490c965d34\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'1e3e7efca290c9bb4c2e2f490c965d34\'\r\n\r\n				value=\"${(p_fm_api.API_ISCACHE)!\'\'}\"\r\n\r\n		id=\'587e1abd-af5a-44bc-8fc8-392e4b9ce9ee\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'1e3e7efca290c9bb4c2e2f490c965d34\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-3 col-md-3 col-md-3\">\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'7d30522a458c1582db43c7763f055afc\'][\'hidden\'])?? && status[\'7d30522a458c1582db43c7763f055afc\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_7d30522a458c1582db43c7763f055afc\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'7d30522a458c1582db43c7763f055afc\'][\'hidden\'])?? && status[\'7d30522a458c1582db43c7763f055afc\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'7d30522a458c1582db43c7763f055afc\'][\'disabled\'])?? && status[\'7d30522a458c1582db43c7763f055afc\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'7d30522a458c1582db43c7763f055afc\'][\'readonly\'])?? && status[\'7d30522a458c1582db43c7763f055afc\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'7d30522a458c1582db43c7763f055afc\'\r\n			rows=\'3\'\r\n		id=\'867988b0-edc0-4f71-89d1-965b0c4c84df\'\r\n			title=\'\'\r\n		>${(p_fm_api.API_SQL)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'5fc043ba56d6eda3c771775e7ac69f37\'][\'hidden\'])?? && status[\'5fc043ba56d6eda3c771775e7ac69f37\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_5fc043ba56d6eda3c771775e7ac69f37\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'5fc043ba56d6eda3c771775e7ac69f37\'][\'hidden\'])?? && status[\'5fc043ba56d6eda3c771775e7ac69f37\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'5fc043ba56d6eda3c771775e7ac69f37\'][\'disabled\'])?? && status[\'5fc043ba56d6eda3c771775e7ac69f37\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'5fc043ba56d6eda3c771775e7ac69f37\'][\'readonly\'])?? && status[\'5fc043ba56d6eda3c771775e7ac69f37\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'5fc043ba56d6eda3c771775e7ac69f37\'\r\n			rows=\'3\'\r\n		id=\'da639ea4-2d6f-48c8-b213-71d5e28a1d18\'\r\n			title=\'\'\r\n		>${(p_fm_api.API_DESC)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n	<div class=\"customGroup\">\r\n		<label class=\"control-label\">${others[\'title_78a2d4ef1a762365f3224092111d3b80\']!\'\'}</label>\r\n		<div id=\"easyui_dg_8555626c-6361-4380-ab2a-4084596937ed\" style=\"border:solid 1px #ccc\" class=\"<#if (status[\'78a2d4ef1a762365f3224092111d3b80\'][\'hidden\'])?? && status[\'78a2d4ef1a762365f3224092111d3b80\'][\'hidden\'] == \'true\'>hidden</#if>\r\n	<#if (status[\'78a2d4ef1a762365f3224092111d3b80\'][\'disabled\'])?? && status[\'78a2d4ef1a762365f3224092111d3b80\'][\'disabled\'] == \'true\'> disabled</#if>\">\r\n			<div id=\"tb_8555626c-6361-4380-ab2a-4084596937ed\" style=\"height:auto\">\r\n					<a href=\"javascript:void(0)\" class=\"btn-xs btn-default easyui-add\"><i class=\"fa fa-fw fa-plus\"></i>增加</a>\r\n					<a href=\"javascript:void(0)\" class=\"btn-xs btn-default easyui-edit\"><i class=\"fa fa-fw fa-edit\"></i>更改</a>\r\n					<a href=\"javascript:void(0)\" class=\"btn-xs btn-default easyui-remove\"><i class=\"fa fa-fw fa-remove\"></i>移除</a>\r\n			</div>\r\n			<table id=\"dg_8555626c-6361-4380-ab2a-4084596937ed\" style=\"width:100%\"></table>\r\n		</div>\r\n	</div>\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'e41c4ba928af1bcce05e71d66424bd97\'][\'hidden\'])?? && status[\'e41c4ba928af1bcce05e71d66424bd97\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_e41c4ba928af1bcce05e71d66424bd97\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'e41c4ba928af1bcce05e71d66424bd97\'][\'hidden\'])?? && status[\'e41c4ba928af1bcce05e71d66424bd97\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'e41c4ba928af1bcce05e71d66424bd97\'][\'disabled\'])?? && status[\'e41c4ba928af1bcce05e71d66424bd97\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'e41c4ba928af1bcce05e71d66424bd97\'][\'readonly\'])?? && status[\'e41c4ba928af1bcce05e71d66424bd97\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'e41c4ba928af1bcce05e71d66424bd97\'\r\n			rows=\'10\'\r\n		id=\'a72417ba-8efe-4e1c-9339-833e357bf94a\'\r\n			title=\'\'\r\n		>${(p_fm_api.API_OUTPUT)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n			</div>\r\n				\r\n		</div>	\r\n						</div>\r\n					</form>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script>\r\n	//引入组件脚本\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n			scriptFile[\"${basePath}template/easyui/js/jquery.easyui.min.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n\r\n\r\n	for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n	scriptFile=null;\r\n	</script>\r\n	<script>\r\n\r\n	(function(){\r\n			$(\'#587e1abd-af5a-44bc-8fc8-392e4b9ce9ee\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#587e1abd-af5a-44bc-8fc8-392e4b9ce9ee\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n\r\n\r\n	(function(){\r\n			$(\'#9934c176-a78a-45c9-9fce-b5bc01f1ba7a\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#9934c176-a78a-45c9-9fce-b5bc01f1ba7a\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n\r\n\r\n\r\n	(function(){\r\n			$(\'#22490515-7014-4954-a947-b3a79ccf7d43\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#22490515-7014-4954-a947-b3a79ccf7d43\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n	(function(){\r\n			$(\'#4de3dd38-a1e2-42ce-a7ec-6f657216e226\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#4de3dd38-a1e2-42ce-a7ec-6f657216e226\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n(function(){\r\n		var that=$(\'#c0a27a2a-4430-4795-814b-7585e4a4308d\');\r\n			\r\n})();\r\n\r\n(function(){\r\n		var that=$(\'#26ffc23b-cb46-4656-aac1-6b5f85e08dd1\');\r\n			\r\n})();\r\n\r\n	(function(){\r\n		var basePath=\"${basePath}\";\r\n		$(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid({\r\n									  pageSize: 10,        //设置默认分页大小\r\n									  pagination: (\"0\"==\"1\"?true:false),//分页控件 \r\n	        						  rownumbers: true,//行号\r\n	        						  border: false, \r\n	        						  collapsible: false,\r\n	        						  checkOnSelect:false,\r\n	        						  selectOnCheck:false,\r\n	        						  fitColumns:true,\r\n	   								  loadFilter:function(data){\r\n											var d={};\r\n							  				if(data.status==0){\r\n												d.rows=data.data;\r\n												d.total=data.total;  								  				\r\n							  				}else{\r\n							  					d.rows=[];\r\n							  					d.total=0;\r\n							  				}\r\n							  				return d;\r\n									  },     						  \r\n									  url:basePath+\"document/refreshDataGrid.do?componentId=8555626c-6361-4380-ab2a-4084596937ed\",\r\n									  queryParams:{\"apiid\":\r\n									  	 \"${(p_fm_api.API_ID)!\'\'}\"\r\n									  ,pageSize:10},\r\n									  columns:[[\r\n									  	{field:\'ck\',checkbox:true}\r\n											,{\r\n											field:\"NAME\",\r\n											title:\"参数名\",\r\n											width:\"18%\"\r\n											}\r\n											,{\r\n											field:\"TYPE\",\r\n											title:\"类型\",\r\n											width:\"12%\"\r\n											}\r\n											,{\r\n											field:\"PARAM_PLACE\",\r\n											title:\"参数位置\",\r\n											width:\"12%\"\r\n											}\r\n											,{\r\n											field:\"ISNECESSARY\",\r\n											title:\"必填\",\r\n											width:\"8%\"\r\n											}\r\n											,{\r\n											field:\"DEFAULTVAL\",\r\n											title:\"默认值\",\r\n											width:\"10%\"\r\n											}\r\n											,{\r\n											field:\"DESCRIBES\",\r\n											title:\"描述\",\r\n											width:\"29%\"\r\n											}\r\n									  ]],\r\n									  toolbar: \"tb_8555626c-6361-4380-ab2a-4084596937ed\",\r\n									  onClickCell:onClickCell,\r\n									  onLoadSuccess:onLoadSuccess\r\n		});\r\n		function onLoadSuccess(){\r\n				\r\n		}\r\n		function getCombobox(value){\r\n			var result={valueField:\'id\',textField:\'text\',data:[]};\r\n			if(value.indexOf(\"=\")!=-1){\r\n				var options=value.split(\";\");\r\n				var len=options.length;\r\n				for(var i=0;i<len;i++){\r\n					if(options[i]){\r\n						var option=options[i].split(\"=\");\r\n						result.data.push({id:option[0],text:option[1]});\r\n					}\r\n				}\r\n			}else{\r\n				$.ajax({ \r\n			          type : \"get\", \r\n			          url : basePath+value, \r\n			          async : false, \r\n			          success : function(data){\r\n			          	if(data.status==0){\r\n				            result.data = data.data;\r\n			          	}\r\n			          } \r\n		          });\r\n			}\r\n			return result;\r\n		}\r\n		var editIndex = undefined;\r\n		function endEditing(){\r\n			if (editIndex == undefined){return true}\r\n			if ($(\'#dg_8555626c-6361-4380-ab2a-4084596937ed\').datagrid(\'validateRow\', editIndex)){\r\n				$(\'#dg_8555626c-6361-4380-ab2a-4084596937ed\').datagrid(\'endEdit\', editIndex);\r\n				editIndex = undefined;\r\n				return true;\r\n			} else {\r\n				return false;\r\n			}\r\n		}\r\n		function onClickCell(index, field){\r\n			if (editIndex != index){\r\n				if (endEditing()){\r\n					$(\'#dg_8555626c-6361-4380-ab2a-4084596937ed\').datagrid(\'selectRow\', index)\r\n							.datagrid(\'beginEdit\', index);\r\n					editIndex = index;\r\n				}\r\n			}\r\n		}\r\n		var p = $(\'#dg_8555626c-6361-4380-ab2a-4084596937ed\').datagrid(\'getPager\');\r\n	    $(p).pagination({\r\n	        pageSize: 10,//每页显示的记录条数，默认为10 \r\n	        pageList: [10, 20, 30, 40, 50],//可以设置每页记录条数的列表 \r\n	        beforePageText: \'第\',//页数文本框前显示的汉字 \r\n	        afterPageText: \'页    共 {pages} 页\',\r\n	        displayMsg: \'当前显示 {from} - {to} 条记录   共 {total} 条记录\'\r\n	    });\r\n		$(\"#tb_8555626c-6361-4380-ab2a-4084596937ed\").on(\"click\",\"a\",function(){\r\n			//增加\r\n			if($(this).hasClass(\"easyui-add\")){\r\n				var paramData = {};\r\n				var str=\"\";\r\n						str+=\"&\";\r\n						str+=\"22de342f-18b2-4a8d-a23f-d4f30bff5ba3\";\r\n				        str+=\"=\";\r\n				        str+=\"${(p_fm_api.API_ID)!\'\'}\";\r\n						paramData[\'22de342f-18b2-4a8d-a23f-d4f30bff5ba3\']=\'${(p_fm_api.API_ID)!\'\'}\';\r\n			    \r\n				top.dialog({\r\n					id : \'edit-dialog\' + Math.ceil(Math.random() * 10000),\r\n					title : \'新增...\',\r\n					url : \"${basePath}document/view.do?dynamicPageId=3\"+str,\r\n					data: paramData,\r\n					skin:\"col-md-8\",\r\n					onclose : function() {\r\n						if (this.returnValue) {\r\n							$(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'reload\');\r\n						}\r\n					}\r\n				}).showModal();\r\n	\r\n				return false;\r\n			\r\n			}\r\n			//编辑\r\n			else if($(this).hasClass(\"easyui-edit\")){\r\n				var ids = $(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'getChecked\');\r\n				if(!ids||ids.length!=1){\r\n					Comm.alert(\"请勾选中一项操作\");\r\n					return false;\r\n				}\r\n				top.dialog({\r\n					id : \'edit-dialog\' + Math.ceil(Math.random() * 10000),\r\n					title : \'编辑...\',\r\n					url : \"${basePath}document/view.do?dynamicPageId=3&id=\" + ids[0][\"ID\"],\r\n					skin:\"col-md-8\",\r\n					onclose : function() {\r\n						if (this.returnValue) {\r\n							$(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'clearChecked\').datagrid(\'reload\');\r\n						}\r\n					}\r\n				}).showModal();\r\n			}\r\n			//保存\r\n			else if($(this).hasClass(\"easyui-save\")){\r\n				var rows = $(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'getChanges\');\r\n				if(!rows||rows.length==0){\r\n					return;\r\n				}\r\n				var data = {apiid:\r\n									  	 \'${(p_fm_api.API_ID)!\'\'}\'\r\n									  ,pageSize:10};\r\n				data.method = \'save\';\r\n				data.json = JSON.stringify(rows);\r\n				$(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'load\',data);\r\n			}\r\n			//撤销\r\n			else if($(this).hasClass(\"easyui-undo\")){\r\n				var rows = $(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'getChanges\');\r\n				if(rows&&rows.length>0){\r\n					$(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'reload\');\r\n				}\r\n			}\r\n			//删除\r\n			else if($(this).hasClass(\"easyui-remove\")){\r\n				var ids = $(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'getChecked\');\r\n				if(!ids||ids.length==0){\r\n					Comm.alert(\'请勾选数据\');\r\n				}else{\r\n					var data = {apiid:\r\n									  	 \'${(p_fm_api.API_ID)!\'\'}\'\r\n									  ,pageSize:10};\r\n					data.method = \'delete\';\r\n					data._selects = ids.map(x=>x.ID).join(\",\");\r\n					$(\"#dg_8555626c-6361-4380-ab2a-4084596937ed\").datagrid(\'clearChecked\').datagrid(\'load\',data);\r\n				}\r\n				return false;\r\n			}\r\n		})\r\n	\r\n	})();\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n	(function(){\r\n			$(\'#500377e9-7464-4b0d-aff2-0fd057c4f637\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#500377e9-7464-4b0d-aff2-0fd057c4f637\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n\r\n\r\n		\r\n		\r\n					var api_name=$(\"#c0a27a2a-4430-4795-814b-7585e4a4308d\").val();\r\n$(\"#7547d92a-a8a3-4780-8930-ddae6e9a0e46\").text(\"接口地址：baseUrl+api/execute/\"+api_name);\r\nvar reader=$(\"#suggestion\").val();\r\nif(reader==1){\r\n$(\"#9e45b738-f5c9-4ef6-862c-bb74da985c2b\").parent().parent().hide();\r\n$(\"#9e45b738-f5c9-4ef6-862c-bb74da985c2b\").parent().parent().next().hide();\r\n$(\"#9e45b738-f5c9-4ef6-862c-bb74da985c2b\").parent().parent().next().next().next().hide();\r\n}\r\nvar id=$(\"#818bb1c3-bf87-4861-a9b7-88b605f1a2e3\").val();\r\nif(id==null || id==\"\" || reader==1){\r\n$(\"#tb_8555626c-6361-4380-ab2a-4084596937ed\").hide();\r\n}\r\n$(\"textarea\").each(function(){\r\n$(this).css({\'height\':\'auto\',\'overflow-y\':\'hidden\'}).height(this.scrollHeight)\r\n})\r\n			\r\n			\r\n	$(\"#5d41e4d1-7bba-487b-9f98-b9e19172c2a6\").click(function(){\r\n				var actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);\r\n	$.ajax({\r\n	   type: \"POST\",\r\n	   url: basePath + \"document/excuteOnly.do\",\r\n	   data:$(\"#groupForm\").serialize(),\r\n	   async : false,\r\n	   success: function(data){\r\n			if(data==1){\r\n				alert(\"保存成功\");\r\n				location.href= basePath + \"document/view.do?id=&dynamicPageId=1\";\r\n			}\r\n			else{\r\n				alert(data);\r\n			}\r\n	   }\r\n	});\r\n	});\r\n	$(\"#0e68706c-4700-4767-ad34-41a17b9ee052\").click(function(){\r\n				history.back(-1);\r\n	});\r\n			\r\n	</script>\r\n</body>\r\n</html>', '2', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('20', '23', null, 'API权限编辑控制列表', null, '1003', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;7ae24746-1201-3062-fa90-2634170e21a0&quot;,&quot;name&quot;:&quot;p_fm_api_privilege&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;1&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;\\&quot;SELECT  a.DESCRIBES,a.ID,a.RULES,a.REMARK,a.TYPE,a.NAME,a.MESSAGE,(select count(ID) from p_fm_api_privilege_relation where PRI_ID=a.ID) as Num  FROM  p_fm_api_privilege a group by a.ID\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_api_privilege&quot;,&quot;modelItemCodes&quot;:&quot;MESSAGE,DESCRIBES,ID,RULES,REMARK,TYPE,NAME&quot;}]', '0', null, null, '2017-09-19 19:44:34', '2017-12-18 15:32:46', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_api_privilege_list?? && p_fm_api_privilege_list?size gt 0>\r\n             <#assign p_fm_api_privilege = p_fm_api_privilege_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head lang=\"en\">\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-table.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n</head>\r\n<body>\r\n	<!-- Main content -->\r\n  <section class=\"content\">	\r\n		<div class=\"opeBtnGrop\">\r\n\r\n		<button id=\'2dd97ba8-18c8-47e5-a207-8ef7f6a0d5dc\' title=\"\"\r\n				class=\" btn   btn-primary\r\n\r\n				\"\r\n					<#if (pageActStatus[\'2dd97ba8-18c8-47e5-a207-8ef7f6a0d5dc\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'2dd97ba8-18c8-47e5-a207-8ef7f6a0d5dc\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'2dd97ba8-18c8-47e5-a207-8ef7f6a0d5dc\'][\'hidden\'])?? && pageActStatus[\'2dd97ba8-18c8-47e5-a207-8ef7f6a0d5dc\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-plus\'></i>\r\n			新增</button>\r\n\r\n\r\n		<button id=\'9da08795-b7c5-48c9-bc5e-07ce6f07c86e\' title=\"\"\r\n				class=\" btn   btn-success\r\n\r\n				\"\r\n					<#if (pageActStatus[\'9da08795-b7c5-48c9-bc5e-07ce6f07c86e\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'9da08795-b7c5-48c9-bc5e-07ce6f07c86e\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'9da08795-b7c5-48c9-bc5e-07ce6f07c86e\'][\'hidden\'])?? && pageActStatus[\'9da08795-b7c5-48c9-bc5e-07ce6f07c86e\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-edit\'></i>\r\n			编辑</button>\r\n\r\n\r\n		<button id=\'170aa29f-6722-4422-b023-9a8ec24a7afe\' title=\"\"\r\n				class=\" btn   btn-danger\r\n\r\n				\"\r\n					<#if (pageActStatus[\'170aa29f-6722-4422-b023-9a8ec24a7afe\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'170aa29f-6722-4422-b023-9a8ec24a7afe\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'170aa29f-6722-4422-b023-9a8ec24a7afe\'][\'hidden\'])?? && pageActStatus[\'170aa29f-6722-4422-b023-9a8ec24a7afe\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-trash\'></i>\r\n			删除</button>\r\n\r\n\r\n		<button id=\'117e77cc-22d0-4084-8ede-56127c807440\' title=\"\"\r\n				class=\" btn   btn-info\r\n\r\n				\"\r\n					<#if (pageActStatus[\'117e77cc-22d0-4084-8ede-56127c807440\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'117e77cc-22d0-4084-8ede-56127c807440\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'117e77cc-22d0-4084-8ede-56127c807440\'][\'hidden\'])?? && pageActStatus[\'117e77cc-22d0-4084-8ede-56127c807440\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-tasks\'></i>\r\n			关联接口</button>\r\n\r\n\r\n		<button id=\'39f87bd3-384b-463c-9388-78b2bc291070\' title=\"\"\r\n				class=\" btn   btn-warning\r\n\r\n				\"\r\n					<#if (pageActStatus[\'39f87bd3-384b-463c-9388-78b2bc291070\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'39f87bd3-384b-463c-9388-78b2bc291070\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'39f87bd3-384b-463c-9388-78b2bc291070\'][\'hidden\'])?? && pageActStatus[\'39f87bd3-384b-463c-9388-78b2bc291070\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			返回</button>\r\n\r\n		\r\n		</div>\r\n		<div class=\"row\">\r\n		<div class=\"col-md-12\">\r\n			<div class=\"box box-info\">\r\n				<div class=\"box-body\">\r\n					\r\n						<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n							<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"${p_fm_api_privilege_list_paginator.getLimit()}\">\r\n							<input type=\"hidden\" id=\"totalCount\" name=\"totalCount\" value=\"${p_fm_api_privilege_list_paginator.getTotalCount()}\">\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n							<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n							<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n							<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							<table id=\"bootstrapTable\" class=\"table table-hover\">\r\n								<thead>\r\n									<tr>\r\n										<th class=\"hidden\"></th>\r\n										<th data-width=\"\" data-field=\"\" data-checkbox=\"true\"></th>\r\n										<th data-width=\"50\">序号</th>\r\n		<th 	data-width=\"\"\r\n		class=\"<#if (status[\'e05dad078fb4d6f8228a08f9f372e46c\'][\'hidden\'])?? && status[\'e05dad078fb4d6f8228a08f9f372e46c\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"权限名称\">\r\n		权限名称\r\n		\r\n		</th>\r\n		<th 	data-width=\"\"\r\n		class=\"<#if (status[\'f3ce8a84e2353f3e3b7b0390af4c39e0\'][\'hidden\'])?? && status[\'f3ce8a84e2353f3e3b7b0390af4c39e0\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"权限类型\">\r\n		权限类型\r\n		\r\n		</th>\r\n		<th 	data-width=\"\"\r\n		class=\"<#if (status[\'89055fd707054c41eb89ebb30861dcb3\'][\'hidden\'])?? && status[\'89055fd707054c41eb89ebb30861dcb3\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"已关联接口数\">\r\n		已关联接口数\r\n		\r\n		</th>\r\n		<th 	data-width=\"\"\r\n		class=\"<#if (status[\'8b80b5c364ebe08f5d238a63e66d3408\'][\'hidden\'])?? && status[\'8b80b5c364ebe08f5d238a63e66d3408\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"描述\">\r\n		描述\r\n		\r\n		</th>\r\n									</tr>\r\n								</thead>\r\n								<tbody>\r\n									<#if p_fm_api_privilege_list?? && p_fm_api_privilege_list?size gte 1>	\r\n										<#list\r\n											p_fm_api_privilege_list as p_fm_api_privilege>\r\n										<tr>\r\n											<td class=\"hidden formData\">\r\n												<input type=\"hidden\" value=\"${p_fm_api_privilege.ID!\'\'}\">\r\n											</td>\r\n											<td></td>\r\n									<td>${(p_fm_api_privilege_list_paginator.getPage() * p_fm_api_privilege_list_paginator.getLimit() - p_fm_api_privilege_list_paginator.getLimit() + p_fm_api_privilege_index + 1)}\r\n									</td>\r\n		<td class=\"<#if (status[\'e05dad078fb4d6f8228a08f9f372e46c\'][\'hidden\'])?? && status[\'e05dad078fb4d6f8228a08f9f372e46c\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api_privilege.NAME!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'f3ce8a84e2353f3e3b7b0390af4c39e0\'][\'hidden\'])?? && status[\'f3ce8a84e2353f3e3b7b0390af4c39e0\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api_privilege.TYPE!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'89055fd707054c41eb89ebb30861dcb3\'][\'hidden\'])?? && status[\'89055fd707054c41eb89ebb30861dcb3\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api_privilege.Num!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'8b80b5c364ebe08f5d238a63e66d3408\'][\'hidden\'])?? && status[\'8b80b5c364ebe08f5d238a63e66d3408\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api_privilege.DESCRIBES!\'\'}\r\n		</td>\r\n										</tr>\r\n										</#list>\r\n									</#if>\r\n								</tbody>\r\n							</table>\r\n						</form>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</section>\r\n	<script src=\"${basePath}template/AdminLTE/js/jquery.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/editTable.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/bootstrap-table.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js\"></script>\r\n	<script type=\"text/javascript\" src=\'${basePath}venson/js/common.js\'></script>\r\n	<script type=\"text/javascript\" src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script type=\"text/javascript\">\r\n		var basePath = \'${basePath}\';\r\n		var count=0;//默认选择行数为0\r\n	$(document).ready(function(){\r\n		\r\n		 $(\"#bootstrapTable\").bootstrapTable({\r\n        	 pageSize:parseInt($(\"#pageSize\").val()),\r\n        	 pageNumber:parseInt($(\"#currentPage\").val()),\r\n        	 totalRows:parseInt($(\"#totalCount\").val()),\r\n        	 sidePagination:\"server\",\r\n        	 pagination:true,\r\n        	 onPageChange:function(number, size){\r\n        		$(\"#pageSize\").val(size);\r\n        		$(\"#currentPage\").val(number);\r\n        		$(\"#groupForm\").submit();\r\n        	 },\r\n        	 onCheck: function(row,$element){  \r\n				  $element.closest(\"tr\").find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onUncheck:function(row,$element){ \r\n        		 $element.closest(\"tr\").find(\"input[type=\'hidden\']\").removeAttr(\"name\");\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n        	 }, \r\n        	 onCheckAll: function (rows) {\r\n        		 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").attr(\"name\",\"_selects\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n             onUncheckAll: function (rows) {          	 \r\n            	 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").removeAttr(\"name\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             }\r\n         });							\r\n		\r\n$(\"#2dd97ba8-18c8-47e5-a207-8ef7f6a0d5dc\").click(function(){\r\n					location.href= basePath + \"document/view.do?dynamicPageId=19\";\r\n});\r\n$(\"#9da08795-b7c5-48c9-bc5e-07ce6f07c86e\").click(function(){\r\n					if(count!=1){\r\n	alert(\"请选择一项进行操作\");\r\n	return false;\r\n}\r\nvar id = $(\"input[name=\'_selects\']\").val();\r\nlocation.href = basePath + \"document/view.do?dynamicPageId=19&id=\" + id;\r\n});\r\n$(\"#170aa29f-6722-4422-b023-9a8ec24a7afe\").click(function(){\r\n					if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nif(!confirm(\"是否确认删除?\")){\r\n	return false;\r\n}\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize(),\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"删除成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=20&edit=true\";\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n});\r\n$(\"#117e77cc-22d0-4084-8ede-56127c807440\").click(function(){\r\n					if(count!=1){\r\n	alert(\"请选择一项进行操作\");\r\n	return false;\r\n}\r\nlocation.href = basePath+\"document/view.do?dynamicPageId=7&pid=\" + $(\"input[name=\'_selects\']\").val();\r\n});\r\n$(\"#39f87bd3-384b-463c-9388-78b2bc291070\").click(function(){\r\n					location.href = basePath + \"document/view.do?dynamicPageId=1\";\r\n});\r\n        \r\n		});\r\n	</script>\r\n</body>\r\n</html>', '1', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', null);
INSERT INTO `p_fm_dynamicpage` VALUES ('3', '23', null, 'API参数表单', null, '1002', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;de05e2fd-6cb8-a6ed-f9e3-924b3c5dd59f&quot;,&quot;name&quot;:&quot;p_fm_api_describe&quot;,&quot;isSingle&quot;:&quot;0&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id=request.getParameter(\\&quot;id\\&quot;);\\r\\n\\&quot;SELECT  DEFAULTVAL,NAME,ID,TYPE,ISNECESSARY,DESCRIBES,APIID,PARAM_PLACE  FROM  p_fm_api_describe where id=\'\\&quot;+id+\\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_api_describe&quot;,&quot;modelItemCodes&quot;:&quot;DEFAULTVAL,NAME,PARAM_PLACE,DESCRIBES,ID,APIID,TYPE,ISNECESSARY&quot;}]', '0', null, null, '2017-05-02 16:27:12', '2017-12-18 15:31:35', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_api_describe_list?? && p_fm_api_describe_list?size gt 0>\r\n             <#assign p_fm_api_describe = p_fm_api_describe_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/font-awesome.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrapValidator.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n	<script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n			cssFile[\"${basePath}template/AdminLTE/css/select2/select2.css\"]=null;\r\n	for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n	cssFile=null;\r\n	</script>\r\n	\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n        \r\n	<![endif]-->\r\n	<script>\r\n		\r\n    </script>\r\n</head>\r\n<body>\r\n    <div class=\"content\"> \r\n		<div class=\"row opeBtnGrop\" id=\"buttons\">\r\n			<div class=\"col-md-12\">\r\n		<button data-valid=\"flase\" id=\'84c38f07-1ed7-4618-af82-2938d047f4c9\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'84c38f07-1ed7-4618-af82-2938d047f4c9\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'84c38f07-1ed7-4618-af82-2938d047f4c9\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'84c38f07-1ed7-4618-af82-2938d047f4c9\'][\'hidden\'])?? && pageActStatus[\'84c38f07-1ed7-4618-af82-2938d047f4c9\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-save\'></i>\r\n			保存</button>\r\n		<button data-valid=\"flase\" id=\'bd23b782-f45a-40de-a96a-bb7ce828b1db\' title=\"\"\r\n				class=\" btn   btn-danger\"\r\n					<#if (pageActStatus[\'bd23b782-f45a-40de-a96a-bb7ce828b1db\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'bd23b782-f45a-40de-a96a-bb7ce828b1db\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'bd23b782-f45a-40de-a96a-bb7ce828b1db\'][\'hidden\'])?? && pageActStatus[\'bd23b782-f45a-40de-a96a-bb7ce828b1db\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-remove\'></i>\r\n			关闭</button>\r\n			</div>\r\n		</div>\r\n		<div class=\"row\" id=\"newTitle\"></div>\r\n		<div class=\"row\">\r\n			<div class=\"col-md-12\">\r\n				<div class=\"box box-info\">\r\n					<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}workflow/wf/excute.do\" role=\"customForm\" method=\"post\">\r\n						<div class=\"box-body\">\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div  data-required=\"0\" data-placeholder=\"\" style=\"display:none;\">\r\n		<input class=\"dataItemCode\" type=\'hidden\'\r\n			name=\'accf420a1c4eae4d1ed04302003bbc8e\'\r\n				value=\"${(p_fm_api_describe.ID)!\'\'}\"\r\n		id=\'22de342f-18b2-4a8d-a23f-d4f30bff5ba3\'\r\n		/>\r\n	</div>\r\n	<div  data-required=\"0\" data-placeholder=\"\" style=\"display:none;\">\r\n		<input class=\"dataItemCode\" type=\'hidden\'\r\n			name=\'APIID\'\r\n				value=\"${(p_fm_api_describe.APIID)!\'\'}\"\r\n		id=\'6f901b0c-c88e-44c5-be21-2ca37ff8e577\'\r\n		/>\r\n	</div>\r\n	<div class=\"customGroup\"  data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'d787bbd6f39fb6364faa6d1ef65cc492\'][\'hidden\'])?? && status[\'d787bbd6f39fb6364faa6d1ef65cc492\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_d787bbd6f39fb6364faa6d1ef65cc492\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'d787bbd6f39fb6364faa6d1ef65cc492\'\r\n		<#if (status[\'d787bbd6f39fb6364faa6d1ef65cc492\'][\'disabled\'])?? && status[\'d787bbd6f39fb6364faa6d1ef65cc492\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'d787bbd6f39fb6364faa6d1ef65cc492\'][\'readonly\'])?? && status[\'d787bbd6f39fb6364faa6d1ef65cc492\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_fm_api_describe.NAME)!\'\'}\"\r\n		id=\'e4353eb2-44db-4af8-83fc-bcb27fcda1d7\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\"  data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'1c8a83b6dc3f671944ac75799fd0080c\'][\'hidden\'])?? && status[\'1c8a83b6dc3f671944ac75799fd0080c\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_1c8a83b6dc3f671944ac75799fd0080c\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'1c8a83b6dc3f671944ac75799fd0080c\'\r\n		<#if (status[\'1c8a83b6dc3f671944ac75799fd0080c\'][\'disabled\'])?? && status[\'1c8a83b6dc3f671944ac75799fd0080c\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'1c8a83b6dc3f671944ac75799fd0080c\'][\'readonly\'])?? && status[\'1c8a83b6dc3f671944ac75799fd0080c\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_fm_api_describe.DEFAULTVAL)!\'\'}\"\r\n		id=\'255b8e3a-e5ba-4553-ad44-70f59b1ecd27\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'f6d3a972c1a277dd3ad98af4567b0f83\'][\'hidden\'])?? && status[\'f6d3a972c1a277dd3ad98af4567b0f83\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_f6d3a972c1a277dd3ad98af4567b0f83\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'f6d3a972c1a277dd3ad98af4567b0f83\'][\'disabled\'])?? && status[\'f6d3a972c1a277dd3ad98af4567b0f83\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'f6d3a972c1a277dd3ad98af4567b0f83\'][\'readonly\'])?? && status[\'f6d3a972c1a277dd3ad98af4567b0f83\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'f6d3a972c1a277dd3ad98af4567b0f83\'\r\n\r\n				value=\"${(p_fm_api_describe.ISNECESSARY)!\'\'}\"\r\n\r\n		id=\'0a50bfa7-fe7f-44c1-a422-7adbb36db292\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'f6d3a972c1a277dd3ad98af4567b0f83\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'0273716065e800348c8818da9953fa2d\'][\'hidden\'])?? && status[\'0273716065e800348c8818da9953fa2d\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_0273716065e800348c8818da9953fa2d\']!\'\'}</label>\r\n		<select class=\'dataItemCode \r\n			form-control\r\n		\'\r\n			style=\'\'\r\n\r\n		<#if (status[\'0273716065e800348c8818da9953fa2d\'][\'disabled\'])?? && status[\'0273716065e800348c8818da9953fa2d\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'0273716065e800348c8818da9953fa2d\'][\'readonly\'])?? && status[\'0273716065e800348c8818da9953fa2d\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'0273716065e800348c8818da9953fa2d\'\r\n\r\n				value=\"${(p_fm_api_describe.TYPE)!\'\'}\"\r\n\r\n		id=\'20b3ff78-d393-4d0d-aa7c-f98506d65320\'\r\n			title=\'\'\r\n		>\r\n			<#if others??> ${others[\'0273716065e800348c8818da9953fa2d\']!\'\'}</#if>\r\n		</select>\r\n	</div>\r\n			</div>\r\n			<div class=\"col-sm-4 col-md-4 col-md-4\">\r\n	<div class=\"customGroup\"  data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'799debf0f892b02131fb959f035bbea2\'][\'hidden\'])?? && status[\'799debf0f892b02131fb959f035bbea2\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_799debf0f892b02131fb959f035bbea2\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'799debf0f892b02131fb959f035bbea2\'\r\n		<#if (status[\'799debf0f892b02131fb959f035bbea2\'][\'disabled\'])?? && status[\'799debf0f892b02131fb959f035bbea2\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'799debf0f892b02131fb959f035bbea2\'][\'readonly\'])?? && status[\'799debf0f892b02131fb959f035bbea2\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_fm_api_describe.PARAM_PLACE)!\'\'}\"\r\n		id=\'2fd5a8e7-b224-4b19-b23f-4bf290a52031\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n	<div class=\"customGroup\" data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'1b2908851c2768ccde98e75e50a5ecad\'][\'hidden\'])?? && status[\'1b2908851c2768ccde98e75e50a5ecad\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_1b2908851c2768ccde98e75e50a5ecad\']!\'\'}</label>\r\n		<textarea   class=\'dataItemCode \r\n			form-control\r\n		<#if (status[\'1b2908851c2768ccde98e75e50a5ecad\'][\'hidden\'])?? && status[\'1b2908851c2768ccde98e75e50a5ecad\'][\'hidden\'] == \'true\'>hidden</#if>\r\n		\'\r\n\r\n			style=\'width:100%\'\r\n		<#if (status[\'1b2908851c2768ccde98e75e50a5ecad\'][\'disabled\'])?? && status[\'1b2908851c2768ccde98e75e50a5ecad\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'1b2908851c2768ccde98e75e50a5ecad\'][\'readonly\'])?? && status[\'1b2908851c2768ccde98e75e50a5ecad\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n			name=\'1b2908851c2768ccde98e75e50a5ecad\'\r\n			rows=\'7\'\r\n		id=\'6157b6c5-587a-4491-a397-79049fae5257\'\r\n			title=\'\'\r\n		>${(p_fm_api_describe.DESCRIBES)!\'\'}</textarea>\r\n</div>\r\n			</div>\r\n				\r\n		</div>	\r\n						</div>\r\n					</form>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script>\r\n	//引入组件脚本\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/select2.full.js\"]=null;\r\n			scriptFile[\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"]=null;\r\n\r\n\r\n	for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n	scriptFile=null;\r\n	</script>\r\n	<script>\r\n\r\n\r\n\r\n\r\n\r\n(function(){\r\n		var that=$(\'#e4353eb2-44db-4af8-83fc-bcb27fcda1d7\');\r\n			\r\n})();\r\n\r\n\r\n\r\n\r\n	(function(){\r\n			$(\'#0a50bfa7-fe7f-44c1-a422-7adbb36db292\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#0a50bfa7-fe7f-44c1-a422-7adbb36db292\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n(function(){\r\n		var that=$(\'#2fd5a8e7-b224-4b19-b23f-4bf290a52031\');\r\n			\r\n})();\r\n\r\n	(function(){\r\n			$(\'#20b3ff78-d393-4d0d-aa7c-f98506d65320\').select2({\r\n				 width:\"100%\",\r\n				 placeholder:\'请选择\',\r\n				 language:\'zh-CN\'\r\n			});\r\n			$(\'#20b3ff78-d393-4d0d-aa7c-f98506d65320\').on(\"change\",function(){\r\n				\r\n			});\r\n	})();\r\n\r\n(function(){\r\n		var that=$(\'#255b8e3a-e5ba-4553-ad44-70f59b1ecd27\');\r\n			\r\n})();\r\n		\r\n		\r\n			\r\n			\r\n	$(\"#84c38f07-1ed7-4618-af82-2938d047f4c9\").click(function(){\r\n				var apiid=$(\"#6f901b0c-c88e-44c5-be21-2ca37ff8e577\").val();\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);\r\n	$.ajax({\r\n	   type: \"POST\",\r\n	   url: basePath + \"document/excuteOnly.do\",\r\n	   data:$(\"#groupForm\").serialize(),\r\n	   async : false,\r\n	   success: function(data){\r\n			if(data==1){\r\n				top.dialog({id : window.name}).close(\"Y\");                              \r\n			}\r\n			else{\r\n				alert(data);\r\n			}\r\n	   }\r\n	});\r\n	});\r\n	$(\"#bd23b782-f45a-40de-a96a-bb7ce828b1db\").click(function(){\r\n				top.dialog({id : window.name}).close();\r\n	});\r\n			\r\n	</script>\r\n</body>\r\n</html>', '2', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('4', '11', null, '页面模块列表', null, '1003', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;ef532723-e22a-39e9-5244-fa1277d2510c&quot;,&quot;name&quot;:&quot;p_fm_modular&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;1&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;\\&quot;select ID,modularName,(select GROUP_CONCAT(name) from p_fm_dynamicpage where modular=t.ID) as pages from p_fm_modular t\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_modular&quot;,&quot;modelItemCodes&quot;:&quot;ID,modularName&quot;}]', '0', null, null, '2017-07-14 14:22:33', '2017-12-18 15:05:35', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_modular_list?? && p_fm_modular_list?size gt 0>\r\n             <#assign p_fm_modular = p_fm_modular_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head lang=\"en\">\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-table.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/select2/select2.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">  \r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n    <script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n		for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n		cssFile=null;\r\n	</script>\r\n</head>\r\n<body>\r\n	<!-- Main content -->\r\n  <section class=\"content\">	\r\n		<div class=\"opeBtnGrop\">\r\n		<button data-valid=\"flase\" id=\'931b0578-396f-4f70-a79e-3bed6b8e19f3\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'931b0578-396f-4f70-a79e-3bed6b8e19f3\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'931b0578-396f-4f70-a79e-3bed6b8e19f3\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'5\'\r\n					<#if (pageActStatus[\'931b0578-396f-4f70-a79e-3bed6b8e19f3\'][\'hidden\'])?? && pageActStatus[\'931b0578-396f-4f70-a79e-3bed6b8e19f3\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-plus\'></i>\r\n			新增</button>\r\n		<button data-valid=\"flase\" id=\'65f86c59-c80b-40e8-8b40-ddd7695b4570\' title=\"\"\r\n				class=\" btn   btn-success\"\r\n					<#if (pageActStatus[\'65f86c59-c80b-40e8-8b40-ddd7695b4570\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'65f86c59-c80b-40e8-8b40-ddd7695b4570\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'65f86c59-c80b-40e8-8b40-ddd7695b4570\'][\'hidden\'])?? && pageActStatus[\'65f86c59-c80b-40e8-8b40-ddd7695b4570\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-edit\'></i>\r\n			编辑</button>\r\n		<button data-valid=\"flase\" id=\'4cc5ece5-29de-4c01-8166-514e311e3704\' title=\"\"\r\n				class=\" btn   btn-danger\"\r\n					<#if (pageActStatus[\'4cc5ece5-29de-4c01-8166-514e311e3704\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'4cc5ece5-29de-4c01-8166-514e311e3704\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'4cc5ece5-29de-4c01-8166-514e311e3704\'][\'hidden\'])?? && pageActStatus[\'4cc5ece5-29de-4c01-8166-514e311e3704\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-trash\'></i>\r\n			删除</button>\r\n		</div>\r\n		<div class=\"row\">\r\n		<div class=\"col-md-12\">\r\n			<div class=\"box box-info\">\r\n				<div class=\"box-body\">					\r\n						<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n							<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"${p_fm_modular_list_paginator.getLimit()}\">\r\n							<input type=\"hidden\" id=\"totalCount\" name=\"totalCount\" value=\"${p_fm_modular_list_paginator.getTotalCount()}\">\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"orderBy\" value=\"${ (vo.orderBy)!\"\"}\" id=\"orderBy\"/>\r\n							<input type=\"hidden\" name=\"allowOrderBy\" id=\"allowOrderBy\" value=\"${ (vo.allowOrderBy)!\"\"}\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							<table id=\"bootstrapTable\" class=\"table table-hover\">\r\n								<thead>\r\n									<tr>\r\n										<th class=\"hidden\"></th>\r\n										<th data-width=\"\" data-field=\"\" data-checkbox=\"true\"></th>\r\n										<th data-width=\"50\">序号</th>\r\n		<th \r\n			data-width=\"15%\"\r\n		<#if (status[\'74e81705f25a857eff7e26c9473a9dfb\'][\'hidden\'])?? && status[\'74e81705f25a857eff7e26c9473a9dfb\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_modular.modularName\"\r\n		>\r\n			模块名称\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"80%\"\r\n		<#if (status[\'f5f70ae163a7e5a779a58ec98b28fc42\'][\'hidden\'])?? && status[\'f5f70ae163a7e5a779a58ec98b28fc42\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_modular.pages\"\r\n		>\r\n			包含的页面\r\n		\r\n		</th>\r\n									</tr>\r\n								</thead>\r\n								<tbody>\r\n									<#if p_fm_modular_list?? && p_fm_modular_list?size gte 1>	\r\n										<#list\r\n											p_fm_modular_list as p_fm_modular>\r\n										<tr>\r\n											<td class=\"hidden formData\">\r\n												<input type=\"hidden\" value=\"${p_fm_modular.ID!\'\'}\">\r\n											</td>\r\n											<td></td>\r\n									<td>${(p_fm_modular_list_paginator.getPage() * p_fm_modular_list_paginator.getLimit() - p_fm_modular_list_paginator.getLimit() + p_fm_modular_index + 1)}\r\n									</td>\r\n		<td <#if (status[\'74e81705f25a857eff7e26c9473a9dfb\'][\'hidden\'])?? && status[\'74e81705f25a857eff7e26c9473a9dfb\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_modular.modularName!\'\'}\r\n		</td>\r\n		<td <#if (status[\'f5f70ae163a7e5a779a58ec98b28fc42\'][\'hidden\'])?? && status[\'f5f70ae163a7e5a779a58ec98b28fc42\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_modular.pages!\'\'}\r\n		</td>\r\n										</tr>\r\n										</#list>\r\n									</#if>\r\n								</tbody>\r\n							</table>\r\n						</form>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</section>\r\n	<script src=\"${basePath}template/AdminLTE/js/jquery.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/editTable.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/bootstrap-table.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/select2.full.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js\"></script>\r\n	<script src=\'${basePath}venson/js/common.js\'></script>\r\n	<script src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script>\r\n		for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n		scriptFile=null;\r\n		var basePath = \'${basePath}\';\r\n		var count=0;//默认选择行数为0\r\n	$(document).ready(function(){\r\n		$(document).keyup(function(event) {\r\n	    	if (event.keyCode == 13) {\r\n	    		$(\"#groupForm\").submit();\r\n	    	}\r\n	    });\r\n		 $(\"#bootstrapTable\").bootstrapTable({\r\n        	 pageSize:parseInt($(\"#pageSize\").val()),\r\n        	 pageNumber:parseInt($(\"#currentPage\").val()),\r\n        	 totalRows:parseInt($(\"#totalCount\").val()),\r\n        	 sidePagination:\"server\",\r\n        	 pagination:true,\r\n        	 onPageChange:function(number, size){\r\n        		$(\"#pageSize\").val(size);\r\n        		$(\"#currentPage\").val(number);\r\n        		$(\"#groupForm\").submit();\r\n        	 },\r\n        	 onClickRow:function(row,$element,field){\r\n        	  	  var $checkbox=$element.find(\":checkbox\").eq(0);\r\n        	  	  if($checkbox.get(0).checked){\r\n					  $checkbox.get(0).checked=false;\r\n					  $element.find(\"input[type=\'hidden\']\").removeAttr(\"name\",\"_selects\");\r\n        	  	  }else{\r\n					  $checkbox.get(0).checked=true;\r\n					  $element.find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n        	  	  }\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onCheck: function(row,$element){  \r\n				  $element.closest(\"tr\").find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onUncheck:function(row,$element){ \r\n        		 $element.closest(\"tr\").find(\"input[type=\'hidden\']\").removeAttr(\"name\");\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n        	 }, \r\n        	 onCheckAll: function (rows) {\r\n        		 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").attr(\"name\",\"_selects\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n             onUncheckAll: function (rows) {          	 \r\n            	 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").removeAttr(\"name\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n			 onSort:function(name, order){\r\n				 $(\"#allowOrderBy\").val(\"1\");\r\n				 var orderBy =$(\"#orderBy\").val();\r\n				 console.log(orderBy,name)\r\n				 if(orderBy.indexOf(name)!=-1&&orderBy.indexOf(\"desc\")!=-1){\r\n					 $(\"#orderBy\").val(name+\" asc\");\r\n				 }else{\r\n					 $(\"#orderBy\").val(name+\" desc\");\r\n				 }\r\n        		$(\"#groupForm\").submit();\r\n			 }\r\n         });							\r\n		\r\n	$(\"#931b0578-396f-4f70-a79e-3bed6b8e19f3\").click(function(){\r\n				actNewRun(\"931b0578-396f-4f70-a79e-3bed6b8e19f3\",\"5\");\r\n	});\r\n	$(\"#65f86c59-c80b-40e8-8b40-ddd7695b4570\").click(function(){\r\n				if(count!=1){	\r\n	alert(\"请选择一项进行操作\");\r\n	return false;\r\n}\r\nvar id = $(\"input[name=\'_selects\']\").val();\r\nlocation.href = basePath + \"document/view.do?dynamicPageId=5&id=\" + id;\r\n	});\r\n	$(\"#4cc5ece5-29de-4c01-8166-514e311e3704\").click(function(){\r\n				if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nif(!confirm(\"是否确认删除?\")){\r\n	return false;\r\n}\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize(),\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"删除成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=4\";\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n	});\r\n        \r\n		});\r\n	</script>\r\n</body>\r\n</html>', '1', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('5', '11', null, '页面模块新增表单', null, '1002', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;86db6a1b-c716-0648-ee8a-a579655c2a8b&quot;,&quot;name&quot;:&quot;p_fm_modular&quot;,&quot;isSingle&quot;:&quot;0&quot;,&quot;isPage&quot;:&quot;0&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var id = request.getParameter(\\&quot;id\\&quot;);\\r\\n\\&quot;select ID,modularName from p_fm_modular where ID=\'\\&quot; + id + \\&quot;\'\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_modular&quot;,&quot;modelItemCodes&quot;:&quot;ID,modularName&quot;}]', '0', null, null, '2017-07-14 14:35:26', '2017-08-11 16:45:51', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_modular_list?? && p_fm_modular_list?size gt 0>\r\n             <#assign p_fm_modular = p_fm_modular_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/font-awesome.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrapValidator.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n	<script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n	for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n	cssFile=null;\r\n	</script>\r\n	\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n        \r\n	<![endif]-->\r\n	<script>\r\n		\r\n    </script>\r\n</head>\r\n<body>\r\n    <div class=\"content\"> \r\n		<div class=\"row opeBtnGrop\" id=\"buttons\">\r\n			<div class=\"col-md-12\">\r\n		<button data-valid=\"flase\" id=\'784099e4-1234-45c2-ad79-0ba378c53e79\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'784099e4-1234-45c2-ad79-0ba378c53e79\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'784099e4-1234-45c2-ad79-0ba378c53e79\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'784099e4-1234-45c2-ad79-0ba378c53e79\'][\'hidden\'])?? && pageActStatus[\'784099e4-1234-45c2-ad79-0ba378c53e79\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-save\'></i>\r\n			保存</button>\r\n		<button data-valid=\"flase\" id=\'7a9564cc-dd35-4757-9a61-060fde449e20\' title=\"\"\r\n				class=\" btn   btn-default\"\r\n					<#if (pageActStatus[\'7a9564cc-dd35-4757-9a61-060fde449e20\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'7a9564cc-dd35-4757-9a61-060fde449e20\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'7a9564cc-dd35-4757-9a61-060fde449e20\'][\'hidden\'])?? && pageActStatus[\'7a9564cc-dd35-4757-9a61-060fde449e20\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			返回</button>\r\n			</div>\r\n		</div>\r\n		<div class=\"row\" id=\"newTitle\"></div>\r\n		<div class=\"row\">\r\n			<div class=\"col-md-12\">\r\n				<div class=\"box box-info\">\r\n					<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}workflow/wf/excute.do\" role=\"customForm\" method=\"post\">\r\n						<div class=\"box-body\">\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-12 col-md-12 col-md-12\">\r\n			</div>\r\n				\r\n		</div>	\r\n				\r\n		<div class=\"form-group\">		\r\n			<div class=\"col-sm-2 col-md-2 col-md-2\">\r\n			</div>\r\n			<div class=\"col-sm-10 col-md-10 col-md-10\">\r\n	<div  data-required=\"0\" data-placeholder=\"\" style=\"display:none;\">\r\n		<input class=\"dataItemCode\" type=\'hidden\'\r\n			name=\'62c36a03f422bb1e4ada5ff5b2474f23\'\r\n				value=\"${(p_fm_modular.ID)!\'\'}\"\r\n		id=\'1f3d9e98-4dc3-4476-822c-b8990da6566e\'\r\n		/>\r\n	</div>\r\n	<div class=\"customGroup\"  data-required=\"0\" data-placeholder=\"\" style=\"<#if (status[\'7d3f0663468c82ca1f6fdb4726a5d6a2\'][\'hidden\'])?? && status[\'7d3f0663468c82ca1f6fdb4726a5d6a2\'][\'hidden\'] == \'true\'>display:none;</#if>\">\r\n		<label class=\"control-label\">${others[\'title_7d3f0663468c82ca1f6fdb4726a5d6a2\']!\'\'}</label>\r\n		<input type=\'text\' class=\'dataItemCode \r\n			form-control\r\n		\'\r\n		style=\'\'\r\n			name=\'7d3f0663468c82ca1f6fdb4726a5d6a2\'\r\n		<#if (status[\'7d3f0663468c82ca1f6fdb4726a5d6a2\'][\'disabled\'])?? && status[\'7d3f0663468c82ca1f6fdb4726a5d6a2\'][\'disabled\'] == \'true\'>disabled=\"disabled\"</#if>\r\n		<#if (status[\'7d3f0663468c82ca1f6fdb4726a5d6a2\'][\'readonly\'])?? && status[\'7d3f0663468c82ca1f6fdb4726a5d6a2\'][\'readonly\'] == \'true\'>readonly=\"readonly\"</#if>\r\n				value=\"${(p_fm_modular.modularName)!\'\'}\"\r\n		id=\'e25b58bb-db99-4d9d-b39e-8fa07d0ff15c\'\r\n			title=\'\'\r\n		/>\r\n	</div>\r\n			</div>\r\n				\r\n		</div>	\r\n						</div>\r\n					</form>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script>\r\n	//引入组件脚本\r\n\r\n\r\n	for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n	scriptFile=null;\r\n	</script>\r\n	<script>\r\n\r\n\r\n\r\n(function(){\r\n		var that=$(\'#e25b58bb-db99-4d9d-b39e-8fa07d0ff15c\');\r\n			\r\n})();\r\n		\r\n		\r\n			\r\n			\r\n	$(\"#784099e4-1234-45c2-ad79-0ba378c53e79\").click(function(){\r\n				saveFormPage(4,$(this).attr(\"id\"));\r\n	});\r\n	$(\"#7a9564cc-dd35-4757-9a61-060fde449e20\").click(function(){\r\n				backToViewPage(4);\r\n	});\r\n			\r\n	</script>\r\n</body>\r\n</html>', '2', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', '1');
INSERT INTO `p_fm_dynamicpage` VALUES ('6', '23', null, '未绑定绑定API列表', null, '1003', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;02a45ce8-19b8-353d-8855-a414ac838757&quot;,&quot;name&quot;:&quot;p_fm_api&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;1&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var pid=request.getParameter(\\&quot;pid\\&quot;);\\r\\nvar name=request.getParameter(\\&quot;name\\&quot;);\\r\\nvar desc=request.getParameter(\\&quot;desc\\&quot;);\\r\\nvar id=request.getParameter(\\&quot;id\\&quot;);\\r\\nvar where =\\&quot;\\&quot;;\\r\\nif(name!=null &amp;&amp; name!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_NAME LIKE \'%\\&quot;+name+\\&quot;%\'\\&quot;;\\r\\n}\\r\\nif(desc!=null &amp;&amp; desc!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_DESC LIKE \'%\\&quot;+desc+\\&quot;%\'\\&quot;;\\r\\n}\\r\\nif(id!=null &amp;&amp; id!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_ID LIKE \'%\\&quot;+id+\\&quot;%\'\\&quot;;\\r\\n}\\r\\n\\&quot;SELECT a.API_ID AS IDS, a.API_SQL,a.API_DESC,a.API_METHOD,case a.API_IS_LOGIN when \'1\' then \'需登录\' else \'不需登录\' end API_IS_LOGIN ,case a.API_STATE when \'0\' then \'禁用\' else \'启用\' end API_STATE,a.API_NAME,a.API_ID as ID,a.API_TABLE,case a.API_TYPE when \'0\' then \'forbidden\' when \'1\' then \'add\' when \'2\' then \'update\' when \'3\' then \'get\' when \'4\' then \'delete\' when \'5\' then \'excute\' when \'6\' then \'query\' when \'7\' then \'page\' when \'8\' then \'executeScript\' end API_TYPE FROM  p_fm_api a   where a.API_ID not in (select API_ID from p_fm_api_privilege_relation where PRI_ID=\'\\&quot;+pid+\\&quot;\')  \\&quot;+where+\\&quot; order by a.API_CREATETIME DESC \\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_api&quot;,&quot;modelItemCodes&quot;:&quot;API_SQL,API_METHOD,API_TYPE,API_IS_LOGIN,API_REMARK,API_CREATETIME,API_ID,API_OUTPUT,API_CREATOR,API_STATE,API_TABLE,API_ISCACHE,API_DESC,API_NAME&quot;}]', '0', null, 'var pid = Comm.getUrlParam(&quot;pid&quot;);\r\nvar action = $(&quot;#groupForm&quot;).attr(&quot;action&quot;) + &quot;?pid=&quot; + pid;\r\n$(&quot;#groupForm&quot;).attr(&quot;action&quot;,action);', '2017-10-18 14:42:01', '2017-12-18 15:34:47', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_api_list?? && p_fm_api_list?size gt 0>\r\n             <#assign p_fm_api = p_fm_api_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head lang=\"en\">\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-table.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/select2/select2.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">  \r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n</head>\r\n<body>\r\n	<!-- Main content -->\r\n  <section class=\"content\">	\r\n		<div class=\"opeBtnGrop\">\r\n\r\n		<button data-valid=\"flase\" id=\'856b9940-cc0a-48d4-9cb9-84a33aabd92f\' title=\"\"\r\n				class=\" btn   btn-primary\"\r\n					<#if (pageActStatus[\'856b9940-cc0a-48d4-9cb9-84a33aabd92f\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'856b9940-cc0a-48d4-9cb9-84a33aabd92f\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'856b9940-cc0a-48d4-9cb9-84a33aabd92f\'][\'hidden\'])?? && pageActStatus[\'856b9940-cc0a-48d4-9cb9-84a33aabd92f\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-save\'></i>\r\n			确定</button>\r\n\r\n\r\n		<button data-valid=\"flase\" id=\'7872a81c-fa98-4c25-9eb9-a1b971faf329\' title=\"\"\r\n				class=\" btn   btn-sback\"\r\n					<#if (pageActStatus[\'7872a81c-fa98-4c25-9eb9-a1b971faf329\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'7872a81c-fa98-4c25-9eb9-a1b971faf329\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'7872a81c-fa98-4c25-9eb9-a1b971faf329\'][\'hidden\'])?? && pageActStatus[\'7872a81c-fa98-4c25-9eb9-a1b971faf329\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			关闭</button>\r\n\r\n\r\n		<button data-valid=\"flase\" id=\'b68250bb-14f8-48f3-b8da-6ef3fdc1f97e\' title=\"\"\r\n				class=\" btn   btn-info\"\r\n					<#if (pageActStatus[\'b68250bb-14f8-48f3-b8da-6ef3fdc1f97e\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'b68250bb-14f8-48f3-b8da-6ef3fdc1f97e\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'b68250bb-14f8-48f3-b8da-6ef3fdc1f97e\'][\'hidden\'])?? && pageActStatus[\'b68250bb-14f8-48f3-b8da-6ef3fdc1f97e\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-search\'></i>\r\n			搜索</button>\r\n\r\n		</div>\r\n		<div class=\"row\">\r\n		<div class=\"col-md-12\">\r\n			<div class=\"box box-info\">\r\n				<div class=\"box-body\">					\r\n						<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n							<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"${p_fm_api_list_paginator.getLimit()}\">\r\n							<input type=\"hidden\" id=\"totalCount\" name=\"totalCount\" value=\"${p_fm_api_list_paginator.getTotalCount()}\">\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"orderBy\" value=\"${ (vo.orderBy)!\"\"}\" id=\"orderBy\"/>\r\n							<input type=\"hidden\" name=\"allowOrderBy\" id=\"allowOrderBy\" value=\"${ (vo.allowOrderBy)!\"\"}\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n		<div class=\"search_main row\">\r\n			<input type=\"hidden\" value=\"\" id=\"selectOption\">\r\n			<input type=\"hidden\" value=\"\" id=\"selectLabel\">\r\n			<input type=\"hidden\" value=\"\" id=\"selectName\">\r\n			<input type=\"hidden\" value=\"ID@名称@描述\" id=\"textLabel\">\r\n			<input type=\"hidden\" value=\"id@name@desc\" id=\"textName\">\r\n			<input type=\"hidden\" value=\"\" id=\"dateSelectLabel\">\r\n			<input type=\"hidden\" value=\"\" id=\"dateSelectName\">\r\n			<input type=\"hidden\" value=\"\" id=\"userSelectLabel\">\r\n			<input type=\"hidden\" value=\"\" id=\"userSelectName\">\r\n		</div>\r\n							<table id=\"bootstrapTable\" class=\"table table-hover\">\r\n								<thead>\r\n									<tr>\r\n										<th class=\"hidden\"></th>\r\n										<th data-width=\"\" data-field=\"\" data-checkbox=\"true\"></th>\r\n										<th data-width=\"50\">序号</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'16862552b05f5bc5aa6756a165dd395b\'][\'hidden\'])?? && status[\'16862552b05f5bc5aa6756a165dd395b\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.IDS\"\r\n		>\r\n			ID\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'4625097cc6cbeb6dfc35473487feec07\'][\'hidden\'])?? && status[\'4625097cc6cbeb6dfc35473487feec07\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.API_NAME\"\r\n		>\r\n			API名称\r\n		\r\n		</th>\r\n		<th \r\n			data-width=\"\"\r\n		<#if (status[\'e74fae5d18cc2bda717a86277d5b53d9\'][\'hidden\'])?? && status[\'e74fae5d18cc2bda717a86277d5b53d9\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>\r\n			data-field=\"p_fm_api.API_DESC\"\r\n		>\r\n			API描述\r\n		\r\n		</th>\r\n									</tr>\r\n								</thead>\r\n								<tbody>\r\n									<#if p_fm_api_list?? && p_fm_api_list?size gte 1>	\r\n										<#list\r\n											p_fm_api_list as p_fm_api>\r\n										<tr>\r\n											<td class=\"hidden formData\">\r\n												<input type=\"hidden\" value=\"${p_fm_api.ID!\'\'}\">\r\n											</td>\r\n											<td></td>\r\n									<td>${(p_fm_api_list_paginator.getPage() * p_fm_api_list_paginator.getLimit() - p_fm_api_list_paginator.getLimit() + p_fm_api_index + 1)}\r\n									</td>\r\n		<td <#if (status[\'16862552b05f5bc5aa6756a165dd395b\'][\'hidden\'])?? && status[\'16862552b05f5bc5aa6756a165dd395b\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.IDS!\'\'}\r\n		</td>\r\n		<td <#if (status[\'4625097cc6cbeb6dfc35473487feec07\'][\'hidden\'])?? && status[\'4625097cc6cbeb6dfc35473487feec07\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.API_NAME!\'\'}\r\n		</td>\r\n		<td <#if (status[\'e74fae5d18cc2bda717a86277d5b53d9\'][\'hidden\'])?? && status[\'e74fae5d18cc2bda717a86277d5b53d9\'][\'hidden\'] == \'true\'>class=\"hidden\"</#if>>\r\n				${p_fm_api.API_DESC!\'\'}\r\n		</td>\r\n										</tr>\r\n										</#list>\r\n									</#if>\r\n								</tbody>\r\n							</table>\r\n						</form>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</section>\r\n	<script src=\"${basePath}template/AdminLTE/js/jquery.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/editTable.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/bootstrap-table.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/select2.full.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js\"></script>\r\n	<script src=\'${basePath}venson/js/common.js\'></script>\r\n	<script src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script src=\"${basePath}resources/scripts/plateform.userSelect.js\"></script>\r\n	<script>\r\n		var basePath = \'${basePath}\';\r\n		var count=0;//默认选择行数为0\r\n	$(document).ready(function(){\r\n		$(document).keyup(function(event) {\r\n	    	if (event.keyCode == 13) {\r\n	    		$(\"#groupForm\").submit();\r\n	    	}\r\n	    });\r\n		 $(\"#bootstrapTable\").bootstrapTable({\r\n        	 pageSize:parseInt($(\"#pageSize\").val()),\r\n        	 pageNumber:parseInt($(\"#currentPage\").val()),\r\n        	 totalRows:parseInt($(\"#totalCount\").val()),\r\n        	 sidePagination:\"server\",\r\n        	 pagination:true,\r\n        	 onPageChange:function(number, size){\r\n        		$(\"#pageSize\").val(size);\r\n        		$(\"#currentPage\").val(number);\r\n        		$(\"#groupForm\").submit();\r\n        	 },\r\n        	 onCheck: function(row,$element){  \r\n				  $element.closest(\"tr\").find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onUncheck:function(row,$element){ \r\n        		 $element.closest(\"tr\").find(\"input[type=\'hidden\']\").removeAttr(\"name\");\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n        	 }, \r\n        	 onCheckAll: function (rows) {\r\n        		 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").attr(\"name\",\"_selects\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n             onUncheckAll: function (rows) {          	 \r\n            	 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").removeAttr(\"name\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n			 onSort:function(name, order){\r\n				 $(\"#allowOrderBy\").val(\"1\");\r\n				 var orderBy =$(\"#orderBy\").val();\r\n				 console.log(orderBy,name)\r\n				 if(orderBy.indexOf(name)!=-1&&orderBy.indexOf(\"desc\")!=-1){\r\n					 $(\"#orderBy\").val(name+\" asc\");\r\n				 }else{\r\n					 $(\"#orderBy\").val(name+\" desc\");\r\n				 }\r\n        		$(\"#groupForm\").submit();\r\n			 }\r\n         });							\r\n	\r\n	new addSearch();\r\n		\r\n$(\"#856b9940-cc0a-48d4-9cb9-84a33aabd92f\").click(function(){\r\n	if($(this).attr(\"data-valid\")==\"true\"){\r\n		if(!Comm.validForm())\r\n			return;\r\n	}\r\n			if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nvar pid = Comm.getUrlParam(\"pid\");\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n   type: \"POST\",\r\n   url: basePath + \"document/excuteOnly.do\",\r\n   data:$(\"#groupForm\").serialize()+\"&pid=\"+pid,\r\n   async : false,\r\n   success: function(data){\r\n		if(data==1){\r\n			alert(\"关联成功\");\r\n			top.dialog({id : window.name}).close();\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n   }\r\n});\r\n});\r\n$(\"#7872a81c-fa98-4c25-9eb9-a1b971faf329\").click(function(){\r\n	if($(this).attr(\"data-valid\")==\"true\"){\r\n		if(!Comm.validForm())\r\n			return;\r\n	}\r\n			top.dialog({id : window.name}).close();\r\n});\r\n$(\"#b68250bb-14f8-48f3-b8da-6ef3fdc1f97e\").click(function(){\r\n	if($(this).attr(\"data-valid\")==\"true\"){\r\n		if(!Comm.validForm())\r\n			return;\r\n	}\r\n			$(\"#groupForm\").submit();\r\n});\r\n        \r\n				var pid = Comm.getUrlParam(\"pid\");\r\nvar action = $(\"#groupForm\").attr(\"action\") + \"?pid=\" + pid;\r\n$(\"#groupForm\").attr(\"action\",action);\r\n		});\r\n	</script>\r\n</body>\r\n</html>', '1', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', null);
INSERT INTO `p_fm_dynamicpage` VALUES ('7', '23', null, '已绑定API列表', null, '1003', '0', null, null, null, null, null, '[{&quot;id&quot;:&quot;0db0d488-e514-ac6d-de1d-83d59a35d2f6&quot;,&quot;name&quot;:&quot;p_fm_api&quot;,&quot;isSingle&quot;:&quot;1&quot;,&quot;isPage&quot;:&quot;1&quot;,&quot;limitCount&quot;:&quot;0&quot;,&quot;description&quot;:&quot;&quot;,&quot;sqlScript&quot;:&quot;var pid=request.getParameter(\\&quot;pid\\&quot;);\\r\\nvar name=request.getParameter(\\&quot;name\\&quot;);\\r\\nvar desc=request.getParameter(\\&quot;desc\\&quot;);\\r\\nvar id=request.getParameter(\\&quot;id\\&quot;);\\r\\nvar where =\\&quot;\\&quot;;\\r\\nif(name!=null &amp;&amp; name!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_NAME LIKE \'%\\&quot;+name+\\&quot;%\'\\&quot;;\\r\\n}\\r\\nif(desc!=null &amp;&amp; desc!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_DESC LIKE \'%\\&quot;+desc+\\&quot;%\'\\&quot;;\\r\\n}\\r\\nif(id!=null &amp;&amp; id!=\\&quot;\\&quot;){\\r\\nwhere += \\&quot; and a.API_ID LIKE \'%\\&quot;+id+\\&quot;%\'\\&quot;;\\r\\n}\\r\\n\\&quot;SELECT a.API_ID AS IDS, a.API_SQL,a.API_DESC,a.API_METHOD,case a.API_IS_LOGIN when \'1\' then \'需登录\' else \'不需登录\' end API_IS_LOGIN ,case a.API_STATE when \'0\' then \'禁用\' else \'启用\' end API_STATE,a.API_NAME,a.API_ID as ID,a.API_TABLE,case a.API_TYPE when \'0\' then \'forbidden\' when \'1\' then \'add\' when \'2\' then \'update\' when \'3\' then \'get\' when \'4\' then \'delete\' when \'5\' then \'excute\' when \'6\' then \'query\' when \'7\' then \'page\' when \'8\' then \'executeScript\' end API_TYPE FROM  p_fm_api a left join p_fm_api_privilege_relation b on a.API_ID=b.API_ID  where b.PRI_ID=\'\\&quot;+pid+\\&quot;\' \\&quot;+where+\\&quot; order by a.API_CREATETIME DESC\\&quot;;&quot;,&quot;deleteSql&quot;:&quot;&quot;,&quot;modelCode&quot;:&quot;p_fm_api&quot;,&quot;modelItemCodes&quot;:&quot;API_SQL,API_METHOD,API_TYPE,API_IS_LOGIN,API_REMARK,API_CREATETIME,API_ID,API_OUTPUT,API_CREATOR,API_STATE,API_TABLE,API_ISCACHE,API_DESC,API_NAME&quot;}]', '0', null, 'var pid = Comm.getUrlParam(&quot;pid&quot;);\r\nvar action = $(&quot;#groupForm&quot;).attr(&quot;action&quot;) + &quot;?pid=&quot; + pid;\r\n$(&quot;#groupForm&quot;).attr(&quot;action&quot;,action);', '2017-10-18 17:36:08', '2017-12-18 15:34:57', null, null, '<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n	<#if p_fm_api_list?? && p_fm_api_list?size gt 0>\r\n             <#assign p_fm_api = p_fm_api_list[0]!\'\'>\r\n        </#if>\r\n<!DOCTYPE html>\r\n<html>\r\n<head lang=\"en\">\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-table.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n</head>\r\n<body>\r\n	<!-- Main content -->\r\n  <section class=\"content\">	\r\n		<div class=\"opeBtnGrop\">\r\n\r\n		<button id=\'e52aa25e-b423-4c47-8e15-5d7c558b9a40\' title=\"\"\r\n				class=\" btn   btn-primary\r\n\r\n				\"\r\n					<#if (pageActStatus[\'e52aa25e-b423-4c47-8e15-5d7c558b9a40\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'e52aa25e-b423-4c47-8e15-5d7c558b9a40\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'e52aa25e-b423-4c47-8e15-5d7c558b9a40\'][\'hidden\'])?? && pageActStatus[\'e52aa25e-b423-4c47-8e15-5d7c558b9a40\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-plus\'></i>\r\n			新增关联API</button>\r\n\r\n\r\n		<button id=\'602835ef-4703-4705-af53-11a529f61ff2\' title=\"\"\r\n				class=\" btn   btn-warning\r\n\r\n				\"\r\n					<#if (pageActStatus[\'602835ef-4703-4705-af53-11a529f61ff2\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'602835ef-4703-4705-af53-11a529f61ff2\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'602835ef-4703-4705-af53-11a529f61ff2\'][\'hidden\'])?? && pageActStatus[\'602835ef-4703-4705-af53-11a529f61ff2\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-minus\'></i>\r\n			取消关联</button>\r\n\r\n\r\n		<button id=\'1fc34482-8178-41f3-a44e-4bf571ed866d\' title=\"\"\r\n				class=\" btn   btn-default\r\n\r\n				\"\r\n					<#if (pageActStatus[\'1fc34482-8178-41f3-a44e-4bf571ed866d\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'1fc34482-8178-41f3-a44e-4bf571ed866d\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'1fc34482-8178-41f3-a44e-4bf571ed866d\'][\'hidden\'])?? && pageActStatus[\'1fc34482-8178-41f3-a44e-4bf571ed866d\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-reply\'></i>\r\n			返回</button>\r\n\r\n\r\n		<button id=\'a8d978b4-4bf6-4fe9-91c5-4414a9b42003\' title=\"\"\r\n				class=\" btn   btn-success\r\n\r\n				\"\r\n					<#if (pageActStatus[\'a8d978b4-4bf6-4fe9-91c5-4414a9b42003\'][\'backId\'])??>\r\n						backId=\"${pageActStatus[\'a8d978b4-4bf6-4fe9-91c5-4414a9b42003\'][\'backId\']!\'\'}\"\r\n					</#if>\r\n				target=\'\'\r\n					<#if (pageActStatus[\'a8d978b4-4bf6-4fe9-91c5-4414a9b42003\'][\'hidden\'])?? && pageActStatus[\'a8d978b4-4bf6-4fe9-91c5-4414a9b42003\'][\'hidden\']>\r\n						style=\"display:none;\"\r\n					</#if>\r\n			 >\r\n			<i class=\'icon-search\'></i>\r\n			搜索</button>\r\n\r\n		\r\n		</div>\r\n		<div class=\"row\">\r\n		<div class=\"col-md-12\">\r\n			<div class=\"box box-info\">\r\n				<div class=\"box-body\">\r\n					\r\n						<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n							<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"${p_fm_api_list_paginator.getLimit()}\">\r\n							<input type=\"hidden\" id=\"totalCount\" name=\"totalCount\" value=\"${p_fm_api_list_paginator.getTotalCount()}\">\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n							<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n							<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n							<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n		<div class=\"search_main\">\r\n			<input type=\"hidden\" value=\"\r\n								\" id=\"selectOption\">\r\n			<input type=\"hidden\" value=\"\" id=\"selectLabel\">\r\n			<input type=\"hidden\" value=\"\" id=\"selectName\">\r\n			<input type=\"hidden\" value=\"ID@API名称@API描述\" id=\"textLabel\">\r\n			<input type=\"hidden\" value=\"id@name@desc\" id=\"textName\">\r\n		</div>\r\n		<!--\r\n			<input type=\"submit\" value=\"搜索\" class=\"btn btn-primary\">  \r\n			<input type=\"reset\" value=\"清空\" class=\"btn btn-primary\">\r\n		-->\r\n							<table id=\"bootstrapTable\" class=\"table table-hover\">\r\n								<thead>\r\n									<tr>\r\n										<th class=\"hidden\"></th>\r\n										<th data-width=\"\" data-field=\"\" data-checkbox=\"true\"></th>\r\n										<th data-width=\"50\">序号</th>\r\n		<th 	data-width=\"5%\"\r\n		class=\"<#if (status[\'99e176ba5274020f52c40db6a2c51329\'][\'hidden\'])?? && status[\'99e176ba5274020f52c40db6a2c51329\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"ID\">\r\n		ID\r\n		\r\n		</th>\r\n		<th 	data-width=\"15%\"\r\n		class=\"<#if (status[\'cff681bf5f3e3b1622e7d7235b1db597\'][\'hidden\'])?? && status[\'cff681bf5f3e3b1622e7d7235b1db597\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"API名称\">\r\n		API名称\r\n		\r\n		</th>\r\n		<th 	data-width=\"5%\"\r\n		class=\"<#if (status[\'eaa021ec7f5a4a28eefa671bcf91da61\'][\'hidden\'])?? && status[\'eaa021ec7f5a4a28eefa671bcf91da61\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"API状态\">\r\n		API状态\r\n		\r\n		</th>\r\n		<th 	data-width=\"10%\"\r\n		class=\"<#if (status[\'1eba1a9bf4f96ce448352f10b6f652d0\'][\'hidden\'])?? && status[\'1eba1a9bf4f96ce448352f10b6f652d0\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"类型\">\r\n		类型\r\n		\r\n		</th>\r\n		<th 	data-width=\"7%\"\r\n		class=\"<#if (status[\'30bed74225cca93529f7bff89d4a0d84\'][\'hidden\'])?? && status[\'30bed74225cca93529f7bff89d4a0d84\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"请求方式\">\r\n		请求方式\r\n		\r\n		</th>\r\n		<th 	data-width=\"8%\"\r\n		class=\"<#if (status[\'7e73d5d1c8a30a870aab5ddc6732b7ee\'][\'hidden\'])?? && status[\'7e73d5d1c8a30a870aab5ddc6732b7ee\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"是否登录\">\r\n		是否登录\r\n		\r\n		</th>\r\n		<th 	data-width=\"\"\r\n		class=\"<#if (status[\'a0499334fb80971f4cc27ed3c9d73f68\'][\'hidden\'])?? && status[\'a0499334fb80971f4cc27ed3c9d73f68\'][\'hidden\'] == \'true\'>hidden</#if>  text-ellipsis\"  title=\"描述\">\r\n		描述\r\n		\r\n		</th>\r\n									</tr>\r\n								</thead>\r\n								<tbody>\r\n									<#if p_fm_api_list?? && p_fm_api_list?size gte 1>	\r\n										<#list\r\n											p_fm_api_list as p_fm_api>\r\n										<tr>\r\n											<td class=\"hidden formData\">\r\n												<input type=\"hidden\" value=\"${p_fm_api.ID!\'\'}\">\r\n											</td>\r\n											<td></td>\r\n									<td>${(p_fm_api_list_paginator.getPage() * p_fm_api_list_paginator.getLimit() - p_fm_api_list_paginator.getLimit() + p_fm_api_index + 1)}\r\n									</td>\r\n		<td class=\"<#if (status[\'99e176ba5274020f52c40db6a2c51329\'][\'hidden\'])?? && status[\'99e176ba5274020f52c40db6a2c51329\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api.API_ID!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'cff681bf5f3e3b1622e7d7235b1db597\'][\'hidden\'])?? && status[\'cff681bf5f3e3b1622e7d7235b1db597\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api.API_NAME!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'eaa021ec7f5a4a28eefa671bcf91da61\'][\'hidden\'])?? && status[\'eaa021ec7f5a4a28eefa671bcf91da61\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api.API_STATE!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'1eba1a9bf4f96ce448352f10b6f652d0\'][\'hidden\'])?? && status[\'1eba1a9bf4f96ce448352f10b6f652d0\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api.API_TYPE!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'30bed74225cca93529f7bff89d4a0d84\'][\'hidden\'])?? && status[\'30bed74225cca93529f7bff89d4a0d84\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api.API_METHOD!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'7e73d5d1c8a30a870aab5ddc6732b7ee\'][\'hidden\'])?? && status[\'7e73d5d1c8a30a870aab5ddc6732b7ee\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api.API_IS_LOGIN!\'\'}\r\n		</td>\r\n		<td class=\"<#if (status[\'a0499334fb80971f4cc27ed3c9d73f68\'][\'hidden\'])?? && status[\'a0499334fb80971f4cc27ed3c9d73f68\'][\'hidden\'] == \'true\'>hidden</#if>\">\r\n				${p_fm_api.API_DESC!\'\'}\r\n		</td>\r\n										</tr>\r\n										</#list>\r\n									</#if>\r\n								</tbody>\r\n							</table>\r\n						</form>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</section>\r\n	<script src=\"${basePath}template/AdminLTE/js/jquery.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/editTable.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/bootstrap-table.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js\"></script>\r\n	<script type=\"text/javascript\" src=\'${basePath}venson/js/common.js\'></script>\r\n	<script type=\"text/javascript\" src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script type=\"text/javascript\">\r\n		var basePath = \'${basePath}\';\r\n		var count=0;//默认选择行数为0\r\n	$(document).ready(function(){\r\n		\r\n		 $(\"#bootstrapTable\").bootstrapTable({\r\n        	 pageSize:parseInt($(\"#pageSize\").val()),\r\n        	 pageNumber:parseInt($(\"#currentPage\").val()),\r\n        	 totalRows:parseInt($(\"#totalCount\").val()),\r\n        	 sidePagination:\"server\",\r\n        	 pagination:true,\r\n        	 onPageChange:function(number, size){\r\n        		$(\"#pageSize\").val(size);\r\n        		$(\"#currentPage\").val(number);\r\n        		$(\"#groupForm\").submit();\r\n        	 },\r\n        	 onCheck: function(row,$element){  \r\n				  $element.closest(\"tr\").find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onUncheck:function(row,$element){ \r\n        		 $element.closest(\"tr\").find(\"input[type=\'hidden\']\").removeAttr(\"name\");\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n        	 }, \r\n        	 onCheckAll: function (rows) {\r\n        		 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").attr(\"name\",\"_selects\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n             onUncheckAll: function (rows) {          	 \r\n            	 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").removeAttr(\"name\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             }\r\n         });							\r\n	\r\n	new addSearch();\r\n		\r\n$(\"#e52aa25e-b423-4c47-8e15-5d7c558b9a40\").click(function(){\r\n					var pid = Comm.getUrlParam(\"pid\");\r\ntop.dialog({\r\n	id : \'add-dialog\' + Math.ceil(Math.random() * 10000),\r\n	title : \'未关联的接口\',\r\n	url : basePath+\"document/view.do?dynamicPageId=6&pid=\" + pid,\r\n	height : 900,\r\n	width : 1200,\r\n	onclose : function() {\r\n		location.href = basePath + \"document/view.do?dynamicPageId=7&pid=\" + pid;\r\n	}\r\n}).showModal();\r\n});\r\n$(\"#602835ef-4703-4705-af53-11a529f61ff2\").click(function(){\r\n					if(count==0){\r\n	alert(\"请至少选择一项进行操作\");\r\n	return false;\r\n}\r\nvar pid = Comm.getUrlParam(\"pid\");\r\nvar actId = $(this).attr(\"id\");\r\n$(\"#actId\").val(actId);	\r\n$.ajax({\r\n    type: \"POST\",\r\n    url: basePath + \"document/excuteOnly.do\",\r\n    data:$(\"#groupForm\").serialize()+\"&pid=\"+pid,\r\n    async : false,\r\n    success: function(data){\r\n		if(data==1){\r\n			alert(\"取消关联成功\");\r\n			location.href = basePath + \"document/view.do?dynamicPageId=7&pid=\"+pid;\r\n		}\r\n		else{\r\n			alert(data);\r\n		}\r\n    }\r\n});\r\n});\r\n$(\"#1fc34482-8178-41f3-a44e-4bf571ed866d\").click(function(){\r\n					location.href = basePath + \"document/view.do?dynamicPageId=20\";\r\n});\r\n$(\"#a8d978b4-4bf6-4fe9-91c5-4414a9b42003\").click(function(){\r\n					$(\"#groupForm\").submit();\r\n});\r\n        \r\n				var pid = Comm.getUrlParam(\"pid\");\r\nvar action = $(\"#groupForm\").attr(\"action\") + \"?pid=\" + pid;\r\n$(\"#groupForm\").attr(\"action\",action);\r\n		});\r\n	</script>\r\n</body>\r\n</html>', '1', '110', null, null, null, null, null, null, '1', '9999', '9999', '9999', '0', '0', null, '0', null, '0', null);

-- ----------------------------
-- Table structure for p_fm_flow
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_flow`;
CREATE TABLE `p_fm_flow` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAGE_ID` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '页面ID',
  `JSON` text COLLATE utf8_bin COMMENT '选择人员',
  `FLOW_ID` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '流程编号',
  `CREATOR` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `REMARK` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PAGE_ID` (`PAGE_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of p_fm_flow
-- ----------------------------
INSERT INTO `p_fm_flow` VALUES ('34', '2062', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('35', '2063', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('36', '2064', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('37', '2065', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('38', '2066', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('39', '2067', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('40', '2068', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('41', '2069', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('42', '2070', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('43', '2071', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('44', '2072', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('45', '2073', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('46', '2074', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('47', '2075', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('48', '2076', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('49', '2077', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('50', '2078', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('51', '2079', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('52', '2080', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);
INSERT INTO `p_fm_flow` VALUES ('53', '2081', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '005', null, '2017-08-24 20:42:23', null);

-- ----------------------------
-- Table structure for p_fm_logs
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_logs`;
CREATE TABLE `p_fm_logs` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTENT` text COLLATE utf8mb4_bin COMMENT '内容',
  `REMINDER` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '提醒谁看',
  `WORK_ID` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程实例编号',
  `PAGE_ID` int(11) DEFAULT NULL COMMENT '页面ID',
  `RECORD_ID` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录ID',
  `CREATOR` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `SEND_TIME` datetime DEFAULT NULL COMMENT '发送时间',
  `SEQ` int(11) DEFAULT NULL COMMENT '排序',
  `REMARK` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of p_fm_logs
-- ----------------------------

-- ----------------------------
-- Table structure for p_fm_modular
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_modular`;
CREATE TABLE `p_fm_modular` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `modularName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_modular
-- ----------------------------
INSERT INTO `p_fm_modular` VALUES ('11', '系统管理');
INSERT INTO `p_fm_modular` VALUES ('23', 'api模块');

-- ----------------------------
-- Table structure for p_fm_store
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_store`;
CREATE TABLE `p_fm_store` (
  `ID` varchar(36) NOT NULL COMMENT '主键',
  `CODE` varchar(255) DEFAULT NULL COMMENT '编码',
  `NAME` varchar(100) DEFAULT NULL COMMENT '名称',
  `CONTENT` text COMMENT '内容',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '备注',
  `DATA_STATUS` varchar(2) DEFAULT NULL COMMENT '状态',
  `SYSTEM_ID` bigint(20) DEFAULT NULL COMMENT '所属系统ID',
  `DYNAMICPAGE_ID` char(36) DEFAULT NULL COMMENT '所属页面ID',
  `T_ORDER` int(11) DEFAULT NULL COMMENT '排序字段',
  `BTN_GROUP` int(11) DEFAULT NULL COMMENT '按钮组',
  `IS_CHECKOUT` int(11) DEFAULT NULL,
  `CHECKOUT_USER` varchar(250) DEFAULT NULL,
  `CREATED_USER` varchar(250) DEFAULT NULL,
  `UPDATED_USER` varchar(250) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_store
-- ----------------------------
INSERT INTO `p_fm_store` VALUES ('0087c98c-d89e-4c4e-a4c8-368a7650cd37', '0.1.7.1500013953287', 'f5f70ae163a7e5a779a58ec98b28fc42', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"包含的页面\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_modular.pages\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"4\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"f5f70ae163a7e5a779a58ec98b28fc42\",\"order\":2,\"orderby\":\"1\",\"pageId\":\"0087c98c-d89e-4c4e-a4c8-368a7650cd37\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"80%\"}', null, null, '110', '4', '2', null, '1', '9999', '9999', '9999', '2017-07-14 14:32:33', '2017-07-14 14:32:33', '1');
INSERT INTO `p_fm_store` VALUES ('02269a26-e7a2-47c9-8f95-141af1d2b649', '0.1.7.1508320056784', 'a0499334fb80971f4cc27ed3c9d73f68', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":7,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_DESC\",\"excelShowScript\":\"\",\"pageId\":\"02269a26-e7a2-47c9-8f95-141af1d2b649\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"a0499334fb80971f4cc27ed3c9d73f68\",\"width\":\"\",\"dynamicPageId\":\"7\",\"validateErrorTip\":\"\",\"columnName\":\"描述\"}', null, null, '110', '7', '7', null, '1', '9999', '9999', '9999', '2017-10-18 17:46:58', '2017-10-18 17:46:58', null);
INSERT INTO `p_fm_store` VALUES ('02940dbe-7c48-4fc8-97f1-b65cb7204326', '0.1.5.1504942655055', '禁用', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar pid=Comm.getUrlParam(&quot;pid&quot;);\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;禁用成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=18&amp;pid=&quot;+pid;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"code\":\"0.1.5.1504942655055\",\"color\":\"warning\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":18,\"dynamicPageName\":\"系统属性字典配置列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-off\",\"name\":\"禁用\",\"order\":4,\"pageId\":\"02940dbe-7c48-4fc8-97f1-b65cb7204326\",\"serverScript\":\"var retVal = &quot;系统错误&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nvar a=DocumentUtils.excuteUpdate(&quot;UPDATE P_DATA_DICTIONARY SET DICT_STATUS=\'0\' WHERE ID=\'&quot;+ids[i]+&quot;\'&quot;);\\r\\nif(a&gt;0){\\r\\nretVal=&quot;1&quot;;\\r\\n}\\r\\n}\\r\\nretVal;\",\"systemId\":110}', null, null, '110', '18', '4', '1', '1', '9999', '9999', '9999', '2017-09-09 15:37:35', '2017-09-09 15:37:35', null);
INSERT INTO `p_fm_store` VALUES ('03fa3296-7ec6-4d05-b464-2b8c44e9adbc', '0.1.8.1513599001601', '_row1_cod1', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row1_cod1\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"03fa3296-7ec6-4d05-b464-2b8c44e9adbc\",\"parentId\":\"c168bf66-ea0e-457f-b56d-18708e52ac4a\",\"order\":1}', 'c168bf66-ea0e-457f-b56d-18708e52ac4a', null, '110', '17', '1', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('04d89536-5109-49b3-8f86-4d2033baa848', '0.1.7.1508322878636', 'e74fae5d18cc2bda717a86277d5b53d9', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":3,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_DESC\",\"excelShowScript\":\"\",\"pageId\":\"04d89536-5109-49b3-8f86-4d2033baa848\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"e74fae5d18cc2bda717a86277d5b53d9\",\"width\":\"\",\"dynamicPageId\":\"6\",\"validateErrorTip\":\"\",\"columnName\":\"API描述\"}', null, null, '110', '6', '3', null, '1', '9999', '9999', '9999', '2017-10-18 18:34:39', '2017-10-18 18:34:39', null);
INSERT INTO `p_fm_store` VALUES ('0696aa15-8ae8-40d6-bcc3-fed4e0de0b49', '0.1.7.1505822417635', 'e05dad078fb4d6f8228a08f9f372e46c', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"权限名称\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api_privilege.NAME\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"20\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"e05dad078fb4d6f8228a08f9f372e46c\",\"order\":1,\"orderby\":\"1\",\"pageId\":\"0696aa15-8ae8-40d6-bcc3-fed4e0de0b49\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"\"}', null, null, '110', '20', '1', null, '1', '9999', '9999', '9999', '2017-09-19 20:00:18', '2017-09-19 20:00:18', null);
INSERT INTO `p_fm_store` VALUES ('07401641-b6b5-441d-a3e1-d80d90072517', '0.1.8.1513599001717', '_row1_cod3', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row1_cod3\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"07401641-b6b5-441d-a3e1-d80d90072517\",\"parentId\":\"c168bf66-ea0e-457f-b56d-18708e52ac4a\",\"order\":3}', 'c168bf66-ea0e-457f-b56d-18708e52ac4a', null, '110', '17', '3', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('083bc58d-6490-49de-af15-c1dbff097fd5', '0.1.5.1504942624398', '新增', '{\"serverScript\":\"\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1504942624398\",\"color\":\"primary\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-plus\",\"description\":\"\",\"pageId\":\"083bc58d-6490-49de-af15-c1dbff097fd5\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"系统属性字典配置列表\",\"enName\":\"\",\"name\":\"新增\",\"dynamicPageId\":\"18\",\"actType\":2000,\"clientScript\":\"if(count==1){\\r\\nvar pid = $(&quot;input[name=\'_selects\']&quot;).val();\\r\\nlocation.href = basePath + &quot;document/view.do?dynamicPageId=17&amp;pid=&quot;+pid;//如果选中某行点新增，就直接添加这行下属项\\r\\n}else{\\r\\nvar pid=Comm.getUrlParam(&quot;pid&quot;);\\r\\nif(pid==null){\\r\\npid=&quot;&quot;;\\r\\n}\\r\\nlocation.href = basePath + &quot;document/view.do?dynamicPageId=17&amp;pid=&quot;+pid;\\r\\n}\",\"chooseValidate\":false,\"order\":1}', null, null, '110', '18', '1', '1', '1', '9999', '9999', '9999', '2017-09-09 15:37:04', '2017-09-09 15:37:04', null);
INSERT INTO `p_fm_store` VALUES ('0a50bfa7-fe7f-44c1-a422-7adbb36db292', '0.1.7.1513590803733', 'f6d3a972c1a277dd3ad98af4567b0f83', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"是否必须\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"layoutName\":\"_row2_cod1\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":21,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"0=否;1=是;\\\";\",\"dataItemCode\":\"p_fm_api_describe.ISNECESSARY\",\"pageId\":\"0a50bfa7-fe7f-44c1-a422-7adbb36db292\",\"layoutId\":\"4f1c6a3d-943f-4960-aa3a-18e9b7cff2d5\",\"defaultValueScript\":\"\\\"1\\\";\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"f6d3a972c1a277dd3ad98af4567b0f83\",\"dynamicPageId\":\"3\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '3', '21', null, '1', '9999', '9999', '9999', '2017-05-02 16:40:58', '2017-05-02 16:40:58', '1');
INSERT INTO `p_fm_store` VALUES ('0a6a2f72-5ed5-4136-ade0-80b4816918bf', '0.1.8.1493717849993', '_row3_cod2', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"1\",\"left\":\"\",\"name\":\"_row3_cod2\",\"order\":6,\"pageId\":\"0a6a2f72-5ed5-4136-ade0-80b4816918bf\",\"parentId\":\"0f99411b-ea2b-4c21-ba77-c56b90da8c4b\",\"proportion\":\"2\",\"systemId\":111,\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', '0f99411b-ea2b-4c21-ba77-c56b90da8c4b', null, '111', '2020', '6', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('0ae1284a-91ec-40ce-929d-26b99981fe15', '0.1.8.1513591081262', '_row3_cod1', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row3_cod1\",\"dynamicPageId\":\"19\",\"layoutType\":1,\"pageId\":\"0ae1284a-91ec-40ce-929d-26b99981fe15\",\"parentId\":\"aea3d763-3d8f-429a-8fdc-cc138176b7db\",\"order\":3}', 'aea3d763-3d8f-429a-8fdc-cc138176b7db', null, '110', '19', '3', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('0e68706c-4700-4767-ad34-41a17b9ee052', '0.1.5.1490520605653', '返回', '{\"actType\":2002,\"buttonGroup\":1,\"buttons\":\"\",\"chooseValidate\":false,\"clientScript\":\"history.back(-1);\",\"code\":\"0.1.5.1490520605653\",\"color\":\"warning\",\"confirm\":false,\"content\":\"\",\"contentType\":3,\"description\":\"\",\"dynamicPageId\":2,\"dynamicPageName\":\"API管理表单\",\"enName\":\"\",\"extbute\":{\"target\":\"1\"},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-reply\",\"name\":\"返回\",\"order\":2,\"pageId\":\"0e68706c-4700-4767-ad34-41a17b9ee052\",\"serverScript\":\"\",\"systemId\":110,\"tittle\":\"\"}', null, null, '110', '2', '2', '1', '1', '9999', '9999', '9999', '2017-03-26 17:30:05', '2017-03-26 17:30:05', '1');
INSERT INTO `p_fm_store` VALUES ('0f99411b-ea2b-4c21-ba77-c56b90da8c4b', '0.1.8.1493714255139', '_row3', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":2,\"left\":0,\"name\":\"_row3\",\"order\":3,\"pageId\":\"0f99411b-ea2b-4c21-ba77-c56b90da8c4b\",\"parentId\":\"\",\"proportion\":12,\"systemId\":111,\"top\":0}', null, null, '111', '2020', '3', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:05', '2017-05-03 11:07:05', '1');
INSERT INTO `p_fm_store` VALUES ('1016bb26-aa38-4569-b85a-cca69c1a0280', '0.1.8.1513578936531', '_row2_cod4', '{\"proportion\":3,\"top\":0,\"left\":0,\"name\":\"_row2_cod4\",\"dynamicPageId\":\"2\",\"layoutType\":1,\"pageId\":\"1016bb26-aa38-4569-b85a-cca69c1a0280\",\"parentId\":\"74203716-54fc-4964-b484-cfd09d8e32ef\",\"order\":8}', '74203716-54fc-4964-b484-cfd09d8e32ef', null, '110', '2', '8', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('117e77cc-22d0-4084-8ede-56127c807440', '0.1.5.1505824130381', '关联接口', '{\"serverScript\":\"\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1505824130381\",\"color\":\"info\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-tasks\",\"description\":\"\",\"pageId\":\"117e77cc-22d0-4084-8ede-56127c807440\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"API权限编辑控制列表\",\"enName\":\"\",\"name\":\"关联接口\",\"dynamicPageId\":20,\"actType\":2000,\"clientScript\":\"if(count!=1){\\r\\n\\talert(&quot;请选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nlocation.href = basePath+&quot;document/view.do?dynamicPageId=7&amp;pid=&quot; + $(&quot;input[name=\'_selects\']&quot;).val();\",\"chooseValidate\":false,\"order\":4}', null, null, '110', '20', '4', '1', '1', '9999', '9999', '9999', '2017-09-19 20:28:50', '2017-09-19 20:28:50', null);
INSERT INTO `p_fm_store` VALUES ('133db9dd-fcba-4c88-ad74-b18b803c1e55', '0.1.7.1513599156751', '11eda505319a6e269f3c4067f21c5f40', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"父级名称\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"layoutName\":\"_row1_cod3\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":13,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"var id=request.getParameter(\\\"id\\\");\\r\\nvar pid=request.getParameter(\\\"pid\\\");\\r\\nif(id!=null && id!=\\\"\\\"){\\r\\n\\tpid=DocumentUtils.queryObjectById(\\\"p_data_dictionary\\\",\\\"PID\\\",\\\"ID\\\",id);\\r\\n\\tvar pid2=DocumentUtils.queryObjectById(\\\"p_data_dictionary\\\",\\\"PID\\\",\\\"ID\\\",pid);\\r\\n\\tvar sql = \\\"\\\";\\r\\n\\tif(pid2!=null && pid2!= \\\"\\\"){\\r\\n\\t\\tsql = \\\"select * from p_data_dictionary where PID=\'\\\"+pid2+\\\"\'\\\";\\r\\n\\t}else{\\r\\n\\t\\tsql = \\\"select * from p_data_dictionary where PID is null or PID=\'\' \\\";\\r\\n\\t}\\r\\n\\tvar  retList = DocumentUtils.excuteQueryForList(sql);\\r\\n\\tvar options=\\\"\\\";\\r\\n\\tfor(var i=0;i<retList.size();i++){\\r\\n\\t\\tvar recMap = retList.get(i);\\r\\n\\t\\tvar keyStr = recMap.get(\\\"ID\\\");\\r\\n\\t\\t\\tvar valueStr = recMap.get(\\\"DATA_KEY\\\");\\r\\n\\t\\tvar appStr = keyStr+\\\"=\\\"+valueStr+\\\";\\\";\\r\\n\\t\\toptions+=appStr;\\r\\n\\t}\\r\\n\\toptions;\\r\\n}else if(pid!=null && pid!=\\\"\\\"){\\r\\n\\tvar pid2=DocumentUtils.queryObjectById(\\\"p_data_dictionary\\\",\\\"PID\\\",\\\"ID\\\",pid);\\r\\n\\tvar sql = \\\"\\\";\\r\\n\\tif(pid2!=null && pid2!= \\\"\\\"){\\r\\n\\t\\tsql = \\\"select * from p_data_dictionary where PID=\'\\\"+pid2+\\\"\'\\\";\\r\\n\\t}else{\\r\\n\\t\\tsql = \\\"select * from p_data_dictionary where PID is null or PID=\'\' \\\";\\r\\n\\t}\\r\\n\\tvar  retList = DocumentUtils.excuteQueryForList(sql);\\r\\n\\tvar options=\\\"\\\";\\r\\n\\tfor(var i=0;i<retList.size();i++){\\r\\n\\t\\tvar recMap = retList.get(i);\\r\\n\\t\\tvar keyStr = recMap.get(\\\"ID\\\");\\r\\n\\t\\t\\tvar valueStr = recMap.get(\\\"DATA_KEY\\\");\\r\\n\\t\\tvar appStr = keyStr+\\\"=\\\"+valueStr+\\\";\\\";\\r\\n\\t\\toptions+=appStr;\\r\\n\\t}\\r\\n\\toptions;\\r\\n}\",\"dataItemCode\":\"p_data_dictionary.PID\",\"pageId\":\"133db9dd-fcba-4c88-ad74-b18b803c1e55\",\"layoutId\":\"07401641-b6b5-441d-a3e1-d80d90072517\",\"defaultValueScript\":\"request.getParameter(\\\"pid\\\");\",\"enTitle\":\"\",\"validateAllowNull\":\"1\",\"name\":\"11eda505319a6e269f3c4067f21c5f40\",\"dynamicPageId\":\"17\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '17', '13', null, '1', '9999', '9999', '9999', '2017-09-09 16:15:23', '2017-09-09 16:15:23', null);
INSERT INTO `p_fm_store` VALUES ('14373470-45a0-4106-8f5d-19d6d8bb8fd6', '0.1.8.1513590678388', '_row3_cod1', '{\"proportion\":\"12\",\"textstyle\":\"\",\"pageId\":\"14373470-45a0-4106-8f5d-19d6d8bb8fd6\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"b3cb4562-dca3-4cba-bec1-bf6ded4c522f\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row3_cod1\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"3\",\"layoutType\":\"1\",\"order\":3,\"height\":\"\"}', 'b3cb4562-dca3-4cba-bec1-bf6ded4c522f', null, '110', '3', '3', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:05', '2017-12-18 17:51:05', null);
INSERT INTO `p_fm_store` VALUES ('145e3aff-cf5f-45ca-b4f3-7948a9ecfdd4', '0.1.8.1513578936357', '_row2_cod1', '{\"proportion\":3,\"top\":0,\"left\":0,\"name\":\"_row2_cod1\",\"dynamicPageId\":\"2\",\"layoutType\":1,\"pageId\":\"145e3aff-cf5f-45ca-b4f3-7948a9ecfdd4\",\"parentId\":\"74203716-54fc-4964-b484-cfd09d8e32ef\",\"order\":2}', '74203716-54fc-4964-b484-cfd09d8e32ef', null, '110', '2', '2', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:36', '2017-12-18 14:35:36', null);
INSERT INTO `p_fm_store` VALUES ('16520285-2ab0-42ad-9e3a-8e037efdd07f', '0.1.8.1493714255072', '_row2_cod2', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":1,\"left\":0,\"name\":\"_row2_cod2\",\"order\":4,\"pageId\":\"16520285-2ab0-42ad-9e3a-8e037efdd07f\",\"parentId\":\"499d4e8a-b956-4e31-a60b-a2b1a3609340\",\"proportion\":4,\"systemId\":111,\"top\":0}', '499d4e8a-b956-4e31-a60b-a2b1a3609340', null, '111', '2020', '4', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:05', '2017-05-03 11:07:05', '1');
INSERT INTO `p_fm_store` VALUES ('16b61bec-b11a-40db-8e50-bd953b642c4a', '0.1.7.1509418709610', '69988767ca27a6854dedc3aaf4d530af', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":6,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_data_dictionary.DICT_REMARK\",\"excelShowScript\":\"\",\"pageId\":\"16b61bec-b11a-40db-8e50-bd953b642c4a\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"69988767ca27a6854dedc3aaf4d530af\",\"width\":\"\",\"dynamicPageId\":\"18\",\"validateErrorTip\":\"\",\"columnName\":\"描述\"}', null, null, '110', '18', '6', null, '1', '9999', '9999', '9999', '2017-09-09 15:36:07', '2017-09-09 15:36:07', null);
INSERT INTO `p_fm_store` VALUES ('170aa29f-6722-4422-b023-9a8ec24a7afe', '0.1.5.1505823517610', '删除', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nif(!confirm(&quot;是否确认删除?&quot;)){\\r\\n\\treturn false;\\r\\n}\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;删除成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=20&amp;edit=true&quot;;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"code\":\"0.1.5.1505823517610\",\"color\":\"danger\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":20,\"dynamicPageName\":\"API权限编辑控制列表兼弹窗\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-trash\",\"name\":\"删除\",\"order\":3,\"pageId\":\"170aa29f-6722-4422-b023-9a8ec24a7afe\",\"serverScript\":\"var retVal = &quot;系统错误&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nvar a=DocumentUtils.excuteQuery(&quot;select count(id) as a from p_fm_api_privilege_relation where PRI_ID=\'&quot;+ids[i]+&quot;\'&quot;);\\r\\nif(a.get(&quot;a&quot;)==0){\\r\\n\\tvar sql = DocumentUtils.excuteUpdate(&quot;delete from p_fm_api_privilege where ID=\'&quot;+ids[i]+&quot;\'&quot;);\\r\\n\\tif(sql==1){\\r\\n\\t\\tretVal=&quot;1&quot;;\\r\\n\\t}\\r\\n}else{\\r\\nretVal=&quot;请确定该限制条件没有与任何API关联！&quot;;\\r\\n}\\r\\n}\\r\\nretVal;\",\"systemId\":110}', null, null, '110', '20', '3', '1', '1', '9999', '9999', '9999', '2017-09-19 20:18:38', '2017-09-19 20:18:38', null);
INSERT INTO `p_fm_store` VALUES ('18d4aa64-44f3-484d-abe8-41ecee7c638d', '0.1.8.1493714255261', '_row4_cod1', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":1,\"left\":0,\"name\":\"_row4_cod1\",\"order\":4,\"pageId\":\"18d4aa64-44f3-484d-abe8-41ecee7c638d\",\"parentId\":\"5c00e397-369a-4143-af12-d2c73b93f383\",\"proportion\":2,\"systemId\":111,\"top\":0}', '5c00e397-369a-4143-af12-d2c73b93f383', null, '111', '2020', '4', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('19e26d43-f77a-4861-8d31-05f97c133d7b', '0.1.7.1493714458578', 'f6d3a972c1a277dd3ad98af4567b0f83', '{\"componentType\":\"1006\",\"css\":\"\",\"dataItemCode\":\"p_fm_api_describe.ISNECESSARY\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"hiddenScript\":\"\",\"layoutId\":\"0a6a2f72-5ed5-4136-ade0-80b4816918bf\",\"layoutName\":\"_row3_cod2\",\"name\":\"f6d3a972c1a277dd3ad98af4567b0f83\",\"onchangeScript\":\"\",\"optionScript\":\"\\\"0=否;1=是;\\\";\",\"order\":8,\"pageId\":\"19e26d43-f77a-4861-8d31-05f97c133d7b\",\"readonlyScript\":\"\",\"selectNumber\":\"\",\"style\":\"\",\"systemId\":111,\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\"}', null, null, '111', '2020', '8', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('1a619d8b-456d-4e8b-bb3e-0646acebc6ef', '0.1.5.1505872451598', '取消关联', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar priid=Comm.getUrlParam(&quot;priid&quot;);\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize()+&quot;&amp;priid=&quot;+priid,\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;取消关联成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=1&amp;pri=true&amp;priid=&quot;+priid;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"code\":\"0.1.5.1505872451598\",\"color\":\"danger\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"var a=request.getParameter(&quot;pri&quot;);\\r\\nif(a!=\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-off\",\"name\":\"取消关联\",\"order\":7,\"pageId\":\"1a619d8b-456d-4e8b-bb3e-0646acebc6ef\",\"serverScript\":\"var retVal = &quot;1&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nvar priid=request.getParameter(&quot;priid&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nvar sql =DocumentUtils.excuteUpdate( &quot;delete from p_fm_api_privilege_relation where API_ID=\'&quot;+ids[i]+&quot;\' and PRI_ID=\'&quot;+priid+&quot;\'&quot;);\\r\\nif(sql!=1){\\r\\nretVal=&quot;系统错误！&quot;;\\r\\n}\\r\\n}\\r\\nretVal;\",\"systemId\":110}', null, null, '110', '1', '7', '1', '1', '9999', '9999', '9999', '2017-09-20 09:54:12', '2017-09-20 09:54:12', null);
INSERT INTO `p_fm_store` VALUES ('1a8dd5d2-b8e1-4c1b-b4f3-030b8c62ba66', '0.1.7.1513318218426', '77d4bfdb5d8575d7d31bdf48ea087b3c', '{\"componentType\":\"1036\",\"searchLocation\":\"1\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"selectOption\":\"\",\"dateSelectName\":\"\",\"dateSelectLabel\":\"\",\"pageId\":\"1a8dd5d2-b8e1-4c1b-b4f3-030b8c62ba66\",\"layoutId\":\"\",\"selectName\":\"\",\"userSelectLabel\":\"\",\"defaultValueScript\":\"\",\"layoutName\":\"root\",\"userSelectName\":\"\",\"textName\":\"id@name@desc\",\"textLabel\":\"ID@名称@描述\",\"name\":\"77d4bfdb5d8575d7d31bdf48ea087b3c\",\"dynamicPageId\":\"6\",\"selectLabel\":\"\",\"readonlyScript\":\"\",\"order\":4}', null, null, '110', '6', '4', null, '1', '9999', '9999', '9999', '2017-10-18 19:21:59', '2017-10-18 19:21:59', null);
INSERT INTO `p_fm_store` VALUES ('1cd11161-04b4-4ff1-8eb2-832120e96b44', '0.1.5.1505873034577', '关闭', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"top.dialog({id : window.name}).close();\",\"code\":\"0.1.5.1505873034577\",\"color\":\"warning\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"var a=request.getParameter(&quot;pri&quot;);\\r\\nif(a!=\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-remove\",\"name\":\"关闭\",\"order\":8,\"pageId\":\"1cd11161-04b4-4ff1-8eb2-832120e96b44\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '1', '8', '1', '1', '9999', '9999', '9999', '2017-09-20 10:03:55', '2017-09-20 10:03:55', null);
INSERT INTO `p_fm_store` VALUES ('1e810d1b-f703-46fa-ab91-d44083042bea', '0.1.7.1493717923517', '9ed542fbd16a45b7d63f3211d27f2390', '{\"backgroundcolor\":\"white\",\"componentType\":\"1009\",\"css\":\"\",\"dataItemCode\":\"\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"isRequired\":\"2\",\"layoutId\":\"18d4aa64-44f3-484d-abe8-41ecee7c638d\",\"layoutName\":\"_row4_cod1\",\"lineheight\":\"1\",\"name\":\"9ed542fbd16a45b7d63f3211d27f2390\",\"order\":13,\"pageId\":\"1e810d1b-f703-46fa-ab91-d44083042bea\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"title\":\"描述\"}', null, null, '111', '2020', '13', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('1f3d9e98-4dc3-4476-822c-b8990da6566e', '0.1.7.1500014481648', '62c36a03f422bb1e4ada5ff5b2474f23', '{\"componentType\":\"1010\",\"dataItemCode\":\"p_fm_modular.ID\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"5\",\"hiddenScript\":\"\",\"layoutId\":\"c164c6f7-b797-4002-a965-f88ab994b830\",\"layoutName\":\"_row2_cod2\",\"name\":\"62c36a03f422bb1e4ada5ff5b2474f23\",\"order\":1,\"pageId\":\"1f3d9e98-4dc3-4476-822c-b8990da6566e\",\"readonlyScript\":\"\"}', null, null, '110', '5', '1', null, '1', '9999', '9999', '9999', '2017-07-14 14:41:22', '2017-07-14 14:41:22', '1');
INSERT INTO `p_fm_store` VALUES ('1fc34482-8178-41f3-a44e-4bf571ed866d', '0.1.5.1508322798435', '返回', '{\"serverScript\":\"\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1508322798435\",\"color\":\"default\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-reply\",\"description\":\"\",\"pageId\":\"1fc34482-8178-41f3-a44e-4bf571ed866d\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"已绑定API列表\",\"enName\":\"\",\"name\":\"返回\",\"dynamicPageId\":7,\"actType\":2000,\"clientScript\":\"location.href = basePath + &quot;document/view.do?dynamicPageId=20&quot;;\",\"chooseValidate\":false,\"order\":3}', null, null, '110', '7', '3', '1', '1', '9999', '9999', '9999', '2017-10-18 18:33:18', '2017-10-18 18:33:18', null);
INSERT INTO `p_fm_store` VALUES ('202bc5f1-303d-4bed-9dbb-fcff97211399', '0.1.7.1493714360672', '6305688cb666739fd5bda928a2946979', '{\"backgroundcolor\":\"white\",\"componentType\":\"1009\",\"css\":\"\",\"dataItemCode\":\"\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"isRequired\":\"2\",\"layoutId\":\"d069b149-ba66-48b6-a52e-6cc2f44947bc\",\"layoutName\":\"_row2_cod1\",\"lineheight\":\"1\",\"name\":\"6305688cb666739fd5bda928a2946979\",\"order\":3,\"pageId\":\"202bc5f1-303d-4bed-9dbb-fcff97211399\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"title\":\"参数名\"}', null, null, '111', '2020', '3', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('206538cd-c22b-43db-ab43-3eb0dd1e1da1', '0.1.7.1513580104354', '379a5cbf0dea0c7cd3eb6b6d9f2ae9d7', '{\"componentType\":\"1010\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"dataItemCode\":\"p_fm_api.API_CREATOR\",\"description\":\"\",\"pageId\":\"206538cd-c22b-43db-ab43-3eb0dd1e1da1\",\"layoutId\":\"81ec3b50-a72f-46ed-99d8-49ee67dc8af1\",\"defaultValueScript\":\"DocumentUtils.getUser().getUserId();\",\"layoutName\":\"_row1_cod1\",\"name\":\"379a5cbf0dea0c7cd3eb6b6d9f2ae9d7\",\"dynamicPageId\":\"2\",\"readonlyScript\":\"\",\"order\":1}', null, null, '110', '2', '1', null, '1', '9999', '9999', '9999', '2017-09-19 14:35:25', '2017-09-19 14:35:25', null);
INSERT INTO `p_fm_store` VALUES ('20b3ff78-d393-4d0d-aa7c-f98506d65320', '0.1.7.1513590816728', '0273716065e800348c8818da9953fa2d', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"参数类型\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"layoutName\":\"_row2_cod2\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":22,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"varchar=varchar;number=number;boolean=boolean;\\\";\",\"dataItemCode\":\"p_fm_api_describe.TYPE\",\"pageId\":\"20b3ff78-d393-4d0d-aa7c-f98506d65320\",\"layoutId\":\"d6678ca1-4a47-4f10-8d31-f821e5ea4091\",\"defaultValueScript\":\"\\\"varchar\\\";\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"0273716065e800348c8818da9953fa2d\",\"dynamicPageId\":\"3\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '3', '22', null, '1', '9999', '9999', '9999', '2017-05-02 16:43:38', '2017-05-02 16:43:38', '1');
INSERT INTO `p_fm_store` VALUES ('218da08a-1906-41b2-8b28-da3d0dc3c74e', '0.1.7.1513599009404', 'a878a3375bd47a2f773f1d10c6c92ee7', '{\"componentType\":\"1010\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"dataItemCode\":\"p_data_dictionary.ID\",\"description\":\"\",\"pageId\":\"218da08a-1906-41b2-8b28-da3d0dc3c74e\",\"layoutId\":\"03fa3296-7ec6-4d05-b464-2b8c44e9adbc\",\"defaultValueScript\":\"\",\"layoutName\":\"_row1_cod1\",\"name\":\"a878a3375bd47a2f773f1d10c6c92ee7\",\"dynamicPageId\":\"17\",\"readonlyScript\":\"\",\"order\":1}', null, null, '110', '17', '1', null, '1', '9999', '9999', '9999', '2017-09-09 16:12:44', '2017-09-09 16:12:44', null);
INSERT INTO `p_fm_store` VALUES ('22490515-7014-4954-a947-b3a79ccf7d43', '0.1.7.1513579287052', 'd5098699ff59528ffbb64e0c974d0b22', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"是否需要登录\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row3_cod2\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":32,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"0=否;1=是;\\\";\",\"dataItemCode\":\"p_fm_api.API_IS_LOGIN\",\"pageId\":\"22490515-7014-4954-a947-b3a79ccf7d43\",\"layoutId\":\"4e029e2b-812a-49c5-bd07-0c3b1d9e2774\",\"defaultValueScript\":\"\\\"1\\\";\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"d5098699ff59528ffbb64e0c974d0b22\",\"dynamicPageId\":\"2\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '2', '32', null, '1', '9999', '9999', '9999', '2017-04-18 17:47:50', '2017-04-18 17:47:50', '1');
INSERT INTO `p_fm_store` VALUES ('22de342f-18b2-4a8d-a23f-d4f30bff5ba3', '0.1.7.1513590696342', 'accf420a1c4eae4d1ed04302003bbc8e', '{\"componentType\":\"1010\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"dataItemCode\":\"p_fm_api_describe.ID\",\"description\":\"\",\"pageId\":\"22de342f-18b2-4a8d-a23f-d4f30bff5ba3\",\"layoutId\":\"6dcf5d93-1f24-4066-adaf-5d8c91a7dd73\",\"defaultValueScript\":\"\",\"layoutName\":\"_row1_cod1\",\"name\":\"accf420a1c4eae4d1ed04302003bbc8e\",\"dynamicPageId\":\"3\",\"readonlyScript\":\"\",\"order\":1}', null, null, '110', '3', '1', null, '1', '9999', '9999', '9999', '2017-05-02 16:45:35', '2017-05-02 16:45:35', '1');
INSERT INTO `p_fm_store` VALUES ('255348ec-0b5e-472b-bc96-2bf8a0b5f7c9', '0.1.8.1513579443516', '_row5_cod1', '{\"proportion\":\"12\",\"textstyle\":\"\",\"pageId\":\"255348ec-0b5e-472b-bc96-2bf8a0b5f7c9\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"5bec71c7-d09a-4c47-bc0b-1653d2f3939f\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row5_cod1\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"layoutType\":\"1\",\"order\":5,\"height\":\"\"}', '5bec71c7-d09a-4c47-bc0b-1653d2f3939f', null, '110', '2', '5', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('255b8e3a-e5ba-4553-ad44-70f59b1ecd27', '0.1.7.1513590789347', '1c8a83b6dc3f671944ac75799fd0080c', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"默认值\",\"validateInputTip\":\"\",\"layoutName\":\"_row1_cod2\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":12,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_describe.DEFAULTVAL\",\"textType\":\"text\",\"pageId\":\"255b8e3a-e5ba-4553-ad44-70f59b1ecd27\",\"layoutId\":\"cfaae037-00d4-4771-9bed-023af3e2bdd9\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\\\"无\\\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"1c8a83b6dc3f671944ac75799fd0080c\",\"textalign\":\"居左\",\"dynamicPageId\":\"3\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '3', '12', null, '1', '9999', '9999', '9999', '2017-05-02 16:44:06', '2017-05-02 16:44:06', '1');
INSERT INTO `p_fm_store` VALUES ('26ffc23b-cb46-4656-aac1-6b5f85e08dd1', '0.1.7.1513579320184', 'ceed34cb2cbad40f6035ca4f93284db6', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"API表名\",\"validateInputTip\":\"\",\"layoutName\":\"_row2_cod2\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":22,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api.API_TABLE\",\"textType\":\"text\",\"pageId\":\"26ffc23b-cb46-4656-aac1-6b5f85e08dd1\",\"layoutId\":\"e74b3405-2c12-4720-82bd-4c53f06b929f\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\\\"无\\\";\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"ceed34cb2cbad40f6035ca4f93284db6\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '2', '22', null, '1', '9999', '9999', '9999', '2017-03-26 17:10:02', '2017-03-26 17:10:02', '1');
INSERT INTO `p_fm_store` VALUES ('28e55667-9b90-4ff4-a03c-8d6854147445', '0.1.8.1513578935784', '_row1', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row1\",\"dynamicPageId\":\"2\",\"layoutType\":2,\"pageId\":\"28e55667-9b90-4ff4-a03c-8d6854147445\",\"parentId\":\"\",\"order\":1}', null, null, '110', '2', '1', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:36', '2017-12-18 14:35:36', null);
INSERT INTO `p_fm_store` VALUES ('291718d4-76f7-43dc-9854-a5128851c388', '0.1.10.1502503888449', 'autoSave', '{\"code\":\"0.1.10.1502503888449\",\"description\":\"自动保存\",\"name\":\"autoSave\",\"pageId\":\"291718d4-76f7-43dc-9854-a5128851c388\",\"script\":\"function autoSave(dataSource){\\r\\n    var id = DocumentUtils.getDataItem(dataSource,&quot;ID&quot;);\\r\\n    var retVal = id;\\r\\n    if(id == null || id == &quot;&quot;) {\\r\\n        id = UUID.randomUUID().toString();\\r\\n        retVal = id;\\r\\n        DocumentUtils.setDataItem(dataSource,&quot;ID&quot;,id);\\r\\n        if(DocumentUtils.saveData(dataSource) == null &amp;&amp; DocumentUtils.saveDocument() == null){\\r\\n            retVal = &quot;0&quot;;\\r\\n        }\\r\\n    } \\r\\n    else {\\r\\n        if(DocumentUtils.updateData(dataSource) == null){\\r\\n            retVal = &quot;0&quot;;\\r\\n        }\\r\\n    }\\r\\n    return retVal;\\r\\n}\",\"systemId\":110}', '自动保存', null, '110', null, null, null, '1', '9999', '9999', '9999', '2017-08-12 10:11:28', '2017-08-12 10:11:28', null);
INSERT INTO `p_fm_store` VALUES ('2cb57708-41ef-4133-9466-e3dfdac04484', '0.1.7.1493714429020', 'b8a7df2e9bfa47bbbdc511926d975692', '{\"backgroundcolor\":\"white\",\"componentType\":\"1009\",\"css\":\"\",\"dataItemCode\":\"\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"isRequired\":\"2\",\"layoutId\":\"9f1e60b0-435c-4471-aa94-08fdfd39c8de\",\"layoutName\":\"_row3_cod1\",\"lineheight\":\"1\",\"name\":\"b8a7df2e9bfa47bbbdc511926d975692\",\"order\":7,\"pageId\":\"2cb57708-41ef-4133-9466-e3dfdac04484\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"title\":\"是否必须\"}', null, null, '111', '2020', '7', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('2d3d7992-7daa-4781-ac30-c233da644e18', '0.1.5.1504942662507', '删除', '{\"serverScript\":\"var retVal = &quot;1&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\n\\tvar ret2=DocumentUtils.excuteQueryForList(&quot;select ID from p_data_dictionary where PID=\'&quot;+ids[i]+&quot;\'&quot;);\\r\\n\\tprint(&quot;ret2=&quot;+ret2);\\r\\n\\tif(ret2!=null &amp;&amp; ret2!=&quot;&quot;){\\r\\n\\t\\tfor(var j=0;j&lt;ret2.size();j++){\\r\\n\\t\\t\\tvar ret3=ret2.get(j).get(&quot;ID&quot;);\\r\\n\\t\\t\\tprint(&quot;ret3=&quot;+ret3);\\r\\n\\t\\t\\tvar ret4=DocumentUtils.excuteQueryForList(&quot;select ID from p_data_dictionary where PID=\'&quot;+ret3+&quot;\'&quot;);\\r\\n\\t\\t\\tprint(&quot;ret4=&quot;+ret4);\\r\\n\\t\\t\\t\\tif(ret4!=null &amp;&amp; ret4!=&quot;&quot;){\\r\\n\\t\\t\\t\\t\\tfor(var l=0;l&lt;ret4.size();l++){\\r\\n\\t\\t\\t\\t\\t\\tvar ret5=ret4.get(l).get(&quot;ID&quot;);\\r\\n\\t\\t\\t\\t\\t\\tprint(&quot;ret5=&quot;+ret5);\\r\\n\\t\\t\\t\\t\\t\\tvar ret6=DocumentUtils.excuteQueryForList(&quot;select ID from p_data_dictionary where PID=\'&quot;+ret5+&quot;\'&quot;);\\r\\n\\t\\t\\t\\t\\t\\tprint(&quot;ret6=&quot;+ret6);\\r\\n\\t\\t\\t\\t\\t\\tif(ret6!=null &amp;&amp; ret6!=&quot;&quot;){\\r\\n\\t\\t\\t\\t\\t\\t\\tfor(var m=0;m&lt;ret6.size();m++){\\r\\n\\t\\t\\t\\t\\t\\t\\t\\tvar v=DocumentUtils.excuteUpdate(&quot;delete from p_data_dictionary where ID=\'&quot;+ret6.get(m).get(&quot;ID&quot;)+&quot;\'&quot;);\\r\\n\\t\\t\\t\\t\\t\\t\\t\\tif(v!=1){\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t\\tretVal=&quot;系统错误&quot;;\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t}\\r\\n\\t\\t\\t\\t\\t\\t\\t\\tprint(&quot;v=&quot;+v);\\r\\n\\t\\t\\t\\t\\t\\t\\t}\\r\\n\\t\\t\\t\\t\\t\\t}\\r\\n\\t\\t\\t\\t\\t\\tvar w=DocumentUtils.excuteUpdate(&quot;delete from p_data_dictionary where ID=\'&quot;+ret5+&quot;\'&quot;);\\r\\n\\t\\t\\t\\t\\t\\tif(w!=1){\\r\\n\\t\\t\\t\\t\\t\\t\\tretVal=&quot;系统错误&quot;;\\r\\n\\t\\t\\t\\t\\t\\t}\\r\\n\\t\\t\\t\\t\\t\\tprint(&quot;w=&quot;+w);\\r\\n\\t\\t\\t\\t\\t}\\r\\n\\t\\t\\t\\t}\\r\\n\\t\\t\\tvar x=DocumentUtils.excuteUpdate(&quot;delete from p_data_dictionary where ID=\'&quot;+ret3+&quot;\'&quot;);\\r\\n\\t\\t\\tif(x!=1){\\r\\n\\t\\t\\t\\tretVal=&quot;系统错误&quot;;\\r\\n\\t\\t\\t}\\r\\n\\t\\t\\tprint(&quot;x=&quot;+x);\\r\\n\\t\\t}\\r\\n\\t}\\r\\n\\tvar a=DocumentUtils.excuteUpdate(&quot;DELETE FROM  P_DATA_DICTIONARY  WHERE ID=\'&quot;+ids[i]+&quot;\'&quot;);\\r\\n\\tif(a!=1){\\r\\n\\t\\tretVal=&quot;系统错误&quot;;\\r\\n\\t}\\r\\n\\tprint(&quot;a&quot;+a);\\r\\n}\\r\\nretVal;\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1504942662507\",\"color\":\"danger\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-trash\",\"description\":\"\",\"pageId\":\"2d3d7992-7daa-4781-ac30-c233da644e18\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"系统属性字典配置列表\",\"enName\":\"\",\"name\":\"删除\",\"dynamicPageId\":\"18\",\"actType\":2000,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar pid=Comm.getUrlParam(&quot;pid&quot;);\\r\\nif(pid==null){\\r\\npid=&quot;&quot;;\\r\\n}\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;删除成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=18&amp;pid=&quot;+pid;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"chooseValidate\":false,\"order\":5}', null, null, '110', '18', '5', '1', '1', '9999', '9999', '9999', '2017-09-09 15:37:43', '2017-09-09 15:37:43', null);
INSERT INTO `p_fm_store` VALUES ('2d531245-528a-4547-af89-2233312d9ef1', '0.1.8.1493717887083', '_row3_cod6', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"1\",\"left\":\"\",\"name\":\"_row3_cod6\",\"order\":18,\"pageId\":\"2d531245-528a-4547-af89-2233312d9ef1\",\"parentId\":\"0f99411b-ea2b-4c21-ba77-c56b90da8c4b\",\"proportion\":\"2\",\"systemId\":111,\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', '0f99411b-ea2b-4c21-ba77-c56b90da8c4b', null, '111', '2020', '18', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('2dd97ba8-18c8-47e5-a207-8ef7f6a0d5dc', '0.1.5.1505823121318', '新增', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"location.href= basePath + &quot;document/view.do?dynamicPageId=19&quot;;\",\"code\":\"0.1.5.1505823121318\",\"color\":\"primary\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":20,\"dynamicPageName\":\"API权限编辑控制列表兼弹窗\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-plus\",\"name\":\"新增\",\"order\":2,\"pageId\":\"2dd97ba8-18c8-47e5-a207-8ef7f6a0d5dc\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '20', '2', '1', '1', '9999', '9999', '9999', '2017-09-19 20:12:01', '2017-09-19 20:12:01', null);
INSERT INTO `p_fm_store` VALUES ('2fd44ad1-3be7-4467-bd9e-0c53cee02137', '0.1.8.1513591109867', '_row1_cod1', '{\"proportion\":\"9\",\"textstyle\":\"\",\"pageId\":\"2fd44ad1-3be7-4467-bd9e-0c53cee02137\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"7ea086f5-ae16-42f4-879d-66cd9a0428c9\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row1_cod1\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"19\",\"layoutType\":\"1\",\"order\":1,\"height\":\"\"}', '7ea086f5-ae16-42f4-879d-66cd9a0428c9', null, '110', '19', '1', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('2fd5a8e7-b224-4b19-b23f-4bf290a52031', '0.1.7.1513590830381', '799debf0f892b02131fb959f035bbea2', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"参数位置\",\"validateInputTip\":\"\",\"layoutName\":\"_row2_cod3\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":23,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_describe.PARAM_PLACE\",\"textType\":\"text\",\"pageId\":\"2fd5a8e7-b224-4b19-b23f-4bf290a52031\",\"layoutId\":\"3e39213e-e968-4214-817f-83c1041afe4a\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\\\"body\\\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"799debf0f892b02131fb959f035bbea2\",\"textalign\":\"居左\",\"dynamicPageId\":\"3\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '3', '23', null, '1', '9999', '9999', '9999', '2017-05-02 17:39:32', '2017-05-02 17:39:32', '1');
INSERT INTO `p_fm_store` VALUES ('30826f1c-6f66-4db2-b8d9-b86156ef7ae9', '0.1.7.1493717822276', '6c63cd7378b9d22971b64b08f1d92307', '{\"backgroundcolor\":\"white\",\"componentType\":\"1009\",\"css\":\"\",\"dataItemCode\":\"\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"isRequired\":\"2\",\"layoutId\":\"31c10865-0aff-496b-9775-ecfc8a6e34da\",\"layoutName\":\"_row2_cod3\",\"lineheight\":\"1\",\"name\":\"6c63cd7378b9d22971b64b08f1d92307\",\"order\":5,\"pageId\":\"30826f1c-6f66-4db2-b8d9-b86156ef7ae9\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"title\":\"默认值\"}', null, null, '111', '2020', '5', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('31c10865-0aff-496b-9775-ecfc8a6e34da', '0.1.8.1493714255106', '_row2_cod3', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":1,\"left\":0,\"name\":\"_row2_cod3\",\"order\":6,\"pageId\":\"31c10865-0aff-496b-9775-ecfc8a6e34da\",\"parentId\":\"499d4e8a-b956-4e31-a60b-a2b1a3609340\",\"proportion\":2,\"systemId\":111,\"top\":0}', '499d4e8a-b956-4e31-a60b-a2b1a3609340', null, '111', '2020', '6', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('345f28ea-0d09-4e6a-b3db-e535679e950a', '0.1.7.1500013928394', '74e81705f25a857eff7e26c9473a9dfb', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"模块名称\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_modular.modularName\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"4\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"74e81705f25a857eff7e26c9473a9dfb\",\"order\":1,\"orderby\":\"1\",\"pageId\":\"345f28ea-0d09-4e6a-b3db-e535679e950a\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"15%\"}', null, null, '110', '4', '1', null, '1', '9999', '9999', '9999', '2017-07-14 14:32:08', '2017-07-14 14:32:08', '1');
INSERT INTO `p_fm_store` VALUES ('35c5bd13-f1fd-4c26-b7df-bce367f63fad', '0.1.8.1513578937615', '_row6', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row6\",\"dynamicPageId\":\"2\",\"layoutType\":2,\"pageId\":\"35c5bd13-f1fd-4c26-b7df-bce367f63fad\",\"parentId\":\"\",\"order\":6}', null, null, '110', '2', '6', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:38', '2017-12-18 14:35:38', null);
INSERT INTO `p_fm_store` VALUES ('365a0308-52d8-44e9-baa5-6bfe8e4e70b2', '0.1.8.1513578936648', '_row3_cod1', '{\"proportion\":3,\"top\":0,\"left\":0,\"name\":\"_row3_cod1\",\"dynamicPageId\":\"2\",\"layoutType\":1,\"pageId\":\"365a0308-52d8-44e9-baa5-6bfe8e4e70b2\",\"parentId\":\"a3083825-6ae9-47f9-be56-68864ab425c9\",\"order\":3}', 'a3083825-6ae9-47f9-be56-68864ab425c9', null, '110', '2', '3', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('37341f71-a03f-46da-a359-f07960a8c76b', '0.1.8.1513578936765', '_row3_cod3', '{\"proportion\":3,\"top\":0,\"left\":0,\"name\":\"_row3_cod3\",\"dynamicPageId\":\"2\",\"layoutType\":1,\"pageId\":\"37341f71-a03f-46da-a359-f07960a8c76b\",\"parentId\":\"a3083825-6ae9-47f9-be56-68864ab425c9\",\"order\":9}', 'a3083825-6ae9-47f9-be56-68864ab425c9', null, '110', '2', '9', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('377d6861-203c-49ba-acb0-648781cefef7', '0.1.7.1508319972498', 'eaa021ec7f5a4a28eefa671bcf91da61', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":3,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_STATE\",\"excelShowScript\":\"\",\"pageId\":\"377d6861-203c-49ba-acb0-648781cefef7\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"eaa021ec7f5a4a28eefa671bcf91da61\",\"width\":\"5%\",\"dynamicPageId\":\"7\",\"validateErrorTip\":\"\",\"columnName\":\"API状态\"}', null, null, '110', '7', '3', null, '1', '9999', '9999', '9999', '2017-10-18 17:44:45', '2017-10-18 17:44:45', null);
INSERT INTO `p_fm_store` VALUES ('37c5bfda-84e0-4830-a2e1-2a6797a5c73e', '0.1.8.1513578936956', '_row4', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row4\",\"dynamicPageId\":\"2\",\"layoutType\":2,\"pageId\":\"37c5bfda-84e0-4830-a2e1-2a6797a5c73e\",\"parentId\":\"\",\"order\":4}', null, null, '110', '2', '4', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('37e55e1e-5f61-487e-b232-929df1075676', '0.1.7.1513591169016', '65b4ab87288acd632b87cf5402a753fd', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"失败或成功提示\",\"validateInputTip\":\"\",\"layoutName\":\"_row3_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"2\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":31,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_privilege.MESSAGE\",\"pageId\":\"37e55e1e-5f61-487e-b232-929df1075676\",\"layoutId\":\"0ae1284a-91ec-40ce-929d-26b99981fe15\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"65b4ab87288acd632b87cf5402a753fd\",\"textalign\":\"居左\",\"dynamicPageId\":\"19\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '19', '31', null, '1', '9999', '9999', '9999', '2017-09-19 20:08:11', '2017-09-19 20:08:11', null);
INSERT INTO `p_fm_store` VALUES ('38354a89-bd53-40fe-a3ab-b17203bf87a9', '0.1.7.1509936785060', 'd2397b0000095917c02870a461c09a61', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"var list = DocumentUtils.getDataContainer(\\\"p_fm_api_list\\\");\\r\\nvar basePath = DocumentUtils.getBasePath(request);\\r\\nif(list!=null){\\r\\n\\tfor(var i=0;i<list.size();i++){\\r\\n\\t\\tvar vo = list.get(i);\\r\\n\\t\\tvar state = vo.get(\\\"ID\\\");\\t\\r\\n\\t\\tvar id = vo.get(\\\"ID\\\");\\r\\n\\t\\tvar str = \\\"<a href=\'\\\"+basePath+\\\"document/view.do?dynamicPageId=2&id=\\\" + id + \\\"\'>\\\" + state + \\\"</a>\\\";\\r\\n\\t\\tvo.put(\\\"IDS\\\",str);\\r\\n\\t}\\r\\n}\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":1,\"componentType\":\"1008\",\"hiddenScript\":\"true;\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.IDS\",\"excelShowScript\":\"\",\"pageId\":\"38354a89-bd53-40fe-a3ab-b17203bf87a9\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"d2397b0000095917c02870a461c09a61\",\"width\":\"5%\",\"dynamicPageId\":\"1\",\"validateErrorTip\":\"\",\"columnName\":\"ID\"}', null, null, '110', '1', '1', null, '1', '9999', '9999', '9999', '2017-05-03 11:45:01', '2017-05-03 11:45:01', '1');
INSERT INTO `p_fm_store` VALUES ('39f87bd3-384b-463c-9388-78b2bc291070', '0.1.5.1505824917504', '返回', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"location.href = basePath + &quot;document/view.do?dynamicPageId=1&quot;;\",\"code\":\"0.1.5.1505824917504\",\"color\":\"warning\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":20,\"dynamicPageName\":\"API权限编辑控制列表兼弹窗\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-reply\",\"name\":\"返回\",\"order\":5,\"pageId\":\"39f87bd3-384b-463c-9388-78b2bc291070\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '20', '5', '1', '1', '9999', '9999', '9999', '2017-09-19 20:41:58', '2017-09-19 20:41:58', null);
INSERT INTO `p_fm_store` VALUES ('3b3509c5-b5d1-42b1-a172-4732eee9b235', '0.1.7.1493717910338', '2b983bf78080da228322741186ae1e78', '{\"backgroundcolor\":\"white\",\"componentType\":\"1009\",\"css\":\"\",\"dataItemCode\":\"\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"isRequired\":\"2\",\"layoutId\":\"c52f8a00-7d5e-4b67-a80d-1ec6613129ef\",\"layoutName\":\"_row3_cod5\",\"lineheight\":\"1\",\"name\":\"2b983bf78080da228322741186ae1e78\",\"order\":11,\"pageId\":\"3b3509c5-b5d1-42b1-a172-4732eee9b235\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"title\":\"参数位置\"}', null, null, '111', '2020', '11', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('3e39213e-e968-4214-817f-83c1041afe4a', '0.1.8.1513590664839', '_row2_cod3', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row2_cod3\",\"dynamicPageId\":\"3\",\"layoutType\":1,\"pageId\":\"3e39213e-e968-4214-817f-83c1041afe4a\",\"parentId\":\"de3647cc-ea53-464c-bb4e-abbfcb0e58f1\",\"order\":6}', 'de3647cc-ea53-464c-bb4e-abbfcb0e58f1', null, '110', '3', '6', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:05', '2017-12-18 17:51:05', null);
INSERT INTO `p_fm_store` VALUES ('3ef7340a-815a-485d-a89c-a2c14a56a9ca', '0.1.7.1513599183013', '102dfe19c1bbe2b7a96b93f97676e79c', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"VALUE值\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row2_cod2\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":22,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_data_dictionary.DATA_VALUE\",\"textType\":\"text\",\"pageId\":\"3ef7340a-815a-485d-a89c-a2c14a56a9ca\",\"layoutId\":\"d80a99fe-3fc5-4667-ae99-4d1d34c12152\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"102dfe19c1bbe2b7a96b93f97676e79c\",\"textalign\":\"居左\",\"dynamicPageId\":\"17\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '17', '22', null, '1', '9999', '9999', '9999', '2017-09-09 16:17:12', '2017-09-09 16:17:12', null);
INSERT INTO `p_fm_store` VALUES ('40ff9ef5-6c3e-453d-93c6-14dd95a41c03', '0.1.7.1493714370378', 'd787bbd6f39fb6364faa6d1ef65cc492', '{\"backgroundcolor\":\"white\",\"componentType\":\"1001\",\"css\":\"\",\"dataItemCode\":\"p_fm_api_describe.NAME\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"layoutId\":\"16520285-2ab0-42ad-9e3a-8e037efdd07f\",\"layoutName\":\"_row2_cod2\",\"lineheight\":\"1\",\"name\":\"d787bbd6f39fb6364faa6d1ef65cc492\",\"order\":4,\"pageId\":\"40ff9ef5-6c3e-453d-93c6-14dd95a41c03\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\"}', null, null, '111', '2020', '4', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('45d93c90-970f-4eee-8f91-cfe1c4a382f5', '0.1.8.1513591105070', '_row1_cod2', '{\"proportion\":\"3\",\"textstyle\":\"\",\"pageId\":\"45d93c90-970f-4eee-8f91-cfe1c4a382f5\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"7ea086f5-ae16-42f4-879d-66cd9a0428c9\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row1_cod2\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"19\",\"layoutType\":\"1\",\"order\":2,\"height\":\"\"}', '7ea086f5-ae16-42f4-879d-66cd9a0428c9', null, '110', '19', '2', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:25', '2017-12-18 17:58:25', null);
INSERT INTO `p_fm_store` VALUES ('48f32d60-adc4-4b8f-91c6-53cc32fadd78', '0.1.8.1513579382758', '_row7', '{\"proportion\":\"12\",\"textstyle\":\"\",\"pageId\":\"48f32d60-adc4-4b8f-91c6-53cc32fadd78\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row7\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"layoutType\":\"2\",\"order\":7,\"height\":\"\"}', null, null, '110', '2', '7', null, '1', '9999', '9999', '9999', '2017-12-18 14:42:52', '2017-12-18 14:42:52', null);
INSERT INTO `p_fm_store` VALUES ('499d4e8a-b956-4e31-a60b-a2b1a3609340', '0.1.8.1493714255039', '_row2', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":2,\"left\":0,\"name\":\"_row2\",\"order\":2,\"pageId\":\"499d4e8a-b956-4e31-a60b-a2b1a3609340\",\"parentId\":\"\",\"proportion\":12,\"systemId\":111,\"top\":0}', null, null, '111', '2020', '2', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('4a710375-b15c-407b-b961-5eaa3b7058ba', '0.1.8.1513599002051', '_row3', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row3\",\"dynamicPageId\":\"17\",\"layoutType\":2,\"pageId\":\"4a710375-b15c-407b-b961-5eaa3b7058ba\",\"parentId\":\"\",\"order\":3}', null, null, '110', '17', '3', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('4cc5ece5-29de-4c01-8166-514e311e3704', '0.1.5.1500014079374', '删除', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nif(!confirm(&quot;是否确认删除?&quot;)){\\r\\n\\treturn false;\\r\\n}\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;删除成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=4&quot;;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"code\":\"0.1.5.1500014079374\",\"color\":\"danger\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":4,\"dynamicPageName\":\"页面模块列表\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-trash\",\"name\":\"删除\",\"order\":3,\"pageId\":\"4cc5ece5-29de-4c01-8166-514e311e3704\",\"serverScript\":\"var ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\n\\tvar sql = &quot;delete from p_fm_modular Where ID=&quot; + ids[i];\\r\\n\\tDocumentUtils.excuteUpdate(sql);\\r\\n}\\r\\nretVal = &quot;1&quot;;\",\"systemId\":110}', null, null, '110', '4', '3', '1', '1', '9999', '9999', '9999', '2017-07-14 14:34:39', '2017-07-14 14:34:39', '1');
INSERT INTO `p_fm_store` VALUES ('4ce82b25-6d50-4e60-aecd-23930a374a19', '0.1.7.1493717932997', '1b2908851c2768ccde98e75e50a5ecad', '{\"backgroundcolor\":\"white\",\"componentType\":\"1005\",\"css\":\"\",\"dataItemCode\":\"p_fm_api_describe.DESCRIBES\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"layoutId\":\"8abf7929-e802-4937-a948-c2dca659739e\",\"layoutName\":\"_row4_cod2\",\"lineheight\":\"1\",\"name\":\"1b2908851c2768ccde98e75e50a5ecad\",\"order\":14,\"pageId\":\"4ce82b25-6d50-4e60-aecd-23930a374a19\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"rowCount\":\"7\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\"}', null, null, '111', '2020', '14', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('4de3dd38-a1e2-42ce-a7ec-6f657216e226', '0.1.7.1513579313669', '3bc7eeb21ba41cc1baf7c4560da8b3ca', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"API状态\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row2_cod3\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":23,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"0=禁用;1=启用;\\\";\",\"dataItemCode\":\"p_fm_api.API_STATE\",\"pageId\":\"4de3dd38-a1e2-42ce-a7ec-6f657216e226\",\"layoutId\":\"a50bb634-0471-404f-a123-9a34066418ee\",\"defaultValueScript\":\"\\\"1\\\";\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"3bc7eeb21ba41cc1baf7c4560da8b3ca\",\"dynamicPageId\":\"2\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '2', '23', null, '1', '9999', '9999', '9999', '2017-03-26 17:12:47', '2017-03-26 17:12:47', '1');
INSERT INTO `p_fm_store` VALUES ('4e029e2b-812a-49c5-bd07-0c3b1d9e2774', '0.1.8.1513578936707', '_row3_cod2', '{\"proportion\":3,\"top\":0,\"left\":0,\"name\":\"_row3_cod2\",\"dynamicPageId\":\"2\",\"layoutType\":1,\"pageId\":\"4e029e2b-812a-49c5-bd07-0c3b1d9e2774\",\"parentId\":\"a3083825-6ae9-47f9-be56-68864ab425c9\",\"order\":6}', 'a3083825-6ae9-47f9-be56-68864ab425c9', null, '110', '2', '6', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('4f1c6a3d-943f-4960-aa3a-18e9b7cff2d5', '0.1.8.1513590664669', '_row2_cod1', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row2_cod1\",\"dynamicPageId\":\"3\",\"layoutType\":1,\"pageId\":\"4f1c6a3d-943f-4960-aa3a-18e9b7cff2d5\",\"parentId\":\"de3647cc-ea53-464c-bb4e-abbfcb0e58f1\",\"order\":2}', 'de3647cc-ea53-464c-bb4e-abbfcb0e58f1', null, '110', '3', '2', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:05', '2017-12-18 17:51:05', null);
INSERT INTO `p_fm_store` VALUES ('500377e9-7464-4b0d-aff2-0fd057c4f637', '0.1.7.1513579304402', 'e410be02030e3a635ff6ba1f0d9383f2', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"API类型\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"layoutName\":\"_row2_cod4\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":24,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"1=add;2=update;3=get;4=delete;5=excute;6=query;7=page;8=excuteScript\\\";\",\"dataItemCode\":\"p_fm_api.API_TYPE\",\"pageId\":\"500377e9-7464-4b0d-aff2-0fd057c4f637\",\"layoutId\":\"1016bb26-aa38-4569-b85a-cca69c1a0280\",\"defaultValueScript\":\"\\\"8\\\";\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"e410be02030e3a635ff6ba1f0d9383f2\",\"dynamicPageId\":\"2\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '2', '24', null, '1', '9999', '9999', '9999', '2017-03-26 17:14:05', '2017-03-26 17:14:05', '1');
INSERT INTO `p_fm_store` VALUES ('51635e48-4c10-41ee-add7-6baee4325236', '0.1.7.1508325560743', '16862552b05f5bc5aa6756a165dd395b', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":1,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.IDS\",\"excelShowScript\":\"\",\"pageId\":\"51635e48-4c10-41ee-add7-6baee4325236\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"16862552b05f5bc5aa6756a165dd395b\",\"width\":\"\",\"dynamicPageId\":\"6\",\"validateErrorTip\":\"\",\"columnName\":\"ID\"}', null, null, '110', '6', '1', null, '1', '9999', '9999', '9999', '2017-10-18 18:34:02', '2017-10-18 18:34:02', null);
INSERT INTO `p_fm_store` VALUES ('545c7e14-acb0-49e7-89f4-cfbd8eecffa8', '0.1.8.1502440575635', '_row1', '{\"dynamicPageId\":5,\"layoutType\":2,\"left\":0,\"name\":\"_row1\",\"order\":1,\"pageId\":\"545c7e14-acb0-49e7-89f4-cfbd8eecffa8\",\"parentId\":\"\",\"proportion\":12,\"top\":0}', null, null, '110', '5', '1', null, '1', '9999', '9999', '9999', '2017-08-11 16:36:16', '2017-08-11 16:36:16', null);
INSERT INTO `p_fm_store` VALUES ('54736121-b385-43a3-a6ad-187fd1ce9d19', '0.1.5.1504945283533', '返回', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"location.href = basePath + &quot;document/view.do?dynamicPageId=18&quot;;\",\"code\":\"0.1.5.1504945283533\",\"color\":\"warning\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":17,\"dynamicPageName\":\"系统属性字典配置表单\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-reply\",\"name\":\"返回\",\"order\":2,\"pageId\":\"54736121-b385-43a3-a6ad-187fd1ce9d19\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '17', '2', '1', '1', '9999', '9999', '9999', '2017-09-09 16:21:24', '2017-09-09 16:21:24', null);
INSERT INTO `p_fm_store` VALUES ('5604581e-fea0-48a1-864c-bd09943efe96', '0.1.8.1513591081321', '_row4', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row4\",\"dynamicPageId\":\"19\",\"layoutType\":2,\"pageId\":\"5604581e-fea0-48a1-864c-bd09943efe96\",\"parentId\":\"\",\"order\":4}', null, null, '110', '19', '4', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('587e1abd-af5a-44bc-8fc8-392e4b9ce9ee', '0.1.7.1513579278436', '1e3e7efca290c9bb4c2e2f490c965d34', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"是否缓存\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"layoutName\":\"_row3_cod3\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":33,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"0=否;1=是;\\\";\",\"dataItemCode\":\"p_fm_api.API_ISCACHE\",\"pageId\":\"587e1abd-af5a-44bc-8fc8-392e4b9ce9ee\",\"layoutId\":\"37341f71-a03f-46da-a359-f07960a8c76b\",\"defaultValueScript\":\"\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"1e3e7efca290c9bb4c2e2f490c965d34\",\"dynamicPageId\":\"2\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '2', '33', null, '1', '9999', '9999', '9999', '2017-09-19 14:53:34', '2017-09-19 14:53:34', null);
INSERT INTO `p_fm_store` VALUES ('5aa6ae91-d5cf-4148-80fb-41e8852b4854', '0.1.8.1513599002267', '_row3_cod3', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row3_cod3\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"5aa6ae91-d5cf-4148-80fb-41e8852b4854\",\"parentId\":\"4a710375-b15c-407b-b961-5eaa3b7058ba\",\"order\":9}', '4a710375-b15c-407b-b961-5eaa3b7058ba', null, '110', '17', '9', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('5bab06d9-82bd-41f5-b2ab-1f074d2da9e0', '0.1.8.1513591081438', '_row5', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row5\",\"dynamicPageId\":\"19\",\"layoutType\":2,\"pageId\":\"5bab06d9-82bd-41f5-b2ab-1f074d2da9e0\",\"parentId\":\"\",\"order\":5}', null, null, '110', '19', '5', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('5bec71c7-d09a-4c47-bc0b-1653d2f3939f', '0.1.8.1513578937298', '_row5', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row5\",\"dynamicPageId\":\"2\",\"layoutType\":2,\"pageId\":\"5bec71c7-d09a-4c47-bc0b-1653d2f3939f\",\"parentId\":\"\",\"order\":5}', null, null, '110', '2', '5', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('5bee2811-33e0-43f1-9daf-fae4e3ed89c1', '0.1.7.1509418721934', 'ee6276ed9076aa7921cc5a6a41fcd8c3', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":4,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_data_dictionary.DATA_VALUE\",\"excelShowScript\":\"\",\"pageId\":\"5bee2811-33e0-43f1-9daf-fae4e3ed89c1\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"ee6276ed9076aa7921cc5a6a41fcd8c3\",\"width\":\"\",\"dynamicPageId\":\"18\",\"validateErrorTip\":\"\",\"columnName\":\"VALUE值\"}', null, null, '110', '18', '4', null, '1', '9999', '9999', '9999', '2017-09-09 15:35:06', '2017-09-09 15:35:06', null);
INSERT INTO `p_fm_store` VALUES ('5c00e397-369a-4143-af12-d2c73b93f383', '0.1.8.1493714255239', '_row4', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":2,\"left\":0,\"name\":\"_row4\",\"order\":4,\"pageId\":\"5c00e397-369a-4143-af12-d2c73b93f383\",\"parentId\":\"\",\"proportion\":12,\"systemId\":111,\"top\":0}', null, null, '111', '2020', '4', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('5c6eb9b5-03ca-472f-a206-4b9d58b8eace', '0.1.8.1513599001767', '_row2', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row2\",\"dynamicPageId\":\"17\",\"layoutType\":2,\"pageId\":\"5c6eb9b5-03ca-472f-a206-4b9d58b8eace\",\"parentId\":\"\",\"order\":2}', null, null, '110', '17', '2', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('5d14b424-37c8-4e09-b14c-440b99104712', '0.1.5.1505822310538', '返回', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"location.href= basePath + &quot;document/view.do?dynamicPageId=20&amp;edit=true&quot;;\",\"code\":\"0.1.5.1505822310538\",\"color\":\"warning\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":19,\"dynamicPageName\":\"API权限编辑控制表单\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-reply\",\"name\":\"返回\",\"order\":2,\"pageId\":\"5d14b424-37c8-4e09-b14c-440b99104712\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '19', '2', '1', '1', '9999', '9999', '9999', '2017-09-19 19:58:31', '2017-09-19 19:58:31', null);
INSERT INTO `p_fm_store` VALUES ('5d41e4d1-7bba-487b-9f98-b9e19172c2a6', '0.1.5.1490520551451', '保存', '{\"actType\":2001,\"buttonGroup\":1,\"buttons\":\"\",\"chooseValidate\":false,\"clientScript\":\"var actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\r\\n\\t$.ajax({\\r\\n\\t   type: &quot;POST&quot;,\\r\\n\\t   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n\\t   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n\\t   async : false,\\r\\n\\t   success: function(data){\\r\\n\\t\\t\\tif(data==1){\\r\\n\\t\\t\\t\\talert(&quot;保存成功&quot;);\\r\\n\\t\\t\\t\\tlocation.href= basePath + &quot;document/view.do?id=&amp;dynamicPageId=1&quot;;\\r\\n\\t\\t\\t}\\r\\n\\t\\t\\telse{\\r\\n\\t\\t\\t\\talert(data);\\r\\n\\t\\t\\t}\\r\\n\\t   }\\r\\n\\t});\",\"code\":\"0.1.5.1490520551451\",\"color\":\"primary\",\"confirm\":false,\"content\":\"\",\"contentType\":3,\"description\":\"\",\"dynamicPageId\":2,\"dynamicPageName\":\"API管理表单\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"var reader=request.getParameter(&quot;reader&quot;);\\r\\nif(reader==1){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-save\",\"name\":\"保存\",\"order\":1,\"pageId\":\"5d41e4d1-7bba-487b-9f98-b9e19172c2a6\",\"serverScript\":\"var id = DocumentUtils.getDataItem(&quot;p_fm_api&quot;,&quot;API_ID&quot;);\\r\\nvar retVal = &quot;1&quot;;\\r\\nif(id == null || id == &quot;&quot;) {\\r\\n\\tid = UUID.randomUUID().toString();\\r\\n\\tDocumentUtils.setDataItem(&quot;p_fm_api&quot;,&quot;API_ID&quot;,id);\\r\\n\\tif(DocumentUtils.saveData(&quot;p_fm_api&quot;) == null &amp;&amp; DocumentUtils.saveDocument() == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n} \\r\\nelse {\\r\\n\\tif(DocumentUtils.updateData(&quot;p_fm_api&quot;) == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n}\\r\\nretVal;\",\"systemId\":110,\"tittle\":\"\"}', null, null, '110', '2', '1', '1', '1', '9999', '9999', '9999', '2017-03-26 17:29:11', '2017-03-26 17:29:11', '1');
INSERT INTO `p_fm_store` VALUES ('5d72b0ae-def2-48cf-a20c-c53f0815855e', '0.1.7.1508320043185', '30bed74225cca93529f7bff89d4a0d84', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":5,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_METHOD\",\"excelShowScript\":\"\",\"pageId\":\"5d72b0ae-def2-48cf-a20c-c53f0815855e\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"30bed74225cca93529f7bff89d4a0d84\",\"width\":\"7%\",\"dynamicPageId\":\"7\",\"validateErrorTip\":\"\",\"columnName\":\"请求方式\"}', null, null, '110', '7', '5', null, '1', '9999', '9999', '9999', '2017-10-18 17:45:23', '2017-10-18 17:45:23', null);
INSERT INTO `p_fm_store` VALUES ('5e8d06dd-ab45-4334-979d-339f1fc42afa', '0.1.7.1508319983217', '1eba1a9bf4f96ce448352f10b6f652d0', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":4,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_TYPE\",\"excelShowScript\":\"\",\"pageId\":\"5e8d06dd-ab45-4334-979d-339f1fc42afa\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"1eba1a9bf4f96ce448352f10b6f652d0\",\"width\":\"10%\",\"dynamicPageId\":\"7\",\"validateErrorTip\":\"\",\"columnName\":\"类型\"}', null, null, '110', '7', '4', null, '1', '9999', '9999', '9999', '2017-10-18 17:45:01', '2017-10-18 17:45:01', null);
INSERT INTO `p_fm_store` VALUES ('602835ef-4703-4705-af53-11a529f61ff2', '0.1.5.1508321181954', '取消关联', '{\"serverScript\":\"var retVal = &quot;1&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nvar pid=request.getParameter(&quot;pid&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nvar sql =DocumentUtils.excuteUpdate( &quot;delete from p_fm_api_privilege_relation where API_ID=\'&quot;+ids[i]+&quot;\' and PRI_ID=\'&quot;+pid+&quot;\'&quot;);\\r\\nif(sql!=1){\\r\\nretVal=&quot;系统错误！&quot;;\\r\\n}\\r\\n}\\r\\nretVal;\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1508321181954\",\"color\":\"warning\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-minus\",\"description\":\"\",\"pageId\":\"602835ef-4703-4705-af53-11a529f61ff2\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"已绑定API列表\",\"enName\":\"\",\"name\":\"取消关联\",\"dynamicPageId\":7,\"actType\":2000,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar pid = Comm.getUrlParam(&quot;pid&quot;);\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n    type: &quot;POST&quot;,\\r\\n    url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n    data:$(&quot;#groupForm&quot;).serialize()+&quot;&amp;pid=&quot;+pid,\\r\\n    async : false,\\r\\n    success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;取消关联成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=7&amp;pid=&quot;+pid;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n    }\\r\\n});\",\"chooseValidate\":false,\"order\":2}', null, null, '110', '7', '2', '1', '1', '9999', '9999', '9999', '2017-10-18 18:06:22', '2017-10-18 18:06:22', null);
INSERT INTO `p_fm_store` VALUES ('6157b6c5-587a-4491-a397-79049fae5257', '0.1.7.1513590842574', '1b2908851c2768ccde98e75e50a5ecad', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"描述\",\"validateInputTip\":\"\",\"layoutName\":\"_row3_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"7\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":31,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_describe.DESCRIBES\",\"pageId\":\"6157b6c5-587a-4491-a397-79049fae5257\",\"layoutId\":\"14373470-45a0-4106-8f5d-19d6d8bb8fd6\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\\\"无\\\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"1b2908851c2768ccde98e75e50a5ecad\",\"textalign\":\"居左\",\"dynamicPageId\":\"3\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '3', '31', null, '1', '9999', '9999', '9999', '2017-05-02 16:45:02', '2017-05-02 16:45:02', '1');
INSERT INTO `p_fm_store` VALUES ('648c102c-6e41-43cd-885f-0f69c75f54aa', '0.1.7.1493717972987', '799debf0f892b02131fb959f035bbea2', '{\"backgroundcolor\":\"white\",\"componentType\":\"1001\",\"css\":\"\",\"dataItemCode\":\"p_fm_api_describe.PARAM_PLACE\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"layoutId\":\"2d531245-528a-4547-af89-2233312d9ef1\",\"layoutName\":\"_row3_cod6\",\"lineheight\":\"1\",\"name\":\"799debf0f892b02131fb959f035bbea2\",\"order\":12,\"pageId\":\"648c102c-6e41-43cd-885f-0f69c75f54aa\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\"}', null, null, '111', '2020', '12', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('65635ee7-9495-4d50-937c-2343f5e1f5b9', '0.1.5.1504942643736', '启用', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar pid=Comm.getUrlParam(&quot;pid&quot;);\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;启用成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=18&amp;pid=&quot;+pid;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"code\":\"0.1.5.1504942643736\",\"color\":\"info\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":18,\"dynamicPageName\":\"系统属性字典配置列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-tasks\",\"name\":\"启用\",\"order\":3,\"pageId\":\"65635ee7-9495-4d50-937c-2343f5e1f5b9\",\"serverScript\":\"var retVal = &quot;系统错误&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nvar a=DocumentUtils.excuteUpdate(&quot;UPDATE P_DATA_DICTIONARY SET DICT_STATUS=\'1\' WHERE ID=\'&quot;+ids[i]+&quot;\'&quot;);\\r\\nif(a&gt;0){\\r\\nretVal=&quot;1&quot;;\\r\\n}\\r\\n}\\r\\nretVal;\",\"systemId\":110}', null, null, '110', '18', '3', '1', '1', '9999', '9999', '9999', '2017-09-09 15:37:24', '2017-09-09 15:37:24', null);
INSERT INTO `p_fm_store` VALUES ('65f86c59-c80b-40e8-8b40-ddd7695b4570', '0.1.5.1500014027195', '编辑', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count!=1){\\t\\r\\n\\talert(&quot;请选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar id = $(&quot;input[name=\'_selects\']&quot;).val();\\r\\nlocation.href = basePath + &quot;document/view.do?dynamicPageId=5&amp;id=&quot; + id;\",\"code\":\"0.1.5.1500014027195\",\"color\":\"success\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":4,\"dynamicPageName\":\"页面模块列表\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-edit\",\"name\":\"编辑\",\"order\":2,\"pageId\":\"65f86c59-c80b-40e8-8b40-ddd7695b4570\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '4', '2', '1', '1', '9999', '9999', '9999', '2017-07-14 14:33:47', '2017-07-14 14:33:47', '1');
INSERT INTO `p_fm_store` VALUES ('67a26eb1-5b79-4858-97c6-1b41389cd9bd', '0.1.7.1505117735030', '4745599bc0b79f03f3e88b67dcec024d', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"父级KEY值\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_data_dictionary.PKEY\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"18\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"4745599bc0b79f03f3e88b67dcec024d\",\"order\":2,\"orderby\":\"1\",\"pageId\":\"67a26eb1-5b79-4858-97c6-1b41389cd9bd\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"\"}', null, null, '110', '18', '2', null, '1', '9999', '9999', '9999', '2017-09-11 16:15:35', '2017-09-11 16:15:35', null);
INSERT INTO `p_fm_store` VALUES ('6818c272-cada-4742-a491-09df5916a49b', '0.1.7.1503383565792', '28620aebdc7e1645b9cbf2f0a60901b9', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"API名称\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api.API_NAME\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"1\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"28620aebdc7e1645b9cbf2f0a60901b9\",\"order\":2,\"orderby\":\"1\",\"pageId\":\"6818c272-cada-4742-a491-09df5916a49b\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"var list = DocumentUtils.getDataContainer(\\\"p_fm_api_list\\\");\\r\\nvar basePath = DocumentUtils.getBasePath(request);\\r\\nif(list!=null){\\r\\n\\tfor(var i=0;i<list.size();i++){\\r\\n\\t\\tvar vo = list.get(i);\\r\\n\\t\\tvar state = vo.get(\\\"API_NAME\\\");\\t\\r\\n\\t\\tvar id = vo.get(\\\"ID\\\");\\r\\n\\t\\tvar str = \\\"<a href=\'\\\"+basePath+\\\"document/view.do?dynamicPageId=2&readonly=true&id=\\\" + id + \\\"&reader=1\'>\\\" + state + \\\"</a>\\\";\\r\\n\\t\\tvo.put(\\\"API_NAME\\\",str);\\r\\n\\t}\\r\\n}\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"15%\"}', null, null, '110', '1', '2', null, '1', '9999', '9999', '9999', '2017-03-26 17:04:50', '2017-03-26 17:04:50', '1');
INSERT INTO `p_fm_store` VALUES ('683694bf-f8c2-4999-a9cb-b264025a80d7', '0.1.7.1503307017197', '41d99837c5c89caa1f485ec90e91a972', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"请求方式\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api.API_METHOD\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"1\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"41d99837c5c89caa1f485ec90e91a972\",\"order\":8,\"orderby\":\"1\",\"pageId\":\"683694bf-f8c2-4999-a9cb-b264025a80d7\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"7%\"}', null, null, '110', '1', '8', null, '1', '9999', '9999', '9999', '2017-04-18 17:41:50', '2017-04-18 17:41:50', '1');
INSERT INTO `p_fm_store` VALUES ('6bb39ce0-7de2-4666-a68c-e94a4fe43a1f', '0.1.7.1513591156299', '05ea582e6e23c0d83aee97fff763d878', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"控制规则\",\"validateInputTip\":\"\",\"layoutName\":\"_row2_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"4\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":21,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_privilege.RULES\",\"pageId\":\"6bb39ce0-7de2-4666-a68c-e94a4fe43a1f\",\"layoutId\":\"f8774250-58ae-4070-a601-ed8fb4f6b49b\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"05ea582e6e23c0d83aee97fff763d878\",\"textalign\":\"居左\",\"dynamicPageId\":\"19\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '19', '21', null, '1', '9999', '9999', '9999', '2017-09-19 19:55:46', '2017-09-19 19:55:46', null);
INSERT INTO `p_fm_store` VALUES ('6c80b819-288d-4c89-9450-01e5e5f8bb7d', '0.1.7.1506482556139', '0a00b1952547bc9e5ee04c96124062d9', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":4,\"componentType\":\"1008\",\"hiddenScript\":\"true;\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_dynamicpage.modular\",\"excelShowScript\":\"\",\"pageId\":\"6c80b819-288d-4c89-9450-01e5e5f8bb7d\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"0a00b1952547bc9e5ee04c96124062d9\",\"width\":\"\",\"dynamicPageId\":\"10\",\"validateErrorTip\":\"\",\"columnName\":\"模块id\"}', null, null, '110', '10', '4', null, '1', '9999', '9999', '9999', '2017-09-27 11:22:36', '2017-09-27 11:22:36', null);
INSERT INTO `p_fm_store` VALUES ('6d40f03f-5686-446e-b88a-8b013ad1c739', '0.1.7.1509418811975', 'a206c2cf59badfd8b15a5880f70ae485', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":5,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_data_dictionary.PID\",\"excelShowScript\":\"\",\"pageId\":\"6d40f03f-5686-446e-b88a-8b013ad1c739\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"a206c2cf59badfd8b15a5880f70ae485\",\"width\":\"\",\"dynamicPageId\":\"18\",\"validateErrorTip\":\"\",\"columnName\":\"PID\"}', null, null, '110', '18', '5', null, '1', '9999', '9999', '9999', '2017-10-31 11:00:12', '2017-10-31 11:00:12', null);
INSERT INTO `p_fm_store` VALUES ('6dcf5d93-1f24-4066-adaf-5d8c91a7dd73', '0.1.8.1513590664310', '_row1_cod1', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row1_cod1\",\"dynamicPageId\":\"3\",\"layoutType\":1,\"pageId\":\"6dcf5d93-1f24-4066-adaf-5d8c91a7dd73\",\"parentId\":\"8ec0e1b3-0c5f-48af-8b2e-730a06abecae\",\"order\":1}', '8ec0e1b3-0c5f-48af-8b2e-730a06abecae', null, '110', '3', '1', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:04', '2017-12-18 17:51:04', null);
INSERT INTO `p_fm_store` VALUES ('6f901b0c-c88e-44c5-be21-2ca37ff8e577', '0.1.7.1513590702797', 'APIID', '{\"componentType\":\"1010\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"dataItemCode\":\"p_fm_api_describe.APIID\",\"description\":\"\",\"pageId\":\"6f901b0c-c88e-44c5-be21-2ca37ff8e577\",\"layoutId\":\"6dcf5d93-1f24-4066-adaf-5d8c91a7dd73\",\"defaultValueScript\":\"request.getParameter(\\\"22de342f-18b2-4a8d-a23f-d4f30bff5ba3\\\");\",\"layoutName\":\"_row1_cod1\",\"name\":\"APIID\",\"dynamicPageId\":\"3\",\"readonlyScript\":\"\",\"order\":1}', null, null, '110', '3', '1', null, '1', '9999', '9999', '9999', '2017-05-02 16:46:17', '2017-05-02 16:46:17', '1');
INSERT INTO `p_fm_store` VALUES ('7131e773-6ae3-4c37-bbfe-1e0314cafd70', '0.1.7.1508319962159', '99e176ba5274020f52c40db6a2c51329', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":1,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_ID\",\"excelShowScript\":\"\",\"pageId\":\"7131e773-6ae3-4c37-bbfe-1e0314cafd70\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"99e176ba5274020f52c40db6a2c51329\",\"width\":\"5%\",\"dynamicPageId\":\"7\",\"validateErrorTip\":\"\",\"columnName\":\"ID\"}', null, null, '110', '7', '1', null, '1', '9999', '9999', '9999', '2017-10-18 17:44:04', '2017-10-18 17:44:04', null);
INSERT INTO `p_fm_store` VALUES ('74203716-54fc-4964-b484-cfd09d8e32ef', '0.1.8.1513578936297', '_row2', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row2\",\"dynamicPageId\":\"2\",\"layoutType\":2,\"pageId\":\"74203716-54fc-4964-b484-cfd09d8e32ef\",\"parentId\":\"\",\"order\":2}', null, null, '110', '2', '2', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:36', '2017-12-18 14:35:36', null);
INSERT INTO `p_fm_store` VALUES ('7547d92a-a8a3-4780-8930-ddae6e9a0e46', '0.1.7.1513580483338', 'f864c9d106a4235def3c3ce2edbb55bf', '{\"css\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"接口地址\",\"layoutName\":\"_row1_cod2\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"readonlyScript\":\"\",\"order\":12,\"componentType\":\"1009\",\"isRequired\":\"2\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"\",\"pageId\":\"7547d92a-a8a3-4780-8930-ddae6e9a0e46\",\"layoutId\":\"acfd5adb-795d-4a76-b088-2d36b7cf18c0\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"name\":\"f864c9d106a4235def3c3ce2edbb55bf\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"style\":\"\",\"textRise\":\"0\"}', null, null, '110', '2', '12', null, '1', '9999', '9999', '9999', '2017-12-18 15:01:23', '2017-12-18 15:01:23', null);
INSERT INTO `p_fm_store` VALUES ('760ddf1b-e415-4fa7-b7f2-8d5ab0201106', '0.1.7.1505822693312', 'f3ce8a84e2353f3e3b7b0390af4c39e0', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"权限类型\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api_privilege.TYPE\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"20\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"f3ce8a84e2353f3e3b7b0390af4c39e0\",\"order\":2,\"orderby\":\"1\",\"pageId\":\"760ddf1b-e415-4fa7-b7f2-8d5ab0201106\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"\"}', null, null, '110', '20', '2', null, '1', '9999', '9999', '9999', '2017-09-19 20:04:53', '2017-09-19 20:04:53', null);
INSERT INTO `p_fm_store` VALUES ('7759ea99-88f2-4dd2-a5fd-8784e0d0c3ce', '0.1.7.1513591183345', '6e354f2a3cdba2d79af87123cb96efad', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"描述\",\"validateInputTip\":\"\",\"layoutName\":\"_row4_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"3\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":41,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_privilege.DESCRIBES\",\"pageId\":\"7759ea99-88f2-4dd2-a5fd-8784e0d0c3ce\",\"layoutId\":\"ce6a7eae-5c51-440e-9140-9fba88739a7e\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"6e354f2a3cdba2d79af87123cb96efad\",\"textalign\":\"居左\",\"dynamicPageId\":\"19\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '19', '41', null, '1', '9999', '9999', '9999', '2017-09-19 19:56:14', '2017-09-19 19:56:14', null);
INSERT INTO `p_fm_store` VALUES ('784099e4-1234-45c2-ad79-0ba378c53e79', '0.1.5.1500014538864', '保存', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"saveFormPage(4,$(this).attr(&quot;id&quot;));\",\"code\":\"0.1.5.1500014538864\",\"color\":\"primary\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":5,\"dynamicPageName\":\"页面模块新增表单\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-save\",\"name\":\"保存\",\"order\":1,\"pageId\":\"784099e4-1234-45c2-ad79-0ba378c53e79\",\"serverScript\":\"#include &quot;saveForIncID&quot;;\\r\\nsaveForIncID(&quot;p_fm_modular&quot;);\",\"systemId\":110}', null, null, '110', '5', '1', '1', '1', '9999', '9999', '9999', '2017-07-14 14:42:19', '2017-07-14 14:42:19', '1');
INSERT INTO `p_fm_store` VALUES ('7872a81c-fa98-4c25-9eb9-a1b971faf329', '0.1.5.1508323134669', '关闭', '{\"serverScript\":\"\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1508323134669\",\"color\":\"sback\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-reply\",\"description\":\"\",\"pageId\":\"7872a81c-fa98-4c25-9eb9-a1b971faf329\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"未绑定绑定API列表\",\"enName\":\"\",\"name\":\"关闭\",\"dynamicPageId\":6,\"actType\":2000,\"clientScript\":\"top.dialog({id : window.name}).close();\",\"chooseValidate\":false,\"order\":2}', null, null, '110', '6', '2', '1', '1', '9999', '9999', '9999', '2017-10-18 18:38:55', '2017-10-18 18:38:55', null);
INSERT INTO `p_fm_store` VALUES ('7a29eb59-2baa-4b2f-8a7b-62db100ec8ae', '0.1.7.1505823009032', '8b80b5c364ebe08f5d238a63e66d3408', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"描述\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api_privilege.DESCRIBES\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"20\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"8b80b5c364ebe08f5d238a63e66d3408\",\"order\":4,\"orderby\":\"1\",\"pageId\":\"7a29eb59-2baa-4b2f-8a7b-62db100ec8ae\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"\"}', null, null, '110', '20', '4', null, '1', '9999', '9999', '9999', '2017-09-19 20:10:09', '2017-09-19 20:10:09', null);
INSERT INTO `p_fm_store` VALUES ('7a8a774c-5eba-4d0d-9b71-86d4a5121377', '0.1.7.1508326380891', '89055fd707054c41eb89ebb30861dcb3', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":3,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api_privilege.Num\",\"excelShowScript\":\"\",\"pageId\":\"7a8a774c-5eba-4d0d-9b71-86d4a5121377\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"89055fd707054c41eb89ebb30861dcb3\",\"width\":\"\",\"dynamicPageId\":\"20\",\"validateErrorTip\":\"\",\"columnName\":\"已关联接口数\"}', null, null, '110', '20', '3', null, '1', '9999', '9999', '9999', '2017-09-19 20:09:48', '2017-09-19 20:09:48', null);
INSERT INTO `p_fm_store` VALUES ('7a9564cc-dd35-4757-9a61-060fde449e20', '0.1.5.1500014556920', '返回', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"backToViewPage(4);\",\"code\":\"0.1.5.1500014556920\",\"color\":\"default\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":5,\"dynamicPageName\":\"页面模块新增表单\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-reply\",\"name\":\"返回\",\"order\":2,\"pageId\":\"7a9564cc-dd35-4757-9a61-060fde449e20\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '5', '2', '1', '1', '9999', '9999', '9999', '2017-07-14 14:42:37', '2017-07-14 14:42:37', '1');
INSERT INTO `p_fm_store` VALUES ('7dcba478-7377-4fbe-adf5-f59c0b00bb3e', '0.1.5.1490520333474', '新增', '{\"actType\":2009,\"buttonGroup\":1,\"buttons\":\"\",\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"location.href= basePath + &quot;document/view.do?id=&amp;dynamicPageId=2&quot;;\",\"code\":\"0.1.5.1490520333474\",\"color\":\"primary\",\"confirm\":false,\"content\":\"\",\"contentType\":3,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{\"target\":\"2\"},\"hiddenScript\":\"var a=request.getParameter(&quot;pri&quot;);\\r\\nvar b=request.getParameter(&quot;relation&quot;);\\r\\nif(a==\'true\' || b==\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-plus\",\"name\":\"新增\",\"order\":1,\"pageId\":\"7dcba478-7377-4fbe-adf5-f59c0b00bb3e\",\"serverScript\":\"\",\"systemId\":110,\"tittle\":\"\"}', null, null, '110', '1', '1', '1', '1', '9999', '9999', '9999', '2017-03-26 17:25:33', '2017-03-26 17:25:33', '1');
INSERT INTO `p_fm_store` VALUES ('7ea086f5-ae16-42f4-879d-66cd9a0428c9', '0.1.8.1513591080992', '_row1', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row1\",\"dynamicPageId\":\"19\",\"layoutType\":2,\"pageId\":\"7ea086f5-ae16-42f4-879d-66cd9a0428c9\",\"parentId\":\"\",\"order\":1}', null, null, '110', '19', '1', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('818bb1c3-bf87-4861-a9b7-88b605f1a2e3', '0.1.7.1513578953057', 'apiid', '{\"componentType\":\"1010\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"dataItemCode\":\"p_fm_api.API_ID\",\"description\":\"\",\"pageId\":\"818bb1c3-bf87-4861-a9b7-88b605f1a2e3\",\"layoutId\":\"81ec3b50-a72f-46ed-99d8-49ee67dc8af1\",\"defaultValueScript\":\"\",\"layoutName\":\"_row1_cod1\",\"name\":\"apiid\",\"dynamicPageId\":\"2\",\"readonlyScript\":\"\",\"order\":1}', null, null, '110', '2', '1', null, '1', '9999', '9999', '9999', '2017-03-26 17:15:25', '2017-03-26 17:15:25', '1');
INSERT INTO `p_fm_store` VALUES ('81ec3b50-a72f-46ed-99d8-49ee67dc8af1', '0.1.8.1513579825760', '_row1_cod1', '{\"proportion\":\"3\",\"textstyle\":\"\",\"pageId\":\"81ec3b50-a72f-46ed-99d8-49ee67dc8af1\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"28e55667-9b90-4ff4-a03c-8d6854147445\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row1_cod1\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"layoutType\":\"1\",\"order\":1,\"height\":\"\"}', '28e55667-9b90-4ff4-a03c-8d6854147445', null, '110', '2', '1', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:36', '2017-12-18 14:35:36', null);
INSERT INTO `p_fm_store` VALUES ('81fdf93d-8d8f-428a-a10d-d2f96af66c57', '0.1.5.1493780871533', 'sdfsd', '{\"actType\":2001,\"buttonGroup\":1,\"buttons\":\"\",\"chooseValidate\":false,\"clientScript\":\"\",\"code\":\"0.1.5.1493780871533\",\"color\":\"default\",\"confirm\":false,\"content\":\"\",\"contentType\":3,\"description\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"extbute\":{},\"hiddenScript\":\"var flag=false;\\r\\nif(DocumentUtils.hasRole(82)){\\r\\n     flag=true;\\r\\n}\\r\\nflag;\",\"hiddenStatus\":false,\"icon\":\"icon-file\",\"name\":\"sdfsd\",\"pageId\":\"81fdf93d-8d8f-428a-a10d-d2f96af66c57\",\"serverScript\":\"\",\"systemId\":111,\"tittle\":\"\"}', null, null, '111', '2020', null, '1', '1', '9999', '9999', '9999', '2017-05-03 11:07:51', '2017-05-03 11:07:51', '1');
INSERT INTO `p_fm_store` VALUES ('83519b5a-2576-4c70-8136-267ccab815c2', '0.1.7.1513599169615', 'be2298e3da03834143b3ca069c743bfc', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"层级\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row1_cod2\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":12,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"1=1;2=2;3=3;4=4;\\\";\",\"dataItemCode\":\"p_data_dictionary.LEVEL\",\"pageId\":\"83519b5a-2576-4c70-8136-267ccab815c2\",\"layoutId\":\"bbcc8d68-ac53-4d20-85fd-3c720f0d4bf4\",\"defaultValueScript\":\"var pid=request.getParameter(\\\"pid\\\");\\r\\nvar a=\\\"1\\\";\\r\\nif(pid!=null && pid!=\\\"\\\"){\\r\\na=DocumentUtils.queryObjectById(\\\"p_data_dictionary\\\",\\\"LEVEL\\\",\\\"ID\\\",pid);\\r\\na=a+1;\\r\\n}\\r\\na.toString();\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"be2298e3da03834143b3ca069c743bfc\",\"dynamicPageId\":\"17\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"var a=$(\\\"#83519b5a-2576-4c70-8136-267ccab815c2\\\").val();\\r\\nif(a==\'1\'){\\r\\n$(\\\"#b8e6dc9f-4eb4-4c31-a315-0963c09466d1\\\").parent().hide();\\r\\n$(\\\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\\\").parent().hide();\\r\\n$(\\\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\\\").attr(\\\"disabled\\\",true);\\r\\n}else{\\r\\n$(\\\"#b8e6dc9f-4eb4-4c31-a315-0963c09466d1\\\").parent().show();\\r\\n$(\\\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\\\").parent().show();\\r\\n$(\\\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\\\").attr(\\\"disabled\\\",false);\\r\\n}\\r\\n$.get(basePath+\\\"api/executeAPI.do?APIId=getPID\\\",{ level: this.value},function(data){\\r\\nvar a=$(\\\"#133db9dd-fcba-4c88-ad74-b18b803c1e55\\\")[0].options;\\r\\nvar b=a.length;\\r\\nfor(var i=0;i<b;i++){\\r\\n\\ta[0].remove();\\r\\n}\\r\\nvar c=data.data.length;\\r\\nfor(var i=0;i<c;i++){\\r\\n\\tvar e=new Option(data.data[i].DATA_KEY,data.data[i].ID);\\r\\n\\ta.add(e);\\r\\n}\\r\\n});\"}', null, null, '110', '17', '12', null, '1', '9999', '9999', '9999', '2017-09-09 16:14:10', '2017-09-09 16:14:10', null);
INSERT INTO `p_fm_store` VALUES ('84c38f07-1ed7-4618-af82-2938d047f4c9', '0.1.5.1493714886428', '保存', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"var apiid=$(&quot;#6f901b0c-c88e-44c5-be21-2ca37ff8e577&quot;).val();\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\r\\n\\t$.ajax({\\r\\n\\t   type: &quot;POST&quot;,\\r\\n\\t   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n\\t   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n\\t   async : false,\\r\\n\\t   success: function(data){\\r\\n\\t\\t\\tif(data==1){\\r\\n\\t\\t\\t\\ttop.dialog({id : window.name}).close(&quot;Y&quot;);                              \\r\\n\\t\\t\\t}\\r\\n\\t\\t\\telse{\\r\\n\\t\\t\\t\\talert(data);\\r\\n\\t\\t\\t}\\r\\n\\t   }\\r\\n\\t});\",\"code\":\"0.1.5.1493714886428\",\"color\":\"primary\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":3,\"dynamicPageName\":\"API参数表单\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-save\",\"name\":\"保存\",\"order\":1,\"pageId\":\"84c38f07-1ed7-4618-af82-2938d047f4c9\",\"serverScript\":\"var id = DocumentUtils.getDataItem(&quot;p_fm_api_describe&quot;,&quot;ID&quot;);\\r\\nvar retVal = &quot;1&quot;;\\r\\nif(id == null || id == &quot;&quot;) {\\r\\n\\tid = UUID.randomUUID().toString();\\r\\n\\tDocumentUtils.setDataItem(&quot;p_fm_api_describe&quot;,&quot;ID&quot;,id);\\r\\n\\tif(DocumentUtils.saveData(&quot;p_fm_api_describe&quot;) == null &amp;&amp; DocumentUtils.saveDocument() == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n} \\r\\nelse {\\r\\n\\tif(DocumentUtils.updateData(&quot;p_fm_api_describe&quot;) == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n}\\r\\nretVal;\",\"systemId\":110}', null, null, '110', '3', '1', '1', '1', '9999', '9999', '9999', '2017-05-02 16:48:06', '2017-05-02 16:48:06', '1');
INSERT INTO `p_fm_store` VALUES ('8555626c-6361-4380-ab2a-4084596937ed', '0.1.7.1513579247972', '78a2d4ef1a762365f3224092111d3b80', '{\"operateAdd\":\"1\",\"hasPager\":\"0\",\"operateEdit\":\"1\",\"operateDelete\":\"1\",\"validatJson\":\"\",\"disabledScript\":\"\",\"columns\":[{\"columnWidth\":\"18%\",\"editType\":\"0\",\"columnTitle\":\"参数名\",\"columnField\":\"NAME\",\"order\":\"1\"},{\"columnWidth\":\"12%\",\"editType\":\"0\",\"columnTitle\":\"类型\",\"columnField\":\"TYPE\",\"order\":\"2\"},{\"columnWidth\":\"12%\",\"editType\":\"0\",\"columnTitle\":\"参数位置\",\"columnField\":\"PARAM_PLACE\",\"order\":\"3\"},{\"columnWidth\":\"8%\",\"editType\":\"0\",\"columnTitle\":\"必填\",\"columnField\":\"ISNECESSARY\",\"order\":\"4\"},{\"columnWidth\":\"10%\",\"editType\":\"0\",\"columnTitle\":\"默认值\",\"columnField\":\"DEFAULTVAL\",\"order\":\"5\"},{\"columnWidth\":\"29%\",\"editType\":\"0\",\"columnTitle\":\"描述\",\"columnField\":\"DESCRIBES\",\"order\":\"6\"}],\"pdfTemplatePage\":\"\",\"description\":\"\",\"viewHeight\":\"800\",\"title\":\"传入参数\",\"validateInputTip\":\"\",\"layoutName\":\"_row6_cod1\",\"lineHeightType\":\"\",\"param\":[{\"exportParam\":\"p_fm_api.API_ID\",\"storeParamId\":\"22de342f-18b2-4a8d-a23f-d4f30bff5ba3\",\"storeParam\":\"p_fm_api_describe.ID\"}],\"alertPage\":\"3\",\"readonlyScript\":\"\",\"order\":61,\"minLineCount\":\"\",\"imageWidth\":\"800\",\"componentType\":\"1017\",\"viewWidth\":\"1000\",\"hiddenScript\":\"\",\"dataAlias\":\"p_fm_api_describe\",\"pageId\":\"8555626c-6361-4380-ab2a-4084596937ed\",\"connditions\":[{\"paramKey\":\"apiid\",\"paramValue\":\"p_fm_api.API_ID\"}],\"layoutId\":\"a5e235c6-b319-4533-bda3-162072e8a64c\",\"imageHeight\":\"500\",\"defaultValueScript\":\"\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"78a2d4ef1a762365f3224092111d3b80\",\"maxLineCount\":\"\",\"dynamicPageId\":\"2\",\"style\":\"\",\"lineHeight\":\"\",\"validateErrorTip\":\"\",\"proportions\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '2', '61', null, '1', '9999', '9999', '9999', '2017-05-02 17:01:02', '2017-05-02 17:01:02', '1');
INSERT INTO `p_fm_store` VALUES ('855d3397-bb88-4679-b340-841110988987', '0.1.5.1502443952168', '确定更改', '{\"serverScript\":\"var retVal = &quot;系统错误&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nvar mid=request.getParameter(&quot;mid&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nprint(mid+&quot;OOOOOOOOOOOOOOOOOOOOOOOOOOOO&quot;+ids[0]);\\r\\nvar sql = &quot;update p_fm_dynamicpage set modular=\'&quot;+mid+&quot;\' where id=\'&quot;+ids[i]+&quot;\' &quot;;\\r\\nDocumentUtils.excuteUpdate(sql);\\r\\nretVal=&quot;1&quot;;\\r\\n}\\r\\nretVal;\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1502443952168\",\"color\":\"primary\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-save\",\"description\":\"\",\"pageId\":\"855d3397-bb88-4679-b340-841110988987\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"模块添加页面\",\"enName\":\"\",\"name\":\"确定更改\",\"dynamicPageId\":10,\"actType\":2000,\"clientScript\":\"if(count==0){\\r\\nalert(&quot;请至少选择一项&quot;);\\r\\nreturn false;\\r\\n}\\r\\nvar mid=Comm.getUrlParam(&quot;818bb1c3-bf87-4861-a9b7-88b605f1a2e3&quot;);\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize()+&quot;&amp;mid=&quot;+mid,\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;关联成功&quot;);\\r\\ntop.dialog({id : window.name}).close(&quot;Y&quot;);\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"chooseValidate\":false,\"order\":1}', null, null, '110', '10', '1', '1', '1', '9999', '9999', '9999', '2017-08-11 17:32:32', '2017-08-11 17:32:32', null);
INSERT INTO `p_fm_store` VALUES ('856b9940-cc0a-48d4-9cb9-84a33aabd92f', '0.1.5.1508323017715', '确定', '{\"serverScript\":\"var pid=request.getParameter(&quot;pid&quot;);\\r\\nvar retVal = &quot;1&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nvar sql = DocumentUtils.excuteUpdate(&quot;insert into p_fm_api_privilege_relation(API_ID,PRI_ID) VALUES (\'&quot;+ids[i]+&quot;\',\'&quot;+pid+&quot;\')&quot;);\\r\\nif(sql!=1){\\r\\nretVal=&quot;系统错误！&quot;;\\r\\n}\\r\\n}\\r\\nretVal;\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1508323017715\",\"color\":\"primary\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-save\",\"description\":\"\",\"pageId\":\"856b9940-cc0a-48d4-9cb9-84a33aabd92f\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"未绑定绑定API列表\",\"enName\":\"\",\"name\":\"确定\",\"dynamicPageId\":6,\"actType\":2000,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar pid = Comm.getUrlParam(&quot;pid&quot;);\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize()+&quot;&amp;pid=&quot;+pid,\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;关联成功&quot;);\\r\\n\\t\\t\\ttop.dialog({id : window.name}).close();\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"chooseValidate\":false,\"order\":1}', null, null, '110', '6', '1', '1', '1', '9999', '9999', '9999', '2017-10-18 18:36:58', '2017-10-18 18:36:58', null);
INSERT INTO `p_fm_store` VALUES ('867988b0-edc0-4f71-89d1-965b0c4c84df', '0.1.7.1513580269293', '7d30522a458c1582db43c7763f055afc', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"脚本\",\"validateInputTip\":\"\",\"layoutName\":\"_row4_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"3\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":41,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api.API_SQL\",\"pageId\":\"867988b0-edc0-4f71-89d1-965b0c4c84df\",\"layoutId\":\"b24f1c86-6519-4f72-aa88-13bc469177b5\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"7d30522a458c1582db43c7763f055afc\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '2', '41', null, '1', '9999', '9999', '9999', '2017-03-26 17:14:49', '2017-03-26 17:14:49', '1');
INSERT INTO `p_fm_store` VALUES ('88fcf567-c371-49bd-ba68-c053447d17ba', '0.1.8.1513599001875', '_row2_cod1', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row2_cod1\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"88fcf567-c371-49bd-ba68-c053447d17ba\",\"parentId\":\"5c6eb9b5-03ca-472f-a206-4b9d58b8eace\",\"order\":2}', '5c6eb9b5-03ca-472f-a206-4b9d58b8eace', null, '110', '17', '2', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('8919406e-0f11-4044-ba1b-48a34bda1c40', '0.1.8.1513578936898', '_row3_cod4', '{\"proportion\":3,\"top\":0,\"left\":0,\"name\":\"_row3_cod4\",\"dynamicPageId\":\"2\",\"layoutType\":1,\"pageId\":\"8919406e-0f11-4044-ba1b-48a34bda1c40\",\"parentId\":\"a3083825-6ae9-47f9-be56-68864ab425c9\",\"order\":12}', 'a3083825-6ae9-47f9-be56-68864ab425c9', null, '110', '2', '12', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('8ab7977a-8d8f-40a0-9210-00f93e3aee47', '0.1.7.1502443807135', '13689a2c2062ed8d9844d0c32fdbec0b', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"名称\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_dynamicpage.name\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"10\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"13689a2c2062ed8d9844d0c32fdbec0b\",\"order\":2,\"orderby\":\"1\",\"pageId\":\"8ab7977a-8d8f-40a0-9210-00f93e3aee47\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"\"}', null, null, '110', '10', '2', null, '1', '9999', '9999', '9999', '2017-08-11 17:30:07', '2017-08-11 17:30:07', null);
INSERT INTO `p_fm_store` VALUES ('8abf7929-e802-4937-a948-c2dca659739e', '0.1.8.1493714304997', '_row4_cod2', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"1\",\"left\":\"\",\"name\":\"_row4_cod2\",\"order\":8,\"pageId\":\"8abf7929-e802-4937-a948-c2dca659739e\",\"parentId\":\"5c00e397-369a-4143-af12-d2c73b93f383\",\"proportion\":\"10\",\"systemId\":111,\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', '5c00e397-369a-4143-af12-d2c73b93f383', null, '111', '2020', '8', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:05', '2017-05-03 11:07:05', '1');
INSERT INTO `p_fm_store` VALUES ('8bfc78aa-7ae2-4b1b-89fe-d41680c99bf0', '0.1.7.1493717796279', '880858f8d48e6465cf6078dcefb15fee', '{\"backgroundcolor\":\"white\",\"componentType\":\"1009\",\"css\":\"\",\"dataItemCode\":\"\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"isRequired\":\"2\",\"layoutId\":\"cbac7213-501b-465f-86c2-c2a8f3394aa2\",\"layoutName\":\"_row3_cod3\",\"lineheight\":\"1\",\"name\":\"880858f8d48e6465cf6078dcefb15fee\",\"order\":9,\"pageId\":\"8bfc78aa-7ae2-4b1b-89fe-d41680c99bf0\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"title\":\"参数类型\"}', null, null, '111', '2020', '9', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('8c1d6fcb-6d26-4fc0-93b6-6335db015d98', '0.1.7.1513591115815', 'ce8ad88d98557614a3929baaaa7ecf2a', '{\"componentType\":\"1010\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"dataItemCode\":\"p_fm_api_privilege.ID\",\"description\":\"\",\"pageId\":\"8c1d6fcb-6d26-4fc0-93b6-6335db015d98\",\"layoutId\":\"2fd44ad1-3be7-4467-bd9e-0c53cee02137\",\"defaultValueScript\":\"\",\"layoutName\":\"_row1_cod1\",\"name\":\"ce8ad88d98557614a3929baaaa7ecf2a\",\"dynamicPageId\":\"19\",\"readonlyScript\":\"\",\"order\":1}', null, null, '110', '19', '1', null, '1', '9999', '9999', '9999', '2017-09-19 19:53:45', '2017-09-19 19:53:45', null);
INSERT INTO `p_fm_store` VALUES ('8e826b78-af31-441b-a058-04eb84a335d6', '0.1.7.1493714735284', 'accf420a1c4eae4d1ed04302003bbc8e', '{\"componentType\":\"1010\",\"dataItemCode\":\"p_fm_api_describe.ID\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"hiddenScript\":\"\",\"layoutId\":\"16520285-2ab0-42ad-9e3a-8e037efdd07f\",\"layoutName\":\"_row2_cod2\",\"name\":\"accf420a1c4eae4d1ed04302003bbc8e\",\"order\":1,\"pageId\":\"8e826b78-af31-441b-a058-04eb84a335d6\",\"readonlyScript\":\"\",\"systemId\":111}', null, null, '111', '2020', '1', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('8ec0e1b3-0c5f-48af-8b2e-730a06abecae', '0.1.8.1513590664225', '_row1', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row1\",\"dynamicPageId\":\"3\",\"layoutType\":2,\"pageId\":\"8ec0e1b3-0c5f-48af-8b2e-730a06abecae\",\"parentId\":\"\",\"order\":1}', null, null, '110', '3', '1', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:04', '2017-12-18 17:51:04', null);
INSERT INTO `p_fm_store` VALUES ('908c9d03-bffa-4dc2-9398-fd2c66175982', '0.1.7.1509418194996', '53477f984f5893e8b7ffce3e495f85bb', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"var list = DocumentUtils.getDataContainer(\\\"p_data_dictionary_list\\\");\\r\\nvar basePath = DocumentUtils.getBasePath(request);\\r\\nif(list!=null){\\r\\n\\tfor(var i=0;i<list.size();i++){\\r\\n\\t\\tvar vo = list.get(i);\\r\\n\\t\\tvar state = vo.get(\\\"DATA_KEY\\\");\\t\\r\\n\\t\\tvar id = vo.get(\\\"ID\\\");\\r\\n\\t\\tvar count=DocumentUtils.excuteQueryForObject(\\\"select count(ID) from p_data_dictionary where PID=?\\\",id);\\r\\n\\t\\tvar str = \\\"<a href=\'\\\"+basePath+\\\"document/view.do?dynamicPageId=18&pid=\\\"+id+\\\"\'>\\\" + state + \\\"</a>\\\";\\r\\n\\t\\tif(count==0){\\r\\n\\t\\t\\tstr = \\\"<a href=\'\\\"+basePath+\\\"document/view.do?dynamicPageId=17&id=\\\"+id+\\\"\'>\\\" + state + \\\"</a>\\\";\\r\\n\\t\\t}\\r\\n\\t\\tvo.put(\\\"DATA_KEY\\\",str);\\r\\n\\t}\\r\\n}\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":3,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_data_dictionary.DATA_KEY\",\"excelShowScript\":\"\",\"pageId\":\"908c9d03-bffa-4dc2-9398-fd2c66175982\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"53477f984f5893e8b7ffce3e495f85bb\",\"width\":\"\",\"dynamicPageId\":\"18\",\"validateErrorTip\":\"\",\"columnName\":\"KEY值\"}', null, null, '110', '18', '3', null, '1', '9999', '9999', '9999', '2017-09-09 15:34:28', '2017-09-09 15:34:28', null);
INSERT INTO `p_fm_store` VALUES ('91bbd982-ff1c-4b35-a0bc-8773a242ada0', '0.1.10.1500014719605', 'saveForIncID', '{\"code\":\"0.1.10.1500014719605\",\"description\":\"普通页面保存，主键自增长\",\"name\":\"saveForIncID\",\"pageId\":\"91bbd982-ff1c-4b35-a0bc-8773a242ada0\",\"script\":\"function saveForIncID(dataSource){\\r\\n\\tvar id = DocumentUtils.getDataItem(dataSource,&quot;ID&quot;);\\r\\n\\tvar retVal = &quot;1&quot;;\\r\\n\\tif(id == null || id == &quot;&quot;) {\\r\\n\\t\\tif(DocumentUtils.saveData(dataSource) == null &amp;&amp; DocumentUtils.saveDocument() == null){\\r\\n\\t\\t\\tretVal = &quot;保存失败！&quot;;\\r\\n\\t\\t}\\r\\n\\t} \\r\\n\\telse {\\r\\n\\t\\tif(DocumentUtils.updateData(dataSource) == null){\\r\\n\\t\\t\\tretVal = &quot;更新失败！&quot;;\\r\\n\\t\\t}\\r\\n\\t}\\r\\n\\treturn retVal;\\r\\n}\",\"systemId\":110}', '普通页面保存，主键自增长', null, '110', null, null, null, '1', '9999', '9999', '9999', '2017-07-14 14:45:20', '2017-07-14 14:45:20', '1');
INSERT INTO `p_fm_store` VALUES ('92a764f2-4d0e-45de-8c0a-0f5a79da8eca', '0.1.5.1505876296457', '返回', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"var priid=Comm.getUrlParam(&quot;priid&quot;);\\r\\nlocation.href=basePath+&quot;document/view.do?dynamicPageId=1&amp;pri=true&amp;priid=&quot;+priid;\",\"code\":\"0.1.5.1505876296457\",\"color\":\"warning\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"var a=request.getParameter(&quot;relation&quot;);\\r\\nif(a!=\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-reply\",\"name\":\"返回\",\"order\":10,\"pageId\":\"92a764f2-4d0e-45de-8c0a-0f5a79da8eca\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '1', '10', '1', '1', '9999', '9999', '9999', '2017-09-20 10:58:16', '2017-09-20 10:58:16', null);
INSERT INTO `p_fm_store` VALUES ('931b0578-396f-4f70-a79e-3bed6b8e19f3', '0.1.5.1500013987099', '新增', '{\"actType\":2009,\"buttonGroup\":1,\"buttons\":\"\",\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"\",\"code\":\"0.1.5.1500013987099\",\"color\":\"primary\",\"confirm\":false,\"content\":\"\",\"contentType\":3,\"description\":\"\",\"dynamicPageId\":4,\"dynamicPageName\":\"页面模块列表\",\"extbute\":{\"target\":\"5\"},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-plus\",\"name\":\"新增\",\"order\":1,\"pageId\":\"931b0578-396f-4f70-a79e-3bed6b8e19f3\",\"serverScript\":\"\",\"systemId\":110,\"tittle\":\"\"}', null, null, '110', '4', '1', '1', '1', '9999', '9999', '9999', '2017-07-14 14:33:07', '2017-07-14 14:33:07', '1');
INSERT INTO `p_fm_store` VALUES ('932b591b-5539-469d-b10c-90bba457904f', '0.1.8.1513599002152', '_row3_cod1', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row3_cod1\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"932b591b-5539-469d-b10c-90bba457904f\",\"parentId\":\"4a710375-b15c-407b-b961-5eaa3b7058ba\",\"order\":3}', '4a710375-b15c-407b-b961-5eaa3b7058ba', null, '110', '17', '3', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('962cb200-eef2-4116-9fd5-e1bc5ea25178', '0.1.8.1500014303504', '_row2_cod1', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":\"5\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"1\",\"left\":\"\",\"name\":\"_row2_cod1\",\"order\":2,\"pageId\":\"962cb200-eef2-4116-9fd5-e1bc5ea25178\",\"parentId\":\"c3e77840-a90b-464f-8044-568d01679ca8\",\"proportion\":\"2\",\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', 'c3e77840-a90b-464f-8044-568d01679ca8', null, '110', '5', '2', null, '1', '9999', '9999', '9999', '2017-07-14 14:38:02', '2017-07-14 14:38:02', '1');
INSERT INTO `p_fm_store` VALUES ('98bc667a-a0c3-4e0a-8779-8d92c998f2a9', '0.1.8.1513599002201', '_row3_cod2', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row3_cod2\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"98bc667a-a0c3-4e0a-8779-8d92c998f2a9\",\"parentId\":\"4a710375-b15c-407b-b961-5eaa3b7058ba\",\"order\":6}', '4a710375-b15c-407b-b961-5eaa3b7058ba', null, '110', '17', '6', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('9934c176-a78a-45c9-9fce-b5bc01f1ba7a', '0.1.7.1513579296272', 'dbe7659d0468e35f17bf2aec91681b5c', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"请求方式\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row3_cod1\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":31,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"both=both;post=post;get=get;\\\";\",\"dataItemCode\":\"p_fm_api.API_METHOD\",\"pageId\":\"9934c176-a78a-45c9-9fce-b5bc01f1ba7a\",\"layoutId\":\"365a0308-52d8-44e9-baa5-6bfe8e4e70b2\",\"defaultValueScript\":\"\\\"both\\\";\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"dbe7659d0468e35f17bf2aec91681b5c\",\"dynamicPageId\":\"2\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '2', '31', null, '1', '9999', '9999', '9999', '2017-04-18 17:46:10', '2017-04-18 17:46:10', '1');
INSERT INTO `p_fm_store` VALUES ('9cb9f74c-1fd3-40ae-9802-786c70b2725b', '0.1.5.1509417789454', '返回上一级', '{\"serverScript\":\"var pid=request.getParameter(&quot;pid&quot;);\\r\\nvar pidpid=DocumentUtils.queryObjectById(&quot;p_data_dictionary&quot;,&quot;PID&quot;,&quot;ID&quot;,pid);\\r\\nprint(pidpid+&quot;AAAAAAAAAAAAAAA&quot;);\\r\\npidpid;\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1509417789454\",\"color\":\"info\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-reply\",\"description\":\"\",\"pageId\":\"9cb9f74c-1fd3-40ae-9802-786c70b2725b\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"系统属性字典配置列表\",\"enName\":\"\",\"name\":\"返回上一级\",\"dynamicPageId\":\"18\",\"actType\":2000,\"clientScript\":\"var pid=Comm.getUrlParam(&quot;pid&quot;);\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize()+&quot;&amp;pid=&quot;+pid,\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data.length==36 ){\\r\\n\\t\\t\\tlocation.href=basePath + &quot;document/view.do?dynamicPageId=18&amp;pid=&quot;+data;\\r\\n\\t\\t}else if(data==\'null\'){\\r\\n\\t\\t\\tlocation.href=basePath + &quot;document/view.do?dynamicPageId=18&quot;;\\r\\n\\t\\t}else{\\r\\n\\t\\t\\tComm.alert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"chooseValidate\":false,\"order\":6}', null, null, '110', '18', '6', '1', '1', '9999', '9999', '9999', '2017-10-31 10:43:09', '2017-10-31 10:43:09', null);
INSERT INTO `p_fm_store` VALUES ('9d479de4-e5a6-4924-98fc-69371e880128', '0.1.7.1513591143210', '882c4f948d0b6f97bba05653ab4ef094', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"description\":\"\",\"title\":\"类型\",\"selectNumber\":\"0\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row1_cod2\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":12,\"componentType\":\"1006\",\"hiddenScript\":\"\",\"optionScript\":\"\\\"0=sql;1=其他;\\\";\",\"dataItemCode\":\"p_fm_api_privilege.TYPE\",\"pageId\":\"9d479de4-e5a6-4924-98fc-69371e880128\",\"layoutId\":\"45d93c90-970f-4eee-8f91-cfe1c4a382f5\",\"defaultValueScript\":\"\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"882c4f948d0b6f97bba05653ab4ef094\",\"dynamicPageId\":\"19\",\"style\":\"\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '19', '12', null, '1', '9999', '9999', '9999', '2017-09-19 19:55:01', '2017-09-19 19:55:01', null);
INSERT INTO `p_fm_store` VALUES ('9da08795-b7c5-48c9-bc5e-07ce6f07c86e', '0.1.5.1505823178708', '编辑', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count!=1){\\r\\n\\talert(&quot;请选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar id = $(&quot;input[name=\'_selects\']&quot;).val();\\r\\nlocation.href = basePath + &quot;document/view.do?dynamicPageId=19&amp;id=&quot; + id;\",\"code\":\"0.1.5.1505823178708\",\"color\":\"success\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":20,\"dynamicPageName\":\"API权限编辑控制列表兼弹窗\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-edit\",\"name\":\"编辑\",\"order\":2,\"pageId\":\"9da08795-b7c5-48c9-bc5e-07ce6f07c86e\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '20', '2', '1', '1', '9999', '9999', '9999', '2017-09-19 20:12:59', '2017-09-19 20:12:59', null);
INSERT INTO `p_fm_store` VALUES ('9e3226b5-27ab-4dce-9935-3ed1ab992240', '0.1.5.1505467167033', '搜索', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"$(&quot;#groupForm&quot;).submit();\",\"code\":\"0.1.5.1505467167033\",\"color\":\"success\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-search\",\"name\":\"搜索\",\"order\":11,\"pageId\":\"9e3226b5-27ab-4dce-9935-3ed1ab992240\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '1', '11', '1', '1', '9999', '9999', '9999', '2017-09-15 17:19:27', '2017-09-15 17:19:27', null);
INSERT INTO `p_fm_store` VALUES ('9e88c9f1-d5be-4df5-b8ba-e60dcc2037e3', '0.1.8.1502440575644', '_row1_cod1', '{\"dynamicPageId\":5,\"layoutType\":1,\"left\":0,\"name\":\"_row1_cod1\",\"order\":1,\"pageId\":\"9e88c9f1-d5be-4df5-b8ba-e60dcc2037e3\",\"parentId\":\"545c7e14-acb0-49e7-89f4-cfbd8eecffa8\",\"proportion\":12,\"top\":0}', '545c7e14-acb0-49e7-89f4-cfbd8eecffa8', null, '110', '5', '1', null, '1', '9999', '9999', '9999', '2017-08-11 16:36:16', '2017-08-11 16:36:16', null);
INSERT INTO `p_fm_store` VALUES ('9f1e60b0-435c-4471-aa94-08fdfd39c8de', '0.1.8.1493714255164', '_row3_cod1', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":1,\"left\":0,\"name\":\"_row3_cod1\",\"order\":3,\"pageId\":\"9f1e60b0-435c-4471-aa94-08fdfd39c8de\",\"parentId\":\"0f99411b-ea2b-4c21-ba77-c56b90da8c4b\",\"proportion\":2,\"systemId\":111,\"top\":0}', '0f99411b-ea2b-4c21-ba77-c56b90da8c4b', null, '111', '2020', '3', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:05', '2017-05-03 11:07:05', '1');
INSERT INTO `p_fm_store` VALUES ('9f234c39-26ce-4760-9fec-a1f262f22297', '0.1.5.1505872811703', '确定关联', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar priid=Comm.getUrlParam(&quot;priid&quot;)\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize()+&quot;&amp;priid=&quot;+priid,\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;关联成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=1&amp;pri=true&amp;priid=&quot;+priid;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"code\":\"0.1.5.1505872811703\",\"color\":\"primary\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"var a=request.getParameter(&quot;relation&quot;);\\r\\nif(a!=\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-save\",\"name\":\"确定关联\",\"order\":9,\"pageId\":\"9f234c39-26ce-4760-9fec-a1f262f22297\",\"serverScript\":\"var priid=request.getParameter(&quot;priid&quot;);\\r\\nvar retVal = &quot;1&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nvar sql = DocumentUtils.excuteUpdate(&quot;insert into p_fm_api_privilege_relation(API_ID,PRI_ID) VALUES (\'&quot;+ids[i]+&quot;\',\'&quot;+priid+&quot;\')&quot;);\\r\\nif(sql!=1){\\r\\nretVal=&quot;系统错误！&quot;;\\r\\n}\\r\\n}\\r\\nretVal;\",\"systemId\":110}', null, null, '110', '1', '9', '1', '1', '9999', '9999', '9999', '2017-09-20 10:00:12', '2017-09-20 10:00:12', null);
INSERT INTO `p_fm_store` VALUES ('9f4932c4-b50f-4145-bd30-cb4de1fff7c7', '0.1.7.1493716124603', 'APIID', '{\"componentType\":\"1010\",\"dataItemCode\":\"p_fm_api_describe.APIID\",\"defaultValueScript\":\"request.getParameter(\\\"22de342f-18b2-4a8d-a23f-d4f30bff5ba3\\\");\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"hiddenScript\":\"\",\"layoutId\":\"16520285-2ab0-42ad-9e3a-8e037efdd07f\",\"layoutName\":\"_row2_cod2\",\"name\":\"APIID\",\"order\":1,\"pageId\":\"9f4932c4-b50f-4145-bd30-cb4de1fff7c7\",\"readonlyScript\":\"\",\"systemId\":111}', null, null, '111', '2020', '1', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('a090523f-cdf1-44f6-bdfc-0c7783ee6acd', '0.1.7.1503306988526', '761bb2191e0dbd7b064a5caa9885309a', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"是否登录\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api.API_IS_LOGIN\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"1\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"761bb2191e0dbd7b064a5caa9885309a\",\"order\":8,\"orderby\":\"1\",\"pageId\":\"a090523f-cdf1-44f6-bdfc-0c7783ee6acd\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"8%\"}', null, null, '110', '1', '8', null, '1', '9999', '9999', '9999', '2017-04-18 17:42:04', '2017-04-18 17:42:04', '1');
INSERT INTO `p_fm_store` VALUES ('a3083825-6ae9-47f9-be56-68864ab425c9', '0.1.8.1513578936589', '_row3', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row3\",\"dynamicPageId\":\"2\",\"layoutType\":2,\"pageId\":\"a3083825-6ae9-47f9-be56-68864ab425c9\",\"parentId\":\"\",\"order\":3}', null, null, '110', '2', '3', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('a3d91d29-a576-4c2a-bfc4-74df8c2896e4', '0.1.5.1493714886428', '保存', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"var apiid=$(&quot;#6f901b0c-c88e-44c5-be21-2ca37ff8e577&quot;).val();\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\r\\n\\t$.ajax({\\r\\n\\t   type: &quot;POST&quot;,\\r\\n\\t   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n\\t   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n\\t   async : false,\\r\\n\\t   success: function(data){\\r\\n\\t\\t\\tif(data==1){\\r\\n\\t\\t\\t\\talert(&quot;保存成功&quot;);\\r\\n\\t\\t\\t\\ttop.dialog({id : window.name}).close();\\r\\n$(&quot;#8555626c-6361-4380-ab2a-4084596937ed&quot;).dataGrid({url:\'${basePath}document/refreshDataGrid.do?componentId=8555626c-6361-4380-ab2a-4084596937ed\'});\\r\\n\\t\\t\\t}\\r\\n\\t\\t\\telse{\\r\\n\\t\\t\\t\\talert(data);\\r\\n\\t\\t\\t}\\r\\n\\t   }\\r\\n\\t});\",\"code\":\"0.1.5.1493714886428\",\"color\":\"primary\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-save\",\"name\":\"保存\",\"order\":1,\"pageId\":\"a3d91d29-a576-4c2a-bfc4-74df8c2896e4\",\"serverScript\":\"var id = DocumentUtils.getDataItem(&quot;p_fm_api_describe&quot;,&quot;ID&quot;);\\r\\nvar retVal = &quot;1&quot;;\\r\\nif(id == null || id == &quot;&quot;) {\\r\\n\\tid = UUID.randomUUID().toString();\\r\\n\\tDocumentUtils.setDataItem(&quot;p_fm_api_describe&quot;,&quot;ID&quot;,id);\\r\\n\\tif(DocumentUtils.saveData(&quot;p_fm_api_describe&quot;) == null &amp;&amp; DocumentUtils.saveDocument() == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n} \\r\\nelse {\\r\\n\\tif(DocumentUtils.updateData(&quot;p_fm_api_describe&quot;) == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n}\\r\\nretVal;\",\"systemId\":111}', null, null, '111', '2020', '1', '1', '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('a4f696db-a9be-46b4-a7cd-4849417056da', '0.1.7.1493714336731', 'cd6572bd53b9992965d8883ed78f42d7', '{\"backgroundcolor\":\"white\",\"componentType\":\"1009\",\"css\":\"\",\"dataItemCode\":\"\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"isRequired\":\"2\",\"layoutId\":\"ad2d5134-8381-4248-b166-51df38386240\",\"layoutName\":\"_row1_cod1\",\"lineheight\":\"1\",\"name\":\"cd6572bd53b9992965d8883ed78f42d7\",\"order\":2,\"pageId\":\"a4f696db-a9be-46b4-a7cd-4849417056da\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"title\":\"参数编辑表单\"}', null, null, '111', '2020', '2', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('a50bb634-0471-404f-a123-9a34066418ee', '0.1.8.1513578936473', '_row2_cod3', '{\"proportion\":3,\"top\":0,\"left\":0,\"name\":\"_row2_cod3\",\"dynamicPageId\":\"2\",\"layoutType\":1,\"pageId\":\"a50bb634-0471-404f-a123-9a34066418ee\",\"parentId\":\"74203716-54fc-4964-b484-cfd09d8e32ef\",\"order\":6}', '74203716-54fc-4964-b484-cfd09d8e32ef', null, '110', '2', '6', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:36', '2017-12-18 14:35:36', null);
INSERT INTO `p_fm_store` VALUES ('a5e235c6-b319-4533-bda3-162072e8a64c', '0.1.8.1513579449326', '_row6_cod1', '{\"proportion\":\"12\",\"textstyle\":\"\",\"pageId\":\"a5e235c6-b319-4533-bda3-162072e8a64c\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"35c5bd13-f1fd-4c26-b7df-bce367f63fad\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row6_cod1\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"layoutType\":\"1\",\"order\":6,\"height\":\"\"}', '35c5bd13-f1fd-4c26-b7df-bce367f63fad', null, '110', '2', '6', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:38', '2017-12-18 14:35:38', null);
INSERT INTO `p_fm_store` VALUES ('a72417ba-8efe-4e1c-9339-833e357bf94a', '0.1.7.1513579176874', 'e41c4ba928af1bcce05e71d66424bd97', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"输出示例\",\"validateInputTip\":\"\",\"layoutName\":\"_row6_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"10\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":61,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api.API_OUTPUT\",\"pageId\":\"a72417ba-8efe-4e1c-9339-833e357bf94a\",\"layoutId\":\"a5e235c6-b319-4533-bda3-162072e8a64c\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\\\"无\\\";\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"e41c4ba928af1bcce05e71d66424bd97\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '2', '61', null, '1', '9999', '9999', '9999', '2017-05-02 16:57:08', '2017-05-02 16:57:08', '1');
INSERT INTO `p_fm_store` VALUES ('a8806732-e509-4550-91d8-162cb8ea24f8', '0.1.8.1493714254858', '_row1', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":2,\"left\":0,\"name\":\"_row1\",\"order\":1,\"pageId\":\"a8806732-e509-4550-91d8-162cb8ea24f8\",\"parentId\":\"\",\"proportion\":12,\"systemId\":111,\"top\":0}', null, null, '111', '2020', '1', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:05', '2017-05-03 11:07:05', '1');
INSERT INTO `p_fm_store` VALUES ('a8be2469-80a5-4b5d-9ff6-2c4033e107c4', '0.1.7.1513318201638', 'b3ee459d1b0f96fca6b3332d12c9bc25', '{\"componentType\":\"1036\",\"searchLocation\":\"1\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"selectOption\":\"1=1;2=2;3=3\",\"dateSelectName\":\"\",\"dateSelectLabel\":\"\",\"pageId\":\"a8be2469-80a5-4b5d-9ff6-2c4033e107c4\",\"layoutId\":\"\",\"selectName\":\"level\",\"userSelectLabel\":\"\",\"defaultValueScript\":\"\",\"layoutName\":\"root\",\"userSelectName\":\"\",\"textName\":\"name\",\"textLabel\":\"名称\",\"name\":\"b3ee459d1b0f96fca6b3332d12c9bc25\",\"dynamicPageId\":\"18\",\"selectLabel\":\"级层\",\"readonlyScript\":\"\",\"order\":1}', null, null, '110', '18', '1', null, '1', '9999', '9999', '9999', '2017-09-09 16:07:50', '2017-09-09 16:07:50', null);
INSERT INTO `p_fm_store` VALUES ('a8d978b4-4bf6-4fe9-91c5-4414a9b42003', '0.1.5.1508325357292', '搜索', '{\"serverScript\":\"\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1508325357292\",\"color\":\"success\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-search\",\"description\":\"\",\"pageId\":\"a8d978b4-4bf6-4fe9-91c5-4414a9b42003\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"已绑定API列表\",\"enName\":\"\",\"name\":\"搜索\",\"dynamicPageId\":7,\"actType\":2000,\"clientScript\":\"$(&quot;#groupForm&quot;).submit();\",\"chooseValidate\":false,\"order\":4}', null, null, '110', '7', '4', '1', '1', '9999', '9999', '9999', '2017-10-18 19:15:57', '2017-10-18 19:15:57', null);
INSERT INTO `p_fm_store` VALUES ('a9c6bed7-11fb-4042-aa0c-9d21f101ba8d', '0.1.7.1503306525125', '2e4ebaa7b3b59912bef217445242f707', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"API状态\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api.API_STATE\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"1\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"2e4ebaa7b3b59912bef217445242f707\",\"order\":3,\"orderby\":\"1\",\"pageId\":\"a9c6bed7-11fb-4042-aa0c-9d21f101ba8d\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"5%\"}', null, null, '110', '1', '3', null, '1', '9999', '9999', '9999', '2017-03-26 17:05:10', '2017-03-26 17:05:10', '1');
INSERT INTO `p_fm_store` VALUES ('ab50a3d1-abd1-4fbc-a4ba-0c1413c830f0', '0.1.8.1513579821453', '_row1_cod3', '{\"proportion\":\"3\",\"textstyle\":\"\",\"pageId\":\"ab50a3d1-abd1-4fbc-a4ba-0c1413c830f0\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"28e55667-9b90-4ff4-a03c-8d6854147445\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row1_cod3\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"layoutType\":\"1\",\"order\":3,\"height\":\"\"}', '28e55667-9b90-4ff4-a03c-8d6854147445', null, '110', '2', '3', null, '1', '9999', '9999', '9999', '2017-12-18 14:50:21', '2017-12-18 14:50:21', null);
INSERT INTO `p_fm_store` VALUES ('acfd5adb-795d-4a76-b088-2d36b7cf18c0', '0.1.8.1513579808085', '_row1_cod2', '{\"proportion\":\"6\",\"textstyle\":\"\",\"pageId\":\"acfd5adb-795d-4a76-b088-2d36b7cf18c0\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"28e55667-9b90-4ff4-a03c-8d6854147445\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row1_cod2\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"layoutType\":\"1\",\"order\":2,\"height\":\"\"}', '28e55667-9b90-4ff4-a03c-8d6854147445', null, '110', '2', '2', null, '1', '9999', '9999', '9999', '2017-12-18 14:50:08', '2017-12-18 14:50:08', null);
INSERT INTO `p_fm_store` VALUES ('ad2d5134-8381-4248-b166-51df38386240', '0.1.8.1493714265408', '_row1_cod1', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"1\",\"left\":\"\",\"name\":\"_row1_cod1\",\"order\":1,\"pageId\":\"ad2d5134-8381-4248-b166-51df38386240\",\"parentId\":\"a8806732-e509-4550-91d8-162cb8ea24f8\",\"proportion\":\"12\",\"systemId\":111,\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', 'a8806732-e509-4550-91d8-162cb8ea24f8', null, '111', '2020', '1', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('ad36fbe4-de5e-4ed8-86be-2422e3480b1f', '0.1.7.1513591193611', '3e56b9fc74b6ad988e248bf0b53a7f92', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"备注\",\"validateInputTip\":\"\",\"layoutName\":\"_row5_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"3\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":51,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_privilege.REMARK\",\"pageId\":\"ad36fbe4-de5e-4ed8-86be-2422e3480b1f\",\"layoutId\":\"adafacde-bda7-4f3f-9634-b5ba911ecddb\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"3e56b9fc74b6ad988e248bf0b53a7f92\",\"textalign\":\"居左\",\"dynamicPageId\":\"19\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '19', '51', null, '1', '9999', '9999', '9999', '2017-09-19 19:56:46', '2017-09-19 19:56:46', null);
INSERT INTO `p_fm_store` VALUES ('ad999fa4-33f1-40b9-ad1a-4fc2828b3387', '0.1.7.1513599165389', '53a520bae96818822a3783f4de3f34f6', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"排序\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row1_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":11,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_data_dictionary.DATA_ORDER\",\"textType\":\"text\",\"pageId\":\"ad999fa4-33f1-40b9-ad1a-4fc2828b3387\",\"layoutId\":\"03fa3296-7ec6-4d05-b464-2b8c44e9adbc\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"53a520bae96818822a3783f4de3f34f6\",\"textalign\":\"居左\",\"dynamicPageId\":\"17\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '17', '11', null, '1', '9999', '9999', '9999', '2017-09-09 16:16:01', '2017-09-09 16:16:01', null);
INSERT INTO `p_fm_store` VALUES ('adafacde-bda7-4f3f-9634-b5ba911ecddb', '0.1.8.1513591081532', '_row5_cod1', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row5_cod1\",\"dynamicPageId\":\"19\",\"layoutType\":1,\"pageId\":\"adafacde-bda7-4f3f-9634-b5ba911ecddb\",\"parentId\":\"5bab06d9-82bd-41f5-b2ab-1f074d2da9e0\",\"order\":5}', '5bab06d9-82bd-41f5-b2ab-1f074d2da9e0', null, '110', '19', '5', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:02', '2017-12-18 17:58:02', null);
INSERT INTO `p_fm_store` VALUES ('ae88ca65-852a-4daf-8e06-c407ee62d21a', '0.1.5.1493714918001', '关闭', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"top.dialog({id : window.name}).close();\",\"code\":\"0.1.5.1493714918001\",\"color\":\"danger\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-remove\",\"name\":\"关闭\",\"order\":2,\"pageId\":\"ae88ca65-852a-4daf-8e06-c407ee62d21a\",\"serverScript\":\"\",\"systemId\":111}', null, null, '111', '2020', '2', '1', '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('aea3d763-3d8f-429a-8fdc-cc138176b7db', '0.1.8.1513591081212', '_row3', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row3\",\"dynamicPageId\":\"19\",\"layoutType\":2,\"pageId\":\"aea3d763-3d8f-429a-8fdc-cc138176b7db\",\"parentId\":\"\",\"order\":3}', null, null, '110', '19', '3', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('b24f1c86-6519-4f72-aa88-13bc469177b5', '0.1.8.1513579438751', '_row4_cod1', '{\"proportion\":\"12\",\"textstyle\":\"\",\"pageId\":\"b24f1c86-6519-4f72-aa88-13bc469177b5\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"37c5bfda-84e0-4830-a2e1-2a6797a5c73e\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row4_cod1\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"layoutType\":\"1\",\"order\":4,\"height\":\"\"}', '37c5bfda-84e0-4830-a2e1-2a6797a5c73e', null, '110', '2', '4', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:37', '2017-12-18 14:35:37', null);
INSERT INTO `p_fm_store` VALUES ('b27b1e0e-2f2d-4b94-9bf6-82a8fbbc8e07', '0.1.8.1493714255119', '_row2_cod4', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":1,\"left\":0,\"name\":\"_row2_cod4\",\"order\":8,\"pageId\":\"b27b1e0e-2f2d-4b94-9bf6-82a8fbbc8e07\",\"parentId\":\"499d4e8a-b956-4e31-a60b-a2b1a3609340\",\"proportion\":4,\"systemId\":111,\"top\":0}', '499d4e8a-b956-4e31-a60b-a2b1a3609340', null, '111', '2020', '8', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('b3cb4562-dca3-4cba-bec1-bf6ded4c522f', '0.1.8.1513590664935', '_row3', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row3\",\"dynamicPageId\":\"3\",\"layoutType\":2,\"pageId\":\"b3cb4562-dca3-4cba-bec1-bf6ded4c522f\",\"parentId\":\"\",\"order\":3}', null, null, '110', '3', '3', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:05', '2017-12-18 17:51:05', null);
INSERT INTO `p_fm_store` VALUES ('b68250bb-14f8-48f3-b8da-6ef3fdc1f97e', '0.1.5.1508325845607', '搜索', '{\"serverScript\":\"\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1508325845607\",\"color\":\"info\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-search\",\"description\":\"\",\"pageId\":\"b68250bb-14f8-48f3-b8da-6ef3fdc1f97e\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"未绑定绑定API列表\",\"enName\":\"\",\"name\":\"搜索\",\"dynamicPageId\":6,\"actType\":2000,\"clientScript\":\"$(&quot;#groupForm&quot;).submit();\",\"chooseValidate\":false,\"order\":3}', null, null, '110', '6', '3', '1', '1', '9999', '9999', '9999', '2017-10-18 19:24:06', '2017-10-18 19:24:06', null);
INSERT INTO `p_fm_store` VALUES ('b77ab1fb-9a6b-4275-ba7b-b32fb59807de', '0.1.7.1493717806804', '0273716065e800348c8818da9953fa2d', '{\"componentType\":\"1006\",\"css\":\"\",\"dataItemCode\":\"p_fm_api_describe.TYPE\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"hiddenScript\":\"\",\"layoutId\":\"e98610de-bc6d-4b2c-a95d-854a8f1946d7\",\"layoutName\":\"_row3_cod4\",\"name\":\"0273716065e800348c8818da9953fa2d\",\"onchangeScript\":\"\",\"optionScript\":\"\\\"varchar=varchar;number=number;boolean=boolean;\\\";\",\"order\":10,\"pageId\":\"b77ab1fb-9a6b-4275-ba7b-b32fb59807de\",\"readonlyScript\":\"\",\"selectNumber\":\"\",\"style\":\"\",\"systemId\":111,\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\"}', null, null, '111', '2020', '10', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('bae5bf70-94e9-4b3d-b8fc-3c3c16ef5abf', '0.1.5.1505824404816', 'API权限编辑', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"location.href = basePath + &quot;document/view.do?dynamicPageId=20&amp;edit=true&quot;;\",\"code\":\"0.1.5.1505824404816\",\"color\":\"info\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"var a=request.getParameter(&quot;pri&quot;);\\r\\nvar b=request.getParameter(&quot;relation&quot;);\\r\\nif(a==\'true\' || b==\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-edit\",\"name\":\"API权限编辑\",\"order\":6,\"pageId\":\"bae5bf70-94e9-4b3d-b8fc-3c3c16ef5abf\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '1', '6', '1', '1', '9999', '9999', '9999', '2017-09-19 20:33:25', '2017-09-19 20:33:25', null);
INSERT INTO `p_fm_store` VALUES ('bbcc8d68-ac53-4d20-85fd-3c720f0d4bf4', '0.1.8.1513599001647', '_row1_cod2', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row1_cod2\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"bbcc8d68-ac53-4d20-85fd-3c720f0d4bf4\",\"parentId\":\"c168bf66-ea0e-457f-b56d-18708e52ac4a\",\"order\":2}', 'c168bf66-ea0e-457f-b56d-18708e52ac4a', null, '110', '17', '2', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('bc10ac5b-2ab7-477a-a960-1f5bb4a4077a', '0.1.5.1505872065680', '新增关联接口', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"var priid=Comm.getUrlParam(&quot;priid&quot;);\\r\\nlocation.href = basePath+&quot;document/view.do?dynamicPageId=1&amp;relation=true&amp;priid=&quot;+priid;\",\"code\":\"0.1.5.1505872065680\",\"color\":\"primary\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"var a=request.getParameter(&quot;pri&quot;);\\r\\nif(a!=\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-plus\",\"name\":\"新增关联接口\",\"order\":6,\"pageId\":\"bc10ac5b-2ab7-477a-a960-1f5bb4a4077a\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '1', '6', '1', '1', '9999', '9999', '9999', '2017-09-20 09:47:46', '2017-09-20 09:47:46', null);
INSERT INTO `p_fm_store` VALUES ('bd23b782-f45a-40de-a96a-bb7ce828b1db', '0.1.5.1493714918001', '关闭', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"top.dialog({id : window.name}).close();\",\"code\":\"0.1.5.1493714918001\",\"color\":\"danger\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":3,\"dynamicPageName\":\"API参数表单\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-remove\",\"name\":\"关闭\",\"order\":2,\"pageId\":\"bd23b782-f45a-40de-a96a-bb7ce828b1db\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '3', '2', '1', '1', '9999', '9999', '9999', '2017-05-02 16:48:38', '2017-05-02 16:48:38', '1');
INSERT INTO `p_fm_store` VALUES ('be0e4d86-c284-4c08-9ccb-ec4b8b5b140e', '0.1.10.1500014739616', 'saveForUuidID', '{\"code\":\"0.1.10.1500014739616\",\"description\":\"普通页面保存，主键为UUID\",\"name\":\"saveForUuidID\",\"pageId\":\"be0e4d86-c284-4c08-9ccb-ec4b8b5b140e\",\"script\":\"function saveForUuidID(DataSource){\\r\\n\\tvar id = DocumentUtils.getDataItem(DataSource,&quot;ID&quot;);\\r\\n\\tvar retVal = &quot;1&quot;;\\r\\n\\tif(id == null || id == &quot;&quot;) {\\r\\n\\t\\tid = UUID.randomUUID().toString();\\r\\n\\t\\tDocumentUtils.setDataItem(DataSource,&quot;ID&quot;,id);\\r\\n\\t\\tif(DocumentUtils.saveData(DataSource) == null &amp;&amp; DocumentUtils.saveDocument() == null){\\r\\n\\t\\t\\tretVal = &quot;保存失败！&quot;;\\r\\n\\t\\t}\\r\\n\\t} \\r\\n\\telse {\\r\\n\\t\\tif(DocumentUtils.updateData(DataSource) == null){\\r\\n\\t\\t\\tretVal = &quot;更新失败！&quot;;\\r\\n\\t\\t}\\r\\n\\t}\\r\\n\\treturn retVal;\\r\\n}\",\"systemId\":110}', '普通页面保存，主键为UUID', null, '110', null, null, null, '1', '9999', '9999', '9999', '2017-07-14 14:45:40', '2017-07-14 14:45:40', '1');
INSERT INTO `p_fm_store` VALUES ('bea63937-7533-46fd-a261-f57f454998fe', '0.1.7.1509418706220', 'a4b80c4c842973475007ed824320a043', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":7,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_data_dictionary.DATA_ORDER\",\"excelShowScript\":\"\",\"pageId\":\"bea63937-7533-46fd-a261-f57f454998fe\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"a4b80c4c842973475007ed824320a043\",\"width\":\"\",\"dynamicPageId\":\"18\",\"validateErrorTip\":\"\",\"columnName\":\"排序\"}', null, null, '110', '18', '7', null, '1', '9999', '9999', '9999', '2017-09-09 15:36:21', '2017-09-09 15:36:21', null);
INSERT INTO `p_fm_store` VALUES ('bf766420-6706-4691-bf4c-03796ebead73', '0.1.5.1505822289948', '保存', '{\"actType\":2000,\"buttonGroup\":1,\"chooseValidate\":false,\"clientScript\":\"var actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\r\\n\\t$.ajax({\\r\\n\\t   type: &quot;POST&quot;,\\r\\n\\t   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n\\t   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n\\t   async : false,\\r\\n\\t   success: function(data){\\r\\n\\t\\t\\tif(data==1){\\r\\n\\t\\t\\t\\talert(&quot;保存成功&quot;);\\r\\n\\t\\t\\t\\tlocation.href= basePath + &quot;document/view.do?dynamicPageId=20&amp;edit=true&quot;;\\r\\n\\t\\t\\t}\\r\\n\\t\\t\\telse{\\r\\n\\t\\t\\t\\talert(data);\\r\\n\\t\\t\\t}\\r\\n\\t   }\\r\\n\\t});\",\"code\":\"0.1.5.1505822289948\",\"color\":\"primary\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":19,\"dynamicPageName\":\"API权限编辑控制表单\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-save\",\"name\":\"保存\",\"order\":1,\"pageId\":\"bf766420-6706-4691-bf4c-03796ebead73\",\"serverScript\":\"var id = DocumentUtils.getDataItem(&quot;p_fm_api_privilege&quot;,&quot;ID&quot;);\\r\\nvar retVal = &quot;1&quot;;\\r\\nif(id == null || id == &quot;&quot;) {\\r\\n\\tid = UUID.randomUUID().toString();\\r\\n\\tDocumentUtils.setDataItem(&quot;p_fm_api_privilege&quot;,&quot;ID&quot;,id);\\r\\n\\tif(DocumentUtils.saveData(&quot;p_fm_api_privilege&quot;) == null &amp;&amp; DocumentUtils.saveDocument() == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n} \\r\\nelse {\\r\\n\\tif(DocumentUtils.updateData(&quot;p_fm_api_privilege&quot;) == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n}\\r\\nretVal;\",\"systemId\":110}', null, null, '110', '19', '1', '1', '1', '9999', '9999', '9999', '2017-09-19 19:58:10', '2017-09-19 19:58:10', null);
INSERT INTO `p_fm_store` VALUES ('c0a27a2a-4430-4795-814b-7585e4a4308d', '0.1.7.1513579328755', '723d082b1c163b525b0cdfe1b5fab8a6', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"API名称\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row2_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":21,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api.API_NAME\",\"textType\":\"text\",\"pageId\":\"c0a27a2a-4430-4795-814b-7585e4a4308d\",\"layoutId\":\"145e3aff-cf5f-45ca-b4f3-7948a9ecfdd4\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"723d082b1c163b525b0cdfe1b5fab8a6\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '2', '21', null, '1', '9999', '9999', '9999', '2017-03-26 17:09:38', '2017-03-26 17:09:38', '1');
INSERT INTO `p_fm_store` VALUES ('c164c6f7-b797-4002-a965-f88ab994b830', '0.1.8.1500014321348', '_row2_cod2', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":\"5\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"1\",\"left\":\"\",\"name\":\"_row2_cod2\",\"order\":4,\"pageId\":\"c164c6f7-b797-4002-a965-f88ab994b830\",\"parentId\":\"c3e77840-a90b-464f-8044-568d01679ca8\",\"proportion\":\"10\",\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', 'c3e77840-a90b-464f-8044-568d01679ca8', null, '110', '5', '4', null, '1', '9999', '9999', '9999', '2017-07-14 14:38:02', '2017-07-14 14:38:02', '1');
INSERT INTO `p_fm_store` VALUES ('c168bf66-ea0e-457f-b56d-18708e52ac4a', '0.1.8.1513599001541', '_row1', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row1\",\"dynamicPageId\":\"17\",\"layoutType\":2,\"pageId\":\"c168bf66-ea0e-457f-b56d-18708e52ac4a\",\"parentId\":\"\",\"order\":1}', null, null, '110', '17', '1', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('c3e77840-a90b-464f-8044-568d01679ca8', '0.1.8.1502440609294', '_row2', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":\"5\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"2\",\"left\":\"\",\"name\":\"_row2\",\"order\":2,\"pageId\":\"c3e77840-a90b-464f-8044-568d01679ca8\",\"parentId\":\"\",\"proportion\":\"12\",\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', null, null, '110', '5', '2', null, '1', '9999', '9999', '9999', '2017-07-14 14:38:02', '2017-07-14 14:38:02', '1');
INSERT INTO `p_fm_store` VALUES ('c52f8a00-7d5e-4b67-a80d-1ec6613129ef', '0.1.8.1493717873469', '_row3_cod5', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"1\",\"left\":\"\",\"name\":\"_row3_cod5\",\"order\":15,\"pageId\":\"c52f8a00-7d5e-4b67-a80d-1ec6613129ef\",\"parentId\":\"0f99411b-ea2b-4c21-ba77-c56b90da8c4b\",\"proportion\":\"2\",\"systemId\":111,\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', '0f99411b-ea2b-4c21-ba77-c56b90da8c4b', null, '111', '2020', '15', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('c6ea1ace-786b-44a9-83c4-52c5445b1daf', '0.1.7.1508322860878', '4625097cc6cbeb6dfc35473487feec07', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":2,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_NAME\",\"excelShowScript\":\"\",\"pageId\":\"c6ea1ace-786b-44a9-83c4-52c5445b1daf\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"4625097cc6cbeb6dfc35473487feec07\",\"width\":\"\",\"dynamicPageId\":\"6\",\"validateErrorTip\":\"\",\"columnName\":\"API名称\"}', null, null, '110', '6', '2', null, '1', '9999', '9999', '9999', '2017-10-18 18:34:21', '2017-10-18 18:34:21', null);
INSERT INTO `p_fm_store` VALUES ('ca4979d5-ffa9-4f68-9918-831ac3b0b749', '0.1.7.1505119254127', '32dd835f45e8c3340011b8f1a94ea90c', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"类型\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api.API_TYPE\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"1\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"32dd835f45e8c3340011b8f1a94ea90c\",\"order\":6,\"orderby\":\"1\",\"pageId\":\"ca4979d5-ffa9-4f68-9918-831ac3b0b749\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"10%\"}', null, null, '110', '1', '6', null, '1', '9999', '9999', '9999', '2017-03-26 17:39:59', '2017-03-26 17:39:59', '1');
INSERT INTO `p_fm_store` VALUES ('cbac7213-501b-465f-86c2-c2a8f3394aa2', '0.1.8.1493714255205', '_row3_cod3', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":1,\"left\":0,\"name\":\"_row3_cod3\",\"order\":9,\"pageId\":\"cbac7213-501b-465f-86c2-c2a8f3394aa2\",\"parentId\":\"0f99411b-ea2b-4c21-ba77-c56b90da8c4b\",\"proportion\":2,\"systemId\":111,\"top\":0}', '0f99411b-ea2b-4c21-ba77-c56b90da8c4b', null, '111', '2020', '9', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:05', '2017-05-03 11:07:05', '1');
INSERT INTO `p_fm_store` VALUES ('ccd1b7b6-7179-45ff-a558-635bcae85078', '0.1.8.1513599001985', '_row2_cod3', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row2_cod3\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"ccd1b7b6-7179-45ff-a558-635bcae85078\",\"parentId\":\"5c6eb9b5-03ca-472f-a206-4b9d58b8eace\",\"order\":6}', '5c6eb9b5-03ca-472f-a206-4b9d58b8eace', null, '110', '17', '6', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('ce6a7eae-5c51-440e-9140-9fba88739a7e', '0.1.8.1513591081388', '_row4_cod1', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row4_cod1\",\"dynamicPageId\":\"19\",\"layoutType\":1,\"pageId\":\"ce6a7eae-5c51-440e-9140-9fba88739a7e\",\"parentId\":\"5604581e-fea0-48a1-864c-bd09943efe96\",\"order\":4}', '5604581e-fea0-48a1-864c-bd09943efe96', null, '110', '19', '4', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('cfaae037-00d4-4771-9bed-023af3e2bdd9', '0.1.8.1513590664401', '_row1_cod2', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row1_cod2\",\"dynamicPageId\":\"3\",\"layoutType\":1,\"pageId\":\"cfaae037-00d4-4771-9bed-023af3e2bdd9\",\"parentId\":\"8ec0e1b3-0c5f-48af-8b2e-730a06abecae\",\"order\":2}', '8ec0e1b3-0c5f-48af-8b2e-730a06abecae', null, '110', '3', '2', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:04', '2017-12-18 17:51:04', null);
INSERT INTO `p_fm_store` VALUES ('d02e0724-4428-47fa-9344-45f7264abd26', '0.1.7.1513318109257', '4657fec2f5c7f4e9d03f9ee87f4c7b70', '{\"componentType\":\"1036\",\"searchLocation\":\"1\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"selectOption\":\"\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t\",\"dateSelectName\":\"\",\"dateSelectLabel\":\"\",\"pageId\":\"d02e0724-4428-47fa-9344-45f7264abd26\",\"layoutId\":\"\",\"selectName\":\"\",\"userSelectLabel\":\"\",\"defaultValueScript\":\"\",\"layoutName\":\"root\",\"userSelectName\":\"\",\"textName\":\"id@name@desc\",\"textLabel\":\"ID@API名称@描述\",\"name\":\"4657fec2f5c7f4e9d03f9ee87f4c7b70\",\"dynamicPageId\":\"1\",\"selectLabel\":\"\",\"readonlyScript\":\"\",\"order\":10}', null, null, '110', '1', '10', null, '1', '9999', '9999', '9999', '2017-05-03 17:14:33', '2017-05-03 17:14:33', '1');
INSERT INTO `p_fm_store` VALUES ('d069b149-ba66-48b6-a52e-6cc2f44947bc', '0.1.8.1493714255053', '_row2_cod1', '{\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"layoutType\":1,\"left\":0,\"name\":\"_row2_cod1\",\"order\":2,\"pageId\":\"d069b149-ba66-48b6-a52e-6cc2f44947bc\",\"parentId\":\"499d4e8a-b956-4e31-a60b-a2b1a3609340\",\"proportion\":2,\"systemId\":111,\"top\":0}', '499d4e8a-b956-4e31-a60b-a2b1a3609340', null, '111', '2020', '2', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:05', '2017-05-03 11:07:05', '1');
INSERT INTO `p_fm_store` VALUES ('d6678ca1-4a47-4f10-8d31-f821e5ea4091', '0.1.8.1513590664764', '_row2_cod2', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row2_cod2\",\"dynamicPageId\":\"3\",\"layoutType\":1,\"pageId\":\"d6678ca1-4a47-4f10-8d31-f821e5ea4091\",\"parentId\":\"de3647cc-ea53-464c-bb4e-abbfcb0e58f1\",\"order\":4}', 'de3647cc-ea53-464c-bb4e-abbfcb0e58f1', null, '110', '3', '4', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:05', '2017-12-18 17:51:05', null);
INSERT INTO `p_fm_store` VALUES ('d670486d-e040-4fec-b162-782748cb8afb', '0.1.5.1502444262758', '关闭', '{\"serverScript\":\"\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1502444262758\",\"color\":\"warning\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-remove\",\"description\":\"\",\"pageId\":\"d670486d-e040-4fec-b162-782748cb8afb\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"模块添加页面\",\"enName\":\"\",\"name\":\"关闭\",\"dynamicPageId\":10,\"actType\":2000,\"clientScript\":\"top.dialog({id : window.name}).close();\",\"chooseValidate\":false,\"order\":2}', null, null, '110', '10', '2', '1', '1', '9999', '9999', '9999', '2017-08-11 17:37:43', '2017-08-11 17:37:43', null);
INSERT INTO `p_fm_store` VALUES ('d6db3231-d736-431f-b89f-fb66945e33df', '0.1.5.1504945257886', '保存', '{\"serverScript\":\"var id = DocumentUtils.getDataItem(&quot;p_data_dictionary&quot;,&quot;ID&quot;);\\r\\nvar retVal = &quot;1&quot;;\\r\\nif(id == null || id == &quot;&quot;) {\\r\\n\\tid = UUID.randomUUID().toString();\\r\\n\\tDocumentUtils.setDataItem(&quot;p_data_dictionary&quot;,&quot;ID&quot;,id);\\r\\n\\tif(DocumentUtils.saveData(&quot;p_data_dictionary&quot;) == null &amp;&amp; DocumentUtils.saveDocument() == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n} \\r\\nelse {\\r\\n\\tif(DocumentUtils.updateData(&quot;p_data_dictionary&quot;) == null){\\r\\n\\t\\tretVal = &quot;系统错误&quot;;\\r\\n\\t}\\r\\n}\\r\\nretVal;\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1504945257886\",\"color\":\"primary\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-save\",\"description\":\"\",\"pageId\":\"d6db3231-d736-431f-b89f-fb66945e33df\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"系统属性字典配置表单\",\"enName\":\"\",\"name\":\"保存\",\"dynamicPageId\":\"17\",\"actType\":2000,\"clientScript\":\"var actId = $(this).attr(&quot;id&quot;);\\r\\nvar pid=Comm.getUrlParam(&quot;pid&quot;);\\r\\nif(pid==null){\\r\\npid=&quot;&quot;;\\r\\n}\\r\\n$(&quot;#actId&quot;).val(actId);\\r\\n\\t$.ajax({\\r\\n\\t   type: &quot;POST&quot;,\\r\\n\\t   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n\\t   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n\\t   async : false,\\r\\n\\t   success: function(data){\\r\\n\\t\\t\\tif(data==1){\\r\\n\\t\\t\\t\\talert(&quot;保存成功&quot;);\\r\\n\\t\\t\\t\\tlocation.href= basePath + &quot;document/view.do?dynamicPageId=18&amp;pid=&quot;+pid;\\r\\n\\t\\t\\t}\\r\\n\\t\\t\\telse{\\r\\n\\t\\t\\t\\talert(data);\\r\\n\\t\\t\\t}\\r\\n\\t   }\\r\\n\\t});\",\"chooseValidate\":true,\"order\":1}', null, null, '110', '17', '1', '1', '1', '9999', '9999', '9999', '2017-09-09 16:20:58', '2017-09-09 16:20:58', null);
INSERT INTO `p_fm_store` VALUES ('d80a99fe-3fc5-4667-ae99-4d1d34c12152', '0.1.8.1513599001925', '_row2_cod2', '{\"proportion\":4,\"top\":0,\"left\":0,\"name\":\"_row2_cod2\",\"dynamicPageId\":\"17\",\"layoutType\":1,\"pageId\":\"d80a99fe-3fc5-4667-ae99-4d1d34c12152\",\"parentId\":\"5c6eb9b5-03ca-472f-a206-4b9d58b8eace\",\"order\":4}', '5c6eb9b5-03ca-472f-a206-4b9d58b8eace', null, '110', '17', '4', null, '1', '9999', '9999', '9999', '2017-12-18 20:10:02', '2017-12-18 20:10:02', null);
INSERT INTO `p_fm_store` VALUES ('da639ea4-2d6f-48c8-b213-71d5e28a1d18', '0.1.7.1513579257572', '5fc043ba56d6eda3c771775e7ac69f37', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"描述\",\"validateInputTip\":\"\",\"layoutName\":\"_row5_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"3\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":51,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api.API_DESC\",\"pageId\":\"da639ea4-2d6f-48c8-b213-71d5e28a1d18\",\"layoutId\":\"255348ec-0b5e-472b-bc96-2bf8a0b5f7c9\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\\\"无\\\";\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"5fc043ba56d6eda3c771775e7ac69f37\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '2', '51', null, '1', '9999', '9999', '9999', '2017-03-26 17:38:35', '2017-03-26 17:38:35', '1');
INSERT INTO `p_fm_store` VALUES ('dcce85d5-5c7a-43f2-be7b-c1f59aa12742', '0.1.8.1513579392037', '_row7_cod1', '{\"proportion\":\"12\",\"textstyle\":\"\",\"pageId\":\"dcce85d5-5c7a-43f2-be7b-c1f59aa12742\",\"textverticalalign\":\"垂直居中\",\"parentId\":\"48f32d60-adc4-4b8f-91c6-53cc32fadd78\",\"backgroundcolor\":\"white\",\"top\":\"\",\"heightType\":\"1\",\"left\":\"\",\"name\":\"_row7_cod1\",\"width\":\"\",\"textalign\":\"居左\",\"dynamicPageId\":\"2\",\"layoutType\":\"1\",\"order\":7,\"height\":\"\"}', '48f32d60-adc4-4b8f-91c6-53cc32fadd78', null, '110', '2', '7', null, '1', '9999', '9999', '9999', '2017-12-18 14:42:52', '2017-12-18 14:42:52', null);
INSERT INTO `p_fm_store` VALUES ('dcd83cde-c798-4e89-aa75-439e71b2c3aa', '0.1.5.1504942631196', '编辑', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count!=1){\\r\\n\\talert(&quot;请选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar id = $(&quot;input[name=\'_selects\']&quot;).val();\\r\\nlocation.href = basePath + &quot;document/view.do?dynamicPageId=17&amp;id=&quot; + id;\",\"code\":\"0.1.5.1504942631196\",\"color\":\"success\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":18,\"dynamicPageName\":\"系统属性字典配置列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-edit\",\"name\":\"编辑\",\"order\":2,\"pageId\":\"dcd83cde-c798-4e89-aa75-439e71b2c3aa\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '18', '2', '1', '1', '9999', '9999', '9999', '2017-09-09 15:37:11', '2017-09-09 15:37:11', null);
INSERT INTO `p_fm_store` VALUES ('dd3537ad-69f6-4cfe-8783-b2256249a472', '0.1.10.1500014763915', 'deleteRecord', '{\"code\":\"0.1.10.1500014763915\",\"description\":\"删除数据\",\"name\":\"deleteRecord\",\"pageId\":\"dd3537ad-69f6-4cfe-8783-b2256249a472\",\"script\":\"function deleteRecord(sql){\\r\\n\\tvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\n\\tfor(var i=0;i&lt;ids.length;i++){\\r\\n\\t\\tDocumentUtils.excuteUpdate(sql,ids[i]);\\r\\n\\t}\\r\\n\\t&quot;1&quot;;\\r\\n}\",\"systemId\":110}', '删除数据', null, '110', null, null, null, '1', '9999', '9999', '9999', '2017-07-14 14:46:04', '2017-07-14 14:46:04', '1');
INSERT INTO `p_fm_store` VALUES ('de3647cc-ea53-464c-bb4e-abbfcb0e58f1', '0.1.8.1513590664589', '_row2', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row2\",\"dynamicPageId\":\"3\",\"layoutType\":2,\"pageId\":\"de3647cc-ea53-464c-bb4e-abbfcb0e58f1\",\"parentId\":\"\",\"order\":2}', null, null, '110', '3', '2', null, '1', '9999', '9999', '9999', '2017-12-18 17:51:05', '2017-12-18 17:51:05', null);
INSERT INTO `p_fm_store` VALUES ('dea500a0-6caf-4e32-adb0-ef9e8a2816d8', '0.1.5.1490520365651', '修改', '{\"actType\":2010,\"buttonGroup\":1,\"buttons\":\"\",\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count!=1){\\r\\n\\talert(&quot;请选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nvar id = $(&quot;input[name=\'_selects\']&quot;).val();\\r\\nlocation.href = basePath + &quot;document/view.do?dynamicPageId=2&amp;id=&quot;+id;\",\"code\":\"0.1.5.1490520365651\",\"color\":\"success\",\"confirm\":false,\"content\":\"\",\"contentType\":3,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{\"target\":\"2\"},\"hiddenScript\":\"var a=request.getParameter(&quot;pri&quot;);\\r\\nvar b=request.getParameter(&quot;relation&quot;);\\r\\nif(a==\'true\' || b==\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-edit\",\"name\":\"修改\",\"order\":2,\"pageId\":\"dea500a0-6caf-4e32-adb0-ef9e8a2816d8\",\"serverScript\":\"\",\"systemId\":110,\"tittle\":\"\"}', null, null, '110', '1', '2', '1', '1', '9999', '9999', '9999', '2017-03-26 17:26:05', '2017-03-26 17:26:05', '1');
INSERT INTO `p_fm_store` VALUES ('df44bba2-84ac-47ae-8572-419d2e8411d3', '0.1.7.1508325128926', 'e1ba84ea7e8c6219d333bdd6f635b0e7', '{\"componentType\":\"1036\",\"searchLocation\":\"1\",\"disabledScript\":\"\",\"hiddenScript\":\"\",\"selectOption\":\"\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t\",\"pageId\":\"df44bba2-84ac-47ae-8572-419d2e8411d3\",\"layoutId\":\"\",\"selectName\":\"\",\"defaultValueScript\":\"\",\"layoutName\":\"root\",\"textName\":\"id@name@desc\",\"textLabel\":\"ID@API名称@API描述\",\"name\":\"e1ba84ea7e8c6219d333bdd6f635b0e7\",\"dynamicPageId\":\"7\",\"selectLabel\":\"\",\"readonlyScript\":\"\",\"order\":8}', null, null, '110', '7', '8', null, '1', '9999', '9999', '9999', '2017-10-18 17:48:23', '2017-10-18 17:48:23', null);
INSERT INTO `p_fm_store` VALUES ('e25b58bb-db99-4d9d-b39e-8fa07d0ff15c', '0.1.7.1513586137382', '7d3f0663468c82ca1f6fdb4726a5d6a2', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"模块名称\",\"validateInputTip\":\"\",\"layoutName\":\"_row2_cod2\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":3,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_modular.modularName\",\"textType\":\"text\",\"pageId\":\"e25b58bb-db99-4d9d-b39e-8fa07d0ff15c\",\"layoutId\":\"c164c6f7-b797-4002-a965-f88ab994b830\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"7d3f0663468c82ca1f6fdb4726a5d6a2\",\"textalign\":\"居左\",\"dynamicPageId\":\"5\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '5', '3', null, '1', '9999', '9999', '9999', '2017-07-14 14:42:05', '2017-07-14 14:42:05', '1');
INSERT INTO `p_fm_store` VALUES ('e2a34cd1-54e4-4104-8872-07f7df7eacc3', '0.1.5.1504948371369', '搜索', '{\"actType\":2000,\"buttonGroup\":1,\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"$(&quot;#groupForm&quot;).submit()\",\"code\":\"0.1.5.1504948371369\",\"color\":\"info\",\"confirm\":false,\"description\":\"\",\"dynamicPageId\":18,\"dynamicPageName\":\"系统属性字典配置列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"\",\"hiddenStatus\":false,\"icon\":\"icon-search\",\"name\":\"搜索\",\"order\":6,\"pageId\":\"e2a34cd1-54e4-4104-8872-07f7df7eacc3\",\"serverScript\":\"\",\"systemId\":110}', null, null, '110', '18', '6', '1', '1', '9999', '9999', '9999', '2017-09-09 17:12:51', '2017-09-09 17:12:51', null);
INSERT INTO `p_fm_store` VALUES ('e4353eb2-44db-4af8-83fc-bcb27fcda1d7', '0.1.7.1513590779279', 'd787bbd6f39fb6364faa6d1ef65cc492', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"参数名\",\"validateInputTip\":\"\",\"layoutName\":\"_row1_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":11,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_describe.NAME\",\"textType\":\"text\",\"pageId\":\"e4353eb2-44db-4af8-83fc-bcb27fcda1d7\",\"layoutId\":\"6dcf5d93-1f24-4066-adaf-5d8c91a7dd73\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"d787bbd6f39fb6364faa6d1ef65cc492\",\"textalign\":\"居左\",\"dynamicPageId\":\"3\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '3', '11', null, '1', '9999', '9999', '9999', '2017-05-02 16:39:30', '2017-05-02 16:39:30', '1');
INSERT INTO `p_fm_store` VALUES ('e52aa25e-b423-4c47-8e15-5d7c558b9a40', '0.1.5.1508321088542', '新增关联API', '{\"serverScript\":\"\",\"systemId\":110,\"buttonGroup\":1,\"code\":\"0.1.5.1508321088542\",\"color\":\"primary\",\"chooseScript\":\"\",\"hiddenStatus\":false,\"hiddenScript\":\"\",\"icon\":\"icon-plus\",\"description\":\"\",\"pageId\":\"e52aa25e-b423-4c47-8e15-5d7c558b9a40\",\"extbute\":{},\"confirm\":false,\"dynamicPageName\":\"已绑定API列表\",\"enName\":\"\",\"name\":\"新增关联API\",\"dynamicPageId\":7,\"actType\":2000,\"clientScript\":\"var pid = Comm.getUrlParam(&quot;pid&quot;);\\r\\ntop.dialog({\\r\\n\\tid : \'add-dialog\' + Math.ceil(Math.random() * 10000),\\r\\n\\ttitle : \'未关联的接口\',\\r\\n\\turl : basePath+&quot;document/view.do?dynamicPageId=6&amp;pid=&quot; + pid,\\r\\n\\theight : 900,\\r\\n\\twidth : 1200,\\r\\n\\tonclose : function() {\\r\\n\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=7&amp;pid=&quot; + pid;\\r\\n\\t}\\r\\n}).showModal();\",\"chooseValidate\":false,\"order\":1}', null, null, '110', '7', '1', '1', '1', '9999', '9999', '9999', '2017-10-18 18:04:49', '2017-10-18 18:04:49', null);
INSERT INTO `p_fm_store` VALUES ('e5f88fe4-2ee4-42ed-a85a-0cc383687527', '0.1.7.1508319967467', 'cff681bf5f3e3b1622e7d7235b1db597', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":2,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_NAME\",\"excelShowScript\":\"\",\"pageId\":\"e5f88fe4-2ee4-42ed-a85a-0cc383687527\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"cff681bf5f3e3b1622e7d7235b1db597\",\"width\":\"15%\",\"dynamicPageId\":\"7\",\"validateErrorTip\":\"\",\"columnName\":\"API名称\"}', null, null, '110', '7', '2', null, '1', '9999', '9999', '9999', '2017-10-18 17:44:25', '2017-10-18 17:44:25', null);
INSERT INTO `p_fm_store` VALUES ('e74b3405-2c12-4720-82bd-4c53f06b929f', '0.1.8.1513578936414', '_row2_cod2', '{\"proportion\":3,\"top\":0,\"left\":0,\"name\":\"_row2_cod2\",\"dynamicPageId\":\"2\",\"layoutType\":1,\"pageId\":\"e74b3405-2c12-4720-82bd-4c53f06b929f\",\"parentId\":\"74203716-54fc-4964-b484-cfd09d8e32ef\",\"order\":4}', '74203716-54fc-4964-b484-cfd09d8e32ef', null, '110', '2', '4', null, '1', '9999', '9999', '9999', '2017-12-18 14:35:36', '2017-12-18 14:35:36', null);
INSERT INTO `p_fm_store` VALUES ('e92c560e-9be0-4987-9793-a7619dcf53bb', '0.1.7.1513599079315', 'f9574122a2d617f157e422a04dc3f0d7', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"描述\",\"validateInputTip\":\"\",\"layoutName\":\"_row3_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"rowCount\":\"3\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":31,\"componentType\":\"1005\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_data_dictionary.DICT_REMARK\",\"pageId\":\"e92c560e-9be0-4987-9793-a7619dcf53bb\",\"layoutId\":\"932b591b-5539-469d-b10c-90bba457904f\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"f9574122a2d617f157e422a04dc3f0d7\",\"textalign\":\"居左\",\"dynamicPageId\":\"17\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\"}', null, null, '110', '17', '31', null, '1', '9999', '9999', '9999', '2017-09-09 16:17:45', '2017-09-09 16:17:45', null);
INSERT INTO `p_fm_store` VALUES ('e98610de-bc6d-4b2c-a95d-854a8f1946d7', '0.1.8.1493717855133', '_row3_cod4', '{\"backgroundcolor\":\"white\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"height\":\"\",\"heightType\":\"1\",\"layoutType\":\"1\",\"left\":\"\",\"name\":\"_row3_cod4\",\"order\":12,\"pageId\":\"e98610de-bc6d-4b2c-a95d-854a8f1946d7\",\"parentId\":\"0f99411b-ea2b-4c21-ba77-c56b90da8c4b\",\"proportion\":\"2\",\"systemId\":111,\"textalign\":\"居左\",\"textstyle\":\"\",\"textverticalalign\":\"垂直居中\",\"top\":\"\",\"width\":\"\"}', '0f99411b-ea2b-4c21-ba77-c56b90da8c4b', null, '111', '2020', '12', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:05', '2017-05-03 11:07:05', '1');
INSERT INTO `p_fm_store` VALUES ('ea3ff63d-1a1c-454c-8fba-ae893bc237cb', '0.1.7.1502447544945', '24c4d0ab3a5d01a59ef342970ea5397a', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"pageId\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_dynamicpage.ID\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"10\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"24c4d0ab3a5d01a59ef342970ea5397a\",\"order\":1,\"orderby\":\"1\",\"pageId\":\"ea3ff63d-1a1c-454c-8fba-ae893bc237cb\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"\"}', null, null, '110', '10', '1', null, '1', '9999', '9999', '9999', '2017-08-11 17:29:52', '2017-08-11 17:29:52', null);
INSERT INTO `p_fm_store` VALUES ('ee3bd513-ff4d-45df-a760-783e06c72280', '0.1.7.1493717835408', '1c8a83b6dc3f671944ac75799fd0080c', '{\"backgroundcolor\":\"white\",\"componentType\":\"1001\",\"css\":\"\",\"dataItemCode\":\"p_fm_api_describe.DEFAULTVAL\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":2020,\"dynamicPageName\":\"API参数表单_1493780825797\",\"fontcolor\":\"black\",\"fontfamily\":\"宋体\",\"fontsize\":\"10.5\",\"hiddenScript\":\"\",\"layoutId\":\"b27b1e0e-2f2d-4b94-9bf6-82a8fbbc8e07\",\"layoutName\":\"_row2_cod4\",\"lineheight\":\"1\",\"name\":\"1c8a83b6dc3f671944ac75799fd0080c\",\"order\":6,\"pageId\":\"ee3bd513-ff4d-45df-a760-783e06c72280\",\"printAllOption\":\"0\",\"readonlyScript\":\"\",\"style\":\"\",\"systemId\":111,\"textRise\":\"0\",\"textalign\":\"居左\",\"textindent\":\"0\",\"textverticalalign\":\"垂直居中\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\"}', null, null, '111', '2020', '6', null, '1', '9999', '9999', '9999', '2017-05-03 11:07:06', '2017-05-03 11:07:06', '1');
INSERT INTO `p_fm_store` VALUES ('efba3d33-16ce-40b6-8104-b96f1ee56ca5', '0.1.7.1503306600262', '56cdde60383ab88f136b75266a8dac2c', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"描述\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_api.API_DESC\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"1\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"56cdde60383ab88f136b75266a8dac2c\",\"order\":9,\"orderby\":\"1\",\"pageId\":\"efba3d33-16ce-40b6-8104-b96f1ee56ca5\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"\"}', null, null, '110', '1', '9', null, '1', '9999', '9999', '9999', '2017-03-26 17:40:10', '2017-03-26 17:40:10', '1');
INSERT INTO `p_fm_store` VALUES ('f18d6a38-d0fd-448d-a694-373f07da1603', '0.1.7.1508320047952', '7e73d5d1c8a30a870aab5ddc6732b7ee', '{\"sortName\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"pdfWidth\":\"\",\"description\":\"\",\"validateInputTip\":\"\",\"showScript\":\"\",\"alloworderby\":\"-1\",\"readonlyScript\":\"\",\"columnFormat\":\"\",\"order\":6,\"componentType\":\"1008\",\"hiddenScript\":\"\",\"orderby\":\"1\",\"dataItemCode\":\"p_fm_api.API_IS_LOGIN\",\"excelShowScript\":\"\",\"pageId\":\"f18d6a38-d0fd-448d-a694-373f07da1603\",\"columnPatten\":\"\",\"defaultValueScript\":\"\",\"validateAllowNull\":\"0\",\"name\":\"7e73d5d1c8a30a870aab5ddc6732b7ee\",\"width\":\"8%\",\"dynamicPageId\":\"7\",\"validateErrorTip\":\"\",\"columnName\":\"是否登录\"}', null, null, '110', '7', '6', null, '1', '9999', '9999', '9999', '2017-10-18 17:46:43', '2017-10-18 17:46:43', null);
INSERT INTO `p_fm_store` VALUES ('f2068ebf-3b49-4917-ac87-da85d470f9f5', '0.1.7.1513599178488', '2f97a429f8b6eb2cae13cbbc8bea86f8', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"KEY值\",\"validateInputTip\":\"\",\"required\":\"1\",\"layoutName\":\"_row2_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":21,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_data_dictionary.DATA_KEY\",\"textType\":\"text\",\"pageId\":\"f2068ebf-3b49-4917-ac87-da85d470f9f5\",\"layoutId\":\"88fcf567-c371-49bd-ba68-c053447d17ba\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"2f97a429f8b6eb2cae13cbbc8bea86f8\",\"textalign\":\"居左\",\"dynamicPageId\":\"17\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '17', '21', null, '1', '9999', '9999', '9999', '2017-09-09 16:16:43', '2017-09-09 16:16:43', null);
INSERT INTO `p_fm_store` VALUES ('f2b2807c-881e-4d92-b999-1e70e3a846e2', '0.1.7.1513591125840', 'c5d2f388a8a682b440436f6f247a41f0', '{\"css\":\"\",\"validatJson\":\"\",\"disabledScript\":\"\",\"lineheight\":\"1\",\"description\":\"\",\"fontsize\":\"10.5\",\"textindent\":\"0\",\"title\":\"权限名称\",\"validateInputTip\":\"\",\"layoutName\":\"_row1_cod1\",\"backgroundcolor\":\"white\",\"fontcolor\":\"black\",\"placeholder\":\"\",\"readonlyScript\":\"\",\"order\":11,\"componentType\":\"1001\",\"hiddenScript\":\"\",\"fontfamily\":\"宋体\",\"dataItemCode\":\"p_fm_api_privilege.NAME\",\"textType\":\"text\",\"pageId\":\"f2b2807c-881e-4d92-b999-1e70e3a846e2\",\"layoutId\":\"2fd44ad1-3be7-4467-bd9e-0c53cee02137\",\"textverticalalign\":\"垂直居中\",\"defaultValueScript\":\"\",\"printAllOption\":\"0\",\"enTitle\":\"\",\"validateAllowNull\":\"0\",\"name\":\"c5d2f388a8a682b440436f6f247a41f0\",\"textalign\":\"居左\",\"dynamicPageId\":\"19\",\"style\":\"\",\"textRise\":\"0\",\"validateErrorTip\":\"\",\"onchangeScript\":\"\"}', null, null, '110', '19', '11', null, '1', '9999', '9999', '9999', '2017-09-19 19:54:00', '2017-09-19 19:54:00', null);
INSERT INTO `p_fm_store` VALUES ('f2b9dd5f-ed58-4ca2-8e6a-5273bea703fa', '0.1.7.1502443829544', 'f05d0e802a5f89e32324012de9ee74a6', '{\"alloworderby\":\"-1\",\"columnFormat\":\"\",\"columnName\":\"目前所属模块\",\"columnPatten\":\"\",\"componentType\":\"1008\",\"dataItemCode\":\"p_fm_dynamicpage.mname\",\"defaultValueScript\":\"\",\"description\":\"\",\"disabledScript\":\"\",\"dynamicPageId\":\"10\",\"excelShowScript\":\"\",\"hiddenScript\":\"\",\"name\":\"f05d0e802a5f89e32324012de9ee74a6\",\"order\":3,\"orderby\":\"1\",\"pageId\":\"f2b9dd5f-ed58-4ca2-8e6a-5273bea703fa\",\"pdfWidth\":\"\",\"readonlyScript\":\"\",\"showScript\":\"\",\"sortName\":\"\",\"validatJson\":\"\",\"validateAllowNull\":\"0\",\"validateErrorTip\":\"\",\"validateInputTip\":\"\",\"width\":\"\"}', null, null, '110', '10', '3', null, '1', '9999', '9999', '9999', '2017-08-11 17:30:30', '2017-08-11 17:30:30', null);
INSERT INTO `p_fm_store` VALUES ('f3affc8f-7f43-4aa6-b330-c738ea867e99', '0.1.8.1513591081104', '_row2', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row2\",\"dynamicPageId\":\"19\",\"layoutType\":2,\"pageId\":\"f3affc8f-7f43-4aa6-b330-c738ea867e99\",\"parentId\":\"\",\"order\":2}', null, null, '110', '19', '2', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('f8774250-58ae-4070-a601-ed8fb4f6b49b', '0.1.8.1513591081163', '_row2_cod1', '{\"proportion\":12,\"top\":0,\"left\":0,\"name\":\"_row2_cod1\",\"dynamicPageId\":\"19\",\"layoutType\":1,\"pageId\":\"f8774250-58ae-4070-a601-ed8fb4f6b49b\",\"parentId\":\"f3affc8f-7f43-4aa6-b330-c738ea867e99\",\"order\":2}', 'f3affc8f-7f43-4aa6-b330-c738ea867e99', null, '110', '19', '2', null, '1', '9999', '9999', '9999', '2017-12-18 17:58:01', '2017-12-18 17:58:01', null);
INSERT INTO `p_fm_store` VALUES ('f95066ec-c892-469e-9e53-9f8161fd1506', '0.1.5.1490520426541', '删除', '{\"actType\":2003,\"buttonGroup\":1,\"buttons\":\"\",\"chooseScript\":\"\",\"chooseValidate\":false,\"clientScript\":\"if(count==0){\\r\\n\\talert(&quot;请至少选择一项进行操作&quot;);\\r\\n\\treturn false;\\r\\n}\\r\\nif(!confirm(&quot;是否确认删除?&quot;)){\\r\\n\\treturn false;\\r\\n}\\r\\nvar actId = $(this).attr(&quot;id&quot;);\\r\\n$(&quot;#actId&quot;).val(actId);\\t\\r\\n$.ajax({\\r\\n   type: &quot;POST&quot;,\\r\\n   url: basePath + &quot;document/excuteOnly.do&quot;,\\r\\n   data:$(&quot;#groupForm&quot;).serialize(),\\r\\n   async : false,\\r\\n   success: function(data){\\r\\n\\t\\tif(data==1){\\r\\n\\t\\t\\talert(&quot;删除成功&quot;);\\r\\n\\t\\t\\tlocation.href = basePath + &quot;document/view.do?dynamicPageId=1&quot;;\\r\\n\\t\\t}\\r\\n\\t\\telse{\\r\\n\\t\\t\\talert(data);\\r\\n\\t\\t}\\r\\n   }\\r\\n});\",\"code\":\"0.1.5.1490520426541\",\"color\":\"danger\",\"confirm\":false,\"content\":\"\",\"contentType\":3,\"description\":\"\",\"dynamicPageId\":1,\"dynamicPageName\":\"API管理列表\",\"enName\":\"\",\"extbute\":{},\"hiddenScript\":\"var a=request.getParameter(&quot;pri&quot;);\\r\\nvar b=request.getParameter(&quot;relation&quot;);\\r\\nif(a==\'true\' || b==\'true\'){\\r\\ntrue;\\r\\n}\",\"hiddenStatus\":false,\"icon\":\"icon-trash\",\"name\":\"删除\",\"order\":3,\"pageId\":\"f95066ec-c892-469e-9e53-9f8161fd1506\",\"serverScript\":\"var retVal = &quot;1&quot;;\\r\\nvar ids = request.getParameterValues(&quot;_selects&quot;);\\r\\nfor(var i=0;i&lt;ids.length;i++){\\r\\nvar sql = &quot;delete from p_fm_api where API_ID=\'&quot;+ids[i]+&quot;\'&quot;;\\r\\nDocumentUtils.excuteUpdate(sql);\\r\\n}\\r\\nretVal;\",\"systemId\":110,\"tittle\":\"\"}', null, null, '110', '1', '3', '1', '1', '9999', '9999', '9999', '2017-03-26 17:27:06', '2017-03-26 17:27:06', '1');

-- ----------------------------
-- Table structure for p_fm_template
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_template`;
CREATE TABLE `p_fm_template` (
  `id` char(36) NOT NULL,
  `file_name` varchar(32) DEFAULT NULL,
  `sys_id` bigint(32) DEFAULT NULL,
  `classify` varchar(20) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `file_location` varchar(500) DEFAULT NULL,
  `signatureImg` varchar(100) DEFAULT NULL,
  `content` text,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_template
-- ----------------------------
INSERT INTO `p_fm_template` VALUES ('1', 'pc_lte_table_list', null, null, 'PC端LTE风格表格列表模板', '', null, '<#include \"templates/parseAdminLTE.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse>\r\n	<#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if>\r\n	</#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head lang=\"en\">\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-table.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/select2/select2.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">  \r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n    <script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n		for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n		cssFile=null;\r\n	</script>\r\n</head>\r\n<body>\r\n	<!-- Main content -->\r\n  <section class=\"content\">	\r\n		<div class=\"opeBtnGrop\">\r\n</#noparse>				\r\n		<#if pageActs??>\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<@convertButton act />\r\n			</#list>\r\n		</#if>						\r\n<#noparse>\r\n		</div>\r\n		<div class=\"row\">\r\n		<div class=\"col-md-12\">\r\n			<div class=\"box box-info\">\r\n				<div class=\"box-body\">					\r\n						<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n							<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"${</#noparse>${dataAlias[0]}_list_paginator.getLimit()<#noparse>}</#noparse><#noparse>\">\r\n							<input type=\"hidden\" id=\"totalCount\" name=\"totalCount\" value=\"${</#noparse>${dataAlias[0]}_list_paginator.getTotalCount()<#noparse>}</#noparse><#noparse>\">\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"orderBy\" value=\"${ (vo.orderBy)!\"\"}\" id=\"orderBy\"/>\r\n							<input type=\"hidden\" name=\"allowOrderBy\" id=\"allowOrderBy\" value=\"${ (vo.allowOrderBy)!\"\"}\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n							</#noparse>\r\n							<#if components?? >\r\n									<#list components?sort_by(\"order\") as c>\r\n										<#if c[\'componentType\']==\'1036\'>\r\n											<@convertAddSearch c />\r\n										</#if>\r\n									</#list>\r\n							</#if>\r\n							<#noparse>\r\n							<table id=\"bootstrapTable\" class=\"table table-hover\">\r\n								<thead>\r\n									<tr>\r\n										<th class=\"hidden\"></th>\r\n										<th data-width=\"\" data-field=\"\" data-checkbox=\"true\"></th>\r\n										<th data-width=\"50\">序号</th>\r\n							</#noparse>\r\n								<#if components?? >\r\n									 <#list components?sort_by(\"order\") as c>\r\n										<#if c[\'componentType\']==\'1008\' >\r\n											<@parseColumn c />\r\n										</#if>\r\n									</#list>\r\n								</#if>\r\n							<#noparse>\r\n									</tr>\r\n								</thead>\r\n								<tbody>\r\n									<#if </#noparse>${dataAlias[0]}<#noparse>_list?? && </#noparse>${dataAlias[0]}<#noparse>_list?size gte 1>	\r\n										<#list\r\n											</#noparse>\r\n											${dataAlias[0]}_list as ${dataAlias[0]}>\r\n											<#noparse> \r\n										<tr>\r\n											<td class=\"hidden formData\">\r\n												<input type=\"hidden\" value=\"${</#noparse>${dataAlias[0]}<#noparse>.ID!\'\'}\">\r\n											</td>\r\n											<td></td>\r\n											</#noparse>				\r\n									<td><#noparse>${(</#noparse>${dataAlias[0]}_list_paginator.getPage() * ${dataAlias[0]}_list_paginator.getLimit() - ${dataAlias[0]}_list_paginator.getLimit() + ${dataAlias[0]}_index + 1<#noparse>)}</#noparse>\r\n									</td>\r\n									<#if components?? >\r\n										 <#list components?sort_by(\"order\") as c>\r\n											<#if c[\'componentType\']==\'1008\' >\r\n												<@parseColumnData c />\r\n											</#if>\r\n										</#list>\r\n									</#if>\r\n				<#noparse>				\r\n										</tr>\r\n										</#list>\r\n									</#if>\r\n								</tbody>\r\n							</table>\r\n						</form>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</section>\r\n	<script src=\"${basePath}template/AdminLTE/js/jquery.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/editTable.js\"></script>\r\n    <script src=\"${basePath}template/AdminLTE/js/bootstrap-table.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/select2.full.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js\"></script>\r\n	<script src=\'${basePath}venson/js/common.js\'></script>\r\n	<script src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script>\r\n		for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n		scriptFile=null;\r\n		var basePath = \'${basePath}\';\r\n		var count=0;//默认选择行数为0\r\n	$(document).ready(function(){\r\n		$(document).keyup(function(event) {\r\n	    	if (event.keyCode == 13) {\r\n	    		$(\"#groupForm\").submit();\r\n	    	}\r\n	    });\r\n		 $(\"#bootstrapTable\").bootstrapTable({\r\n        	 pageSize:parseInt($(\"#pageSize\").val()),\r\n        	 pageNumber:parseInt($(\"#currentPage\").val()),\r\n        	 totalRows:parseInt($(\"#totalCount\").val()),\r\n        	 sidePagination:\"server\",\r\n        	 pagination:true,\r\n        	 onPageChange:function(number, size){\r\n        		$(\"#pageSize\").val(size);\r\n        		$(\"#currentPage\").val(number);\r\n        		$(\"#groupForm\").submit();\r\n        	 },\r\n        	 onClickRow:function(row,$element,field){\r\n        	  	  var $checkbox=$element.find(\":checkbox\").eq(0);\r\n        	  	  if($checkbox.get(0).checked){\r\n					  $checkbox.get(0).checked=false;\r\n					  $element.find(\"input[type=\'hidden\']\").removeAttr(\"name\",\"_selects\");\r\n        	  	  }else{\r\n					  $checkbox.get(0).checked=true;\r\n					  $element.find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n        	  	  }\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onCheck: function(row,$element){  \r\n				  $element.closest(\"tr\").find(\"input[type=\'hidden\']\").attr(\"name\",\"_selects\");\r\n				  count = $(\"input[name=\'_selects\']\").length;\r\n        	 },\r\n        	 onUncheck:function(row,$element){ \r\n        		 $element.closest(\"tr\").find(\"input[type=\'hidden\']\").removeAttr(\"name\");\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n        	 }, \r\n        	 onCheckAll: function (rows) {\r\n        		 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").attr(\"name\",\"_selects\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n             onUncheckAll: function (rows) {          	 \r\n            	 $.each(rows,function(i,e){\r\n        			$(\"input[value=\'\"+$($.trim(e[\"0\"])).attr(\"value\")+\"\']\").removeAttr(\"name\");\r\n        		 })\r\n				 count = $(\"input[name=\'_selects\']\").length;\r\n             },\r\n			 onSort:function(name, order){\r\n				 $(\"#allowOrderBy\").val(\"1\");\r\n				 var orderBy =$(\"#orderBy\").val();\r\n				 console.log(orderBy,name)\r\n				 if(orderBy.indexOf(name)!=-1&&orderBy.indexOf(\"desc\")!=-1){\r\n					 $(\"#orderBy\").val(name+\" asc\");\r\n				 }else{\r\n					 $(\"#orderBy\").val(name+\" desc\");\r\n				 }\r\n        		$(\"#groupForm\").submit();\r\n			 }\r\n         });							\r\n</#noparse>\r\n		<#if components?? >\r\n				<#list components?sort_by(\"order\") as c>\r\n					<#if c[\'componentType\']==\'1036\'>\r\n						<@convertAddSearchScript c />\r\n					</#if>\r\n				</#list>\r\n			</#if>\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>  \r\n        \r\n        <#if page??>\r\n			<#if page.getAfterLoadScript()??>\r\n				${page.getAfterLoadScript()}\r\n			</#if>\r\n		</#if>\r\n<#noparse>\r\n		});\r\n</#noparse>		  	\r\n	</script>\r\n</body>\r\n</html>', null);
INSERT INTO `p_fm_template` VALUES ('109', '手机表单片段模板', null, null, '手机表单片段模板', null, null, '<#include \"templates/appParseFunction.ftl\">\r\n\r\n		<--MYLayoutAndCom-->\r\n			<#if pageActs??>\r\n				<#list pageActs?sort_by(\"order\") as act>\r\n					<#noparse><@shiro.hasPermission name=\"11111:</#noparse>${(pageActPermission[act.getPageId()])!\"\"}<#noparse>\"></#noparse>\r\n						<@convertButton act />\r\n					<#noparse></@shiro.hasPermission></#noparse>\r\n				</#list>\r\n			</#if>\r\n		\r\n		         <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >	\r\n			 <#if layouts??>\r\n			 	<#if components??>\r\n			 		<#list layouts?sort_by(\"order\") as layout>\r\n			 			<@parseLayout2 layout components/>\r\n			 		</#list> \r\n			 	</#if>\r\n			 </#if>\r\n			 </table>\r\n		<--MYLayoutAndCom/-->\r\n		\r\n			<--MYpageJScript-->\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<@convertComponentScriptApp component />\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			\r\n			 <#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n			</#if>\r\n			\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n									<#if (component??) && (component[\'componentType\']==\'1011\')>\r\n					 				<@convertFileScript component />\r\n									</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			\r\n			if(window.KindEditor){ //富文本编辑框\r\n		  		KindEditor.ready(function(K){\r\n		  			var options ={\r\n			            allowFileManager : true,\r\n			            allowUpload : true,\r\n			            uploadJson : basePath+\'common/file/uploadImg.do\',\r\n			            //readonlyMode:true,\r\n			            bodyClass : \'article-content\',\r\n			            afterBlur: function(){this.sync();$(this.container[0]).removeClass(\'focus\');\r\n			            },\r\n			            afterFocus: function(){$(this.container[0]).addClass(\'focus\');},\r\n			            afterCreate : function(){\r\n			                var doc = this.edit.doc; \r\n			                var cmd = this.edit.cmd; \r\n			                if(K.WEBKIT){\r\n			                    $(doc.body).bind(\'paste\', function(ev){\r\n			                        var $this = $(this);\r\n			                        var original =  ev.originalEvent;\r\n			                        var file =  original.clipboardData.items[0].getAsFile();\r\n			                        var reader = new FileReader();\r\n			                        reader.onload = function (evt){\r\n			                            var result = evt.target.result;\r\n			                            var arr = result.split(\",\");\r\n			                            var data = arr[1]; // raw base64\r\n			                            var contentType = arr[0].split(\";\")[0].split(\":\")[1];\r\n			                            //html = \'<img src=\"\' + result + \'\" alt=\"\" />\';\r\n			                            //cmd.inserthtml(html);\r\n			                            //post本地图片到服务器并返回服务器存放地址\r\n										$.ajax({\r\n			                            	type:\"post\",\r\n			                            	url: basePath+\"common/file/generateImageByBase64Str.do\",\r\n			                            	dataType:\"html\",\r\n			                            	data:{imgStr:result},\r\n			                            	success:function(data){\r\n			                            	var html = \'<img src=\"\'+basePath+\'/attached/image/\' +data + \'\" alt=\"\" />\';\r\n			                            	cmd.inserthtml(html)\r\n			                            	}\r\n			                            });\r\n			                            //$.post(\'<%=basePath%>resources/demo/pages/ajaxPasteImage.html\', {editor: html}, function(data){cmd.inserthtml(data);});\r\n			                        };\r\n			                        reader.readAsDataURL(file);\r\n			                    });\r\n			                }\r\n			                if(K.GECKO){\r\n			                    K(doc.body).bind(\'paste\', function(ev){\r\n			                        setTimeout(function(){\r\n			                            var html = K(doc.body).html();\r\n			                            if(html.search(/<img src=\"data:.+;base64,/) > -1)\r\n			                            {\r\n			                                $.post(basePath+\'resources/demo/pages/ajaxPasteImage.html\', {editor: html}, function(data){K(doc.body).html(data);});\r\n			                            }\r\n			                        }, 80);\r\n			                    });\r\n			                }\r\n			            }\r\n			        };\r\n		  			<#if components??>\r\n						 <#if components?keys??>\r\n							  <#list components?keys as layoutId>\r\n									<#if components[layoutId]??>\r\n									<#list components[layoutId] as component>\r\n										<#if (component??) && (component[\'componentType\']==\'1019\')>\r\n										<@convertKindEditorScript component />\r\n										</#if>\r\n									</#list>\r\n									</#if>\r\n							  </#list>\r\n						</#if> \r\n					</#if>\r\n		  		    \r\n				});\r\n	  		};\r\n\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n									<#if (component??) && (component[\'componentType\']==\'1016\')>\r\n					 				<@convertImageScript component />\r\n									</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			<--MYpageJScript/-->\r\n		\r\n			<--MYvalidate-->\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n						 			<#list components[layoutId] as component>\r\n						 				<#if component[\'componentType\']!=\'1012\' && component[\'componentType\']!=\'1009\'>\r\n							 				<#if valdatorsMap[component[\'pageId\']]?? || (component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number==0)>\r\n							 					<#if component[\'componentType\']==\'1011\'>\r\n													$(\'#${(component[\'pageId\'])!\"\"} .orgfile\').formValidator({onFocus:\"${(component[\'validateInputTip\'])!\'\'}\",onCorrect:\"${(component[\'validateErrorTip\'])!\'\'}\",<#if component[\'componentType\']==\'1002\'> triggerEvent : \"change\" </#if>\r\n														\r\n													})\r\n												<#else>\r\n													$(\'#${(component[\'pageId\'])!\"\"}\').formValidator({onFocus:\"${(component[\'validateInputTip\'])!\'\'}\",onCorrect:\"${(component[\'validateErrorTip\'])!\'\'}\",<#if component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number==1> empty:true, </#if><#if component[\'componentType\']==\'1002\'> triggerEvent : \"change\" </#if>\r\n														\r\n													})\r\n												</#if>\r\n							 					<#if (component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number ==0) >\r\n													<#if component[\'componentType\']==\'1011\'>\r\n														.inputValidator({min:1,empty:{leftEmpty:true,rightEmpty:true,emptyError:\"\"},onError:\"请确认附件已上传\"})\r\n													<#else>\r\n														.inputValidator({min:1,empty:{leftEmpty:true,rightEmpty:true,emptyError:\"\"},onError:\"<#if component[\'description\']??> ${component[\'description\']} </#if>请输入内容\"})\r\n													</#if>\r\n	       										</#if>\r\n							 					<#if valdatorsMap[component[\'pageId\']]??>\r\n								 					<#list valdatorsMap[component[\'pageId\']] as v>\r\n								 						<@validate v/>\r\n								 					</#list>;\r\n								 				</#if>\r\n							 				</#if>\r\n						 				</#if>	\r\n						 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<#if component[\'pageValidate\']??>\r\n					 					${component[\'pageValidate\']}\r\n					 				</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			<--MYvalidate/-->\r\n		\r\n		<--MYpageActScript-->\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>\r\n		\r\n		<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<#if component[\'pageActScript\']??>\r\n					 					${component[\'pageActScript\']}\r\n					 				</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n		</#if>\r\n		<--MYpageActScript/-->\r\n', null);
INSERT INTO `p_fm_template` VALUES ('110', '手机列表片段模板', null, null, '手机列表片段模板', null, null, '<#include \"templates/appParseFunction.ftl\">\r\n\r\n\r\n<--MYLayoutAndCom--> \r\n		<div class=\"form-group\">\r\n			<div class=\"col-xs-12\">\r\n			<#if pageActs??>\r\n				<#list pageActs?sort_by(\"order\") as act>\r\n					\r\n						<@convertButton act />\r\n					\r\n				</#list>\r\n			</#if>\r\n			</div>\r\n		</div>\r\n	\r\n		\r\n<#noparse>	\r\n		<div class=\"form-group\">\r\n		<div class=\"col-xs-12\">\r\n		<div  id=\"datatable\">\r\n				\r\n					<table class=\"table datatable table-bordered\" style=\"table-layout:fixed;width:100%\">\r\n						<thead>\r\n							<tr>\r\n								<th  class=\"hidden\" style=\"text-align:center;\"></th>\r\n</#noparse>\r\n					<#if components?? >\r\n						 <#list components?sort_by(\"order\") as c>\r\n						 	<#if c[\'componentType\']!=\'1013\' >\r\n								<@parseColumn c />\r\n							</#if>\r\n						</#list>\r\n					</#if>\r\n<#noparse>\r\n							</tr>\r\n						</thead>\r\n						<tbody id=\"includeList3\">\r\n		            	<#if </#noparse>${dataAlias[0]}<#noparse>_list?? >	\r\n		            	 	<#list\r\n</#noparse>\r\n		            	 		${dataAlias[0]}_list as ${dataAlias[0]}>\r\n<#noparse> \r\n								<tr>\r\n								\r\n</#noparse>				\r\n						<#if components?? >\r\n							 <#list components?sort_by(\"order\") as c>\r\n							 	<#if c[\'componentType\']!=\'1013\' >\r\n									<@parseColumnData c />\r\n								</#if>\r\n							</#list>\r\n						</#if>\r\n<#noparse>				\r\n								</tr>\r\n							</#list>\r\n						</#if>\r\n					</tbody>\r\n				</table>\r\n			</div>\r\n			</div>\r\n		</div>\r\n</#noparse>\r\n<--MYLayoutAndCom/-->\r\n\r\n<--MYpageJScript-->\r\n<#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n			</#if>\r\n<--MYpageJScript/-->\r\n<--MYvalidate-->\r\n<--MYvalidate/-->\r\n\r\n<--MYpageActScript-->\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list> \r\n<--MYpageActScript/--> \r\n', null);
INSERT INTO `p_fm_template` VALUES ('120', '树形模板', null, null, '', 'resources\\template\\1489734209691', null, '<#include \"templates/parseFunction.ftl\">\r\n<#include \"templates/pagerParserFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n	<meta name=\"renderer\" content=\"webkit\">	\r\n 	<link rel=\"stylesheet\" href=\"${basePath}base/resources/zui/dist/css/zui.min.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/main.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/zTreeStyle/szcloud.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/layout-default-latest.css\"/>\r\n\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n	<![endif]-->	       \r\n</head>\r\n<body id=\"main\">\r\n			\r\n			<div class=\"panel ui-layout-west\">\r\n				\r\n				<div class=\"panel-body\" id=\"p1\" style=\"height:320px;\">\r\n					<ul id=\"tree1\" class=\"ztree\"></ul>\r\n				</div>\r\n			</div>\r\n			<iframe class=\"ui-layout-center\" name=\"sysEditFrame\" id=\"sysEditFrame\" scrolling=\"auto\" frameborder=\"0\" width=\"100%\"></iframe>\r\n</#noparse>	\r\n\r\n<#noparse>	\r\n\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery-ui-latest.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery.layout-latest.js\"></script>\r\n	<script type=\"text/javascript\">\r\n		var basePath = \'${basePath}\';\r\n		$(document).ready(function(){\r\n			var setting = {\r\n		    	   			view: {showLine: false,showIcon: false},\r\n		    	   			data: {simpleData: {enable: true}}\r\n		    	   	};\r\n		        	$.ajax({\r\n		                type:\"post\",\r\n		                url:basePath+\"getTreeData.do\",\r\n		                data:{dynamicPageId:\"${ (vo.dynamicPageId)!\"\"}\"},\r\n		                datatype:\"json\",\r\n		                success:function(json){\r\n		                       $.fn.zTree.init($(\"#tree1\"), setting, json);\r\n		                }\r\n		            });\r\n		        	\r\n		        	$(\'body\').layout({\r\n		    			west__size:					240\r\n		    		,	west__spacing_closed:		20\r\n		    		,	west__togglerLength_closed:	100\r\n		    		,	west__togglerAlign_closed:	\"middle\"\r\n		    		,	west__togglerContent_closed:\"菜单树\"\r\n		    		,	west__togglerTip_closed:	\"打开并固定菜单\"\r\n		    		,	west__sliderTip:			\"Slide Open Menu\"\r\n		    		,	west__slideTrigger_open:	\"mouseover\"\r\n		    		,	north__closeable: false\r\n		    		,	north__resizable:false\r\n		    		,	north__spacing_open:0\r\n		    		,	center__maskContents:true // IMPORTANT - enable iframe masking\r\n		    		\r\n		    		});\r\n\r\n		\r\n</#noparse>\r\n		<#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n		</#if>\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>  \r\n<#noparse>\r\n		});\r\n</#noparse>		  \r\n		\r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('121', '树形菜单', null, null, '', 'resources\\template\\1490260358150', '', '<#include \"templates/parseFunction.ftl\">\r\n<#include \"templates/pagerParserFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n<!DOCTYPE html>\r\n<HTML>\r\n\r\n	<HEAD>\r\n		<TITLE>树形菜单</TITLE>\r\n		<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\r\n		<link rel=\"stylesheet\" href=\"${basePath}resources/ztree/css/zTreeStyle.css\" type=\"text/css\">\r\n		<link rel=\"stylesheet\" href=\"${basePath}resources/ztree/css/index.css\" type=\"text/css\">\r\n		<script type=\"text/javascript\" src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n		<script type=\"text/javascript\" src=\"${basePath}resources/ztree/js/jquery.ztree.core.min.js\"></script>\r\n		<script type=\"text/javascript\" src=\"${basePath}resources/ztree/js/jquery.ztree.excheck.min.js\"></script>\r\n		<script type=\"text/javascript\" src=\"${basePath}resources/ztree/js/jquery.ztree.exedit.min.js\"></script>\r\n	</HEAD>\r\n<BODY>\r\n		<div class=\"content_wrap\">\r\n			<div class=\"zTreeDemoBackground left content\">\r\n				</#noparse>\r\n					<#if pageActs??>\r\n						<#list pageActs?sort_by(\"order\") as act>						\r\n							<@convertButton act />\r\n						</#list>\r\n					</#if>\r\n				<#noparse>\r\n				<ul id=\"treeList1\" class=\"ztree\"></ul>\r\n			</div>\r\n		</div>\r\n</#noparse>	\r\n\r\n<#noparse>	\r\n\r\n	<SCRIPT type=\"text/javascript\">\r\n			var basePath= \'${basePath}\';\r\n			$(document).ready(function(){\r\n			var $_this = window;\r\n			\r\n			\r\n			var setting = {\r\n				check: {\r\n					enable: true,\r\n					nocheckInherit: true\r\n				},\r\n				data: {\r\n					simpleData: {\r\n						enable: true\r\n					}\r\n				},\r\n				callback: {\r\n					onCheck: zTreeOnCheck,\r\n					onNodeCreated:uneventDefault,\r\n				},\r\n				view: {\r\n					addDiyDom: addDiyDom\r\n				}\r\n			};\r\n			\r\n\r\n			var code;\r\n			function showIconForTree(treeId, treeNode) {\r\n				return !treeNode.isParent;\r\n			};\r\n				$.ajax({\r\n		                type:\"post\",\r\n		                url:basePath+\"getTreeData.do\",\r\n		                data:{dynamicPageId:\"${ (vo.dynamicPageId)!\"\"}\"},\r\n		                datatype:\"json\",\r\n		                success:function(json){\r\n		                       $.fn.zTree.init($(\"#treeList1\"), setting, json);\r\n		                }\r\n		            });        	\r\n			\r\n			//勾选的节点    js事件对象     节点id  对应json数据 \r\n			function zTreeOnCheck(event, treeId, treeNode) {\r\n				var treeObj = $.fn.zTree.getZTreeObj(treeId);\r\n				//全部勾选状态被改变的节点集合\r\n				//var nodes = treeObj.getChangeCheckedNodes();\r\n				//全部符合要求的节点集合\r\n				//console.log( treeObj.getCheckedNodes(true))\r\n				//绑定方法到window上\r\n				$_this.savaNowNode  = function(fn){\r\n					fn(treeNode);\r\n				};\r\n			};\r\n		\r\n			\r\n			//取消a的默认事件\r\n			function uneventDefault(){\r\n				$(\'a\').bind(\'click\',function(e){\r\n					e.preventDefault();\r\n				})\r\n			}\r\n				//添加自定义节点\r\n			function addDiyDom(treeList1, treeNode) {\r\n				var aObj = $(\"#\" + treeNode.tId + \"_a\");\r\n				if($(\"#diyBtn_\" + treeNode.id).length > 0) return;\r\n				\r\n				if(typeof(treeNode.GUANKONGLX) == \'string\'){\r\n					//创建节点\r\n					var editStr =$(\'<span class=\"creatNodeData\"></span>\');\r\n					editStr.append(creatDomNode(treeNode.ANSHANGJIYSPFSXE,\'select\'));\r\n					editStr.append(creatDomNode(treeNode.GUANKONGLX,\'span\'));\r\n						\r\n					aObj.parent().append(editStr);\r\n					aObj.parent().css(\'display\', \'flex\');\r\n					//console.log(parseInt($(\'.remark\').css(\'width\')))\r\n					$(\'.creatNodeData\').css({ \'width\': parseInt($(\'.remark\').eq(1).css(\'width\')) * ($(\'.remark\').length - 1) + \'px\', \'display\': \'flex\', \'position\': \'absolute\', \'right\': \'0\' })\r\n				}\r\n			};\r\n			\r\n			function creatDomNode(data,type){\r\n				var span;\r\n				if(type == \'select\'){\r\n					span =$(\'<span style=\"flex:1\"><select style=\"width:60%;margin:0 20%;overflow: hidden;\"><option role=\"option\" value=\"\">请选择</option><option role=\"option\" value=\"0\">公用</option><option role=\"option\" value=\"1\">事业单位</option><option role=\"option\" value=\"2\">学校</option></select></span>\');\r\n					if(data == \'\' && data != 0){\r\n						$(span).find(\'option\').eq(0).attr(\'selected\',\'selected\')\r\n					}else{\r\n						$(span).find(\'option\').eq((parseInt(data)+1)).attr(\'selected\',\'selected\')\r\n					}\r\n					\r\n				}else{\r\n					span = $(\"<span style=\'flex:1;text-align: center\'>	\"+ data +\"</span>\")\r\n				}\r\n				//console.log(span)\r\n				$(span).find(\'select\').bind(\'change\',function(){\r\n					var tid = $(this).parents(\'li\').eq(0).attr(\'id\');\r\n					var treeObj = $.fn.zTree.getZTreeObj(\"treeList1\");\r\n					// 包含数据id\r\n					var node = treeObj.getNodeByTId(tid);\r\n					//修改后的值\r\n					var val = $(this).val();\r\n				})\r\n				return span;\r\n			}\r\n			\r\n			\r\n</#noparse>\r\n		<#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n		</#if>\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>  \r\n<#noparse>\r\n		});\r\n  \r\n		\r\n	</script>\r\n</body>\r\n</html>\r\n</#noparse>		', '1');
INSERT INTO `p_fm_template` VALUES ('122', '表格树模板', null, null, '', 'resources\\template\\1490689544931', '04dbd24e-f099-4db5-ba9e-2624ac47a258', '<#include \"templates/parseFunction.ftl\">\r\n<#include \"templates/pagerParserFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n<!DOCTYPE html>\r\n<html>\r\n\r\n	<head>\r\n		<meta charset=\"UTF-8\">\r\n		<title>tree</title>\r\n		<link rel=\"stylesheet\" type=\"text/css\" href=\"${basePath}resources/ztree/css/easyui.css\">\r\n		<link rel=\"stylesheet\" type=\"text/css\" href=\"${basePath}resources/ztree/css/icon.css\">\r\n		<link rel=\"stylesheet\" type=\"text/css\" href=\"${basePath}resources/ztree/css/index.css\">\r\n		<script type=\"text/javascript\" src=\"${basePath}resources/JqEdition/jquery.min.js\"></script>\r\n		<!--------------------------easyUI.min.js有修改------------------------------->		\r\n		<script type=\"text/javascript\" src=\"${basePath}resources/ztree/js/jquery.easyui.min.js\"></script>\r\n	</head>\r\n\r\n<BODY>\r\n		<div id=\"diyBtn\" style=\"margin:10px 0 20px 0;\">\r\n			</#noparse>\r\n					<#if pageActs??>\r\n						<#list pageActs?sort_by(\"order\") as act>						\r\n							<@convertButton act />\r\n						</#list>\r\n					</#if>\r\n			<#noparse>\r\n		</div>\r\n		<table id=\"tg\" class=\"easyui-treegrid\" data-options=\"\r\n				iconCls: \'icon-ok\',\r\n				rownumbers: false,\r\n				collapsible: true,\r\n				fitColumns: true,\r\n				url: \'${basePath}getTreeData.do?dynamicPageId=${ (vo.dynamicPageId)!\"\"}#true\',\r\n				method: \'get\',\r\n				idField: \'id\',\r\n				treeField: \'name\',\r\n				animate:false,\r\n				onLoadSuccess:treeSuccess\r\n			\">\r\n\r\n				<thead>\r\n					<tr id=\'trTitle\'>\r\n						<th data-options=\"field: \'name\',width:180,editor: \'text\' \">Task Name</th>\r\n					<th data-options=\"field: \'number\',width:60,align: \'right\',editor: \'numberbox\' \">number</th>\r\n					<th data-options=\"field: \'GUANKONGLX\',width:80,editor: \'datebox\' \">GUANKONGLX</th>\r\n					</tr>\r\n				</thead>\r\n			</table>\r\n\r\n\r\n	<SCRIPT type=\"text/javascript\">\r\n			var basePath= \'${basePath}\';\r\n			var editingId;\r\n			var extend = {};\r\n	\r\n	\r\n		\r\n		//获取选中的id\r\n		extend.getId = function() {\r\n			var row = $(\'#tg\').treegrid(\'getSelected\');\r\n			if(row) {\r\n				return row.id;\r\n			}\r\n		}\r\n		\r\n		\r\n		//节点加载完\r\n		function treeSuccess(node, data) {\r\n			$(\'#tg\').treegrid(\'collapseAll\');\r\n		}\r\n		$(document).ready(function(){\r\n			\r\n			\r\n			\r\n</#noparse>\r\n		<#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n		</#if>\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>  \r\n<#noparse>\r\n		});\r\n  \r\n		\r\n	</script>\r\n</body>\r\n</html>\r\n</#noparse>		', '1');
INSERT INTO `p_fm_template` VALUES ('124', 'Div表单模板(带权限)', null, null, '带权限控制', '', null, '<#include \"templates/parseFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if></#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=9; IE=8; IE=7; IE=EDGE\">\r\n	<meta name=\"renderer\" content=\"webkit\">\r\n    <meta http-equiv=\"pragma\" content=\"no-cache\">\r\n 	<meta http-equiv=\"cache-control\" content=\"no-cache\"> \r\n    <meta http-equiv=\"expires\" content=\"0\">\r\n		\r\n 	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/dist/css/zui_define.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/main.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}base/resources/artDialog/css/ui-dialog.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css\" type=\"text/css\" />\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/content/uploader.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/content/layout.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}formdesigner/page/component/tab/tab.css\"  />\r\n	<style>\r\n		label.control-label{font-weight:bold;}\r\n		.form-condensed .form-group{margin-bottom:0px;}\r\n		.check-all{text-align:center;}\r\n		.check-row{text-align:center;}\r\n		.hides{display: block;width: 0;height: 0;overflow: hidden;}\r\n		input,textarea,select {border : 1px solid #ccc!important;}\r\n		.col-xs-1, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, \r\n		.col-xs-7, .col-xs-8, .col-xs-9, .col-xs-10, .col-xs-11, .col-xs-12, \r\n		.col-sm-1, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, \r\n		.col-sm-7, .col-sm-8, .col-sm-9, .col-sm-10, .col-sm-11, .col-sm-12, \r\n		.col-md-1, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, \r\n		.col-md-7, .col-md-8, .col-md-9, .col-md-10, .col-md-11, .col-md-12, \r\n		.col-lg-1, .col-lg-2, .col-lg-3, .col-lg-4, .col-lg-5, .col-lg-6, \r\n		.col-lg-7, .col-lg-8, .col-lg-9, .col-lg-10, .col-lg-11, .col-lg-12 {\r\n		    padding-right: 0px!important;\r\n		    padding-left: 0px!important;\r\n		    padding-top: 2px;\r\n		    padding-bottom: 2px;\r\n		}	\r\n		.form-group{margin:0px!important;}\r\n		#groupForm{width:920px;}\r\n	</style>	\r\n</head>\r\n</#noparse>\r\n<body  id=\"main\">\r\n    <div class=\"container\" style=\"min-width:920px;\"> \r\n    \r\n    	<div class=\"row\" id=\"buttons\">\r\n			<#if pageActs??>\r\n				<#list pageActs?sort_by(\"order\") as act>\r\n					<#noparse><@shiro.hasPermission name=\"11111:</#noparse>${(pageActPermission[act.getPageId()])!\"\"}<#noparse>\"></#noparse>\r\n						<@convertButton act />\r\n					<#noparse></@shiro.hasPermission></#noparse>\r\n				</#list>\r\n			</#if>\r\n		</div>		\r\n<#noparse>	\r\n		<div class=\"row\">\r\n			<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}workflow/wf/excute.do\" role=\"customForm\" method=\"post\">\r\n				<input type=\"hidden\" name=\"IsRead\" value=\"${ IsRead!\"\"}\" id=\"IsRead\"/>\r\n				<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n				<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n				<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n				<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n				<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n				<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n				<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n				<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n				<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n            	<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n				<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n                <input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n                <input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n                <input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n                <input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n                <input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n                <input type=\"hidden\" name=\"mainTextId\" value=\"${(mainTextId)!\"\"}\" id=\"mainTextId\"/>\r\n				<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n                <input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n                <input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n             	<input type=\"hidden\" name=\"json\" value=\'${json!\"\"}\' id=\"json\"/>\r\n             	<input type=\"hidden\" name=\"data\" value=\'${data!\"\"}\' id=\"data\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n</#noparse>\r\n			<#if tipsComs?? >\r\n				<#list tipsComs?sort_by(\"order\") as c>\r\n					<#if c[\'componentType\']==\'1015\'>\r\n						<@convertTips c />\r\n					</#if>\r\n				</#list>\r\n			</#if>\r\n			<#if layouts??>\r\n				<#if components??>\r\n			 		<#list layouts?sort_by(\"order\") as layout>\r\n			 			<@parseLayout layout components/>\r\n			 		</#list> \r\n			 	</#if>\r\n			</#if>\r\n			</form>\r\n		</div>\r\n	</div>\r\n<#noparse>\r\n	<script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n	</script>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/zui/dist/js/zui.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}base/resources/artDialog/dist/dialog-plus-min.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/tips/jquery.poshytip.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js\" charset=\"UTF-8\"></script>\r\n	<script src=\"${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js\" charset=\"UTF-8\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/ajaxfileupload.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/uploadPreview.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/map.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/webuploader-0.1.5/dist/webuploader.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/uploader.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/select2/select2.js\"></script>\r\n    <script src=\"${basePath}resources/scripts/ntkoWord.js\"></script>  \r\n	<script src=\"${basePath}venson/js/common.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/easyUI/js/jquery.easyui.min.js\" ></script>	\r\n	<script src=\"${basePath}resources/plugins/select2/select2_locale_zh-CN.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/zui/assets/kindeditor/kindeditor-min.js\"></script>\r\n    <script src=\"${basePath}resources/plugins/zui/assets/kindeditor/lang/zh_CN.js\"></script>\r\n    <script src=\"${basePath}resources/plugins/zTree_v3/zTree.js\" ></script>\r\n    <script src=\"${basePath}resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js\" ></script>\r\n    <script src=\"${basePath}resources/scripts/dynamicSelect.js\"></script>\r\n    <script src=\"${basePath}resources/scripts/workFlow.js\"></script>\r\n    <script src=\"${basePath}resources/scripts/defineNote.js\"></script>\r\n	<script  src=\"${basePath}resources/scripts/platform.dataGrid.js\"></script>   \r\n	<script src=\"${basePath}resources/scripts/plateform.userSelect.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/plateform.subUserSelect.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/plateform.sqlUserSelect.js\"></script>\r\n    <script src=\"${basePath}formdesigner/page/component/tab/swiper.min.js\"></script>\r\n    <script src=\"${basePath}formdesigner/page/component/tab/tab.js\"></script>\r\n	<script type=\"text/javascript\">\r\n</#noparse>\r\n		\r\n		$(\"document\").ready(function(){\r\n			$(\".disabled\").find(\"input,select,textarea,button,a\").attr(\"disabled\",\"disabled\");					\r\n			$(\"#groupForm\").children(\"div\").eq(0).find(\"label\").css(\"font-size\",\"22px\");\r\n			$(\"label.centerLabel\").parent().css(\"text-align\",\"center\");\r\n			$(\"label.rightLabel\").css(\"padding-right\",\"17px\").parent().css(\"text-align\",\"right\");\r\n			$(\"label.required\").parent().css(\"text-align\",\"right\");\r\n			$(\"label.radio-inline\").parent().css(\"text-align\",\"left\");\r\n			$(\"input[type=\'radio\']\").parent(\"label\").parent().css(\"text-align\",\"center\").css(\"padding-right\",\"0px\");\r\n			$(\"th\").css(\"padding\",\"2px\").css(\"text-align\",\"center\").css(\"vetical-align\",\"middle\");\r\n			\r\n			$(\"input.isNumber\").each(function(i){\r\n				$(this).val(formatNumber($(this).val()));\r\n			});\r\n			\r\n			$(\"input.isNumber\").bind(\"keyup\",function(event){\r\n				var obj = event.target;\r\n				if(checkIsNumber(obj)){	\r\n					var num = $.trim($(obj).val());	\r\n					num = num.replace(/,/g,\'\');\r\n					var num = formatNumber(num);\r\n					$(obj).val(num);\r\n				}					\r\n			}); \r\n					\r\n			$(\"div[id=\'MoreDownload\']\").bind(\"click\",function(event){\r\n				var ids = new Array();\r\n				$(event.target).parent().parent().find(\"table.table-datatable tbody tr\").each(function(){\r\n					if($(this).hasClass(\"active\")){\r\n						ids.push($(this).children(\'td\').eq(1).children().eq(0).val());\r\n					}\r\n				});\r\n				if(ids.length>0){\r\n					location.href=basePath+\"common/file/batchDownload.do?fileIds=\"+ids;\r\n				}else{\r\n					dialogAlert(\"请选择至少一个文件下载\");\r\n				}\r\n			});	\r\n		\r\n			<#if components??>\r\n		 		 <#if components?keys??>\r\n			 		  <#list components?keys as layoutId>\r\n				 			<#if components[layoutId]??>\r\n				 			<#list components[layoutId] as component>\r\n				 				<@convertComponentScript component />\r\n								<#if (component??) && (component[\'componentType\']==\'1019\')>\r\n										<@convertKindEditorScript component />\r\n								</#if>\r\n								<#if (component??) && (component[\'componentType\']==\'1034\')>\r\n										<@converttabScript component />\r\n								</#if>\r\n				 			</#list>\r\n				 			</#if>\r\n			 		  </#list>\r\n		 		</#if> \r\n			</#if>\r\n\r\n			<#if components??>\r\n	 		 	<#if components?keys??>\r\n		 		  	<#list components?keys as layoutId>\r\n			 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n			 				<@convertComponentScript component />\r\n			 			</#list>\r\n			 			</#if>\r\n		 		  	</#list>\r\n	 			</#if> \r\n			</#if>\r\n\r\n			$.formValidator.initConfig({				\r\n				debug : false,\r\n				onSuccess : function() {				\r\n				},\r\n				onError : function(msg,obj) {\r\n					dialogAlert(msg);\r\n					obj.focus();\r\n				}\r\n			});\r\n			\r\n			<#if components??>\r\n			 	<#if components?keys??>\r\n				 	<#list components?keys as layoutId>\r\n						<#if components[layoutId]??>\r\n			 				<#list components[layoutId] as component>\r\n			 					<#if component[\'componentType\']!=\'1012\' && component[\'componentType\']!=\'1009\'>		\r\n			 						<#if valdatorsMap[component[\'pageId\']]?? || (component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number==0)>\r\n										<#if component[\'componentType\']==\'1011\'>\r\n											$(\'#${(component[\'pageId\'])!\"\"} .orgfile\').formValidator({\r\n												<#if component[\'validateAllowNull\']==\'1\' >empty:true,</#if>\r\n												onFocus:\"${(component[\'validateInputTip\'])!\'\'}\",\r\n												onCorrect:\"${(component[\'validateErrorTip\'])!\'\'}\",\r\n												<#if component[\'componentType\']==\'1002\'> triggerEvent : \"change\" </#if>\r\n											})\r\n											<#else>$(\'#${(component[\'pageId\'])!\"\"}\').formValidator({\r\n												<#if component[\'validateAllowNull\']==\'1\' >empty:true,</#if>\r\n												onFocus:\"${(component[\'validateInputTip\'])!\'\'}\",\r\n												onCorrect:\"${(component[\'validateErrorTip\'])!\'\'}\",\r\n												<#if component[\'componentType\']==\'1002\'> triggerEvent : \"change\" </#if>									\r\n											})\r\n										</#if>\r\n										\r\n			 							<#if (component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number ==0) >\r\n											<#if component[\'componentType\']==\'1011\'>.inputValidator({\r\n												min:1,\r\n												empty:{leftEmpty:true,rightEmpty:true,emptyError:\"\"},\r\n												onError:\"请确认附件已上传\"\r\n											})\r\n											<#else>.inputValidator({\r\n												min:1,\r\n												empty:{leftEmpty:true,rightEmpty:true,emptyError:\"\"},\r\n												onError:\"<#if component[\'description\']??> ${component[\'description\']} </#if>请输入内容\"\r\n											})\r\n										</#if>		\r\n									</#if>\r\n			 						<#if valdatorsMap[component[\'pageId\']]??>\r\n					 					<#list valdatorsMap[component[\'pageId\']] as v>\r\n					 						<@validate v/>\r\n					 					</#list>;\r\n					 				</#if>\r\n				 				</#if>	\r\n				 			</#if>\r\n				 		</#list>\r\n			 		</#if>\r\n		 		  </#list>\r\n	 			</#if> \r\n			</#if>\r\n				\r\n			<#if components??>\r\n	 		 	<#if components?keys??>\r\n		 		  	<#list components?keys as layoutId>\r\n			 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n			 				<#if component[\'pageValidate\']??>\r\n			 					${component[\'pageValidate\']}\r\n			 				</#if>\r\n			 			</#list>\r\n			 			</#if>\r\n		 		  	</#list>\r\n	 			</#if> \r\n			</#if>\r\n			\r\n			<#if components??>\r\n	 		 	<#if components?keys??>\r\n		 		  	<#list components?keys as layoutId>\r\n			 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n							<#if (component??) && (component[\'componentType\']==\'1011\')>\r\n			 				<@convertFileScript component />\r\n							</#if>\r\n			 			</#list>\r\n			 			</#if>\r\n		 		  	</#list>\r\n	 			</#if> \r\n			</#if>\r\n			\r\n			<#if components??>\r\n	 		 	<#if components?keys??>\r\n		 		  	<#list components?keys as layoutId>\r\n			 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n							<#if (component??) && (component[\'componentType\']==\'1016\')>\r\n			 				<@convertImageScript component />\r\n							</#if>\r\n			 			</#list>\r\n			 			</#if>\r\n		 		  	</#list>\r\n	 			</#if> \r\n			</#if>\r\n		\r\n	        <#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n			</#if>\r\n		});	\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>\r\n		\r\n		<#if components??>\r\n		 	<#if components?keys??>\r\n	 		  	<#list components?keys as layoutId>\r\n		 			<#if components[layoutId]??>\r\n		 			<#list components[layoutId] as component>\r\n		 				<#if component[\'pageActScript\']??>\r\n		 					${component[\'pageActScript\']}\r\n		 				</#if>\r\n		 			</#list>\r\n		 			</#if>\r\n	 		  	</#list>\r\n			</#if> \r\n		</#if>\r\n		\r\n		function formatUploadTable(){\r\n			var $table = $(\"div[id=\'datatable-uploadListTable\']\").find(\"table.table\");\r\n			$table.css(\"table-layout\",\"fixed\");\r\n			$table.find(\"tr\").each(function(){\r\n				var $tr = $(this);\r\n				$tr.find(\"td[data-index=\'0\']\").css(\"width\",\"10%\");\r\n				$tr.find(\"td[data-index=\'1\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"td[data-index=\'2\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"td[data-index=\'3\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"th[data-index=\'check\']\").css(\"width\",\"10%\");\r\n				$tr.find(\"th[data-index=\'1\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"th[data-index=\'2\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"th[data-index=\'3\']\").css(\"width\",\"30%\");		\r\n			})			  \r\n		}\r\n	</script>\r\n\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('125', '列表模板(带权限)', null, null, '带权限控制，可以写加载后脚本', '', null, '<#include \"templates/parseFunction.ftl\">\r\n<#include \"templates/pagerParserFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse>\r\n		<#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if>\r\n	</#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n	<meta name=\"renderer\" content=\"webkit\">	\r\n 	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/dist/css/zui.min.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/main.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}base/resources/artDialog/css/ui-dialog.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css\" type=\"text/css\" />\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/dist/css/zui.min.css\"/>\r\n    <link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css\"/>\r\n    <link rel=\"stylesheet\" href=\"${basePath}resources/plugins/easyUI/themes/material/easyui.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/easyUI/themes/icon.css\"/>\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n	<![endif]-->	       \r\n</head>\r\n<body id=\"main\">\r\n    <div class=\"container-fluid\">   	\r\n		<div class=\"row\" id=\"buttons\">\r\n</#noparse>					\r\n		<#if pageActs??>\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<#noparse><@shiro.hasPermission name=\"11111:</#noparse>${(pageActPermission[act.getPageId()])!\"\"}<#noparse>\"></#noparse>\r\n					<@convertButton act />\r\n				<#noparse></@shiro.hasPermission></#noparse>\r\n			</#list>\r\n		</#if>			\r\n<#noparse>\r\n		</div>\r\n</#noparse>		\r\n		<#if components?? >\r\n			<#list components?sort_by(\"order\") as c>				\r\n				<#if c[\'componentType\']==\'1013\' && (c[\'searchLocation\']!\'1\')==\'1\'>\r\n					<@parseComponent c />\r\n				</#if>\r\n			</#list>\r\n		</#if>\r\n	\r\n<#noparse>			\r\n		<div class=\"row\" id=\"datatable\">\r\n			<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n	        	<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n				<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n				<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n				<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n				<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n				<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n				<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n				<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n				<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n                <input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n				<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n				<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n				<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n				<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n				<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n				<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n				<input type=\"hidden\" name=\"mainTextId\" value=\"${(mainTextId)!\"\"}\" id=\"mainTextId\"/>\r\n				<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n                <input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n                <input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n				<input type=\"hidden\" name=\"currentPage\" value=\"${currentPage!\"1\"}\">\r\n				<input type=\"hidden\" name=\"searchInfo\" value=\"${searchInfo!\"\"}\" id=\"searchInfo\"/>\r\n				<table class=\"table datatable table-bordered sortable\">\r\n					<thead>\r\n						<tr>\r\n							<th class=\"hidden\"></th>\r\n							<th data-width=\"70\">序号</th>\r\n</#noparse>\r\n					<#if components?? >\r\n						<#list components?sort_by(\"order\") as c>\r\n							<#if c[\'componentType\']==\'1036\'>\r\n								<@convertAddSearch c />\r\n							</#if>\r\n						</#list>\r\n					</#if>\r\n					<#if components?? >\r\n						 <#list components?sort_by(\"order\") as c>\r\n							<#if c[\'componentType\']!=\'1013\'&&c[\'componentType\']!=\'1036\' >\r\n								<@parseColumn c />\r\n							</#if>\r\n						</#list>\r\n					</#if>\r\n<#noparse>\r\n						</tr>\r\n					</thead>\r\n					<tbody id=\"dataList\">\r\n		            	<#if </#noparse>${dataAlias[0]}<#noparse>_list?? && </#noparse>${dataAlias[0]}<#noparse>_list?size gte 1>	\r\n		            	 	<#list\r\n</#noparse>\r\n		            	 		${dataAlias[0]}_list as ${dataAlias[0]}>\r\n<#noparse> \r\n							<tr>\r\n								<td class=\"hidden formData\"><input id=\"boxs\" type=\"hidden\"\r\n								value=\"${</#noparse>${dataAlias[0]}<#noparse>.ID!\'\'}\"></td> 	\r\n</#noparse>				\r\n						<td><#noparse>${(</#noparse>${dataAlias[0]}_list_paginator.getPage() * ${dataAlias[0]}_list_paginator.getLimit() - ${dataAlias[0]}_list_paginator.getLimit() + ${dataAlias[0]}_index + 1<#noparse>)?string(\'0000\')}</#noparse></td>\r\n						<#if components?? >\r\n							 <#list components?sort_by(\"order\") as c>\r\n								<#if c[\'componentType\']!=\'1013\'&&c[\'componentType\']!=\'1036\' >\r\n									<@parseColumnData c />\r\n								</#if>\r\n							</#list>\r\n						</#if>\r\n<#noparse>				\r\n							</tr>\r\n							</#list>\r\n						</#if>\r\n					</tbody>\r\n				</table>\r\n			</form>\r\n		</div>\r\n</#noparse>	\r\n		<#if components?? >\r\n						 <#list components?sort_by(\"order\") as c>\r\n						 	<#if c[\'componentType\']==\'1013\' && (c[\'searchLocation\']??&&c[\'searchLocation\']==\'2\')>\r\n								<@parseComponent c />\r\n							</#if>\r\n						</#list>\r\n		</#if>\r\n		<div class=\"row navbar-fixed-bottom text-center\" id=\"pagers\">	\r\n			<@pageNavigation  dataAlias[0] />\r\n<#noparse>\r\n		</div>\r\n	</div>\r\n</#noparse>\r\n<#noparse>	\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}base/resources/zui/dist/js/zui.js\"></script>\r\n	<script src=\"${basePath}base/resources/artDialog/dist/dialog-plus-min.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/tips/jquery.poshytip.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js\" charset=\"UTF-8\"></script>\r\n	<script src=\"${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js\" charset=\"UTF-8\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/workFlow.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/pageTurn.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.orderBy.js\"></script>\r\n	<script src=\"${basePath}base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js\"></script>\r\n	<script src=\'${basePath}venson/js/common.js\'></script>\r\n	<script src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script src=\"${basePath}resources/scripts/datepickerInit.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/easyUI/js/jquery.easyui.min.js\" ></script>\r\n	<script>\r\n		var basePath = \'${basePath}\';\r\n		$(document).ready(function(){			\r\n			var count=0;\r\n		  	$(\'table.datatable\').datatable({\r\n			  	checkable: true,\r\n			  	checksChanged:function(event){\r\n				  	this.$table.find(\"tbody tr\").find(\"input#boxs\").removeAttr(\"name\");\r\n				  	var checkArray = event.checks.checks;\r\n				  	count = checkArray.length;\r\n				  	for(var i=0;i<count;i++){\r\n					  	this.$table.find(\"tbody tr\").eq(checkArray[i]).find(\"input#boxs\").attr(\"name\",\"_selects\");\r\n				  	}\r\n			  	}\r\n		  	});\r\n		  	\r\n			$(\".input-group .input-group-addon\").each(function(item){\r\n				 $(this).removeClass(\"fix-border\").removeClass(\"fix-padding\");\r\n			});\r\n			\r\n			initOrderBy();\r\n			\r\n			$.formValidator.initConfig({			\r\n					debug : false,\r\n					onSuccess : function() {					\r\n				},\r\n				onError : function() {\r\n					alert(\"错误，请看提示\")\r\n				}\r\n			});\r\n</#noparse>\r\n		<#if components?? >\r\n				<#list components?sort_by(\"order\") as c>\r\n					<#if c[\'componentType\']==\'1013\' >\r\n						<@convertContainPageScript c />\r\n					</#if>\r\n					<#if c[\'componentType\']==\'1036\'>\r\n						<@convertAddSearchScript c />\r\n					</#if>\r\n				</#list>\r\n			</#if>\r\n		\r\n		<#if page??>\r\n			<#if page.getAfterLoadScript()??>\r\n				${page.getAfterLoadScript()}\r\n			</#if>\r\n		</#if>\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>  \r\n<#noparse>\r\n		});\r\n</#noparse>		  \r\n		\r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('126', 'easyui左右iframe', null, null, 'easyui左右iframe', '', null, '<#include \"templates/parseFunction.ftl\">\r\n<#include \"templates/pagerParserFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n	<meta name=\"renderer\" content=\"webkit\">	\r\n 	<link rel=\"stylesheet\" href=\"${basePath}base/resources/zui/dist/css/zui.min.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/main.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/zTreeStyle/szcloud.css\">\r\n	\r\n\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n	<![endif]-->	       \r\n</head>\r\n<body id=\"main\" class=\"container-fluid\" style=\"overflow: hidden;\">\r\n	<div id=\"layout\" class=\"row\" style=\"display: flex; \">\r\n		<div class=\"left col-xs-4 col-sm-4 col-md-3\">\r\n			<iframe class=\"ui-layout-center\" name=\"sysEditFrameLeft\" id=\"sysEditFrameLeft\" scrolling=\"auto\" frameborder=\"0\" width=\"100%\" height=\"100%\"></iframe>\r\n		</div>\r\n		<div class=\"rifht col-xs-9 col-sm-8 col-md-9\" >\r\n			<iframe class=\"ui-layout-center\" name=\"sysEditFrameRight\" id=\"sysEditFrameRight\" scrolling=\"auto\" frameborder=\"0\" width=\"100%\" height=\"100%\"></iframe>\r\n		</div>\r\n	</div>\r\n</#noparse>	\r\n\r\n<#noparse>	\r\n\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery-ui-latest.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery.layout-latest.js\"></script>\r\n	<script type=\"text/javascript\">\r\n\r\n		var basePath = \'${basePath}\';\r\n\r\n		$(\".left iframe\").css(\"height\",$(window).height()+\"px\");\r\n</#noparse>		\r\n		<#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n		</#if>\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>  \r\n  \r\n		\r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('127', 'easyui上下布局iframe', null, null, 'easyui上下布局iframe', '', null, '<#include \"templates/parseFunction.ftl\">\r\n<#include \"templates/pagerParserFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n	<meta name=\"renderer\" content=\"webkit\">	\r\n 	<link rel=\"stylesheet\" href=\"${basePath}base/resources/zui/dist/css/zui.min.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/main.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/zTreeStyle/szcloud.css\">\r\n	\r\n\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n	<![endif]-->	       \r\n</head>\r\n<body id=\"main\" class=\"container-fluid\" style=\"overflow: hidden;\">\r\n	<div id=\"layout\" class=\"row\">\r\n		<div class=\"row\" height=\"30%\">\r\n			<iframe class=\"ui-layout-center\" name=\"sysEditFrameTop\" id=\"sysEditFrameTop\" scrolling=\"auto\" frameborder=\"0\" width=\"100%\" height=\"100%\" style=\"height:400px;\"></iframe>\r\n		</div>\r\n		<div class=\"row\" height=\"70%\" >\r\n			<iframe class=\"ui-layout-center\" name=\"sysEditFrameDown\" id=\"sysEditFrameDown\" scrolling=\"auto\" frameborder=\"0\" width=\"100%\" height=\"100%\" style=\"height:400px;\"></iframe>\r\n		</div>\r\n	</div>\r\n</#noparse>	\r\n\r\n<#noparse>	\r\n\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery-ui-latest.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}venson/js/common.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery.layout-latest.js\"></script>\r\n	<script type=\"text/javascript\">\r\n\r\n		var basePath = \'${basePath}\';\r\n</#noparse>		\r\n		<#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n		</#if>\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>  \r\n  \r\n		\r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('131', '钉钉表单模板', null, null, '钉钉表单模板', '', null, '<#include \"templates/ddParseFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if></#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"renderer\" content=\"webkit\">\r\n	<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\">\r\n	<link href=\"${basePath}dingding/css/mui.min.css\" rel=\"stylesheet\" />\r\n	<link href=\"${basePath}dingding/css/myDingding.css\" rel=\"stylesheet\" />\r\n</head>\r\n</#noparse>\r\n<body>  			\r\n<#noparse>	\r\n	<form id=\"groupForm\" method=\"post\" style=\"margin-bottom:58px;\">\r\n		<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n		<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n		<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n		<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n		<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n		<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n		<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n		<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n		<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n		<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n		<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n		<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n		<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n		<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n		<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n		<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n		<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n		<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n		<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n		<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n        <input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\" />\r\n        <input type=\"hidden\" name=\"readOnly\" value=\"\" id=\"readOnly\">\r\n		<input type=\"hidden\" id=\"deliver\" />\r\n		<input type=\"hidden\" id=\"GuDing\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowName\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowMaster\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowResult\" />\r\n		<input type=\"hidden\" id=\"slectsUserNames\" name=\"slectsUserNames\" />\r\n        <input type=\"hidden\" id=\"dd_message_title\" name=\"dd_message_title\" />\r\n</#noparse>\r\n	<#if layouts??>\r\n		<#if components??>\r\n	 		<#list layouts?sort_by(\"order\") as layout>\r\n	 			<@parseLayout layout components/>\r\n	 		</#list> \r\n	 	</#if>\r\n	</#if>\r\n	</form>\r\n	<div class=\"submit-foot-content\" id=\"buttons\">\r\n		<#if pageActs??>\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<@convertButton act />\r\n			</#list>\r\n		</#if>\r\n	</div>   \r\n<#noparse>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\'${basePath}venson/js/common.js\'></script> \r\n	<script>		\r\n		if(Comm.isMobile()){\r\n			document.write(\"<script src=\'${basePath}dingding/js/dingtalk.js\' charset=\'utf-8\'><\\/script>\");\r\n		}\r\n		else{\r\n			document.write(\"<script src=\'https://g.alicdn.com/dingding/dingtalk-pc-api/2.9.0/index.js\' charset=\'utf-8\'><\\/script>\");\r\n		}\r\n	</script>\r\n	<script>\r\n		if(window.hasOwnProperty(\"DingTalkPC\")){\r\n			window.dd = DingTalkPC;\r\n		}\r\n	</script>\r\n    <script src=\'${basePath}dingding/js/resource.js\'></script>\r\n    <script src=\'${basePath}dingding/js/autoTextarea.js\'></script>\r\n    <script src=\'${basePath}dingding/js/setApprovalPerson.js\'></script>\r\n</#noparse>\r\n	<script>	\r\n		Comm.ddConfig();\r\n		\r\n		dd.ready(function(){});	      \r\n		\r\n		document.addEventListener(\'backbutton\', function(e) {\r\n          	// 在这里处理你的业务逻辑\r\n			history.go(-2);\r\n          	e.preventDefault(); //backbutton事件的默认行为是回退历史记录，如果你想阻止默认的回退行为，那么可以通过preventDefault()实现\r\n		});\r\n		<#if components??>\r\n 		 	<#if components?keys??>\r\n	 		  	<#list components?keys as layoutId>\r\n		 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n			 				<@convertComponentScript component />\r\n			 			</#list>\r\n		 			</#if>\r\n	 		  	</#list>\r\n 			</#if> \r\n		</#if>\r\n   \r\n	        <#if page??>\r\n			<#if page.getAfterLoadScript()??>\r\n				${page.getAfterLoadScript()}\r\n			</#if>\r\n		</#if>\r\n	\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>\r\n		\r\n		<#if components??>\r\n		 	<#if components?keys??>\r\n	 		  	<#list components?keys as layoutId>\r\n		 			<#if components[layoutId]??>\r\n		 			<#list components[layoutId] as component>\r\n		 				<#if component[\'pageActScript\']??>\r\n		 					${component[\'pageActScript\']}\r\n		 				</#if>\r\n		 			</#list>\r\n		 			</#if>\r\n	 		  	</#list>\r\n			</#if> \r\n		</#if>	\r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('135', '钉钉带流程手机表单', null, null, '带流程的，流程视图滴', '', null, '<#include \"templates/ddParseFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if></#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"renderer\" content=\"webkit\">\r\n	<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\">\r\n	<link href=\"${basePath}dingding/css/mui.min.css\" rel=\"stylesheet\" />\r\n	<link href=\"${basePath}dingding/css/myDingding.css\" rel=\"stylesheet\" />\r\n</head>\r\n</#noparse>\r\n<body>  			\r\n<#noparse>	\r\n	<form id=\"groupForm\" method=\"post\">\r\n		<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n		<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n		<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n		<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n		<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n		<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n		<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n		<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n		<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n		<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n		<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n		<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n		<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n		<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n		<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n		<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n		<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n		<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n		<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n		<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n        <input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\" />\r\n        <input type=\"hidden\" name=\"logIsSubmit\" value=\"${(logIsSubmit)!\"\"}\" id=\"logIsSubmit\">\r\n		<input type=\"hidden\" id=\"deliver\" />\r\n		<input type=\"hidden\" id=\"GuDing\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowName\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowMaster\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowResult\" />\r\n		<input type=\"hidden\" id=\"slectsUserNames\" name=\"slectsUserNames\" />\r\n<input type=\"hidden\" id=\"CC_slectsUserIds\" name=\"CC_slectsUserIds\" />\r\n<input type=\"hidden\" id=\"dd_message_title\" name=\"dd_message_title\" />\r\n<input type=\"hidden\" id=\"work_logs_content\" name=\"work_logs_content\" />\r\n</#noparse>\r\n	<#if layouts??>\r\n		<#if components??>\r\n	 		<#list layouts?sort_by(\"order\") as layout>\r\n	 			<@parseLayout layout components/>\r\n	 		</#list> \r\n	 	</#if>\r\n	</#if>\r\n	</form>\r\n	<div class=\"submit-foot-content\" id=\"buttons\">\r\n		<#if pageActs??>\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<@convertButton act />\r\n			</#list>\r\n		</#if>\r\n	</div>   \r\n<#noparse>\r\n    <script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}dingding/js/dingtalk.js\"></script>\r\n<script src=\'${basePath}venson/js/common.js\'></script>   \r\n    <script src=\'${basePath}dingding/js/resource.js\'></script>\r\n	\r\n    <script src=\'${basePath}dingding/js/autoTextarea.js\'></script>\r\n    <script src=\'${basePath}dingding/js/setApprovalPerson.js\'></script>\r\n</#noparse>\r\n	<script>	\r\n        (function(){\r\n            var html = \'<div class=\"history-record-content-normal\" ><p><span> </span><span class=\"history-record\">\' + dd_res.viewRecord +\r\n                       \'</span></p ></div>\'\r\n            $(\"#groupForm\").prepend(html);\r\n            \r\n            $(\".history-record-content-normal\").click(function(){\r\n                var dynamicPageId = $(\"#dynamicPageId\").val();\r\n                location.href = baseUrl + \"dingding/historyRecord.html?dynamicPageId=\" + dynamicPageId;\r\n            });\r\n        })();\r\n        \r\n		Comm.ddConfig();\r\n		          \r\n		dd.ready(function(){\r\n			<#if components??>\r\n	 		 	<#if components?keys??>\r\n		 		  	<#list components?keys as layoutId>\r\n			 			<#if components[layoutId]??>\r\n				 			<#list components[layoutId] as component>\r\n				 				<@convertComponentScript component />\r\n				 			</#list>\r\n			 			</#if>\r\n		 		  	</#list>\r\n	 			</#if> \r\n			</#if>\r\n\r\n	        <#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n			</#if>\r\n		\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<#if true>\r\n					<@convertScript act />\r\n				</#if>\r\n			</#list>\r\n			\r\n			<#if components??>\r\n			 	<#if components?keys??>\r\n		 		  	<#list components?keys as layoutId>\r\n			 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n			 				<#if component[\'pageActScript\']??>\r\n			 					${component[\'pageActScript\']}\r\n			 				</#if>\r\n			 			</#list>\r\n			 			</#if>\r\n		 		  	</#list>\r\n				</#if> \r\n			</#if>	\r\n\r\n            setApprovalPerson();\r\n            addCCPerson();\r\n		});	           \r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('136', '钉钉PC端不走流程模板', null, null, '钉钉PC端不走流程模板', '', null, '<#include \"templates/ddPcParseFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if></#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"renderer\" content=\"webkit\">\r\n	<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\">\r\n	<link href=\"${basePath}dingding/css/pc/style.css\" rel=\"stylesheet\" />\r\n	<link href=\"${basePath}dingding/css/pc/sqm.css\" rel=\"stylesheet\" />\r\n	<link href=\"${basePath}dingding/css/pc/datetimepicker.css\" rel=\"stylesheet\" />\r\n</head>\r\n</#noparse>\r\n<body>  			\r\n<#noparse>	\r\n	<form id=\"groupForm\" method=\"post\" style=\"margin-bottom:58px;\">\r\n		<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n		<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n		<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n		<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n		<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n		<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n		<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n		<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n		<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n		<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n		<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n		<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n		<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n		<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n		<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n		<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n		<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n		<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n		<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n		<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n        <input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\" />\r\n        <input type=\"hidden\" name=\"readOnly\" value=\"\" id=\"readOnly\">\r\n		<input type=\"hidden\" id=\"deliver\" />\r\n		<input type=\"hidden\" id=\"GuDing\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowName\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowMaster\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowResult\" />\r\n		<input type=\"hidden\" id=\"slectsUserNames\" name=\"slectsUserNames\" />\r\n        <input type=\"hidden\" id=\"dd_message_title\" name=\"dd_message_title\" />\r\n</#noparse>\r\n	<#if layouts??>\r\n		<#if components??>\r\n	 		<#list layouts?sort_by(\"order\") as layout>\r\n	 			<@parseLayout layout components/>\r\n	 		</#list> \r\n	 	</#if>\r\n	</#if>\r\n	</form>\r\n	<div class=\"submit-foot-content\" id=\"buttons\">\r\n		<#if pageActs??>\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<@convertButton act />\r\n			</#list>\r\n		</#if>\r\n	</div>   \r\n<#noparse>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"https://g.alicdn.com/dingding/dingtalk-pc-api/2.9.0/index.js\" charset=\"utf-8\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script> \r\n    <script src=\"${basePath}dingding/js/resource.js\"></script>\r\n    <script src=\"${basePath}dingding/js/autoTextarea.js\"></script>\r\n    <script src=\"${basePath}dingding/js/ddPc.js\"></script>\r\n</#noparse>\r\n	<script>	\r\n		if(window.hasOwnProperty(\"DingTalkPC\")){\r\n			window.dd = DingTalkPC;\r\n		}\r\n	\r\n		Comm.ddConfig();\r\n\r\n		ddPc.addSendHtml();\r\n\r\n		document.addEventListener(\"backbutton\", function(e) {\r\n          	// 在这里处理你的业务逻辑\r\n			history.go(-2);\r\n          	e.preventDefault(); //backbutton事件的默认行为是回退历史记录，如果你想阻止默认的回退行为，那么可以通过preventDefault()实现\r\n		});\r\n		<#if components??>\r\n 		 	<#if components?keys??>\r\n	 		  	<#list components?keys as layoutId>\r\n		 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n			 				<@convertComponentScript component />\r\n			 			</#list>\r\n		 			</#if>\r\n	 		  	</#list>\r\n 			</#if> \r\n		</#if>\r\n   \r\n	    <#if page??>\r\n			<#if page.getAfterLoadScript()??>\r\n				${page.getAfterLoadScript()}\r\n			</#if>\r\n		</#if>\r\n	\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>\r\n		\r\n		<#if components??>\r\n		 	<#if components?keys??>\r\n	 		  	<#list components?keys as layoutId>\r\n		 			<#if components[layoutId]??>\r\n		 			<#list components[layoutId] as component>\r\n		 				<#if component[\'pageActScript\']??>\r\n		 					${component[\'pageActScript\']}\r\n		 				</#if>\r\n		 			</#list>\r\n		 			</#if>\r\n	 		  	</#list>\r\n			</#if> \r\n		</#if>	\r\n	</script>\r\n</body>\r\n</html>', null);
INSERT INTO `p_fm_template` VALUES ('137', '钉钉PC端流程模板', null, null, '钉钉PC端流程模板', null, null, '<#include \"templates/ddPcParseFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if></#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"renderer\" content=\"webkit\">\r\n	<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\">\r\n	<link href=\"${basePath}dingding/css/pc/style.css\" rel=\"stylesheet\" />\r\n	<link href=\"${basePath}dingding/css/pc/sqm.css\" rel=\"stylesheet\" />\r\n	<link href=\"${basePath}dingding/css/pc/datetimepicker.css\" rel=\"stylesheet\" />\r\n	<link href=\"${basePath}dingding/css/pc/approval.css\" rel=\"stylesheet\" />\r\n	<link href=\"//g.alicdn.com/SWH5/ding-flow-pc/2.2.3/bootstrap.css\" rel=\"stylesheet\" />\r\n	<link href=\"//g.alicdn.com/SWH5/ding-flow-pc/2.2.3/app.css\" rel=\"stylesheet\" />	\r\n	<style>\r\n		.colFirst{\r\n			float: left;\r\n		    padding: 0px 10px 0px 10px;\r\n		    width: 125px;\r\n		    text-align: right;		\r\n		}\r\n		.colSecond{\r\n			overflow: auto;\r\n		}		\r\n	</style>\r\n</head>\r\n</#noparse>\r\n<body>  			\r\n<#noparse>	\r\n	<form id=\"groupForm\" method=\"post\" style=\"margin-bottom:58px;\">\r\n		<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n		<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n		<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n		<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n		<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n		<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n		<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n		<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n		<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n		<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n		<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n		<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n		<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n		<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n		<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n		<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n		<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n		<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n		<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n		<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n        <input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\" />\r\n        <input type=\"hidden\" name=\"logIsSubmit\" value=\"${(logIsSubmit)!\"\"}\" id=\"logIsSubmit\">\r\n		<input type=\"hidden\" id=\"deliver\" />\r\n		<input type=\"hidden\" id=\"GuDing\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowName\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowMaster\" />\r\n		<input type=\"hidden\" id=\"sendMobileFlowResult\" />\r\n		<input type=\"hidden\" id=\"slectsUserNames\" name=\"slectsUserNames\" />\r\n		<input type=\"hidden\" id=\"CC_slectsUserIds\" name=\"CC_slectsUserIds\" />\r\n		<input type=\"hidden\" id=\"dd_message_title\" name=\"dd_message_title\" />\r\n		<input type=\"hidden\" id=\"work_logs_content\" name=\"work_logs_content\" />\r\n</#noparse>\r\n	<#if layouts??>\r\n		<#if components??>\r\n	 		<#list layouts?sort_by(\"order\") as layout>\r\n	 			<@parseLayout layout components/>\r\n	 		</#list> \r\n	 	</#if>\r\n	</#if>\r\n	</form>\r\n	<div class=\"submit-foot-content\" id=\"buttons\">\r\n		<#if pageActs??>\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<@convertButton act />\r\n			</#list>\r\n		</#if>\r\n	</div>   \r\n<#noparse>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"https://g.alicdn.com/dingding/dingtalk-pc-api/2.9.0/index.js\" charset=\"utf-8\"></script>\r\n    <script src=\"${basePath}dingding/js/datetimepicker.js\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script> \r\n    <script src=\"${basePath}dingding/js/resource.js\"></script>\r\n    <script src=\"${basePath}dingding/js/autoTextarea.js\"></script>\r\n    <script src=\"${basePath}dingding/js/ddPc.js\"></script>\r\n</#noparse>\r\n	<script>	\r\n		if(window.hasOwnProperty(\"DingTalkPC\")){\r\n			window.dd = DingTalkPC;\r\n		}\r\n	\r\n		Comm.ddConfig();\r\n\r\n		<#if components??>\r\n 		 	<#if components?keys??>\r\n	 		  	<#list components?keys as layoutId>\r\n		 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n			 				<@convertComponentScript component />\r\n			 			</#list>\r\n		 			</#if>\r\n	 		  	</#list>\r\n 			</#if> \r\n		</#if>\r\n   \r\n	    <#if page??>\r\n			<#if page.getAfterLoadScript()??>\r\n				${page.getAfterLoadScript()}\r\n			</#if>\r\n		</#if>\r\n	\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>\r\n		\r\n		<#if components??>\r\n		 	<#if components?keys??>\r\n	 		  	<#list components?keys as layoutId>\r\n		 			<#if components[layoutId]??>\r\n		 			<#list components[layoutId] as component>\r\n		 				<#if component[\'pageActScript\']??>\r\n		 					${component[\'pageActScript\']}\r\n		 				</#if>\r\n		 			</#list>\r\n		 			</#if>\r\n	 		  	</#list>\r\n			</#if> \r\n		</#if>\r\n		\r\n		ddPc.addApprovalHtml();\r\n	</script>\r\n</body>\r\n</html>', null);
INSERT INTO `p_fm_template` VALUES ('2', 'pc_lte_form', null, null, '', '', null, '<#include \"templates/parseAdminLTE.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if></#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap.min.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/font-awesome.min.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrapValidator.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/ionicons.min.css\">\r\n	<script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		var cssFile={},scriptFile={};\r\n		//引入组件样式表\r\n	</#noparse>\r\n	<#if components??>\r\n			<#if components?keys??>\r\n				<#list components?keys as layoutId>\r\n					<#if components[layoutId]??>\r\n					<#list components[layoutId] as component>\r\n						<@parseComponentCSSFile component />\r\n					</#list>\r\n					</#if>\r\n				</#list>\r\n			</#if> \r\n	</#if>\r\n	<#noparse>\r\n	for(var e in cssFile){document.write(\'<link rel=\"stylesheet\" href=\"\'+e+\'\">\')}\r\n	cssFile=null;\r\n	</script>\r\n	\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/AdminLTE.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/main.css\">\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n        \r\n	<![endif]-->\r\n	<script>\r\n		\r\n    </script>\r\n</head>\r\n<body>\r\n    <div class=\"content\"> \r\n		<div class=\"row opeBtnGrop\" id=\"buttons\">\r\n			<div class=\"col-md-12\">\r\n</#noparse>\r\n				<#if pageActs??>\r\n					<#list pageActs?sort_by(\"order\") as act>\r\n						<@convertButton act />\r\n					</#list>\r\n				</#if>\r\n			</div>\r\n		</div>\r\n		<div class=\"row\" id=\"newTitle\"></div>\r\n<#noparse>\r\n		<div class=\"row\">\r\n			<div class=\"col-md-12\">\r\n				<div class=\"box box-info\">\r\n					<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}workflow/wf/excute.do\" role=\"customForm\" method=\"post\">\r\n						<div class=\"box-body\">\r\n							<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n							<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n							<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n							<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n							<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n							<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n							<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n							<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n							<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n							<input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n							<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n							<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n							<input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\">\r\n							<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n							<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n							<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n							<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n			</#noparse>\r\n							\r\n								<#if layouts??>\r\n									<#if components??>\r\n										<#list layouts?sort_by(\"order\") as layout>\r\n											<@parseLayout layout components/>\r\n										</#list> \r\n									</#if>\r\n								</#if>\r\n			<#noparse>\r\n						</div>\r\n					</form>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/common.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	</#noparse>\r\n	<script>\r\n	//引入组件脚本\r\n	<#if components??>\r\n			<#if components?keys??>\r\n				<#list components?keys as layoutId>\r\n					<#if components[layoutId]??>\r\n					<#list components[layoutId] as component>\r\n						<@parseComponentScriptFile component />\r\n					</#list>\r\n					</#if>\r\n				</#list>\r\n			</#if> \r\n	</#if>\r\n\r\n<#noparse>\r\n\r\n	for(var e in scriptFile){document.write(\'<script src=\"\'+e+\'\"><\\/script>\')}\r\n	scriptFile=null;\r\n	</script>\r\n	<script>\r\n</#noparse>		\r\n			<#if components??>\r\n	 		 	<#if components?keys??>\r\n		 		  	<#list components?keys as layoutId>\r\n			 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n			 				<@convertComponentScript component />\r\n			 			</#list>\r\n			 			</#if>\r\n		 		  	</#list>\r\n	 			</#if> \r\n			</#if>\r\n		\r\n		\r\n	        <#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n			</#if>\r\n			\r\n			\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<#if true>\r\n					<@convertScript act />\r\n				</#if>\r\n			</#list>\r\n			\r\n	</script>\r\n</body>\r\n</html>', null);
INSERT INTO `p_fm_template` VALUES ('54', '片段页面主页面', null, null, 'pp', 'resources\\template\\1420787124322', null, '<#include \"templates/parseFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n	<meta name=\"renderer\" content=\"webkit\">\r\n 		\r\n 	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/dist/css/zui.min.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/main.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/artDialog/css/ui-dialog.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css\" type=\"text/css\" />\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n	<![endif]-->	\r\n</head>\r\n<body  id=\"main\">\r\n    	\r\n		<div class=\"row\" id=\"buttons\">\r\n</#noparse>\r\n		<#--	 后续会有权限 \r\n		<#list pageActs as act>\r\n				\r\n				<#if true>\r\n					${act.html}\r\n				</#if>\r\n			</#list>\r\n		-->	\r\n		\r\n			<#if pageActs??>\r\n				<#list pageActs?sort_by(\"order\") as act>\r\n					<#noparse><@shiro.hasPermission name=\"11111:</#noparse>${(pageActPermission[act.getPageId()])!\"\"}<#noparse>\"></#noparse>\r\n						<@convertButton act />\r\n					<#noparse></@shiro.hasPermission></#noparse>\r\n				</#list>\r\n			</#if>\r\n		\r\n<#noparse>\r\n		</div>\r\n		<div class=\"row\" id=\"dataform\">\r\n			<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}document/excute.do\" role=\"customForm\" method=\"post\">\r\n				<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n				<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n				<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n				<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n				<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n				<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n				<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n</#noparse>\r\n			<#-- ${layout} 最终目标以layout为准\r\n				<#list components as c>\r\n					<#if c_index%2 == 0>\r\n						<div class=\"form-group\">\r\n							<div class=\"col-md-2\">\r\n								${c.html}\r\n							</div>\r\n					</#if>\r\n					<#if c_index%2 == 1>\r\n							<div class=\"col-md-4\">\r\n								${c.html}\r\n							</div>\r\n						</div>\r\n					</#if>\r\n					<#if !c_has_next && c_index%2 == 0>\r\n						</div>\r\n					</#if>\r\n				</#list>\r\n			 -->\r\n			 <#if layouts??>\r\n			 	<#if components??>\r\n			 		<#list layouts?sort_by(\"order\") as layout>\r\n			 			<@parseLayout layout components/>\r\n			 		</#list> \r\n			 	</#if>\r\n			 </#if>\r\n<#noparse>\r\n\r\n			</form>\r\n		</div>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery-1.10.2.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/zui/dist/js/zui.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/artDialog/dist/dialog-plus-min.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/tips/jquery.poshytip.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js\" charset=\"UTF-8\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js\" charset=\"UTF-8\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/common.js\"></script>\r\n	\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n</#noparse>	\r\n	<script type=\"text/javascript\">\r\n		$(document).ready(function(){\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<@convertComponentScript component />\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			\r\n			$.formValidator.initConfig({\r\n				formID : \"groupForm\",\r\n				debug : false,\r\n				onSuccess : function() {\r\n					\r\n				},\r\n				onError : function() {\r\n					alert(\"错误，请看提示\")\r\n				}\r\n			});\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n						 			<#list components[layoutId] as component>\r\n						 			<#if component[\'componentType\']!=\'1012\' && component[\'componentType\']!=\'1009\'>		\r\n						 				<#if valdatorsMap[component[\'pageId\']]?? || (component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number==0)>\r\n						 					$(\'#${(component[\'pageId\'])!\"\"}_${(component[\'name\'])!\"\"}\').formValidator({onFocus:\"${component[\'validateInputTip\']}\",onCorrect:\"${component[\'validateErrorTip\']}\",\r\n						 						\r\n						 					})\r\n						 					<#if (component[\'validateAllowNull\']?number ==0) >\r\n						 						.inputValidator({min:1,empty:{leftEmpty:true,rightEmpty:true,emptyError:\"\"},onError:\"请输入内容\"})\r\n       										</#if>\r\n						 					<#if valdatorsMap[component[\'pageId\']]??>\r\n							 					<#list valdatorsMap[component[\'pageId\']] as v>\r\n							 						<@validate v/>\r\n							 					</#list>;\r\n							 				</#if>\r\n						 				</#if>	\r\n						 			</#if>\r\n						 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n		\r\n		\r\n		<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<#if component[\'pageValidate\']??>\r\n					 					${component[\'pageValidate\']}\r\n					 				</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n		</#if>\r\n		});	\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>\r\n		\r\n		<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<#if component[\'pageActScript\']??>\r\n					 					${component[\'pageActScript\']}\r\n					 				</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n		</#if>\r\n		\r\n		<#--<#list components as c>\r\n			<#if true>\r\n				${c.script}\r\n			</#if>\r\n		</#list>\r\n		 -->\r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('55', '带搜索列表模板--带分页', null, null, '带搜索列表模板--带分页', 'resources\\template\\1420860507744', null, '<#include \"templates/parseFunction.ftl\">\r\n<#include \"templates/pagerParserFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse>\r\n	<#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if>\r\n	</#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n	<meta name=\"renderer\" content=\"webkit\">	\r\n 	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/dist/css/zui.min.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/main.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css\" type=\"text/css\" />\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/select2/select2.css\">\r\n    <link rel=\"stylesheet\" href=\"${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css\">\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n	<![endif]-->	       \r\n</head>\r\n<body  id=\"main\">\r\n    <div class=\"container-fluid\">   	\r\n		<div class=\"row\" id=\"buttons\">\r\n</#noparse>				\r\n		<#if pageActs??>\r\n			<#list pageActs?sort_by(\"order\") as act>					\r\n					<@convertButton act />\r\n			</#list>\r\n		</#if>				\r\n<#noparse>\r\n		</div>\r\n</#noparse>		\r\n		<#if components?? >\r\n			<#list components?sort_by(\"order\") as c>				\r\n				<#if c[\'componentType\']==\'1013\' && (c[\'searchLocation\']!\'1\')==\'1\'>\r\n					<@parseComponent c />\r\n				</#if>\r\n			</#list>\r\n		</#if>\r\n	\r\n<#noparse>			\r\n		<div class=\"row\" id=\"datatable\">\r\n			<form class=\"form-horizontal\" id=\"groupForm\" action=\"${basePath}document/view.do\" method=\"post\">	\r\n	        	<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n<input type=\"hidden\" name=\"currentPage\" value=\"${currentPage!\"0\"}\">\r\n				<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n				<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n				<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n				<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n				<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n				<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n				<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n				<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n				<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n                <input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n				<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n				<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n				<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n				<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n				<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n				<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n				<input type=\"hidden\" name=\"mainTextId\" value=\"${(mainTextId)!\"\"}\" id=\"mainTextId\"/>\r\n				<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n                <input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n                <input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n				<table class=\"table datatable table-bordered sortable\">\r\n					<thead>\r\n						<tr>\r\n							<th class=\"hidden\"></th>\r\n							<th data-width=\"70\">序号</th>\r\n</#noparse>\r\n					<#if components?? >\r\n						<#list components?sort_by(\"order\") as c>\r\n							<#if c[\'componentType\']==\'1036\'>\r\n								<@convertAddSearch c />\r\n							</#if>\r\n						</#list>\r\n					</#if>\r\n					<#if components?? >\r\n						 <#list components?sort_by(\"order\") as c>\r\n							<#if c[\'componentType\']!=\'1013\'&&c[\'componentType\']!=\'1036\' >\r\n								<@parseColumn c />\r\n							</#if>\r\n						</#list>\r\n					</#if>\r\n<#noparse>\r\n						</tr>\r\n					</thead>\r\n					<tbody id=\"dataList\">\r\n		            	<#if </#noparse>${dataAlias[0]}<#noparse>_list?? && </#noparse>${dataAlias[0]}<#noparse>_list?size gte 1>	\r\n		            	 	<#list\r\n</#noparse>\r\n		            	 		${dataAlias[0]}_list as ${dataAlias[0]}>\r\n<#noparse> \r\n							<tr>\r\n								<td class=\"hidden formData\"><input id=\"boxs\" type=\"hidden\"\r\n								value=\"${</#noparse>${dataAlias[0]}<#noparse>.ID!\'\'}\"></td> 	\r\n</#noparse>				\r\n						<td><#noparse>${(</#noparse>${dataAlias[0]}_list_paginator.getPage() * ${dataAlias[0]}_list_paginator.getLimit() - ${dataAlias[0]}_list_paginator.getLimit() + ${dataAlias[0]}_index + 1<#noparse>)?string(\'0000\')}</#noparse></td>\r\n						<#if components?? >\r\n							 <#list components?sort_by(\"order\") as c>\r\n								<#if c[\'componentType\']!=\'1013\'&&c[\'componentType\']!=\'1036\' >\r\n									<@parseColumnData c />\r\n								</#if>\r\n							</#list>\r\n						</#if>\r\n<#noparse>				\r\n							</tr>\r\n							</#list>\r\n						</#if>\r\n					</tbody>\r\n				</table>\r\n			</form>\r\n		</div>\r\n</#noparse>	\r\n		<#if components?? >\r\n						 <#list components?sort_by(\"order\") as c>\r\n						 	<#if c[\'componentType\']==\'1013\' && (c[\'searchLocation\']??&&c[\'searchLocation\']==\'2\')>\r\n								<@parseComponent c />\r\n							</#if>\r\n						</#list>\r\n		</#if>\r\n		<div class=\"row navbar-fixed-bottom text-center\" id=\"pagers\">	\r\n			<@pageNavigation  dataAlias[0] />\r\n<#noparse>\r\n		</div>\r\n	</div>\r\n</#noparse>\r\n<#noparse>	\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}base/resources/zui/dist/js/zui.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}base/resources/artDialog/dist/dialog-plus-min.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/tips/jquery.poshytip.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js\" charset=\"UTF-8\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js\" charset=\"UTF-8\"></script>\r\n    <script type=\"text/javascript\" src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/pageTurn.js\"></script>\r\n	<script type=\"text/javascript\" src=\"${basePath}resources/scripts/platform.document.orderBy.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/select2.full.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/select2/zh-CN.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js\"></script>\r\n	<script type=\"text/javascript\" src=\'${basePath}venson/js/common.js\'></script>\r\n	<script type=\"text/javascript\" src=\'${basePath}venson/js/addSearch.js\'></script>\r\n	<script type=\"text/javascript\">\r\n		var basePath = \'${basePath}\';\r\n		$(document).ready(function(){\r\n			var nodeID=document.getElementById(\"FK_Node\").value;\r\n			if(nodeID==\'3002\'){\r\n			var workID=document.getElementById(\"WorkID\").value;\r\n			var FID=document.getElementById(\"FID\").value;\r\n			var Emp=document.getElementById(\"88e6f232-6dff-45e5-94e8-31e0fb8ad107\").value;\r\n			parent.frames[\"main\"].location.href=basePath+\"WF/MyFlow.jsp?PFlowNo=030&PNodeID=3002&PEmp=\"+Emp+\"&PFID=\"+FID+\"&PWorkID=\"+workID+\"&FK_Flow=031&FK_Node=3101&T=\";\r\n			}\r\n			if(nodeID==\'3003\'){\r\n			var workID=document.getElementById(\"WorkID\").value;\r\n			var FID=document.getElementById(\"FID\").value;\r\n			var Emp=document.getElementById(\"88e6f232-6dff-45e5-94e8-31e0fb8ad107\").value;\r\n			parent.frames[\"main\"].location.href=basePath+\"WF/MyFlow.jsp?PFlowNo=030&PNodeID=3003&PEmp=\"+Emp+\"&PFID=\"+FID+\"&PWorkID=\"+workID+\"&FK_Flow=032&FK_Node=3201&T=\";\r\n			}\r\n\r\n			var count=0;\r\n		  	$(\'table.datatable\').datatable({\r\n			  	checkable: true,\r\n			  	checksChanged:function(event){\r\n				  	this.$table.find(\"tbody tr\").find(\"input#boxs\").removeAttr(\"name\");\r\n				  	var checkArray = event.checks.checks;\r\n				  	count = checkArray.length;\r\n				  	for(var i=0;i<count;i++){\r\n					  	this.$table.find(\"tbody tr\").eq(checkArray[i]).find(\"input#boxs\").attr(\"name\",\"_selects\");\r\n				  	}\r\n			  	}\r\n		  	});\r\n			$(\".input-group .input-group-addon\").each(function(item){\r\n				 $(this).removeClass(\"fix-border\").removeClass(\"fix-padding\");\r\n			});\r\n			\r\n			 $(\'.form-date\').datetimepicker({\r\n						language : \'zh-CN\',\r\n						weekStart : 1,\r\n						todayBtn : 1,\r\n						autoclose : 1,\r\n						todayHighlight : 1,\r\n						startView : 2,\r\n						minView : 2,\r\n						forceParse : 0,\r\n						format : \'yyyy-mm-dd\'\r\n					});\r\n						$(\'.form-datetime\').datetimepicker({\r\n						language : \'zh-CN\',\r\n						weekStart : 1,\r\n						todayBtn : 1,\r\n						autoclose : 1,\r\n						todayHighlight : 1,\r\n						startView : 2,\r\n						forceParse : 0,\r\n						showMeridian : 1,\r\n						format : \'yyyy-mm-dd hh:ii\'\r\n					});\r\n					$(\'.form-time\').datetimepicker({\r\n						language : \'zh-CN\',\r\n						weekStart : 1,\r\n						todayBtn : 1,\r\n						autoclose : 1,\r\n						todayHighlight : 1,\r\n						startView : 1,\r\n						minView : 0,\r\n						maxView : 1,\r\n						forceParse : 0,\r\n						format : \'hh:ii\'\r\n					});\r\n					$(\'.form-date-long-medium\').datetimepicker({\r\n						language : \'zh-CN\',\r\n						weekStart : 1,\r\n						todayBtn : 1,\r\n						autoclose : 1,\r\n						todayHighlight : 1,\r\n						startView : 1,\r\n						minView : 0,\r\n						maxView : 1,\r\n						forceParse : 0,\r\n						format : \'yyyy-mm-dd hh:ii\'\r\n					});\r\n					$(\'.form-date-long-long\').datetimepicker({\r\n						language : \'zh-CN\',\r\n						weekStart : 1,\r\n						todayBtn : 1,\r\n						autoclose : 1,\r\n						todayHighlight : 1,\r\n						startView : 1,\r\n						minView : 0,\r\n						maxView : 1,\r\n						forceParse : 0,\r\n						format : \'yyyy-mm-dd hh:ii:ss\'\r\n					});\r\n                                        \r\n				initOrderBy();\r\n				\r\n				$.formValidator.initConfig({			\r\n					debug : false,\r\n					onSuccess : function() {					\r\n				},\r\n				onError : function() {\r\n					alert(\"错误，请看提示\")\r\n				}\r\n			});\r\n</#noparse>\r\n		<#if components?? >\r\n				<#list components?sort_by(\"order\") as c>\r\n					<#if c[\'componentType\']==\'1013\' >\r\n						<@convertContainPageScript c />\r\n					</#if>\r\n					<#if c[\'componentType\']==\'1036\'>\r\n						<@convertAddSearchScript c />\r\n					</#if>\r\n				</#list>\r\n			</#if>\r\n		\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>  \r\n        \r\n        <#if page??>\r\n			<#if page.getAfterLoadScript()??>\r\n				${page.getAfterLoadScript()}\r\n			</#if>\r\n		</#if>\r\n<#noparse>\r\n		});\r\n</#noparse>		  \r\n		\r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('94', '医院OA系统主页面模板(TABLE)', null, null, 'TABLE模式的表单生成', 'resources\\template\\1436931036343Noname1.ftl', null, '<#include \"templates/parseFunction.ftl\">\r\n<#noparse>\r\n<#assign path = request.getContextPath()>\r\n<#assign basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"/\">\r\n</#noparse>\r\n<#list dataAlias as dataAlia>\r\n	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>\r\n             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!\'\'>\r\n        </#if></#noparse>\r\n</#list>\r\n<#noparse>\r\n<!DOCTYPE html>\r\n<html>\r\n<head>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=9; IE=8; IE=7; IE=EDGE\">\r\n	<meta name=\"renderer\" content=\"webkit\">\r\n    <meta http-equiv=\"pragma\" content=\"no-cache\">\r\n 	<meta http-equiv=\"cache-control\" content=\"no-cache\"> \r\n    <meta http-equiv=\"expires\" content=\"0\">\r\n 	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/dist/css/zui_define.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/main.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css\" type=\"text/css\" />\r\n    <link rel=\"stylesheet\" href=\"${basePath}resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/content/uploader.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/styles/content/layout.css\">\r\n	<link rel=\"stylesheet\" href=\"${basePath}formdesigner/page/component/tab/tab.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/select2/select2.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}resources/plugins/select2/select2-bootstrap.css\"/>\r\n	<link rel=\"stylesheet\" href=\"${basePath}template/easyui/css/easyui.css\"/>\r\n	<style>\r\n		label.control-label{font-weight:bold;}\r\n		.form-condensed .form-group{margin-bottom:0px;}\r\n		.check-all{text-align:center;}\r\n		.hides{display: block;width: 0;height: 0;overflow: hidden;}\r\n	</style>\r\n	<!--[if lt IE 9]>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/html5shiv.js\"></script>\r\n	  <script src=\"${basePath}resources/plugins/zui/assets/respond.js\"></script>\r\n        \r\n	<![endif]-->	\r\n</head>\r\n<body id=\"main\">\r\n    <div class=\"container\"> \r\n		<div class=\"row\" id=\"buttons\">\r\n</#noparse>\r\n			<#if pageActs??>\r\n				<#list pageActs?sort_by(\"order\") as act>\r\n					<@convertButton act />\r\n				</#list>\r\n			</#if>\r\n		</div>\r\n		<div class=\"row\" id=\"newTitle\"></div>\r\n<#noparse>\r\n		<div class=\"row\">\r\n			<form class=\"form-horizontal form-condensed\" id=\"groupForm\" action=\"${basePath}workflow/wf/excute.do\" role=\"customForm\" method=\"post\" style=\"border:#333 1px solid\">\r\n				<input type=\"hidden\" name=\"docId\" value=\"${ (vo.id)!\"\"}\" id=\"docId\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageId\" value=\"${ (vo.dynamicPageId)!\"\"}\" id=\"dynamicPageId\"/>\r\n				<input type=\"hidden\" name=\"workflowId\" value=\"${ (vo.workflowId)!\"\"}\" id=\"workflowId\"/>\r\n				<input type=\"hidden\" name=\"instanceId\" value=\"${ (vo.instanceId)!\"\"}\" id=\"instanceId\"/>\r\n				<input type=\"hidden\" name=\"taskId\" value=\"${ (vo.taskId)!\"\"}\" id=\"taskId\"/>\r\n				<input type=\"hidden\" name=\"nodeId\" value=\"${ (vo.nodeId)!\"\"}\" id=\"nodeId\"/>\r\n				<input type=\"hidden\" name=\"actId\" value=\"${ (vo.actId)!\"\"}\" id=\"actId\"/>\r\n				<input type=\"hidden\" name=\"WorkID\" value=\"${ (vo.workItemId)!\"\"}\" id=\"WorkID\"/>\r\n				<input type=\"hidden\" name=\"FK_Node\" value=\"${ (vo.entryId)!\"\"}\" id=\"FK_Node\"/>\r\n				<input type=\"hidden\" name=\"FK_Flow\" value=\"${ (vo.flowTempleteId)!\"\"}\" id=\"FK_Flow\"/>\r\n                <input type=\"hidden\" name=\"FID\" value=\"${ (vo.fid)!\"\"}\" id=\"FID\"/>\r\n				<input type=\"hidden\" name=\"update\" value=\"${ (vo.update)?string(\"true\",\"false\")}\" id=\"update\"/>\r\n				<input type=\"hidden\" name=\"toNode\" value=\"\" id=\"toNode\">\r\n				<input type=\"hidden\" name=\"message\" value=\"${(message)!\"\"}\" id=\"message\">\r\n				<input type=\"hidden\" name=\"IsRead\" value=\"${(IsRead)!\"\"}\" id=\"IsRead\">\r\n				<input type=\"hidden\" name=\"suggestion\" value=\"${(suggestion)!\"\"}\" id=\"suggestion\"/>\r\n				<input type=\"hidden\" name=\"commonType\" value=\"${(commonType)!\"\"}\" id=\"commonType\"/>\r\n				<input type=\"hidden\" name=\"suggestionType\" value=\"${(suggestionType)!\"\"}\" id=\"suggestionType\"/>\r\n				<input type=\"hidden\" name=\"mainTextId\" value=\"${(mainTextId)!\"\"}\" id=\"mainTextId\"/>\r\n				<input type=\"hidden\" name=\"slectsUserIds\" value=\"${ (vo.slectsUserIds)!\"\"}\" id=\"slectsUserIds\"/>\r\n				<input type=\"hidden\" name=\"slectsUserNames\" value=\"${ (vo.slectsUserNames)!\"\"}\" id=\"slectsUserNames\"/>                       \r\n				<input type=\"hidden\" name=\"masterDataSource\" value=\"\" id=\"masterDataSource\"/>\r\n				<input type=\"hidden\" name=\"dynamicPageName\" value=\"${ (vo.dynamicPageName)!\"\"}\" id=\"dynamicPageName\"/>\r\n</#noparse>\r\n				<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">		\r\n			 		<#if layouts??>\r\n					 	<#if components??>\r\n					 		<#list layouts?sort_by(\"order\") as layout>\r\n					 			<@parseLayout2 layout components/>\r\n					 		</#list> \r\n					 	</#if>\r\n			 		</#if>\r\n<#noparse>\r\n				</table>\r\n			</form>\r\n		</div>\r\n	</div>\r\n	<script src=\"${basePath}resources/JqEdition/jquery-1.10.2.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/zui/dist/js/zui.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/tips/jquery.poshytip.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/jquery.serializejson.min.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/ajaxfileupload.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/uploadPreview.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/map.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/common.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/platform.document.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/webuploader-0.1.5/dist/webuploader.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/uploader.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/select2/select2.js\"></script>\r\n	<script src=\"${basePath}venson/js/common.js\"></script>\r\n	<script>\r\n		var basePath = \'${basePath!\'\'}\';\r\n		function initTable(){\r\n	        if($(\"div.datatable\").size()>0){\r\n	            $(\"div.datatable\").remove();\r\n			}\r\n			$(\"table.table\").removeAttr(\"style\");\r\n			var count=0;//默认选择行数为0\r\n				  \r\n			$(\"table.datatable\").datatable({\r\n				checkable: true,\r\n				datareload:true,\r\n				checksChanged:function(event){\r\n					this.$table.find(\"tbody tr\").find(\"input#boxs\").removeAttr(\"name\");\r\n					var checkArray = event.checks.checks;\r\n					count = checkArray.length;\r\n					for(var i=0;i<count;i++){//给隐藏数据机上name属性\r\n						this.$table.find(\"tbody tr\").eq(checkArray[i]).find(\"input#boxs\").attr(\"name\",\"_selects\");\r\n					}\r\n				}\r\n			});\r\n			$(\"#datatable\").find(\"div.datatable-rows\").find(\"table tr td\").each(function(){\r\n				$(this).css(\"text-align\",\"left\");\r\n			});    	  \r\n			$(\"#datatable\").find(\"div.datatable-rows\").find(\"table tr td:first-child\").each(function(){\r\n				$(this).css(\"text-align\",\"center\");\r\n			});                   \r\n		}\r\n    </script>\r\n	<script src=\"${basePath}resources/plugins/select2/select2_locale_zh-CN.js\"></script>\r\n	<script src=\"${basePath}resources/plugins/zui/assets/kindeditor/kindeditor-min.js\"></script>\r\n    <script src=\"${basePath}resources/plugins/zui/assets/kindeditor/lang/zh_CN.js\"></script>\r\n    <script src=\"${basePath}resources/plugins/zTree_v3/zTree.js\" ></script>\r\n    <script src=\"${basePath}resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js\" ></script>\r\n    <script src=\"${basePath}resources/scripts/dynamicSelect.js\"></script>\r\n    <script src=\"${basePath}resources/scripts/defineNote.js\"></script>  \r\n<script src=\"${basePath}resources/scripts/platform.dataGrid.js\"></script>\r\n	<script src=\"${basePath}resources/scripts/plateform.userSelect.js\"></script>\r\n    <script src=\"${basePath}formdesigner/page/component/tab/swiper.min.js\"></script>\r\n    <script src=\"${basePath}formdesigner/page/component/tab/tab.js\"></script>\r\n 	<script src=\"${basePath}template/easyui/js/jquery.easyui.min.js\"></script>\r\n	<script>\r\n</#noparse>		\r\n		$(\"document\").ready(function(){\r\n            $(\".chosenName\").css(\"height\",\"auto\");\r\n			$(\".disabled\").find(\"input,select,textarea,button,a\").attr(\"disabled\",\"disabled\");		\r\n			initTable();\r\n            $(\"label\").css(\"text-align\",\"center\");\r\n            $(\"td\").css(\"text-align\",\"center\");\r\n            $(\"div.webuploader-container\").css(\"float\",\"left\");\r\n            $(\"div.mb10\").css(\"margin-top\",\"10px\");\r\n            $(\"div.mb10\").addClass(\"clearfix\");\r\n            $(\"div.uploader-list\").css(\"overflow\",\"hidden\");\r\n            $(\"div.picker\").css(\"float\",\"left\");\r\n            $(\".uploader-list tfoot td\").css(\"border-left\",\"none\");\r\n            $(\".radio-inline\").css(\"line-height\",\"20px\");\r\n            $(\".radio-inline\").css(\"margin-left\",\"10px\");\r\n            $(\"div.datatable-head-span\").css(\"width\",\"100%\");\r\n            $(\"#newTitle\").html(\'<label class=\"control-label \" style=\"width: 100%; text-align: center; font-weight: 800; font-size: 24px;\">\'+$(\".container\").find(\"tr\").eq(0).find(\"label\").eq(0).text()+\'</label>\');\r\n            $(\".container\").find(\"table\").find(\"tr\").eq(0).remove();\r\n			$(\"table.table thead tr th\").each(function(){\r\n                $(this).css(\"text-align\",\"center\");\r\n            });\r\n			$(\"#datatable\").find(\"div.datatable-head\").find(\"table\").css(\"table-layout\",\"fixed\");\r\n			$(\"#datatable\").find(\"div.datatable-rows\").find(\"table tr td\").each(function(){\r\n                $(this).css(\"text-align\",\"left\");\r\n			});    	  \r\n			$(\"#datatable\").find(\"div.datatable-rows\").find(\"table tr td:first-child\").each(function(){\r\n                $(this).css(\"text-align\",\"center\");\r\n			});  \r\n			\r\n			<#if components??>\r\n	 		 	<#if components?keys??>\r\n		 		  	<#list components?keys as layoutId>\r\n			 			<#if components[layoutId]??>\r\n			 			<#list components[layoutId] as component>\r\n			 				<@convertComponentScript component />\r\n			 			</#list>\r\n			 			</#if>\r\n		 		  	</#list>\r\n	 			</#if> \r\n			</#if>\r\n		\r\n		\r\n	        <#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n			</#if>\r\n			\r\n			$(\"div.datatable-head-span\").css(\"width\",\"100%\");\r\n			\r\n			function checkRequiredData(){\r\n				<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n						 			<#list components[layoutId] as component>\r\n						 			<#if component[\'componentType\']!=\'1012\' && component[\'componentType\']!=\'1009\'>		\r\n						 				<#if valdatorsMap[component[\'pageId\']]?? || (component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number==0)>\r\n											<#if component[\'componentType\']==\'1011\'>\r\n												var files = $(\'#${(component[\'pageId\'])!\"\"} .orgfile\').val();\r\n												if(!files){\r\n													var tip = \'${(component[\'validateErrorTip\'])!\'\'}\';\r\n													if(!tip){\r\n														tip = \'请确认附件已上传\';\r\n													}\r\n													alert(tip);\r\n													return false;\r\n												}\r\n											<#elseif component[\'componentType\']==\'1003\' || component[\'componentType\']==\'1004\'>\r\n												var checkedCount = $(\'input[name=\"${(component[\'name\'])!\"\"}\"]:checked\').length;\r\n												if(checkedCount==1){//默认有一个空的选择\r\n													var tip = \'${(component[\'validateErrorTip\'])!\'\'}\';\r\n													if(!tip){\r\n														tip = \'请选择数据\';\r\n													}\r\n													alert(tip);\r\n													return false;\r\n												}\r\n											<#else>\r\n												var data = $(\'#${(component[\'pageId\'])!\"\"}\').val();\r\n												if(!data){\r\n													var tip = \'${(component[\'validateErrorTip\'])!\'\'}\';\r\n													if(!tip){\r\n														tip = \'请输入内容\';\r\n													}\r\n													alert(tip);\r\n													return false;\r\n												}\r\n											</#if>\r\n						 				</#if>	\r\n						 			</#if>\r\n						 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n				</#if>\r\n				return true;\r\n			}\r\n			\r\n			<#list pageActs?sort_by(\"order\") as act>\r\n				<#if true>\r\n					<@convertScript act />\r\n				</#if>\r\n			</#list>\r\n		});			\r\n	</script>\r\n	<script>	\r\n		$(\"table\").each(function(index, element) {\r\n			var currentTable = $(this);\r\n			if(currentTable.parent().parent(\"td\").length>0){\r\n				currentTable.find(\"tr\").first().find(\"td\").css(\"border-top\",\"none\");\r\n				currentTable.find(\"tr\").last().find(\"td\").css(\"border-bottom\",\"none\");\r\n				currentTable.find(\"tr\").each(function(index, element) {\r\n					$(this).children().first().css(\"border-left\",\"none\");\r\n					$(this).children().last().css(\"border-right\",\"none\");\r\n				});\r\n			}\r\n		});\r\n\r\n		function formatUploadTable(){\r\n		    var $table = $(\"div[id=\'datatable-uploadListTable\']\").find(\"table.table\");\r\n			$table.css(\"table-layout\",\"fixed\");\r\n			$table.find(\"tr\").each(function(){\r\n				var $tr = $(this);\r\n				$tr.find(\"td[data-index=\'0\']\").css(\"width\",\"10%\");\r\n				$tr.find(\"td[data-index=\'1\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"td[data-index=\'2\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"td[data-index=\'3\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"th[data-index=\'check\']\").css(\"width\",\"10%\");\r\n				$tr.find(\"th[data-index=\'1\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"th[data-index=\'2\']\").css(\"width\",\"30%\");\r\n				$tr.find(\"th[data-index=\'3\']\").css(\"width\",\"30%\");		\r\n			})		                        	  \r\n		}\r\n	</script>\r\n</body>\r\n</html>', '1');
INSERT INTO `p_fm_template` VALUES ('95', '医院OA片段页面模板(TABLE)', null, null, '用表格方式呈现', 'resources\\template\\1436933751199TABLE.ftl', null, '<#include \"templates/parseFunction.ftl\">\r\n\r\n		<--MYLayoutAndCom-->\r\n			<#if pageActs??>\r\n				<#list pageActs?sort_by(\"order\") as act>\r\n					<#noparse><@shiro.hasPermission name=\"11111:</#noparse>${(pageActPermission[act.getPageId()])!\"\"}<#noparse>\"></#noparse>\r\n						<@convertButton act />\r\n					<#noparse></@shiro.hasPermission></#noparse>\r\n				</#list>\r\n			</#if>\r\n		\r\n		         <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >	\r\n			 <#if layouts??>\r\n			 	<#if components??>\r\n			 		<#list layouts?sort_by(\"order\") as layout>\r\n			 			<@parseLayout2 layout components/>\r\n			 		</#list> \r\n			 	</#if>\r\n			 </#if>\r\n			 </table>\r\n		<--MYLayoutAndCom/-->\r\n		\r\n			<--MYpageJScript-->\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<@convertComponentScript component />\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			\r\n			 <#if page??>\r\n				<#if page.getAfterLoadScript()??>\r\n					${page.getAfterLoadScript()}\r\n				</#if>\r\n			</#if>\r\n			\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n									<#if (component??) && (component[\'componentType\']==\'1011\')>\r\n					 				<@convertFileScript component />\r\n									</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n									<#if (component??) && (component[\'componentType\']==\'1016\')>\r\n					 				<@convertImageScript component />\r\n									</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			<--MYpageJScript/-->\r\n		\r\n			<--MYvalidate-->\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n						 			<#list components[layoutId] as component>\r\n						 				<#if component[\'componentType\']!=\'1012\' && component[\'componentType\']!=\'1009\'>\r\n							 				<#if valdatorsMap[component[\'pageId\']]?? || (component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number==0)>\r\n							 					<#if component[\'componentType\']==\'1011\'>\r\n													$(\'#${(component[\'pageId\'])!\"\"} .orgfile\').formValidator({onFocus:\"${(component[\'validateInputTip\'])!\'\'}\",onCorrect:\"${(component[\'validateErrorTip\'])!\'\'}\",<#if component[\'componentType\']==\'1002\'> triggerEvent : \"change\" </#if>\r\n														\r\n													})\r\n												<#else>\r\n													$(\'#${(component[\'pageId\'])!\"\"}\').formValidator({onFocus:\"${(component[\'validateInputTip\'])!\'\'}\",onCorrect:\"${(component[\'validateErrorTip\'])!\'\'}\",<#if component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number==1> empty:true, </#if><#if component[\'componentType\']==\'1002\'> triggerEvent : \"change\" </#if>\r\n														\r\n													})\r\n												</#if>\r\n							 					<#if (component[\'validateAllowNull\']?? && component[\'validateAllowNull\']?number ==0) >\r\n													<#if component[\'componentType\']==\'1011\'>\r\n														.inputValidator({min:1,empty:{leftEmpty:true,rightEmpty:true,emptyError:\"\"},onError:\"请确认附件已上传\"})\r\n													<#else>\r\n														.inputValidator({min:1,empty:{leftEmpty:true,rightEmpty:true,emptyError:\"\"},onError:\"<#if component[\'description\']??> ${component[\'description\']} </#if>请输入内容\"})\r\n													</#if>\r\n	       										</#if>\r\n							 					<#if valdatorsMap[component[\'pageId\']]??>\r\n								 					<#list valdatorsMap[component[\'pageId\']] as v>\r\n								 						<@validate v/>\r\n								 					</#list>;\r\n								 				</#if>\r\n							 				</#if>\r\n						 				</#if>	\r\n						 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			\r\n			<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<#if component[\'pageValidate\']??>\r\n					 					${component[\'pageValidate\']}\r\n					 				</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n			</#if>\r\n			<--MYvalidate/-->\r\n		\r\n		<--MYpageActScript-->\r\n		<#list pageActs?sort_by(\"order\") as act>\r\n			<#if true>\r\n				<@convertScript act />\r\n			</#if>\r\n		</#list>\r\n		\r\n		<#if components??>\r\n			 		 <#if components?keys??>\r\n				 		  <#list components?keys as layoutId>\r\n					 			<#if components[layoutId]??>\r\n					 			<#list components[layoutId] as component>\r\n					 				<#if component[\'pageActScript\']??>\r\n					 					${component[\'pageActScript\']}\r\n					 				</#if>\r\n					 			</#list>\r\n					 			</#if>\r\n				 		  </#list>\r\n			 		</#if> \r\n		</#if>\r\n		<--MYpageActScript/-->\r\n', '1');

-- ----------------------------
-- Table structure for p_fm_wechat_payment_param
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_wechat_payment_param`;
CREATE TABLE `p_fm_wechat_payment_param` (
  `ID` varchar(36) NOT NULL,
  `appid` varchar(32) DEFAULT NULL COMMENT '微信号appid',
  `appsecret` varchar(255) DEFAULT NULL COMMENT '微信公众号的APPSECRET',
  `token` varchar(50) DEFAULT NULL COMMENT '公众号令牌',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号ID',
  `key` varchar(255) DEFAULT NULL COMMENT '商户号秘钥',
  `parent_appid` varchar(32) DEFAULT NULL COMMENT '微信支付服务商的appid',
  `parent_mch_id` varchar(32) DEFAULT NULL,
  `parent_key` varchar(255) DEFAULT NULL COMMENT '微信支付服务商商户号秘钥',
  `sub_mch_id` varchar(32) DEFAULT NULL COMMENT '微信公众号在微信支付服务商那里申请的商户号ID',
  `paymentType` varchar(1) DEFAULT NULL COMMENT '0：通过公众号申请的微信支付支付；1：通过微信支付服务商来实现微信支付',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_fm_wechat_payment_param
-- ----------------------------
INSERT INTO `p_fm_wechat_payment_param` VALUES ('924ac7cd-3a6f-4d12-83da-bf0f8246771e', 'wxc053aefd1dfd9fc1', '40b6495bb86471644d484ae511eff57e', 'yqtao', '1332850301', 'Abcdefg123456789hIjklmn987654321', 'wxe82d24efd5c552a0', '1311397901', 'SZHCzx13113979012016030114086688', '1358723402', '1', '1');

-- ----------------------------
-- Table structure for p_fm_work
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_work`;
CREATE TABLE `p_fm_work` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `JSON` text COLLATE utf8_bin COMMENT '流程人员',
  `CREATOR` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `WORK_ID` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '流程实例编号',
  `FLOW_ID` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '流程编号',
  `NODE_ID` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '节点编号',
  `CURRENT_NODE` int(50) DEFAULT NULL COMMENT '当前环节',
  `PAGE_ID` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '页面ID',
  `RECORD_ID` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '记录ID',
  `REMARK` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `WORK_ID` (`WORK_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=258 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of p_fm_work
-- ----------------------------
INSERT INTO `p_fm_work` VALUES ('242', 0x5B22303936313536353636323337363630313535225D, '096156566237660155', '2017-09-08 17:43:32', '7152', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('243', 0x5B22303936313536353636323337363630313535225D, '096156566237660155', '2017-09-08 17:48:50', '7153', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('244', 0x5B22303936313536353636323337363630313535225D, '096156566237660155', '2017-09-08 17:50:39', '7154', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('245', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '096156566237660155', '2017-09-11 14:05:56', '7155', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('246', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '085322151226274156', '2017-09-11 15:29:28', '7156', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('247', 0x5B226D616E6167657237333932222C22303936313536353636323337363630313535222C22303431343135343032333231353931303936222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '085322151226274156', '2017-09-11 17:24:22', '7157', '005', '501', '2', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('248', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '0366654448841826', '2017-09-12 18:19:57', '7159', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('249', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '0366654448841826', '2017-09-12 18:19:58', '7160', '005', '501', '1', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('250', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '0366654448841826', '2017-09-12 18:23:37', '7161', '005', '501', '1', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('251', 0x5B22303936313536353636323337363630313535225D, '085322151226274156', '2017-09-13 16:28:39', '7162', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('252', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, 'manager7392', '2017-09-13 16:57:26', '7163', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('253', 0x5B226D616E6167657237333932222C223039363135363536363233373636303135352C303835333232313531323236323734313536225D, '085322151226274156', '2017-09-13 17:29:29', '7164', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('254', 0x5B22303936313536353636323337363630313535225D, '085322151226274156', '2017-09-14 16:28:16', '7165', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('255', 0x5B22303936313536353636323337363630313535225D, '085322151226274156', '2017-09-14 16:30:26', '7166', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('256', 0x5B22303936313536353636323337363630313535225D, '085322151226274156', '2017-09-14 16:32:48', '7167', '005', '501', '0', '2081', null, null);
INSERT INTO `p_fm_work` VALUES ('257', 0x5B22303936313536353636323337363630313535225D, '085322151226274156', '2017-09-14 16:43:33', '7168', '005', '501', '0', '2068', null, null);

-- ----------------------------
-- Table structure for p_fm_work_logs
-- ----------------------------
DROP TABLE IF EXISTS `p_fm_work_logs`;
CREATE TABLE `p_fm_work_logs` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTENT` text COLLATE utf8_bin COMMENT '内容',
  `WORK_ID` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '流程实例编号',
  `PAGE_ID` varchar(36) COLLATE utf8_bin DEFAULT NULL COMMENT '页面ID',
  `CREATOR` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `SEND_TIME` datetime DEFAULT NULL COMMENT '发送时间',
  `CURRENT_NODE` int(11) DEFAULT NULL COMMENT '排序',
  `REMARK` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `STATE` int(11) DEFAULT NULL COMMENT '0:同意，1:拒绝',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=884 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of p_fm_work_logs
-- ----------------------------

-- ----------------------------
-- Table structure for p_mm_wdcsmx
-- ----------------------------
DROP TABLE IF EXISTS `p_mm_wdcsmx`;
CREATE TABLE `p_mm_wdcsmx` (
  `field1` int(11) DEFAULT '1',
  `field2` varchar(200) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_mm_wdcsmx
-- ----------------------------

-- ----------------------------
-- Table structure for p_sys_sequence
-- ----------------------------
DROP TABLE IF EXISTS `p_sys_sequence`;
CREATE TABLE `p_sys_sequence` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_sys_sequence
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_access_info
-- ----------------------------
DROP TABLE IF EXISTS `p_un_access_info`;
CREATE TABLE `p_un_access_info` (
  `ACCESS_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `RESOUCE_ID` bigint(20) DEFAULT NULL COMMENT '资源ID',
  `ACCESS_NAME` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `OPERATE_TYPE` varchar(4) DEFAULT NULL COMMENT '操作类型',
  `BAK_LONG` bigint(20) DEFAULT NULL COMMENT '长整型备用字段',
  `BAK_LONG2` bigint(20) DEFAULT NULL COMMENT '长整型备用字段2',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ACCESS_ID`),
  KEY `FK_UN_RESO_ACCE` (`RESOUCE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限信息';

-- ----------------------------
-- Records of p_un_access_info
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_common_group
-- ----------------------------
DROP TABLE IF EXISTS `p_un_common_group`;
CREATE TABLE `p_un_common_group` (
  `id` varchar(150) DEFAULT NULL,
  `group_id` varchar(150) DEFAULT NULL,
  `user_id` varchar(150) DEFAULT NULL,
  `click_number` int(100) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `order_` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_un_common_group
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_common_user
-- ----------------------------
DROP TABLE IF EXISTS `p_un_common_user`;
CREATE TABLE `p_un_common_user` (
  `id` varchar(150) DEFAULT NULL,
  `user_id` bigint(150) DEFAULT NULL,
  `user` bigint(150) DEFAULT NULL,
  `click_number` int(100) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `order_` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_un_common_user
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_general_menu
-- ----------------------------
DROP TABLE IF EXISTS `p_un_general_menu`;
CREATE TABLE `p_un_general_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  `createdate` varchar(255) DEFAULT NULL,
  `group_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_un_general_menu
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_group
-- ----------------------------
DROP TABLE IF EXISTS `p_un_group`;
CREATE TABLE `p_un_group` (
  `GROUP_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `PARENT_GROUP_ID` bigint(20) DEFAULT NULL COMMENT '组ID',
  `GROUP_TYPE` varchar(5) DEFAULT NULL COMMENT '组类型',
  `GROUP_CH_NAME` varchar(255) DEFAULT NULL COMMENT '组中文名称',
  `GROUP_SHORT_NAME` varchar(30) DEFAULT NULL COMMENT '组名称缩写',
  `GROUP_EN_NAME` varchar(100) DEFAULT NULL COMMENT '组英文名称',
  `ORG_CODE` varchar(20) DEFAULT '45575544-2' COMMENT '组织机构代码',
  `GROUP_ADDRESS` varchar(255) DEFAULT NULL COMMENT '组地址',
  `ZIP_CODE` varchar(10) DEFAULT NULL COMMENT '邮编',
  `CONTACT_NUMBER` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `FAX` varchar(20) DEFAULT NULL COMMENT '传真',
  `GROUP_BUSINESS_SPHERE` varchar(4000) DEFAULT NULL COMMENT '业务范围',
  `CREATE_DATE` date DEFAULT NULL,
  `PID` varchar(256) DEFAULT NULL COMMENT '父组',
  `LAST_EDIT_TIME` datetime DEFAULT NULL,
  `NUMBER` int(11) DEFAULT '0',
  PRIMARY KEY (`GROUP_ID`),
  KEY `FK_UN_ORG_SELF` (`PARENT_GROUP_ID`) USING BTREE,
  KEY `IX_group_PID` (`PID`(255)) USING BTREE,
  KEY `IX_group_PARENT_GROUP_ID` (`PARENT_GROUP_ID`) USING BTREE,
  KEY `IX_GROUP_TYPE` (`GROUP_TYPE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=50042925 DEFAULT CHARSET=utf8 COMMENT='组：包括组织和岗位等';

-- ----------------------------
-- Records of p_un_group
-- ----------------------------
INSERT INTO `p_un_group` VALUES ('1', '0', '3', 'AWCP_ZH', 'AWCP_ZH', null, '45575544-2', null, null, null, null, null, null, null, '2017-09-01 14:52:54', '0');
INSERT INTO `p_un_group` VALUES ('48960125', '1', '3', '前端', '前端', null, '45575544-2', null, null, null, null, null, null, null, '2017-09-05 16:00:12', '48960125');
INSERT INTO `p_un_group` VALUES ('49290404', '1', '3', '开发', '开发', null, '45575544-2', null, null, null, null, null, null, null, '2017-09-05 16:00:04', '49290404');
INSERT INTO `p_un_group` VALUES ('49298917', '1', '3', '设计', '设计', null, '45575544-2', null, null, null, null, null, null, null, '2017-09-05 16:03:01', '49298917');
INSERT INTO `p_un_group` VALUES ('49357199', '1', '3', '测试', null, null, '45575544-2', null, null, null, null, null, null, '1', null, '49357199');
INSERT INTO `p_un_group` VALUES ('49403118', '49357199', '3', '设计', null, null, '45575544-2', null, null, null, null, null, null, '49357199', null, '49403118');
INSERT INTO `p_un_group` VALUES ('50042924', '1', '3', '其他', null, null, '45575544-2', null, null, null, null, null, null, '1', null, '50042924');

-- ----------------------------
-- Table structure for p_un_group_sys
-- ----------------------------
DROP TABLE IF EXISTS `p_un_group_sys`;
CREATE TABLE `p_un_group_sys` (
  `GROUP_SYS_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组系统关联ID',
  `GROUP_ID` bigint(20) DEFAULT NULL COMMENT '组ID',
  `SYS_ID` bigint(20) DEFAULT NULL COMMENT '系统ID',
  PRIMARY KEY (`GROUP_SYS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组与系统关系表';

-- ----------------------------
-- Records of p_un_group_sys
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_i18n
-- ----------------------------
DROP TABLE IF EXISTS `p_un_i18n`;
CREATE TABLE `p_un_i18n` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `pageId` int(11) DEFAULT NULL COMMENT '页面Id',
  `key` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '键值',
  `zh` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '中文',
  `en` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '英文',
  `remark` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of p_un_i18n
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_menu
-- ----------------------------
DROP TABLE IF EXISTS `p_un_menu`;
CREATE TABLE `p_un_menu` (
  `MENU_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `PARENT_MENU_ID` bigint(20) DEFAULT NULL COMMENT '资源ID',
  `MENU_NAME` varchar(20) DEFAULT NULL COMMENT '菜单名称',
  `MENU_ADDRESS` varchar(250) DEFAULT NULL COMMENT '菜单地址',
  `MENU_ICON` varchar(100) DEFAULT NULL COMMENT '图标',
  `MENU_SEQ` int(11) DEFAULT NULL COMMENT '顺序',
  `PID` varchar(256) DEFAULT NULL COMMENT '父资源',
  `MENU_TYPE` varchar(4) DEFAULT NULL COMMENT '菜单类型',
  `MENU_NOTE` varchar(256) DEFAULT NULL COMMENT '备注',
  `DYNAMICPAGE_ID` char(36) DEFAULT NULL,
  `SYSTEM_ID` bigint(20) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  `MENU_FLAG` int(2) DEFAULT '0' COMMENT '是否常用',
  `TYPE` int(2) DEFAULT '0' COMMENT '菜单类型：0是中间，1是头部，2是底部',
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10924 DEFAULT CHARSET=utf8 COMMENT='菜单';

-- ----------------------------
-- Records of p_un_menu
-- ----------------------------
INSERT INTO `p_un_menu` VALUES ('10611', '0', '系统管理', '#', null, '49', null, '1', null, null, '110', '1', '0', '0');
INSERT INTO `p_un_menu` VALUES ('10613', '10611', '用户管理', 'unit/punUserBaseInfoList.do', null, '1', '10611,', '1', null, null, '110', '1', '0', '0');
INSERT INTO `p_un_menu` VALUES ('10614', '10611', '部门管理', 'unit/selfGroupGet.do', null, '2', '10611,', '1', null, null, '110', '1', '0', '0');
INSERT INTO `p_un_menu` VALUES ('10615', '10611', '角色管理', 'unit/manageRole.do?boxs=110&currentPage=1', null, '3', '10611,', '1', null, null, '110', '1', '0', '0');
INSERT INTO `p_un_menu` VALUES ('10616', '10611', '组织用户', 'unit/departmentList.jsp', null, '4', '10611,', '1', null, null, '110', '1', '0', '0');
INSERT INTO `p_un_menu` VALUES ('10620', '0', '我的工作', '#', null, '1', null, '1', null, null, '110', '1', '0', '0');
INSERT INTO `p_un_menu` VALUES ('10624', '10620', '待办件', 'workflow/wf/listPersonalTasks.html', null, '2', '10620,', '1', null, null, '110', '1', '1', '0');
INSERT INTO `p_un_menu` VALUES ('10625', '10620', '在办件', 'workflow/wf/inDoingTasks.html', null, '2', '10620,', '1', null, null, '110', '1', '0', '0');
INSERT INTO `p_un_menu` VALUES ('10626', '10620', '办结件', 'workflow/wf/listHistoryTasks.html', null, '3', '10620,', '1', null, null, '110', '1', '0', '0');
INSERT INTO `p_un_menu` VALUES ('10891', '0', '平台管理', '#', null, '7', null, '1', null, null, '110', null, '0', '0');
INSERT INTO `p_un_menu` VALUES ('10902', '10891', '系统属性字典管理', null, null, '2', '10891,', '2', null, '18', '110', null, '0', '0');

-- ----------------------------
-- Table structure for p_un_menu_count
-- ----------------------------
DROP TABLE IF EXISTS `p_un_menu_count`;
CREATE TABLE `p_un_menu_count` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `menu_id` int(10) NOT NULL,
  `clickdate` varchar(225) DEFAULT NULL,
  `group_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_un_menu_count
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_notification
-- ----------------------------
DROP TABLE IF EXISTS `p_un_notification`;
CREATE TABLE `p_un_notification` (
  `ID` char(36) NOT NULL,
  `TITLE` varchar(500) DEFAULT NULL COMMENT '标题',
  `CONTENT` text COMMENT '内容',
  `MSG_URL` varchar(500) DEFAULT NULL COMMENT '链接',
  `ICON` varchar(255) DEFAULT NULL COMMENT '图标',
  `TYPE` varchar(50) DEFAULT NULL COMMENT '类型  数据字典data_value :MessageType',
  `BODY` text COMMENT '表单JSON数据',
  `CREATE_NAME` varchar(255) DEFAULT NULL COMMENT '创建人',
  `CREATOR` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `REMARK` varchar(255) DEFAULT NULL COMMENT '备注',
  `STATE` varchar(2) DEFAULT '0' COMMENT '0未发布，1已发布',
  PRIMARY KEY (`ID`),
  KEY `CREATOR` (`CREATOR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_un_notification
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_notify_user
-- ----------------------------
DROP TABLE IF EXISTS `p_un_notify_user`;
CREATE TABLE `p_un_notify_user` (
  `ID` int(20) NOT NULL AUTO_INCREMENT,
  `MSG_ID` char(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '消息ID',
  `USER` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户ID',
  `IS_READ` char(2) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '是否已读 0未读，1已读',
  `STATE` char(2) COLLATE utf8mb4_bin DEFAULT '1' COMMENT '状态 1显示，0隐藏表示用户已删',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MSG_ID` (`MSG_ID`,`USER`),
  KEY `USER` (`USER`)
) ENGINE=InnoDB AUTO_INCREMENT=1431 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of p_un_notify_user
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_origanize
-- ----------------------------
DROP TABLE IF EXISTS `p_un_origanize`;
CREATE TABLE `p_un_origanize` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `DESCRIBED` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建时间',
  `UPDATE_DATE` date DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of p_un_origanize
-- ----------------------------
INSERT INTO `p_un_origanize` VALUES ('4', '各委，机关各处室', null, '2016-09-19', '2016-09-19');
INSERT INTO `p_un_origanize` VALUES ('13', '机关副处以上干部', null, null, '2016-09-19');
INSERT INTO `p_un_origanize` VALUES ('14', '全体机关干部', null, null, '2016-09-19');
INSERT INTO `p_un_origanize` VALUES ('15', '常委会领导', null, null, '2016-09-22');
INSERT INTO `p_un_origanize` VALUES ('16', '111', null, '2017-03-27', null);
INSERT INTO `p_un_origanize` VALUES ('17', 'pageSize', null, '2017-03-27', null);

-- ----------------------------
-- Table structure for p_un_page_binding
-- ----------------------------
DROP TABLE IF EXISTS `p_un_page_binding`;
CREATE TABLE `p_un_page_binding` (
  `id` varchar(50) DEFAULT NULL,
  `PAGEID_PC` varchar(50) DEFAULT NULL,
  `PAGEID_PC_LIST` varchar(50) DEFAULT NULL,
  `PAGEID_APP` varchar(50) DEFAULT NULL,
  `PAGEID_APP_LIST` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_un_page_binding
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_position
-- ----------------------------
DROP TABLE IF EXISTS `p_un_position`;
CREATE TABLE `p_un_position` (
  `POSITION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL COMMENT '岗位名称',
  `SHORT_NAME` varchar(20) DEFAULT NULL COMMENT '岗位缩写',
  `GRADE` int(11) DEFAULT NULL COMMENT '等级',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  `LAST_EDIT_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`POSITION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of p_un_position
-- ----------------------------
INSERT INTO `p_un_position` VALUES ('4', '普通员工', '普通员工', '1', '1', '2017-08-13 11:50:17');

-- ----------------------------
-- Table structure for p_un_resource
-- ----------------------------
DROP TABLE IF EXISTS `p_un_resource`;
CREATE TABLE `p_un_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `RELATE_RESO_ID` varchar(36) DEFAULT NULL COMMENT '关联资源ID',
  `RESOURCE_NAME` varchar(128) DEFAULT NULL COMMENT '资源名',
  `RESOURCE_TYPE` varchar(8) DEFAULT NULL COMMENT '资源类型',
  `SYS_ID` bigint(20) DEFAULT NULL COMMENT '系统ID',
  `RESOURCE_CREATER` varchar(30) DEFAULT '' COMMENT '创建人',
  `RESOURCE_CREATE_GROUP` varchar(30) DEFAULT '' COMMENT '创建组织',
  `WHICH_END` varchar(1) DEFAULT '' COMMENT '创建人',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`RESOURCE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5971 DEFAULT CHARSET=utf8 COMMENT='资源';

-- ----------------------------
-- Records of p_un_resource
-- ----------------------------
INSERT INTO `p_un_resource` VALUES ('4531', '10611', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4532', '10612', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4533', '10613', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4534', '10614', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4535', '10615', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4536', '10616', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4537', '10617', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4538', '10618', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4539', '10619', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4540', '10620', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4541', '10621', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4542', '10622', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4543', '10623', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4544', '10624', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4545', '10625', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4546', '10626', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4547', '10627', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4548', '10628', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4549', '10629', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4550', '10630', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4551', '10631', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4555', '10632', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4556', '10633', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4557', '10634', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4558', '10635', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4561', '10636', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4562', '10637', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4563', '10638', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4564', '10639', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4565', '10640', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4566', '10641', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4567', '10642', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4568', '10643', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4570', '10644', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4571', '10645', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4572', '10646', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4573', '10647', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4574', '10648', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4575', '10649', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4589', '10650', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4590', '10651', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4594', '10652', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4595', '10653', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4596', '10654', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4597', '10655', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4598', '10656', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4599', '10657', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4600', '10658', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4601', '10659', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4602', '10660', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4603', '10661', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4662', '10662', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4667', '10663', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4668', '10664', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4669', '10665', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4670', '10666', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4671', '10667', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4700', '10668', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4701', '10669', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4704', '10670', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4714', '10692', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4715', '10693', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4716', '10694', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4717', '10695', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4718', '10696', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4719', '10697', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4720', '10698', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4721', '10699', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4722', '10700', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4723', '10701', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4724', '10702', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4725', '10703', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4726', '10704', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4770', '10705', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4771', '10706', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4772', '10707', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4773', '10708', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4774', '10709', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4775', '10710', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4780', '10711', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4782', '10712', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4783', '10713', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4784', '10714', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4785', '10715', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4786', '10716', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4802', '10717', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4803', '10718', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4822', '10719', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4823', '10720', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4824', '10721', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4835', '10722', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4836', '10723', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4837', '10724', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4838', '10725', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4839', '10726', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4840', '10727', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4870', '10728', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4893', '10729', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4894', '10730', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4895', '10731', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4928', '10732', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4929', '10733', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4930', '10734', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4931', '10735', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4932', '10736', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4933', '10737', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4934', '10738', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4935', '10739', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4936', '10740', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4938', '10741', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4939', '10742', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4940', '10743', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4941', '10744', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4942', '10745', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4943', '10746', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4944', '10747', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4946', '10748', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4947', '10749', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4948', '10750', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4949', '10751', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4950', '10752', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('4983', '10753', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5002', '10754', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5003', '10755', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5011', '10756', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5012', '10757', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5013', '10758', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5014', '10759', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5015', '10760', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5016', '10761', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5017', '10762', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5018', '10763', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5019', '10764', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5020', '10765', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5021', '10766', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5022', '10767', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5023', '10768', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5026', '10769', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5027', '10770', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5033', '10771', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5050', '10772', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5079', '10773', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5080', '10774', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5082', '10775', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5123', '10776', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5124', '10777', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5125', '10778', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5126', '10779', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5162', '10780', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5188', '10781', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5197', '10782', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5206', '10783', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5207', '10784', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5208', '10785', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5209', '10786', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5212', '10787', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5213', '10788', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5214', '10789', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5215', '10790', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5216', '10791', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5217', '10792', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5218', '10793', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5219', '10794', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5220', '10795', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5221', '10796', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5222', '10797', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5223', '10798', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5224', '10799', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5225', '10800', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5226', '10801', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5227', '10802', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5228', '10803', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5229', '10804', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5230', '10805', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5233', '10806', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5236', '10807', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5270', '10808', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5276', '10809', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5277', '10810', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5278', '10811', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5279', '10812', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5280', '10813', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5281', '10814', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5282', '10815', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5283', '10816', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5284', '10817', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5285', '10818', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5296', '10819', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5350', '10820', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5354', '10821', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5355', '10822', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5357', '10823', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5362', '10824', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5363', '10825', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5364', '10826', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5365', '10827', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5366', '10828', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5367', '10829', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5370', '10830', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5372', '10831', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5373', '10832', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5374', '10833', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5376', '10834', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5379', '10835', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5381', '10836', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5382', '10837', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5383', '10838', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5384', '10839', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5385', '10840', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5386', '10841', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5387', '10842', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5388', '10843', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5389', '10844', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5390', '10845', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5391', '10846', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5392', '10847', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5393', '10848', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5394', '10849', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5395', '10850', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5396', '10851', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5397', '10852', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5398', '10853', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5403', '10854', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5404', '10855', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5405', '10856', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5406', '10857', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5407', '10858', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5408', '10859', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5409', '10860', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5410', '10861', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5411', '10862', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5412', '10863', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5413', '10864', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5414', '10865', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5415', '10866', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5416', '10867', null, '1', '110', '', '', null, '1');
INSERT INTO `p_un_resource` VALUES ('5460', '10868', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5463', '10869', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5464', '10870', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5471', '10871', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5472', '10872', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5501', '10873', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5502', '10874', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5503', '10875', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5514', '10876', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5515', '10877', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5516', '10878', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5517', '10879', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5518', '10880', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5519', '10881', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5520', '10882', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5522', '10883', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5599', '855d3397-bb88-4679-b340-841110988987', '确定更改', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5600', 'd670486d-e040-4fec-b162-782748cb8afb', '关闭', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5605', '10884', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5606', '10885', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5607', '10886', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5608', '10887', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5609', '10888', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5610', '10889', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5611', '10890', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5640', '10891', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5641', '10892', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5647', '10893', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5671', '10894', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5745', '10895', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5746', '10896', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5747', '10897', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5748', '10898', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5752', '10899', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5772', '083bc58d-6490-49de-af15-c1dbff097fd5', '新增', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5773', 'dcd83cde-c798-4e89-aa75-439e71b2c3aa', '编辑', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5774', '65635ee7-9495-4d50-937c-2343f5e1f5b9', '启用', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5775', '02940dbe-7c48-4fc8-97f1-b65cb7204326', '禁用', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5776', '2d3d7992-7daa-4781-ac30-c233da644e18', '删除', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5777', '10900', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5778', '10901', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5781', 'd6db3231-d736-431f-b89f-fb66945e33df', '保存', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5782', '54736121-b385-43a3-a6ad-187fd1ce9d19', '返回', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5783', '10902', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5786', 'e2a34cd1-54e4-4104-8872-07f7df7eacc3', '搜索', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5793', '10903', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5799', '10904', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5818', '10905', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5819', '10906', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5835', '10907', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5864', '10908', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5871', '10909', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5872', '10910', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5893', '10911', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5895', '8b132c6d-f5e6-4c34-8dcd-374907fb97c3', '返回上一级', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5905', '10912', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5919', '10913', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5920', '10914', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5923', '10915', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5924', '10916', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5927', '10917', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5944', '10918', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5964', '10919', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5965', '10920', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5966', '10921', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5967', '10922', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5968', '10923', null, '1', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5969', 'f95066ec-c892-469e-9e53-9f8161fd1506', '删除', '3', '110', '', '', null, null);
INSERT INTO `p_un_resource` VALUES ('5970', '10261977-6ff7-4a10-baba-ffc3681380fb', 'test', '3', '110', '', '', null, null);

-- ----------------------------
-- Table structure for p_un_role_access
-- ----------------------------
DROP TABLE IF EXISTS `p_un_role_access`;
CREATE TABLE `p_un_role_access` (
  `ROLE_ACC_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户权限关联ID',
  `ROLE_ID` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `RESOURCE_ID` bigint(20) DEFAULT NULL COMMENT '资源ID',
  `OPER_TYPE` bigint(20) DEFAULT NULL COMMENT '操作类型',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ACC_ID`),
  UNIQUE KEY `ROLE_ID` (`ROLE_ID`,`RESOURCE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5640 DEFAULT CHARSET=utf8 COMMENT='角色与权限关联表';

-- ----------------------------
-- Records of p_un_role_access
-- ----------------------------
INSERT INTO `p_un_role_access` VALUES ('4172', '87', '4531', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4173', '87', '4532', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4174', '87', '4533', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4175', '87', '4534', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4176', '87', '4535', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4177', '87', '4536', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4189', '87', '4541', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4193', '87', '4547', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4194', '87', '4548', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4195', '87', '4549', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4196', '87', '4551', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4205', '87', '4555', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4206', '87', '4556', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4207', '87', '4557', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4208', '87', '4558', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4297', '87', '4561', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4298', '87', '4562', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4299', '87', '4563', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4300', '87', '4564', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4301', '87', '4565', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4302', '87', '4566', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4303', '87', '4567', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4330', '87', '4571', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4338', '87', '4568', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4339', '87', '4572', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4340', '87', '4573', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4341', '87', '4574', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4342', '87', '4575', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4345', '87', '4594', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4346', '87', '4595', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4347', '87', '4596', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4348', '87', '4597', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4349', '87', '4598', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4363', '87', '4662', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4366', '87', '4667', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4368', '87', '4570', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4369', '87', '4589', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4370', '87', '4599', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4371', '87', '4600', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4372', '87', '4601', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4373', '87', '4602', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4374', '87', '4603', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4404', '87', '4668', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4405', '87', '4669', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4406', '87', '4670', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4407', '87', '4671', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4417', '87', '4716', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4418', '87', '4717', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4419', '87', '4718', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4420', '87', '4719', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4421', '87', '4720', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4422', '87', '4721', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4423', '87', '4722', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4424', '87', '4723', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4425', '87', '4724', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4426', '87', '4725', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4427', '87', '4726', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4428', '87', '4714', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4430', '87', '4590', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4431', '87', '4738', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4432', '87', '4739', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4433', '87', '4742', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4434', '87', '4741', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4435', '87', '4743', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4436', '87', '4732', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4437', '87', '4730', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4438', '87', '4733', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4439', '87', '4731', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4440', '87', '4750', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4441', '87', '4729', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4442', '87', '4734', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4443', '87', '4728', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4444', '87', '4727', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4445', '87', '4751', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4446', '87', '4745', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4447', '87', '4744', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4448', '87', '4746', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4449', '87', '4736', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4450', '87', '4735', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4451', '87', '4737', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4452', '87', '4749', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4453', '87', '4747', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4454', '87', '4748', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4455', '87', '4740', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4500', '87', '4773', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4501', '87', '4772', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4502', '87', '4780', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4503', '87', '4783', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4504', '87', '4782', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4505', '87', '4784', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4507', '87', '4786', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4508', '87', '4777', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4509', '87', '4779', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4510', '87', '4799', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4511', '87', '4796', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4512', '87', '4801', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4513', '87', '4797', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4514', '87', '4800', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4515', '87', '4795', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4516', '87', '4791', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4517', '87', '4790', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4518', '87', '4776', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4519', '87', '4794', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4520', '87', '4793', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4521', '87', '4754', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4522', '87', '4765', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4523', '87', '4762', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4524', '87', '4755', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4525', '87', '4759', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4526', '87', '4758', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4527', '87', '4757', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4528', '87', '4756', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4529', '87', '4778', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4530', '87', '4753', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4531', '87', '4760', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4532', '87', '4764', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4533', '87', '4761', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4534', '87', '4781', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4535', '87', '4792', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4536', '87', '4763', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4537', '87', '4752', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('4539', '87', '4802', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4541', '87', '4803', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4551', '87', '4785', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4600', '87', '4870', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4601', '87', '4893', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4602', '87', '4895', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4603', '87', '4894', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4610', '87', '4933', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4611', '87', '4934', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4612', '87', '4935', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4613', '87', '4936', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4614', '87', '4930', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4615', '87', '4931', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4616', '87', '4932', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4617', '87', '4928', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4618', '87', '4929', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4619', '87', '4938', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4620', '87', '4939', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4621', '87', '4941', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4622', '87', '4942', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4623', '87', '4943', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4624', '87', '4944', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4625', '87', '4946', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4626', '87', '4947', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4627', '87', '4948', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4628', '87', '4949', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4629', '87', '4950', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4723', '87', '5002', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4724', '87', '5003', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4730', '87', '5012', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4731', '87', '5014', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4732', '87', '5017', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4733', '87', '5018', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4734', '87', '5021', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4735', '87', '5022', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4737', '87', '4983', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4738', '87', '5013', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4748', '87', '5027', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4752', '87', '5033', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4755', '87', '5050', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4764', '87', '5080', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4769', '87', '5082', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4773', '87', '5123', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4774', '87', '5124', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4775', '87', '5125', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4776', '87', '5126', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4815', '87', '5162', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4831', '87', '5188', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4847', '87', '4715', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4848', '87', '5206', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4849', '87', '5207', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4850', '87', '5208', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('4851', '87', '5209', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5053', '87', '5280', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5054', '87', '5281', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5055', '87', '5282', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5057', '87', '5279', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5058', '87', '5197', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5061', '87', '5023', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5127', '87', '5283', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5128', '87', '5284', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5129', '87', '5285', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5130', '87', '4940', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5131', '87', '5278', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5132', '87', '5225', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5166', '87', '5354', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5172', '87', '5355', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5177', '87', '5362', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5179', '87', '5364', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5180', '87', '5365', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5181', '87', '5366', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5182', '87', '5367', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5201', '87', '5370', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5204', '87', '5372', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5205', '87', '5373', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5206', '87', '5374', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5207', '87', '5376', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5209', '87', '5379', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5210', '87', '5381', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5211', '87', '5382', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5213', '87', '5383', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5214', '87', '5384', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5215', '87', '5385', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5216', '87', '5363', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5217', '87', '5387', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5218', '87', '5389', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5219', '87', '5386', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5222', '87', '5391', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5223', '87', '5392', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5224', '87', '5393', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5225', '87', '5394', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5228', '87', '5398', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5281', '87', '5396', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5282', '87', '5397', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5285', '87', '4540', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5287', '87', '4545', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5292', '87', '5403', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5293', '87', '5404', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5306', '87', '4546', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5307', '87', '4544', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5308', '87', '5405', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5309', '87', '5406', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5310', '87', '5407', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5313', '87', '5410', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5322', '87', '5390', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5323', '87', '5411', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5324', '87', '5412', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5325', '87', '5413', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5326', '1', '5447', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('5327', '1', '5448', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('5328', '1', '5449', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('5329', '1', '5446', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('5330', '1', '5450', '11111', '1');
INSERT INTO `p_un_role_access` VALUES ('5336', '113', '5412', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5337', '113', '5404', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5338', '113', '5413', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5339', '87', '5414', '1', '1');
INSERT INTO `p_un_role_access` VALUES ('5340', '87', '5415', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5341', '113', '4540', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5342', '113', '4544', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5343', '113', '4545', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5344', '113', '4546', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5345', '113', '5415', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5346', '113', '5416', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5347', '113', '5460', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5348', '87', '5463', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5349', '113', '5463', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5350', '87', '5460', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5351', '87', '5416', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5352', '87', '5464', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5353', '113', '5464', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5354', '87', '5471', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5355', '87', '5472', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5356', '87', '5496', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5357', '87', '5497', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5358', '87', '5500', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5359', '87', '5498', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5363', '87', '5499', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5364', '87', '5514', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5365', '87', '5515', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5366', '87', '5516', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5367', '87', '5517', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5368', '87', '5518', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5369', '87', '5519', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5373', '113', '5514', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5374', '113', '5515', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5375', '113', '5516', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5376', '113', '5517', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5377', '113', '5518', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5378', '113', '5519', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5379', '87', '5520', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5380', '113', '5522', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5381', '87', '5605', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5382', '87', '5606', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5383', '87', '5607', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5384', '87', '5608', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5385', '87', '5609', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5386', '87', '5610', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5387', '87', '5408', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5388', '87', '5409', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5389', '87', '5611', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5393', '87', '5522', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5394', '87', '5640', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5395', '87', '5641', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5396', '87', '5647', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5397', '87', '5671', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5400', '113', '5408', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5401', '113', '5409', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5402', '113', '5611', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5403', '113', '5647', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5404', '113', '5671', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5405', '113', '5746', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5406', '113', '5747', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5407', '113', '5748', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5408', '87', '5513', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5409', '87', '5506', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5410', '87', '5512', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5411', '87', '5511', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5412', '87', '5507', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5413', '87', '5510', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5414', '87', '5508', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5415', '87', '5509', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5417', '87', '5777', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5418', '87', '5778', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5419', '87', '5783', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5420', '87', '5452', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5421', '87', '5465', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5422', '87', '5454', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5423', '87', '5455', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5424', '87', '5458', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5425', '87', '5457', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5426', '87', '5541', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5427', '87', '5461', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5428', '87', '5462', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5429', '87', '5762', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5430', '87', '5764', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5431', '87', '5484', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5432', '87', '5469', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5433', '87', '5759', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5434', '87', '5761', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5435', '87', '5760', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5436', '87', '5468', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5437', '87', '5485', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5438', '87', '5467', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5439', '87', '5476', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5440', '87', '5479', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5441', '87', '5474', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5442', '87', '5477', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5443', '87', '5478', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5444', '87', '5481', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5445', '87', '5448', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5446', '87', '5473', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5447', '87', '5482', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5448', '87', '5483', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5449', '87', '5447', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5450', '87', '5446', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5451', '87', '5450', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5452', '87', '5449', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5453', '87', '5495', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5454', '87', '5494', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5455', '87', '5488', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5456', '87', '5489', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5457', '87', '5492', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5458', '87', '5490', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5459', '87', '5491', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5460', '87', '5487', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5461', '87', '5744', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5462', '87', '5493', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5463', '87', '5525', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5464', '87', '5550', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5465', '87', '5524', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5466', '87', '5687', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5467', '87', '5694', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5468', '87', '5686', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5469', '87', '5688', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5470', '87', '5532', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5471', '87', '5530', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5472', '87', '5529', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5473', '87', '5693', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5474', '87', '5695', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5475', '87', '5692', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5476', '87', '5528', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5477', '87', '5534', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5478', '87', '5690', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5479', '87', '5691', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5480', '87', '5526', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5481', '87', '5548', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5482', '87', '5705', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5483', '87', '5707', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5484', '87', '5706', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5485', '87', '5547', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5486', '87', '5549', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5487', '87', '5542', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5488', '87', '5545', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5489', '87', '5543', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5490', '87', '5703', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5491', '87', '5702', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5492', '87', '5704', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5493', '87', '5536', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5494', '87', '5601', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5495', '87', '5696', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5496', '87', '5685', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5497', '87', '5697', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5498', '87', '5698', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5499', '87', '5627', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5500', '87', '5683', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5501', '87', '5689', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5502', '87', '5630', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5503', '87', '5628', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5504', '87', '5734', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5505', '87', '5584', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5506', '87', '5733', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5507', '87', '5732', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5508', '87', '5586', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5509', '87', '5558', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5510', '87', '5711', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5511', '87', '5583', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5512', '87', '5556', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5513', '87', '5555', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5514', '87', '5712', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5515', '87', '5713', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5516', '87', '5721', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5517', '87', '5570', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5518', '87', '5567', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5519', '87', '5720', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5520', '87', '5568', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5521', '87', '5722', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5522', '87', '5595', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5523', '87', '5743', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5524', '87', '5598', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5525', '87', '5741', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5526', '87', '5742', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5527', '87', '5596', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5528', '87', '5735', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5529', '87', '5589', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5530', '87', '5590', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5531', '87', '5737', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5532', '87', '5587', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5533', '87', '5736', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5534', '87', '5739', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5535', '87', '5593', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5536', '87', '5740', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5537', '87', '5738', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5538', '87', '5594', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5539', '87', '5700', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5540', '87', '5592', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5541', '87', '5539', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5542', '87', '5701', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5543', '87', '5540', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5544', '87', '5699', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5545', '87', '5723', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5546', '87', '5537', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5547', '87', '5574', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5548', '87', '5724', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5549', '87', '5582', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5550', '87', '5725', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5551', '87', '5571', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5552', '87', '5572', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5553', '87', '5579', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5554', '87', '5581', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5555', '87', '5730', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5556', '87', '5578', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5557', '87', '5729', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5558', '87', '5726', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5559', '87', '5727', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5560', '87', '5731', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5561', '87', '5577', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5562', '87', '5575', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5563', '87', '5708', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5564', '87', '5728', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5565', '87', '5553', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5566', '87', '5709', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5567', '87', '5554', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5568', '87', '5552', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5569', '87', '5718', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5570', '87', '5710', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5571', '87', '5717', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5572', '87', '5564', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5573', '87', '5566', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5574', '87', '5563', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5575', '87', '5715', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5576', '87', '5562', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5577', '87', '5560', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5578', '87', '5719', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5579', '87', '5714', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5580', '87', '5559', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5581', '87', '5716', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5582', '87', '5626', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5583', '87', '5667', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5584', '87', '5631', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5585', '87', '5620', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5586', '87', '5622', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5587', '87', '5664', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5588', '87', '5660', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5589', '87', '5659', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5590', '87', '5632', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5591', '87', '5613', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5592', '87', '5614', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5593', '87', '5612', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5594', '87', '5771', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5595', '87', '5784', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5596', '87', '5785', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5597', '87', '5780', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5598', '87', '5779', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5599', '87', '5793', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5600', '87', '5799', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5601', '87', '5818', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5602', '87', '5819', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5603', '87', '5747', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5604', '87', '5746', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5605', '87', '5748', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5606', '87', '5835', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5607', '87', '5864', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5608', '87', '5871', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5609', '87', '5872', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5611', '87', '5501', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5612', '87', '5502', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5613', '87', '5503', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5614', '87', '5752', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5615', '87', '5892', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5616', '87', '5820', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5617', '87', '5833', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5618', '87', '5798', '11111', null);
INSERT INTO `p_un_role_access` VALUES ('5619', '87', '5893', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5620', '87', '5905', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5627', '87', '5920', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5630', '87', '5923', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5631', '87', '5924', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5632', '87', '5927', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5633', '87', '5944', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5634', '113', '5640', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5635', '113', '5783', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5636', '113', '5964', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5637', '113', '5965', '1', null);
INSERT INTO `p_un_role_access` VALUES ('5639', '113', '5968', '1', null);

-- ----------------------------
-- Table structure for p_un_role_info
-- ----------------------------
DROP TABLE IF EXISTS `p_un_role_info`;
CREATE TABLE `p_un_role_info` (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `SYS_ID` bigint(20) DEFAULT NULL COMMENT '系统ID',
  `ROLE_NAME` varchar(20) DEFAULT NULL COMMENT '角色名称',
  `DICT_REMARK` varchar(255) DEFAULT NULL COMMENT '字典备注',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of p_un_role_info
-- ----------------------------
INSERT INTO `p_un_role_info` VALUES ('1', '110', '超级后台管理员', '拥有平台配置所有权限', '1');
INSERT INTO `p_un_role_info` VALUES ('87', '110', '管理员', '拥有系统操作的所有权限', '1');
INSERT INTO `p_un_role_info` VALUES ('113', '110', '普通用户', '普通用户', '1');

-- ----------------------------
-- Table structure for p_un_suggestion
-- ----------------------------
DROP TABLE IF EXISTS `p_un_suggestion`;
CREATE TABLE `p_un_suggestion` (
  `ID` varchar(40) NOT NULL,
  `Dept` varchar(255) DEFAULT NULL COMMENT '处理人部门',
  `DeptName` varchar(100) DEFAULT NULL,
  `Date` datetime DEFAULT NULL COMMENT '处理时间',
  `Conment` varchar(1000) DEFAULT NULL COMMENT '意见内容',
  `BusinessId` varchar(40) NOT NULL COMMENT '意见ID',
  `Person` varchar(50) DEFAULT NULL COMMENT '处理人',
  `PersonName` varchar(50) DEFAULT NULL,
  `Flag` int(11) DEFAULT '0' COMMENT '数据标识',
  `sendTime` datetime DEFAULT NULL COMMENT '发送时间',
  `LinkName` varchar(30) DEFAULT NULL COMMENT '流程节点名称',
  `Link` varchar(10) DEFAULT NULL COMMENT '流程节点id',
  `Orders` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `isLeader` char(2) DEFAULT NULL COMMENT '是否是领导',
  `deadline` float DEFAULT NULL COMMENT '期限',
  `remark` varchar(1000) DEFAULT NULL,
  `GROUP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_un_suggestion
-- ----------------------------

-- ----------------------------
-- Table structure for p_un_system
-- ----------------------------
DROP TABLE IF EXISTS `p_un_system`;
CREATE TABLE `p_un_system` (
  `SYS_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统ID',
  `SYS_ADDRESS` varchar(255) DEFAULT NULL COMMENT '系统地址',
  `SYS_NAME` varchar(50) DEFAULT NULL COMMENT '系统名称',
  `SYS_SHORT_NAME` varchar(30) DEFAULT NULL COMMENT '系统名称缩写',
  `SYS_CREATE_GROUP` varchar(30) DEFAULT '' COMMENT '创建组织',
  `SYS_CREATER` varchar(30) DEFAULT NULL COMMENT '创建人',
  `SYS_CREATE_TIME` date DEFAULT NULL COMMENT '创建时间',
  `SYS_STATUS` varchar(2) DEFAULT '' COMMENT '系统状态',
  `GROUP_ID` bigint(20) DEFAULT NULL COMMENT '组织ID',
  PRIMARY KEY (`SYS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8 COMMENT='系统';

-- ----------------------------
-- Records of p_un_system
-- ----------------------------
INSERT INTO `p_un_system` VALUES ('110', null, 'AWCP-全栈云开发平台', 'awcp', '1', '112', '2016-09-19', '1', '1');

-- ----------------------------
-- Table structure for p_un_user_base_info
-- ----------------------------
DROP TABLE IF EXISTS `p_un_user_base_info`;
CREATE TABLE `p_un_user_base_info` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USER_NAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  `USER_PWD` varchar(255) NOT NULL COMMENT '密码',
  `USER_ID_CARD_NUMBER` varchar(100) DEFAULT NULL COMMENT '用户身份证号',
  `NAME` varchar(30) DEFAULT NULL COMMENT '姓名',
  `USER_BIRTH_PLACE` varchar(255) DEFAULT NULL COMMENT '用户籍贯',
  `USER_HOUSEHOLD_REGIST` varchar(255) DEFAULT NULL COMMENT '用户户籍地',
  `USER_DOMICILE` varchar(255) DEFAULT NULL COMMENT '用户居住地',
  `USER_OFFICE_PHONE` varchar(20) DEFAULT NULL COMMENT '办公电话',
  `USER_HOUSE_PHONE` varchar(20) DEFAULT NULL COMMENT '家庭电话',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '移动电话',
  `USER_FAX` varchar(20) DEFAULT NULL COMMENT '用户传真',
  `USER_EMAIL` varchar(128) DEFAULT NULL COMMENT '电子邮件',
  `EMPLOYEE_ID` varchar(30) DEFAULT NULL COMMENT '工号',
  `USER_TITLE` varchar(30) DEFAULT NULL COMMENT '用户头衔',
  `USER_DOSSIER_NUMBER` varchar(50) DEFAULT NULL COMMENT '用户档案帐号',
  `USER_OFFICE_NUM` varchar(20) DEFAULT NULL COMMENT '办公室门牌',
  `GROUP_ID` bigint(20) DEFAULT NULL COMMENT '所属组织',
  `ORG_CODE` varchar(20) DEFAULT NULL,
  `USER_STATUS` char(2) DEFAULT NULL COMMENT '用户状态',
  `LAST_EDIT_TIME` datetime DEFAULT NULL,
  `NUMBER` int(11) DEFAULT '0' COMMENT '排序',
  `DEPT_ID` varchar(50) DEFAULT NULL,
  `DEPT_NAME` varchar(50) DEFAULT NULL,
  `SIGNATURE_IMG` varchar(50) DEFAULT NULL COMMENT '签名',
  `USER_HEAD_IMG` varchar(200) DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `IX_user_base_info_USER_ID_CARD_NUMBER` (`USER_ID_CARD_NUMBER`),
  UNIQUE KEY `MOBILE` (`MOBILE`),
  KEY `IX_user_base_info_GROUP_ID` (`GROUP_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=732815 DEFAULT CHARSET=utf8 COMMENT='用户基础信息';

-- ----------------------------
-- Records of p_un_user_base_info
-- ----------------------------
INSERT INTO `p_un_user_base_info` VALUES ('718945', '10001', '9e8e2e085360e6209a995756d9ac61ebc76eb47d1640a819', '10001', '10001', null, '1234', '1234', '0755-8588567', null, '18824159524', '0755-8588567', 'test@altoromutual.com', '10001', 'Managing Director', '9876543210', '9876543210', '1', '45575544-2', null, '0000-00-00 00:00:00', null, null, null, '0569b002-b4ab-4541-a814-2200b4206881', null);
INSERT INTO `p_un_user_base_info` VALUES ('719811', '9999', '9e8e2e085360e6209a995756d9ac61ebc76eb47d1640a819', '9999', '9999', '', '', '', '', '', '18888888881', '', '', '', '', '', '', '1', '45575544-2', '', '0000-00-00 00:00:00', null, '', '', '', '');

-- ----------------------------
-- Table structure for p_un_user_group
-- ----------------------------
DROP TABLE IF EXISTS `p_un_user_group`;
CREATE TABLE `p_un_user_group` (
  `USER_GRUOP_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '身份资格ID',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `GROUP_ID` bigint(20) DEFAULT NULL COMMENT '对应公司在p_un_group中的GROUP_EN_NAME',
  `POSITION_ID` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `IS_MANAGER` int(1) DEFAULT NULL COMMENT '是否管理岗位',
  `leader` bigint(20) DEFAULT NULL COMMENT '上级领导',
  PRIMARY KEY (`USER_GRUOP_ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`,`GROUP_ID`,`POSITION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户和组的关系表';

-- ----------------------------
-- Records of p_un_user_group
-- ----------------------------
INSERT INTO `p_un_user_group` VALUES ('1', '732814', '1', '4', null, null);

-- ----------------------------
-- Table structure for p_un_user_org
-- ----------------------------
DROP TABLE IF EXISTS `p_un_user_org`;
CREATE TABLE `p_un_user_org` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(11) DEFAULT NULL,
  `ORG_ID` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`,`ORG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=802 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of p_un_user_org
-- ----------------------------
INSERT INTO `p_un_user_org` VALUES ('731', '719600', '13');
INSERT INTO `p_un_user_org` VALUES ('601', '719600', '14');
INSERT INTO `p_un_user_org` VALUES ('355', '719600', '15');
INSERT INTO `p_un_user_org` VALUES ('732', '719601', '13');
INSERT INTO `p_un_user_org` VALUES ('602', '719601', '14');
INSERT INTO `p_un_user_org` VALUES ('356', '719601', '15');
INSERT INTO `p_un_user_org` VALUES ('733', '719602', '13');
INSERT INTO `p_un_user_org` VALUES ('603', '719602', '14');
INSERT INTO `p_un_user_org` VALUES ('357', '719602', '15');
INSERT INTO `p_un_user_org` VALUES ('734', '719603', '13');
INSERT INTO `p_un_user_org` VALUES ('604', '719603', '14');
INSERT INTO `p_un_user_org` VALUES ('358', '719603', '15');
INSERT INTO `p_un_user_org` VALUES ('735', '719604', '13');
INSERT INTO `p_un_user_org` VALUES ('605', '719604', '14');
INSERT INTO `p_un_user_org` VALUES ('359', '719604', '15');
INSERT INTO `p_un_user_org` VALUES ('736', '719605', '13');
INSERT INTO `p_un_user_org` VALUES ('606', '719605', '14');
INSERT INTO `p_un_user_org` VALUES ('360', '719605', '15');
INSERT INTO `p_un_user_org` VALUES ('737', '719606', '13');
INSERT INTO `p_un_user_org` VALUES ('607', '719606', '14');
INSERT INTO `p_un_user_org` VALUES ('361', '719606', '15');
INSERT INTO `p_un_user_org` VALUES ('738', '719607', '13');
INSERT INTO `p_un_user_org` VALUES ('608', '719607', '14');
INSERT INTO `p_un_user_org` VALUES ('486', '719608', '4');
INSERT INTO `p_un_user_org` VALUES ('741', '719608', '13');
INSERT INTO `p_un_user_org` VALUES ('611', '719608', '14');
INSERT INTO `p_un_user_org` VALUES ('487', '719609', '4');
INSERT INTO `p_un_user_org` VALUES ('742', '719609', '13');
INSERT INTO `p_un_user_org` VALUES ('612', '719609', '14');
INSERT INTO `p_un_user_org` VALUES ('488', '719610', '4');
INSERT INTO `p_un_user_org` VALUES ('743', '719610', '13');
INSERT INTO `p_un_user_org` VALUES ('613', '719610', '14');
INSERT INTO `p_un_user_org` VALUES ('489', '719611', '4');
INSERT INTO `p_un_user_org` VALUES ('614', '719611', '14');
INSERT INTO `p_un_user_org` VALUES ('490', '719612', '4');
INSERT INTO `p_un_user_org` VALUES ('615', '719612', '14');
INSERT INTO `p_un_user_org` VALUES ('491', '719613', '4');
INSERT INTO `p_un_user_org` VALUES ('616', '719613', '14');
INSERT INTO `p_un_user_org` VALUES ('492', '719614', '4');
INSERT INTO `p_un_user_org` VALUES ('617', '719614', '14');
INSERT INTO `p_un_user_org` VALUES ('493', '719615', '4');
INSERT INTO `p_un_user_org` VALUES ('618', '719615', '14');
INSERT INTO `p_un_user_org` VALUES ('494', '719616', '4');
INSERT INTO `p_un_user_org` VALUES ('619', '719616', '14');
INSERT INTO `p_un_user_org` VALUES ('495', '719617', '4');
INSERT INTO `p_un_user_org` VALUES ('620', '719617', '14');
INSERT INTO `p_un_user_org` VALUES ('496', '719618', '4');
INSERT INTO `p_un_user_org` VALUES ('621', '719618', '14');
INSERT INTO `p_un_user_org` VALUES ('497', '719619', '4');
INSERT INTO `p_un_user_org` VALUES ('622', '719619', '14');
INSERT INTO `p_un_user_org` VALUES ('498', '719620', '4');
INSERT INTO `p_un_user_org` VALUES ('623', '719620', '14');
INSERT INTO `p_un_user_org` VALUES ('499', '719621', '4');
INSERT INTO `p_un_user_org` VALUES ('624', '719621', '14');
INSERT INTO `p_un_user_org` VALUES ('500', '719622', '4');
INSERT INTO `p_un_user_org` VALUES ('625', '719622', '14');
INSERT INTO `p_un_user_org` VALUES ('502', '719623', '4');
INSERT INTO `p_un_user_org` VALUES ('744', '719623', '13');
INSERT INTO `p_un_user_org` VALUES ('627', '719623', '14');
INSERT INTO `p_un_user_org` VALUES ('503', '719624', '4');
INSERT INTO `p_un_user_org` VALUES ('745', '719624', '13');
INSERT INTO `p_un_user_org` VALUES ('628', '719624', '14');
INSERT INTO `p_un_user_org` VALUES ('504', '719625', '4');
INSERT INTO `p_un_user_org` VALUES ('746', '719625', '13');
INSERT INTO `p_un_user_org` VALUES ('629', '719625', '14');
INSERT INTO `p_un_user_org` VALUES ('505', '719626', '4');
INSERT INTO `p_un_user_org` VALUES ('630', '719626', '14');
INSERT INTO `p_un_user_org` VALUES ('506', '719627', '4');
INSERT INTO `p_un_user_org` VALUES ('631', '719627', '14');
INSERT INTO `p_un_user_org` VALUES ('632', '719628', '14');
INSERT INTO `p_un_user_org` VALUES ('633', '719629', '14');
INSERT INTO `p_un_user_org` VALUES ('507', '719630', '4');
INSERT INTO `p_un_user_org` VALUES ('634', '719630', '14');
INSERT INTO `p_un_user_org` VALUES ('508', '719631', '4');
INSERT INTO `p_un_user_org` VALUES ('635', '719631', '14');
INSERT INTO `p_un_user_org` VALUES ('509', '719632', '4');
INSERT INTO `p_un_user_org` VALUES ('636', '719632', '14');
INSERT INTO `p_un_user_org` VALUES ('510', '719633', '4');
INSERT INTO `p_un_user_org` VALUES ('747', '719633', '13');
INSERT INTO `p_un_user_org` VALUES ('637', '719633', '14');
INSERT INTO `p_un_user_org` VALUES ('511', '719634', '4');
INSERT INTO `p_un_user_org` VALUES ('638', '719634', '14');
INSERT INTO `p_un_user_org` VALUES ('512', '719635', '4');
INSERT INTO `p_un_user_org` VALUES ('748', '719635', '13');
INSERT INTO `p_un_user_org` VALUES ('639', '719635', '14');
INSERT INTO `p_un_user_org` VALUES ('513', '719636', '4');
INSERT INTO `p_un_user_org` VALUES ('749', '719636', '13');
INSERT INTO `p_un_user_org` VALUES ('640', '719636', '14');
INSERT INTO `p_un_user_org` VALUES ('514', '719637', '4');
INSERT INTO `p_un_user_org` VALUES ('641', '719637', '14');
INSERT INTO `p_un_user_org` VALUES ('515', '719638', '4');
INSERT INTO `p_un_user_org` VALUES ('642', '719638', '14');
INSERT INTO `p_un_user_org` VALUES ('516', '719639', '4');
INSERT INTO `p_un_user_org` VALUES ('750', '719639', '13');
INSERT INTO `p_un_user_org` VALUES ('643', '719639', '14');
INSERT INTO `p_un_user_org` VALUES ('517', '719640', '4');
INSERT INTO `p_un_user_org` VALUES ('751', '719640', '13');
INSERT INTO `p_un_user_org` VALUES ('644', '719640', '14');
INSERT INTO `p_un_user_org` VALUES ('518', '719641', '4');
INSERT INTO `p_un_user_org` VALUES ('645', '719641', '14');
INSERT INTO `p_un_user_org` VALUES ('519', '719642', '4');
INSERT INTO `p_un_user_org` VALUES ('646', '719642', '14');
INSERT INTO `p_un_user_org` VALUES ('520', '719643', '4');
INSERT INTO `p_un_user_org` VALUES ('647', '719643', '14');
INSERT INTO `p_un_user_org` VALUES ('521', '719644', '4');
INSERT INTO `p_un_user_org` VALUES ('752', '719644', '13');
INSERT INTO `p_un_user_org` VALUES ('648', '719644', '14');
INSERT INTO `p_un_user_org` VALUES ('522', '719645', '4');
INSERT INTO `p_un_user_org` VALUES ('649', '719645', '14');
INSERT INTO `p_un_user_org` VALUES ('523', '719646', '4');
INSERT INTO `p_un_user_org` VALUES ('650', '719646', '14');
INSERT INTO `p_un_user_org` VALUES ('524', '719647', '4');
INSERT INTO `p_un_user_org` VALUES ('651', '719647', '14');
INSERT INTO `p_un_user_org` VALUES ('525', '719648', '4');
INSERT INTO `p_un_user_org` VALUES ('753', '719648', '13');
INSERT INTO `p_un_user_org` VALUES ('652', '719648', '14');
INSERT INTO `p_un_user_org` VALUES ('526', '719649', '4');
INSERT INTO `p_un_user_org` VALUES ('653', '719649', '14');
INSERT INTO `p_un_user_org` VALUES ('527', '719650', '4');
INSERT INTO `p_un_user_org` VALUES ('654', '719650', '14');
INSERT INTO `p_un_user_org` VALUES ('528', '719651', '4');
INSERT INTO `p_un_user_org` VALUES ('754', '719651', '13');
INSERT INTO `p_un_user_org` VALUES ('655', '719651', '14');
INSERT INTO `p_un_user_org` VALUES ('529', '719652', '4');
INSERT INTO `p_un_user_org` VALUES ('755', '719652', '13');
INSERT INTO `p_un_user_org` VALUES ('656', '719652', '14');
INSERT INTO `p_un_user_org` VALUES ('530', '719653', '4');
INSERT INTO `p_un_user_org` VALUES ('756', '719653', '13');
INSERT INTO `p_un_user_org` VALUES ('657', '719653', '14');
INSERT INTO `p_un_user_org` VALUES ('531', '719654', '4');
INSERT INTO `p_un_user_org` VALUES ('757', '719654', '13');
INSERT INTO `p_un_user_org` VALUES ('658', '719654', '14');
INSERT INTO `p_un_user_org` VALUES ('532', '719655', '4');
INSERT INTO `p_un_user_org` VALUES ('758', '719655', '13');
INSERT INTO `p_un_user_org` VALUES ('659', '719655', '14');
INSERT INTO `p_un_user_org` VALUES ('533', '719656', '4');
INSERT INTO `p_un_user_org` VALUES ('759', '719656', '13');
INSERT INTO `p_un_user_org` VALUES ('660', '719656', '14');
INSERT INTO `p_un_user_org` VALUES ('534', '719657', '4');
INSERT INTO `p_un_user_org` VALUES ('760', '719657', '13');
INSERT INTO `p_un_user_org` VALUES ('661', '719657', '14');
INSERT INTO `p_un_user_org` VALUES ('535', '719658', '4');
INSERT INTO `p_un_user_org` VALUES ('761', '719658', '13');
INSERT INTO `p_un_user_org` VALUES ('662', '719658', '14');
INSERT INTO `p_un_user_org` VALUES ('536', '719659', '4');
INSERT INTO `p_un_user_org` VALUES ('663', '719659', '14');
INSERT INTO `p_un_user_org` VALUES ('537', '719660', '4');
INSERT INTO `p_un_user_org` VALUES ('664', '719660', '14');
INSERT INTO `p_un_user_org` VALUES ('538', '719661', '4');
INSERT INTO `p_un_user_org` VALUES ('665', '719661', '14');
INSERT INTO `p_un_user_org` VALUES ('539', '719662', '4');
INSERT INTO `p_un_user_org` VALUES ('666', '719662', '14');
INSERT INTO `p_un_user_org` VALUES ('540', '719663', '4');
INSERT INTO `p_un_user_org` VALUES ('762', '719663', '13');
INSERT INTO `p_un_user_org` VALUES ('667', '719663', '14');
INSERT INTO `p_un_user_org` VALUES ('541', '719664', '4');
INSERT INTO `p_un_user_org` VALUES ('763', '719664', '13');
INSERT INTO `p_un_user_org` VALUES ('668', '719664', '14');
INSERT INTO `p_un_user_org` VALUES ('542', '719665', '4');
INSERT INTO `p_un_user_org` VALUES ('764', '719665', '13');
INSERT INTO `p_un_user_org` VALUES ('669', '719665', '14');
INSERT INTO `p_un_user_org` VALUES ('543', '719666', '4');
INSERT INTO `p_un_user_org` VALUES ('765', '719666', '13');
INSERT INTO `p_un_user_org` VALUES ('670', '719666', '14');
INSERT INTO `p_un_user_org` VALUES ('544', '719667', '4');
INSERT INTO `p_un_user_org` VALUES ('766', '719667', '13');
INSERT INTO `p_un_user_org` VALUES ('671', '719667', '14');
INSERT INTO `p_un_user_org` VALUES ('545', '719668', '4');
INSERT INTO `p_un_user_org` VALUES ('672', '719668', '14');
INSERT INTO `p_un_user_org` VALUES ('546', '719669', '4');
INSERT INTO `p_un_user_org` VALUES ('673', '719669', '14');
INSERT INTO `p_un_user_org` VALUES ('547', '719670', '4');
INSERT INTO `p_un_user_org` VALUES ('674', '719670', '14');
INSERT INTO `p_un_user_org` VALUES ('548', '719671', '4');
INSERT INTO `p_un_user_org` VALUES ('675', '719671', '14');
INSERT INTO `p_un_user_org` VALUES ('549', '719672', '4');
INSERT INTO `p_un_user_org` VALUES ('767', '719672', '13');
INSERT INTO `p_un_user_org` VALUES ('676', '719672', '14');
INSERT INTO `p_un_user_org` VALUES ('550', '719673', '4');
INSERT INTO `p_un_user_org` VALUES ('768', '719673', '13');
INSERT INTO `p_un_user_org` VALUES ('677', '719673', '14');
INSERT INTO `p_un_user_org` VALUES ('551', '719674', '4');
INSERT INTO `p_un_user_org` VALUES ('769', '719674', '13');
INSERT INTO `p_un_user_org` VALUES ('678', '719674', '14');
INSERT INTO `p_un_user_org` VALUES ('552', '719675', '4');
INSERT INTO `p_un_user_org` VALUES ('770', '719675', '13');
INSERT INTO `p_un_user_org` VALUES ('679', '719675', '14');
INSERT INTO `p_un_user_org` VALUES ('553', '719676', '4');
INSERT INTO `p_un_user_org` VALUES ('771', '719676', '13');
INSERT INTO `p_un_user_org` VALUES ('680', '719676', '14');
INSERT INTO `p_un_user_org` VALUES ('554', '719677', '4');
INSERT INTO `p_un_user_org` VALUES ('681', '719677', '14');
INSERT INTO `p_un_user_org` VALUES ('555', '719678', '4');
INSERT INTO `p_un_user_org` VALUES ('682', '719678', '14');
INSERT INTO `p_un_user_org` VALUES ('556', '719679', '4');
INSERT INTO `p_un_user_org` VALUES ('683', '719679', '14');
INSERT INTO `p_un_user_org` VALUES ('772', '719680', '13');
INSERT INTO `p_un_user_org` VALUES ('684', '719680', '14');
INSERT INTO `p_un_user_org` VALUES ('557', '719681', '4');
INSERT INTO `p_un_user_org` VALUES ('773', '719681', '13');
INSERT INTO `p_un_user_org` VALUES ('685', '719681', '14');
INSERT INTO `p_un_user_org` VALUES ('558', '719682', '4');
INSERT INTO `p_un_user_org` VALUES ('774', '719682', '13');
INSERT INTO `p_un_user_org` VALUES ('686', '719682', '14');
INSERT INTO `p_un_user_org` VALUES ('559', '719683', '4');
INSERT INTO `p_un_user_org` VALUES ('775', '719683', '13');
INSERT INTO `p_un_user_org` VALUES ('687', '719683', '14');
INSERT INTO `p_un_user_org` VALUES ('560', '719684', '4');
INSERT INTO `p_un_user_org` VALUES ('776', '719684', '13');
INSERT INTO `p_un_user_org` VALUES ('688', '719684', '14');
INSERT INTO `p_un_user_org` VALUES ('561', '719685', '4');
INSERT INTO `p_un_user_org` VALUES ('777', '719685', '13');
INSERT INTO `p_un_user_org` VALUES ('689', '719685', '14');
INSERT INTO `p_un_user_org` VALUES ('562', '719686', '4');
INSERT INTO `p_un_user_org` VALUES ('778', '719686', '13');
INSERT INTO `p_un_user_org` VALUES ('690', '719686', '14');
INSERT INTO `p_un_user_org` VALUES ('563', '719687', '4');
INSERT INTO `p_un_user_org` VALUES ('691', '719687', '14');
INSERT INTO `p_un_user_org` VALUES ('564', '719688', '4');
INSERT INTO `p_un_user_org` VALUES ('692', '719688', '14');
INSERT INTO `p_un_user_org` VALUES ('565', '719689', '4');
INSERT INTO `p_un_user_org` VALUES ('779', '719689', '13');
INSERT INTO `p_un_user_org` VALUES ('693', '719689', '14');
INSERT INTO `p_un_user_org` VALUES ('566', '719690', '4');
INSERT INTO `p_un_user_org` VALUES ('780', '719690', '13');
INSERT INTO `p_un_user_org` VALUES ('694', '719690', '14');
INSERT INTO `p_un_user_org` VALUES ('567', '719691', '4');
INSERT INTO `p_un_user_org` VALUES ('781', '719691', '13');
INSERT INTO `p_un_user_org` VALUES ('695', '719691', '14');
INSERT INTO `p_un_user_org` VALUES ('696', '719692', '14');
INSERT INTO `p_un_user_org` VALUES ('568', '719693', '4');
INSERT INTO `p_un_user_org` VALUES ('782', '719693', '13');
INSERT INTO `p_un_user_org` VALUES ('697', '719693', '14');
INSERT INTO `p_un_user_org` VALUES ('569', '719694', '4');
INSERT INTO `p_un_user_org` VALUES ('698', '719694', '14');
INSERT INTO `p_un_user_org` VALUES ('570', '719695', '4');
INSERT INTO `p_un_user_org` VALUES ('699', '719695', '14');
INSERT INTO `p_un_user_org` VALUES ('571', '719696', '4');
INSERT INTO `p_un_user_org` VALUES ('783', '719696', '13');
INSERT INTO `p_un_user_org` VALUES ('700', '719696', '14');
INSERT INTO `p_un_user_org` VALUES ('572', '719697', '4');
INSERT INTO `p_un_user_org` VALUES ('784', '719697', '13');
INSERT INTO `p_un_user_org` VALUES ('701', '719697', '14');
INSERT INTO `p_un_user_org` VALUES ('573', '719698', '4');
INSERT INTO `p_un_user_org` VALUES ('785', '719698', '13');
INSERT INTO `p_un_user_org` VALUES ('702', '719698', '14');
INSERT INTO `p_un_user_org` VALUES ('574', '719699', '4');
INSERT INTO `p_un_user_org` VALUES ('786', '719699', '13');
INSERT INTO `p_un_user_org` VALUES ('703', '719699', '14');
INSERT INTO `p_un_user_org` VALUES ('575', '719700', '4');
INSERT INTO `p_un_user_org` VALUES ('787', '719700', '13');
INSERT INTO `p_un_user_org` VALUES ('704', '719700', '14');
INSERT INTO `p_un_user_org` VALUES ('576', '719701', '4');
INSERT INTO `p_un_user_org` VALUES ('788', '719701', '13');
INSERT INTO `p_un_user_org` VALUES ('705', '719701', '14');
INSERT INTO `p_un_user_org` VALUES ('577', '719702', '4');
INSERT INTO `p_un_user_org` VALUES ('706', '719702', '14');
INSERT INTO `p_un_user_org` VALUES ('578', '719703', '4');
INSERT INTO `p_un_user_org` VALUES ('707', '719703', '14');
INSERT INTO `p_un_user_org` VALUES ('579', '719704', '4');
INSERT INTO `p_un_user_org` VALUES ('789', '719704', '13');
INSERT INTO `p_un_user_org` VALUES ('708', '719704', '14');
INSERT INTO `p_un_user_org` VALUES ('580', '719705', '4');
INSERT INTO `p_un_user_org` VALUES ('790', '719705', '13');
INSERT INTO `p_un_user_org` VALUES ('709', '719705', '14');
INSERT INTO `p_un_user_org` VALUES ('581', '719706', '4');
INSERT INTO `p_un_user_org` VALUES ('710', '719706', '14');
INSERT INTO `p_un_user_org` VALUES ('582', '719707', '4');
INSERT INTO `p_un_user_org` VALUES ('711', '719707', '14');
INSERT INTO `p_un_user_org` VALUES ('583', '719708', '4');
INSERT INTO `p_un_user_org` VALUES ('712', '719708', '14');
INSERT INTO `p_un_user_org` VALUES ('791', '719709', '13');
INSERT INTO `p_un_user_org` VALUES ('713', '719709', '14');
INSERT INTO `p_un_user_org` VALUES ('584', '719710', '4');
INSERT INTO `p_un_user_org` VALUES ('792', '719710', '13');
INSERT INTO `p_un_user_org` VALUES ('714', '719710', '14');
INSERT INTO `p_un_user_org` VALUES ('585', '719711', '4');
INSERT INTO `p_un_user_org` VALUES ('793', '719711', '13');
INSERT INTO `p_un_user_org` VALUES ('715', '719711', '14');
INSERT INTO `p_un_user_org` VALUES ('586', '719712', '4');
INSERT INTO `p_un_user_org` VALUES ('794', '719712', '13');
INSERT INTO `p_un_user_org` VALUES ('716', '719712', '14');
INSERT INTO `p_un_user_org` VALUES ('587', '719713', '4');
INSERT INTO `p_un_user_org` VALUES ('795', '719713', '13');
INSERT INTO `p_un_user_org` VALUES ('717', '719713', '14');
INSERT INTO `p_un_user_org` VALUES ('588', '719714', '4');
INSERT INTO `p_un_user_org` VALUES ('796', '719714', '13');
INSERT INTO `p_un_user_org` VALUES ('718', '719714', '14');
INSERT INTO `p_un_user_org` VALUES ('589', '719715', '4');
INSERT INTO `p_un_user_org` VALUES ('797', '719715', '13');
INSERT INTO `p_un_user_org` VALUES ('719', '719715', '14');
INSERT INTO `p_un_user_org` VALUES ('590', '719716', '4');
INSERT INTO `p_un_user_org` VALUES ('720', '719716', '14');
INSERT INTO `p_un_user_org` VALUES ('591', '719717', '4');
INSERT INTO `p_un_user_org` VALUES ('721', '719717', '14');
INSERT INTO `p_un_user_org` VALUES ('592', '719718', '4');
INSERT INTO `p_un_user_org` VALUES ('722', '719718', '14');
INSERT INTO `p_un_user_org` VALUES ('593', '719719', '4');
INSERT INTO `p_un_user_org` VALUES ('798', '719719', '13');
INSERT INTO `p_un_user_org` VALUES ('723', '719719', '14');
INSERT INTO `p_un_user_org` VALUES ('594', '719720', '4');
INSERT INTO `p_un_user_org` VALUES ('799', '719720', '13');
INSERT INTO `p_un_user_org` VALUES ('724', '719720', '14');
INSERT INTO `p_un_user_org` VALUES ('595', '719721', '4');
INSERT INTO `p_un_user_org` VALUES ('800', '719721', '13');
INSERT INTO `p_un_user_org` VALUES ('725', '719721', '14');
INSERT INTO `p_un_user_org` VALUES ('596', '719722', '4');
INSERT INTO `p_un_user_org` VALUES ('726', '719722', '14');
INSERT INTO `p_un_user_org` VALUES ('597', '719723', '4');
INSERT INTO `p_un_user_org` VALUES ('727', '719723', '14');
INSERT INTO `p_un_user_org` VALUES ('598', '719724', '4');
INSERT INTO `p_un_user_org` VALUES ('801', '719724', '13');
INSERT INTO `p_un_user_org` VALUES ('728', '719724', '14');
INSERT INTO `p_un_user_org` VALUES ('599', '719725', '4');
INSERT INTO `p_un_user_org` VALUES ('729', '719725', '14');
INSERT INTO `p_un_user_org` VALUES ('600', '719726', '4');
INSERT INTO `p_un_user_org` VALUES ('730', '719726', '14');
INSERT INTO `p_un_user_org` VALUES ('484', '719727', '4');
INSERT INTO `p_un_user_org` VALUES ('739', '719727', '13');
INSERT INTO `p_un_user_org` VALUES ('609', '719727', '14');
INSERT INTO `p_un_user_org` VALUES ('485', '719728', '4');
INSERT INTO `p_un_user_org` VALUES ('740', '719728', '13');
INSERT INTO `p_un_user_org` VALUES ('610', '719728', '14');
INSERT INTO `p_un_user_org` VALUES ('501', '719729', '4');
INSERT INTO `p_un_user_org` VALUES ('626', '719729', '14');

-- ----------------------------
-- Table structure for p_un_user_role
-- ----------------------------
DROP TABLE IF EXISTS `p_un_user_role`;
CREATE TABLE `p_un_user_role` (
  `USER_ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '身份资格ID',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `ROLE_ID` bigint(20) DEFAULT NULL COMMENT '组ID',
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`USER_ROLE_ID`),
  KEY `USER_ID` (`USER_ID`,`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=974188 DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of p_un_user_role
-- ----------------------------
INSERT INTO `p_un_user_role` VALUES ('718946', '719811', '1', null);
INSERT INTO `p_un_user_role` VALUES ('974183', '718945', '87', null);

-- ----------------------------
-- Table structure for sys_cfield
-- ----------------------------
DROP TABLE IF EXISTS `sys_cfield`;
CREATE TABLE `sys_cfield` (
  `EnsName` varchar(100) NOT NULL,
  `FK_Emp` varchar(100) NOT NULL,
  `Attrs` text,
  PRIMARY KEY (`EnsName`,`FK_Emp`),
  KEY `Sys_CFieldID` (`EnsName`,`FK_Emp`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_cfield
-- ----------------------------

-- ----------------------------
-- Table structure for sys_contrast
-- ----------------------------
DROP TABLE IF EXISTS `sys_contrast`;
CREATE TABLE `sys_contrast` (
  `MyPK` varchar(100) NOT NULL,
  `ContrastKey` varchar(20) DEFAULT NULL,
  `KeyVal1` varchar(20) DEFAULT NULL,
  `KeyVal2` varchar(20) DEFAULT NULL,
  `SortBy` varchar(20) DEFAULT NULL,
  `KeyOfNum` varchar(20) DEFAULT NULL,
  `GroupWay` int(11) DEFAULT NULL,
  `OrderWay` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_ContrastID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_contrast
-- ----------------------------

-- ----------------------------
-- Table structure for sys_datarpt
-- ----------------------------
DROP TABLE IF EXISTS `sys_datarpt`;
CREATE TABLE `sys_datarpt` (
  `MyPK` varchar(100) NOT NULL,
  `ColCount` varchar(50) DEFAULT NULL,
  `RowCount` varchar(50) DEFAULT NULL,
  `Val` float DEFAULT NULL,
  `RefOID` float DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_DataRptID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_datarpt
-- ----------------------------

-- ----------------------------
-- Table structure for sys_defval
-- ----------------------------
DROP TABLE IF EXISTS `sys_defval`;
CREATE TABLE `sys_defval` (
  `No` varchar(50) NOT NULL,
  `ParentNo` varchar(50) DEFAULT NULL,
  `IsParent` int(11) DEFAULT NULL,
  `WordsSort` int(11) DEFAULT NULL,
  `FK_MapData` varchar(50) DEFAULT NULL,
  `NodeAttrKey` varchar(50) DEFAULT NULL,
  `IsHisWords` int(11) DEFAULT NULL,
  `FK_Emp` varchar(100) DEFAULT NULL,
  `CurValue` text,
  PRIMARY KEY (`No`),
  KEY `Sys_DefValID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_defval
-- ----------------------------

-- ----------------------------
-- Table structure for sys_docfile
-- ----------------------------
DROP TABLE IF EXISTS `sys_docfile`;
CREATE TABLE `sys_docfile` (
  `MyPK` varchar(100) NOT NULL,
  `FileName` varchar(200) DEFAULT NULL,
  `FileSize` int(11) DEFAULT NULL,
  `FileType` varchar(50) DEFAULT NULL,
  `D1` text,
  `D2` text,
  `D3` text,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_DocFileID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_docfile
-- ----------------------------

-- ----------------------------
-- Table structure for sys_domain
-- ----------------------------
DROP TABLE IF EXISTS `sys_domain`;
CREATE TABLE `sys_domain` (
  `No` varchar(30) NOT NULL,
  `Name` varchar(30) DEFAULT NULL,
  `DBLink` varchar(130) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `Sys_DomainID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_domain
-- ----------------------------

-- ----------------------------
-- Table structure for sys_encfg
-- ----------------------------
DROP TABLE IF EXISTS `sys_encfg`;
CREATE TABLE `sys_encfg` (
  `No` varchar(100) NOT NULL,
  `GroupTitle` varchar(2000) DEFAULT NULL,
  `FJSavePath` varchar(100) DEFAULT NULL,
  `FJWebPath` varchar(100) DEFAULT NULL,
  `Datan` varchar(200) DEFAULT NULL,
  `UI` varchar(2000) DEFAULT '',
  PRIMARY KEY (`No`),
  KEY `Sys_EnCfgID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_encfg
-- ----------------------------
INSERT INTO `sys_encfg` VALUES ('BP.Sys.MapDataExt', '@No=基本属性@Designer=设计者信息', null, null, null, '');
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.Ext.FlowSheet', '@No=基本配置@FlowRunWay=启动方式,配置工作流程如何自动发起，该选项要与流程服务一起工作才有效.@StartLimitRole=启动限制规则@StartGuideWay=发起前置导航@CFlowWay=延续流程@DTSWay=流程数据与业务数据同步', null, null, null, '');
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.Ext.NodeSheet', '@NodeID=基本配置@FormType=表单@FWCSta=审核组件,适用于sdk表单审核组件与ccform上的审核组件属性设置.@SendLab=按钮权限,控制工作节点可操作按钮.@RunModel=运行模式,分合流,父子流程@AutoJumpRole0=跳转,自动跳转规则当遇到该节点时如何让其自动的执行下一步.@MPhone_WorkModel=移动,与手机平板电脑相关的应用设置.@WarningDays=考核,时效考核,质量考核.@OfficeOpenLab=公文按钮,只有当该节点是公文流程时候有效', null, null, null, '');
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.FlowSheet', '@No=基本配置@FlowRunWay=启动方式,配置工作流程如何自动发起，该选项要与流程服务一起工作才有效.@StartLimitRole=启动限制规则@StartGuideWay=发起前置导航@CFlowWay=延续流程@DTSWay=流程数据与业务数据同步', null, null, null, '');
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.NodeSheet', '@NodeID=基本配置@FormType=表单@FWCSta=审核组件,适用于sdk表单审核组件与ccform上的审核组件属性设置.@SendLab=按钮权限,控制工作节点可操作按钮.@RunModel=运行模式,分合流,父子流程@AutoJumpRole0=跳转,自动跳转规则当遇到该节点时如何让其自动的执行下一步.@MPhone_WorkModel=移动,与手机平板电脑相关的应用设置.@WarningDays=考核,时效考核,质量考核.@OfficeOpenLab=公文按钮,只有当该节点是公文流程时候有效', null, null, null, '');

-- ----------------------------
-- Table structure for sys_ensappcfg
-- ----------------------------
DROP TABLE IF EXISTS `sys_ensappcfg`;
CREATE TABLE `sys_ensappcfg` (
  `MyPK` varchar(100) NOT NULL,
  `EnsName` varchar(100) DEFAULT NULL,
  `CfgKey` varchar(100) DEFAULT NULL,
  `CfgVal` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_EnsAppCfgID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_ensappcfg
-- ----------------------------
INSERT INTO `sys_ensappcfg` VALUES ('BP.Port.Depts@FocusField', 'BP.Port.Depts', 'FocusField', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.Port.Depts@IsEnableDouclickGlo', 'BP.Port.Depts', 'IsEnableDouclickGlo', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.Port.Depts@IsEnableFocusField', 'BP.Port.Depts', 'IsEnableFocusField', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.Port.Depts@IsEnableOpenICON', 'BP.Port.Depts', 'IsEnableOpenICON', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.Port.Depts@IsEnableRefFunc', 'BP.Port.Depts', 'IsEnableRefFunc', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.Port.Depts@UIRowStyleGlo', 'BP.Port.Depts', 'UIRowStyleGlo', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.Port.Depts@WinCardH', 'BP.Port.Depts', 'WinCardH', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.Port.Depts@WinCardW', 'BP.Port.Depts', 'WinCardW', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Data.GenerWorkFlowViews@FocusField', 'BP.WF.Data.GenerWorkFlowViews', 'FocusField', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Data.GenerWorkFlowViews@IsEnableDouclickGlo', 'BP.WF.Data.GenerWorkFlowViews', 'IsEnableDouclickGlo', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Data.GenerWorkFlowViews@IsEnableFocusField', 'BP.WF.Data.GenerWorkFlowViews', 'IsEnableFocusField', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Data.GenerWorkFlowViews@IsEnableOpenICON', 'BP.WF.Data.GenerWorkFlowViews', 'IsEnableOpenICON', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Data.GenerWorkFlowViews@IsEnableRefFunc', 'BP.WF.Data.GenerWorkFlowViews', 'IsEnableRefFunc', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Data.GenerWorkFlowViews@UIRowStyleGlo', 'BP.WF.Data.GenerWorkFlowViews', 'UIRowStyleGlo', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Data.GenerWorkFlowViews@WinCardH', 'BP.WF.Data.GenerWorkFlowViews', 'WinCardH', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Data.GenerWorkFlowViews@WinCardW', 'BP.WF.Data.GenerWorkFlowViews', 'WinCardW', '');
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Entity.WorkFlowDeleteLogs@FocusField', 'BP.WF.Entity.WorkFlowDeleteLogs', 'FocusField', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Entity.WorkFlowDeleteLogs@IsEnableDouclickGlo', 'BP.WF.Entity.WorkFlowDeleteLogs', 'IsEnableDouclickGlo', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Entity.WorkFlowDeleteLogs@IsEnableFocusField', 'BP.WF.Entity.WorkFlowDeleteLogs', 'IsEnableFocusField', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Entity.WorkFlowDeleteLogs@IsEnableOpenICON', 'BP.WF.Entity.WorkFlowDeleteLogs', 'IsEnableOpenICON', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Entity.WorkFlowDeleteLogs@IsEnableRefFunc', 'BP.WF.Entity.WorkFlowDeleteLogs', 'IsEnableRefFunc', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Entity.WorkFlowDeleteLogs@UIRowStyleGlo', 'BP.WF.Entity.WorkFlowDeleteLogs', 'UIRowStyleGlo', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Entity.WorkFlowDeleteLogs@WinCardH', 'BP.WF.Entity.WorkFlowDeleteLogs', 'WinCardH', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Entity.WorkFlowDeleteLogs@WinCardW', 'BP.WF.Entity.WorkFlowDeleteLogs', 'WinCardW', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Template.FlowSheets@EditerType', 'BP.WF.Template.FlowSheets', 'EditerType', null);
INSERT INTO `sys_ensappcfg` VALUES ('BP.WF.Template.NodeSheets@EditerType', 'BP.WF.Template.NodeSheets', 'EditerType', '');

-- ----------------------------
-- Table structure for sys_enum
-- ----------------------------
DROP TABLE IF EXISTS `sys_enum`;
CREATE TABLE `sys_enum` (
  `MyPK` varchar(100) NOT NULL,
  `Lab` varchar(80) DEFAULT NULL,
  `EnumKey` varchar(40) DEFAULT NULL,
  `IntKey` int(11) DEFAULT NULL,
  `Lang` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_EnumID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_enum
-- ----------------------------
INSERT INTO `sys_enum` VALUES ('ActionType_CH_0', 'GET', 'ActionType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ActionType_CH_1', 'POST', 'ActionType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertType_CH_0', '短信', 'AlertType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertType_CH_1', '邮件', 'AlertType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertType_CH_2', '邮件与短信', 'AlertType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertType_CH_3', '系统(内部)消息', 'AlertType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_0', '不接收', 'AlertWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_1', '短信', 'AlertWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_2', '邮件', 'AlertWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_3', '内部消息', 'AlertWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_4', 'QQ消息', 'AlertWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_5', 'RTX消息', 'AlertWay', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_6', 'MSN消息', 'AlertWay', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('AppModel_CH_0', 'BS系统', 'AppModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AppModel_CH_1', 'CS系统', 'AppModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AppType_CH_0', '外部Url连接', 'AppType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AppType_CH_1', '本地可执行文件', 'AppType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AuthorWay_CH_0', '不授权', 'AuthorWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AuthorWay_CH_1', '全部流程授权', 'AuthorWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AuthorWay_CH_2', '指定流程授权', 'AuthorWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('BarType_CH_0', '标题消息列表(Tag1', 'BarType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('BarType_CH_1', '其它', 'BarType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('BatchRole_CH_0', '不可以批处理', 'BatchRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('BatchRole_CH_1', '批量审核', 'BatchRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('BatchRole_CH_2', '分组批量审核', 'BatchRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('BillFileType_CH_0', 'Word', 'BillFileType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('BillFileType_CH_1', 'PDF', 'BillFileType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('BillFileType_CH_2', 'Excel(未完成)', 'BillFileType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('BillFileType_CH_3', 'Html(未完成)', 'BillFileType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('BillFileType_CH_5', '锐浪报表', 'BillFileType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('BlockModel_CH_0', '不阻塞', 'BlockModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('BlockModel_CH_1', '当前节点的所有未完成的子流程', 'BlockModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('BlockModel_CH_2', '按约定格式阻塞未完成子流程', 'BlockModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('BlockModel_CH_3', '按照SQL阻塞', 'BlockModel', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('BlockModel_CH_4', '按照表达式阻塞', 'BlockModel', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('CancelRole_CH_0', '上一步可以撤销', 'CancelRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CancelRole_CH_1', '不能撤销', 'CancelRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CancelRole_CH_2', '上一步与开始节点可以撤销', 'CancelRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CancelRole_CH_3', '指定的节点可以撤销', 'CancelRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_0', '不能抄送', 'CCRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_1', '手工抄送', 'CCRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_2', '自动抄送', 'CCRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_3', '手工与自动', 'CCRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_4', '按表单SysCCEmps字段计算', 'CCRole', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('CCWriteTo_CH_0', '写入抄送列表', 'CCWriteTo', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CCWriteTo_CH_1', '写入待办', 'CCWriteTo', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CCWriteTo_CH_2', '写入待办与抄送列表', 'CCWriteTo', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CFlowWay_CH_0', '无:非延续类流程', 'CFlowWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CFlowWay_CH_1', '按照参数', 'CFlowWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CFlowWay_CH_2', '按照字段配置', 'CFlowWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ChartType_CH_0', '几何图形', 'ChartType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ChartType_CH_1', '肖像图片', 'ChartType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CHWay_CH_0', '不考核', 'CHWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CHWay_CH_1', '按时效', 'CHWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CHWay_CH_2', '按工作量', 'CHWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CondModel_CH_0', '由连接线条件控制', 'CondModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CondModel_CH_1', '让用户手工选择', 'CondModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ConnJudgeWay_CH_0', 'or', 'ConnJudgeWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ConnJudgeWay_CH_1', 'and', 'ConnJudgeWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CtrlWay_CH_0', '按岗位', 'CtrlWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CtrlWay_CH_1', '按部门', 'CtrlWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CtrlWay_CH_2', '按人员', 'CtrlWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CtrlWay_CH_3', '按SQL', 'CtrlWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('DataStoreModel_CH_0', '数据轨迹模式', 'DataStoreModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DataStoreModel_CH_1', '数据合并模式', 'DataStoreModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_0', '应用系统主数据库', 'DBSrcType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_1', 'SQLServer', 'DBSrcType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_2', 'Oracle', 'DBSrcType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_3', 'MySQL', 'DBSrcType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_4', 'Infomix', 'DBSrcType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_0', '不能删除', 'DelEnable', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_1', '逻辑删除', 'DelEnable', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_2', '记录日志方式删除', 'DelEnable', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_3', '彻底删除', 'DelEnable', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_4', '让用户决定删除方式', 'DelEnable', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_0', '01.按当前操作员所属组织结构逐级查找岗位', 'DeliveryWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_1', '02.按节点绑定的部门计算', 'DeliveryWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_10', '11.按绑定的岗位计算并且以绑定的部门集合为纬度', 'DeliveryWay', '10', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_100', '17.按ccflow的BPM模式处理', 'DeliveryWay', '100', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_11', '12.按指定节点的人员岗位计算', 'DeliveryWay', '11', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_12', '13.按SQL确定子线程接受人与数据源', 'DeliveryWay', '12', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_13', '14.由上一节点的明细表来决定子线程的接受人', 'DeliveryWay', '13', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_14', '15.仅按绑定的岗位计算', 'DeliveryWay', '14', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_15', '16.由FEE来决定', 'DeliveryWay', '15', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_2', '03.按设置的SQL获取接受人计算', 'DeliveryWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_3', '04.按节点绑定的人员计算', 'DeliveryWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_4', '05.由上一节点发送人通过“人员选择器”选择接受人', 'DeliveryWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_5', '06.按上一节点表单指定的字段值作为本步骤的接受人', 'DeliveryWay', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_6', '07.与上一节点处理人员相同', 'DeliveryWay', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_7', '08.与开始节点处理人相同', 'DeliveryWay', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_8', '09.与指定节点处理人相同', 'DeliveryWay', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('DeliveryWay_CH_9', '10.按绑定的岗位与部门交集计算', 'DeliveryWay', '9', 'CH');
INSERT INTO `sys_enum` VALUES ('DocType_CH_0', '正式公文', 'DocType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DocType_CH_1', '便函', 'DocType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('Draft_CH_0', '无(不设草稿)', 'Draft', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('Draft_CH_1', '保存到待办', 'Draft', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('Draft_CH_2', '保存到草稿箱', 'Draft', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlOpenType_CH_0', '操作员', 'DtlOpenType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlOpenType_CH_1', '工作ID', 'DtlOpenType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlOpenType_CH_2', '流程ID', 'DtlOpenType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlShowModel_CH_0', '表格', 'DtlShowModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlShowModel_CH_1', '卡片', 'DtlShowModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSearchWay_CH_0', '不启用', 'DTSearchWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSearchWay_CH_1', '按日期', 'DTSearchWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSearchWay_CH_2', '按日期时间', 'DTSearchWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSField_CH_0', '字段名相同', 'DTSField', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSField_CH_1', '按设置的字段匹配', 'DTSField', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSField_CH_2', '以上两者都使用', 'DTSField', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSTime_CH_0', '所有的节点发送后', 'DTSTime', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSTime_CH_1', '指定的节点发送后', 'DTSTime', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSTime_CH_2', '当流程结束时', 'DTSTime', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSWay_CH_0', '不同步', 'DTSWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSWay_CH_1', '按照业务表指定的WorkID计算', 'DTSWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSWay_CH_2', '按照业务表主键字段计算', 'DTSWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('EventDoType_CH_0', '禁用', 'EventDoType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('EventDoType_CH_1', '执行存储过程', 'EventDoType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('EventDoType_CH_2', '执行SQL语句', 'EventDoType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('EventDoType_CH_3', '执行URL', 'EventDoType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('EventDoType_CH_4', 'WebServices(未完成)', 'EventDoType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('EventDoType_CH_5', 'exe文件(未完成)', 'EventDoType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('EventDoType_CH_6', 'EventBase类', 'EventDoType', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('FJOpen_CH_0', '关闭附件', 'FJOpen', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FJOpen_CH_1', '操作员', 'FJOpen', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FJOpen_CH_2', '工作ID', 'FJOpen', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FJOpen_CH_3', '流程ID', 'FJOpen', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowAppType_CH_0', '业务流程', 'FlowAppType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowAppType_CH_1', '工程类(项目组流程)', 'FlowAppType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowAppType_CH_2', '公文流程(VSTO)', 'FlowAppType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowRunWay_CH_0', '手工启动', 'FlowRunWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowRunWay_CH_1', '指定人员按时启动', 'FlowRunWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowRunWay_CH_2', '数据集按时启动', 'FlowRunWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowRunWay_CH_3', '触发式启动', 'FlowRunWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FLRole_CH_0', '按接受人', 'FLRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FLRole_CH_1', '按部门', 'FLRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FLRole_CH_2', '按岗位', 'FLRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FormRunType_CH_0', '傻瓜表单', 'FormRunType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FormRunType_CH_1', '自由表单', 'FormRunType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FormRunType_CH_2', '自定义表单', 'FormRunType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FormRunType_CH_4', 'Silverlight表单', 'FormRunType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('FormType_CH_0', '傻瓜表单', 'FormType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FormType_CH_1', '自由表单', 'FormType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FormType_CH_2', '自定义表单', 'FormType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FormType_CH_3', 'SDK表单', 'FormType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FormType_CH_4', 'SL表单(测试版本)', 'FormType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('FormType_CH_5', '表单树', 'FormType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('FormType_CH_9', '禁用(对多表单流程有效)', 'FormType', '9', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_0', '自由表单', 'FrmType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_1', '傻瓜表单', 'FrmType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_2', '自定义表单', 'FrmType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_3', '表单树', 'FrmType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmUrlShowWay_CH_0', '不显示', 'FrmUrlShowWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmUrlShowWay_CH_1', '自动大小', 'FrmUrlShowWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmUrlShowWay_CH_2', '指定大小', 'FrmUrlShowWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmUrlShowWay_CH_3', '新窗口', 'FrmUrlShowWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCAth_CH_0', '不启用', 'FWCAth', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCAth_CH_1', '多附件', 'FWCAth', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCAth_CH_2', '单附件(暂不支持)', 'FWCAth', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCAth_CH_3', '图片附件(暂不支持)', 'FWCAth', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCShowModel_CH_0', '表格方式', 'FWCShowModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCShowModel_CH_1', '自由模式', 'FWCShowModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCShowModel_CH_2', '签章模式', 'FWCShowModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCSta_CH_0', '禁用', 'FWCSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCSta_CH_1', '启用', 'FWCSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCSta_CH_2', '只读', 'FWCSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCType_CH_0', '审核组件', 'FWCType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCType_CH_1', '日志组件', 'FWCType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('HungUpWay_CH_0', '无限挂起', 'HungUpWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('HungUpWay_CH_1', '按指定的时间解除挂起并通知我自己', 'HungUpWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('HungUpWay_CH_2', '按指定的时间解除挂起并通知所有人', 'HungUpWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('IsAdd_CH_0', '是', 'IsAdd', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('IsAdd_CH_1', '否', 'IsAdd', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('IsRatify_CH_0', '否', 'IsRatify', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('IsRatify_CH_1', '是', 'IsRatify', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('IsResidents_oppose_CH_0', '否', 'IsResidents_oppose', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('IsResidents_oppose_CH_1', '是', 'IsResidents_oppose', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('Issucceed_CH_0', '否', 'Issucceed', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('Issucceed_CH_1', '是', 'Issucceed', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('IsUpdate_CH_0', '否', 'IsUpdate', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('IsUpdate_CH_1', '是', 'IsUpdate', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('JinJiChengDu_CH_0', '平件', 'JinJiChengDu', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('JinJiChengDu_CH_1', '紧急', 'JinJiChengDu', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('JMCD_CH_0', '一般', 'JMCD', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('JMCD_CH_1', '保密', 'JMCD', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('JMCD_CH_2', '秘密', 'JMCD', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('JMCD_CH_3', '机密', 'JMCD', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('judge1_CH_0', '是', 'judge1', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('judge1_CH_1', '否', 'judge1', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('judge2_CH_0', '资料全', 'judge2', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('judge2_CH_1', '资料不全', 'judge2', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('judge2_CH_2', '其他', 'judge2', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('judge3_CH_0', '条件A', 'judge3', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('judge3_CH_1', '条件B', 'judge3', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_0', '不能跳转', 'JumpWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_1', '只能向后跳转', 'JumpWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_2', '只能向前跳转', 'JumpWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_3', '任意节点跳转', 'JumpWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_4', '按指定规则跳转', 'JumpWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('LGType_CH_0', '普通', 'LGType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('LGType_CH_1', '枚举', 'LGType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('LGType_CH_2', '外键', 'LGType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_3', '目录', 'MenuType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_4', '功能', 'MenuType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_5', '功能控制点', 'MenuType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('MiMiDengJi_CH_0', '无', 'MiMiDengJi', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MiMiDengJi_CH_1', '普通', 'MiMiDengJi', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MiMiDengJi_CH_2', '秘密', 'MiMiDengJi', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MiMiDengJi_CH_3', '机密', 'MiMiDengJi', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('MiMiDengJi_CH_4', '绝密', 'MiMiDengJi', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('Model_CH_0', '普通', 'Model', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('Model_CH_1', '固定行', 'Model', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MPad_SrcModel_CH_0', '强制横屏', 'MPad_SrcModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MPad_SrcModel_CH_1', '强制竖屏', 'MPad_SrcModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MPad_SrcModel_CH_2', '由重力感应决定', 'MPad_SrcModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MPad_WorkModel_CH_0', '原生态', 'MPad_WorkModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MPad_WorkModel_CH_1', '浏览器', 'MPad_WorkModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MPad_WorkModel_CH_2', '禁用', 'MPad_WorkModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MPhone_SrcModel_CH_0', '强制横屏', 'MPhone_SrcModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MPhone_SrcModel_CH_1', '强制竖屏', 'MPhone_SrcModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MPhone_SrcModel_CH_2', '由重力感应决定', 'MPhone_SrcModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MPhone_WorkModel_CH_0', '原生态', 'MPhone_WorkModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MPhone_WorkModel_CH_1', '浏览器', 'MPhone_WorkModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MPhone_WorkModel_CH_2', '禁用', 'MPhone_WorkModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MsgCtrl_CH_0', '不发送', 'MsgCtrl', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MsgCtrl_CH_1', '按设置的发送范围自动发送', 'MsgCtrl', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MsgCtrl_CH_2', '由本节点表单系统字段(IsSendEmail,IsSendSMS)来决定', 'MsgCtrl', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MsgCtrl_CH_3', '由SDK开发者参数(IsSendEmail,IsSendSMS)来决定', 'MsgCtrl', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_1', '字符串String', 'MyDataType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_2', '整数类型Int', 'MyDataType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_3', '浮点类型AppFloat', 'MyDataType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_4', '判断类型Boolean', 'MyDataType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_5', '双精度类型Double', 'MyDataType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_6', '日期型Date', 'MyDataType', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_7', '时间类型Datetime', 'MyDataType', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_8', '金额类型AppMoney', 'MyDataType', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_0', '傻瓜表单(ccflow6取消支持)', 'NodeFormType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_1', '自由表单', 'NodeFormType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_100', '禁用(对多表单流程有效)', 'NodeFormType', '100', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_2', '自定义表单', 'NodeFormType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_3', 'SDK表单', 'NodeFormType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_4', 'SL表单(ccflow6取消支持)', 'NodeFormType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_5', '表单树', 'NodeFormType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_6', '动态表单树', 'NodeFormType', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_7', '公文表单(WebOffice)', 'NodeFormType', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_8', 'Excel表单(测试中)', 'NodeFormType', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('NodeFormType_CH_9', 'Word表单(测试中)', 'NodeFormType', '9', 'CH');
INSERT INTO `sys_enum` VALUES ('oa_os_kfgh_30meetingIsAdd_CH_0', '否', 'oa_os_kfgh_30meetingIsAdd', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('oa_os_kfgh_30meetingIsAdd_CH_1', '是', 'oa_os_kfgh_30meetingIsAdd', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('OpenWay_CH_0', '新窗口', 'OpenWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('OpenWay_CH_1', '本窗口', 'OpenWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('OpenWay_CH_2', '覆盖新窗口', 'OpenWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('OutTimeDeal_CH_0', '不处理', 'OutTimeDeal', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('OutTimeDeal_CH_1', '自动向下运动', 'OutTimeDeal', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('OutTimeDeal_CH_2', '自动跳转指定的节点', 'OutTimeDeal', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('OutTimeDeal_CH_3', '自动移交给指定的人员', 'OutTimeDeal', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('OutTimeDeal_CH_4', '向指定的人员发消息', 'OutTimeDeal', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('OutTimeDeal_CH_5', '删除流程', 'OutTimeDeal', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('OutTimeDeal_CH_6', '执行SQL', 'OutTimeDeal', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('PopValFormat_CH_0', 'No(仅编号)', 'PopValFormat', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PopValFormat_CH_1', 'Name(仅名称)', 'PopValFormat', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PopValFormat_CH_2', 'No,Name(编号与名称,比如zhangsan,张三;lisi,李四;)', 'PopValFormat', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintDocEnable_CH_0', '不打印', 'PrintDocEnable', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintDocEnable_CH_1', '打印网页', 'PrintDocEnable', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintDocEnable_CH_2', '打印RTF模板', 'PrintDocEnable', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintDocEnable_CH_3', '打印Word模版', 'PrintDocEnable', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('PRI_CH_0', '低', 'PRI', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PRI_CH_1', '中', 'PRI', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PRI_CH_2', '高', 'PRI', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_0', '按照指定节点的工作人员', 'PushWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_1', '按照指定的工作人员', 'PushWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_2', '按照指定的工作岗位', 'PushWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_3', '按照指定的部门', 'PushWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_4', '按照指定的SQL', 'PushWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_5', '按照系统指定的字段', 'PushWay', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadReceipts_CH_0', '不回执', 'ReadReceipts', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadReceipts_CH_1', '自动回执', 'ReadReceipts', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadReceipts_CH_2', '由上一节点表单字段决定', 'ReadReceipts', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadReceipts_CH_3', '由SDK开发者参数决定', 'ReadReceipts', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_0', '不能退回', 'ReturnRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_1', '只能退回上一个节点', 'ReturnRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_2', '可退回以前任意节点', 'ReturnRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_3', '可退回指定的节点', 'ReturnRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_4', '由流程图设计的退回路线决定', 'ReturnRole', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('RunModel_CH_0', '普通', 'RunModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('RunModel_CH_1', '合流', 'RunModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('RunModel_CH_2', '分流', 'RunModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('RunModel_CH_3', '分合流', 'RunModel', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('RunModel_CH_4', '子线程', 'RunModel', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('SaveModel_CH_0', '仅节点表', 'SaveModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SaveModel_CH_1', '节点表与Rpt表', 'SaveModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectAccepterEnable_CH_0', '不启用', 'SelectAccepterEnable', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectAccepterEnable_CH_1', '单独启用', 'SelectAccepterEnable', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectAccepterEnable_CH_2', '在发送前打开', 'SelectAccepterEnable', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectorDBShowWay_CH_0', '表格显示', 'SelectorDBShowWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectorDBShowWay_CH_1', '树形显示', 'SelectorDBShowWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectorModel_CH_0', '按岗位', 'SelectorModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectorModel_CH_1', '按部门', 'SelectorModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectorModel_CH_2', '按人员', 'SelectorModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectorModel_CH_3', '按SQL', 'SelectorModel', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('SelectorModel_CH_4', '自定义Url', 'SelectorModel', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('SFShowModel_CH_0', '表格方式', 'SFShowModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SFShowModel_CH_1', '自由模式', 'SFShowModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SFSta_CH_0', '禁用', 'SFSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SFSta_CH_1', '启用', 'SFSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SFSta_CH_2', '只读', 'SFSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SFTableType_CH_0', 'NoName类型', 'SFTableType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SFTableType_CH_1', 'NoNameTree类型', 'SFTableType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SFTableType_CH_2', 'NoName行政区划类型', 'SFTableType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SharingType_CH_0', '共享', 'SharingType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SharingType_CH_1', '私有', 'SharingType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ShiFouWeiXiuWanCheng_CH_0', '是', 'ShiFouWeiXiuWanCheng', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ShiFouWeiXiuWanCheng_CH_1', '否', 'ShiFouWeiXiuWanCheng', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ShowWhere_CH_0', '树形表单', 'ShowWhere', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ShowWhere_CH_1', '工具栏', 'ShowWhere', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SSOType_CH_0', 'SID验证', 'SSOType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SSOType_CH_1', '连接', 'SSOType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SSOType_CH_2', '表单提交', 'SSOType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SSOType_CH_3', '不传值', 'SSOType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('StaGrade_CH_1', '高层岗', 'StaGrade', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('StaGrade_CH_2', '中层岗', 'StaGrade', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('StaGrade_CH_3', '执行岗', 'StaGrade', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('StartGuideWay_CH_0', '无', 'StartGuideWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('StartGuideWay_CH_1', '按系统的URL-(父子流程)单条模式', 'StartGuideWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('StartGuideWay_CH_10', '按自定义的Url', 'StartGuideWay', '10', 'CH');
INSERT INTO `sys_enum` VALUES ('StartGuideWay_CH_11', '按用户输入参数', 'StartGuideWay', '11', 'CH');
INSERT INTO `sys_enum` VALUES ('StartGuideWay_CH_2', '按系统的URL-(子父流程)多条模式', 'StartGuideWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('StartGuideWay_CH_3', '按系统的URL-(实体记录,未完成)单条模式', 'StartGuideWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('StartGuideWay_CH_4', '按系统的URL-(实体记录,未完成)多条模式', 'StartGuideWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('StartGuideWay_CH_5', '从开始节点Copy数据', 'StartGuideWay', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_0', '不限制', 'StartLimitRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_1', '每人每天一次', 'StartLimitRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_2', '每人每周一次', 'StartLimitRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_3', '每人每月一次', 'StartLimitRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_4', '每人每季一次', 'StartLimitRole', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_5', '每人每年一次', 'StartLimitRole', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_6', '发起的列不能重复,(多个列可以用逗号分开)', 'StartLimitRole', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_7', '设置的SQL数据源为空,或者返回结果为零时可以启动.', 'StartLimitRole', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('StartLimitRole_CH_8', '设置的SQL数据源为空,或者返回结果为零时不可以启动.', 'StartLimitRole', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowCtrlRole_CH_0', '无', 'SubFlowCtrlRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowCtrlRole_CH_1', '不可以删除子流程', 'SubFlowCtrlRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowCtrlRole_CH_2', '可以删除子流程', 'SubFlowCtrlRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowStartWay_CH_0', '不启动', 'SubFlowStartWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowStartWay_CH_1', '指定的字段启动', 'SubFlowStartWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowStartWay_CH_2', '按明细表启动', 'SubFlowStartWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SubThreadType_CH_0', '同表单', 'SubThreadType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SubThreadType_CH_1', '异表单', 'SubThreadType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('Target_CH_0', '新窗口', 'Target', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('Target_CH_1', '本窗口', 'Target', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('Target_CH_2', '父窗口', 'Target', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TaskSta_CH_0', '未开始', 'TaskSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TaskSta_CH_1', '进行中', 'TaskSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TaskSta_CH_2', '完成', 'TaskSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TaskSta_CH_3', '推迟', 'TaskSta', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('TBModel_CH_0', '单行文本', 'TBModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TBModel_CH_1', '多行文本', 'TBModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TBModel_CH_2', '富文本', 'TBModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ThreadKillRole_CH_0', '不能删除', 'ThreadKillRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ThreadKillRole_CH_1', '手工删除', 'ThreadKillRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ThreadKillRole_CH_2', '自动删除', 'ThreadKillRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TimelineRole_CH_0', '按节点(由节点属性来定义)', 'TimelineRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TimelineRole_CH_1', '按发起人(开始节点SysSDTOfFlow字段计算)', 'TimelineRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TodolistModel_CH_0', '抢办模式', 'TodolistModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TodolistModel_CH_1', '协作模式', 'TodolistModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TodolistModel_CH_2', '队列模式', 'TodolistModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TodolistModel_CH_3', '共享模式', 'TodolistModel', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('TurnToDeal_CH_0', '提示ccflow默认信息', 'TurnToDeal', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TurnToDeal_CH_1', '提示指定信息', 'TurnToDeal', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TurnToDeal_CH_2', '转向指定的url', 'TurnToDeal', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TurnToDeal_CH_3', '按照条件转向', 'TurnToDeal', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('WebOfficeEnable_CH_0', '不启用', 'WebOfficeEnable', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WebOfficeEnable_CH_1', '按钮方式', 'WebOfficeEnable', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WebOfficeEnable_CH_2', '标签页方式', 'WebOfficeEnable', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_10', '批处理', 'WFStateApp', '10', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_2', '运行中', 'WFStateApp', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_3', '已完成', 'WFStateApp', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_4', '挂起', 'WFStateApp', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_5', '退回', 'WFStateApp', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_6', '转发', 'WFStateApp', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_7', '删除', 'WFStateApp', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_8', '加签', 'WFStateApp', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_9', '冻结', 'WFStateApp', '9', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_0', '空白', 'WFState', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_1', '草稿', 'WFState', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_10', '批处理', 'WFState', '10', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_2', '运行中', 'WFState', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_3', '已完成', 'WFState', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_4', '挂起', 'WFState', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_5', '退回', 'WFState', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_6', '转发', 'WFState', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_7', '删除', 'WFState', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_8', '加签', 'WFState', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_9', '冻结', 'WFState', '9', 'CH');
INSERT INTO `sys_enum` VALUES ('WFSta_CH_0', '运行中', 'WFSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WFSta_CH_1', '已完成', 'WFSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WFSta_CH_2', '其他', 'WFSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhenNoWorker_CH_0', '提示错误', 'WhenNoWorker', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WhenNoWorker_CH_1', '自动转到下一步', 'WhenNoWorker', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WhenOverSize_CH_0', '不处理', 'WhenOverSize', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WhenOverSize_CH_1', '向下顺增行', 'WhenOverSize', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WhenOverSize_CH_2', '次页显示', 'WhenOverSize', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoExeIt_CH_0', '操作员执行', 'WhoExeIt', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoExeIt_CH_1', '机器执行', 'WhoExeIt', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoExeIt_CH_2', '混合执行', 'WhoExeIt', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoIsPK_CH_0', '工作ID是主键', 'WhoIsPK', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoIsPK_CH_1', '流程ID是主键', 'WhoIsPK', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoIsPK_CH_2', '父流程ID是主键', 'WhoIsPK', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoIsPK_CH_3', '延续流程ID是主键', 'WhoIsPK', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('XB_CH_0', '女', 'XB', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('XB_CH_1', '男', 'XB', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ZhengJianLeiXing_CH_0', '身份证', 'ZhengJianLeiXing', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ZhengJianLeiXing_CH_1', '居住证', 'ZhengJianLeiXing', '1', 'CH');

-- ----------------------------
-- Table structure for sys_enummain
-- ----------------------------
DROP TABLE IF EXISTS `sys_enummain`;
CREATE TABLE `sys_enummain` (
  `No` varchar(40) NOT NULL,
  `Name` varchar(40) DEFAULT NULL,
  `CfgVal` varchar(1500) DEFAULT NULL,
  `Lang` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `Sys_EnumMainID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_enummain
-- ----------------------------
INSERT INTO `sys_enummain` VALUES ('IsAdd', 'IsAdd', '@0=否@1=是', 'CH');
INSERT INTO `sys_enummain` VALUES ('IsRatify', 'IsRatify', '@0=否@1=是', 'CH');
INSERT INTO `sys_enummain` VALUES ('IsResidents_oppose', 'IsResidents_oppose', '@0=否@1=是', 'CH');
INSERT INTO `sys_enummain` VALUES ('Issucceed', 'Issucceed', '@0=否@1=是', 'CH');
INSERT INTO `sys_enummain` VALUES ('IsUpdate', 'IsUpdate', '@0=否@1=是', 'CH');
INSERT INTO `sys_enummain` VALUES ('judge1', 'judge1', '@0=是@1=否', 'CH');
INSERT INTO `sys_enummain` VALUES ('judge2', 'judge2', '@0=资料全@1=资料不全@2=其他', 'CH');
INSERT INTO `sys_enummain` VALUES ('judge3', 'judge3', '@0=条件A@1=条件B', 'CH');
INSERT INTO `sys_enummain` VALUES ('ZhengJianLeiXing', '证件类型', '@0=身份证@1=居住证', 'CH');

-- ----------------------------
-- Table structure for sys_filemanager
-- ----------------------------
DROP TABLE IF EXISTS `sys_filemanager`;
CREATE TABLE `sys_filemanager` (
  `OID` int(11) NOT NULL,
  `AttrFileName` varchar(50) DEFAULT NULL,
  `AttrFileNo` varchar(50) DEFAULT NULL,
  `EnName` varchar(50) DEFAULT NULL,
  `RefVal` varchar(50) DEFAULT NULL,
  `WebPath` varchar(100) DEFAULT NULL,
  `MyFileName` varchar(100) DEFAULT NULL,
  `MyFilePath` varchar(100) DEFAULT NULL,
  `MyFileExt` varchar(10) DEFAULT NULL,
  `MyFileH` int(11) DEFAULT NULL,
  `MyFileW` int(11) DEFAULT NULL,
  `MyFileSize` float DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `Rec` varchar(50) DEFAULT NULL,
  `Doc` text,
  PRIMARY KEY (`OID`),
  KEY `Sys_FileManagerID` (`OID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_filemanager
-- ----------------------------

-- ----------------------------
-- Table structure for sys_formtree
-- ----------------------------
DROP TABLE IF EXISTS `sys_formtree`;
CREATE TABLE `sys_formtree` (
  `No` varchar(10) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `ParentNo` varchar(100) DEFAULT NULL,
  `TreeNo` varchar(100) DEFAULT NULL,
  `IsDir` int(11) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `Sys_FormTreeID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_formtree
-- ----------------------------
INSERT INTO `sys_formtree` VALUES ('0', '表单库', '', '0', '1', '0');
INSERT INTO `sys_formtree` VALUES ('01', '表单类别1', '0', '', '0', '0');

-- ----------------------------
-- Table structure for sys_frmattachment
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmattachment`;
CREATE TABLE `sys_frmattachment` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `NoOfObj` varchar(50) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Exts` varchar(50) DEFAULT NULL,
  `SaveTo` varchar(150) DEFAULT NULL,
  `Sort` varchar(500) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `W` float DEFAULT NULL,
  `H` float DEFAULT NULL,
  `IsUpload` int(11) DEFAULT NULL,
  `IsDelete` int(11) DEFAULT NULL,
  `IsDownload` int(11) DEFAULT NULL,
  `IsOrder` int(11) DEFAULT NULL,
  `IsAutoSize` int(11) DEFAULT NULL,
  `IsNote` int(11) DEFAULT NULL,
  `IsShowTitle` int(11) DEFAULT NULL,
  `UploadType` int(11) DEFAULT NULL,
  `CtrlWay` int(11) DEFAULT NULL,
  `AthUploadWay` int(11) DEFAULT NULL,
  `AtPara` varchar(3000) DEFAULT NULL,
  `RowIdx` int(11) DEFAULT NULL,
  `GroupID` int(11) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmAttachmentID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmattachment
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmattachmentdb
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmattachmentdb`;
CREATE TABLE `sys_frmattachmentdb` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `FK_FrmAttachment` varchar(500) DEFAULT NULL,
  `RefPKVal` varchar(50) DEFAULT NULL,
  `Sort` varchar(200) DEFAULT NULL,
  `FileFullName` varchar(700) DEFAULT NULL,
  `FileName` varchar(500) DEFAULT NULL,
  `FileExts` varchar(50) DEFAULT NULL,
  `FileSize` float DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `Rec` varchar(50) DEFAULT NULL,
  `RecName` varchar(50) DEFAULT NULL,
  `MyNote` text,
  `NodeID` varchar(50) DEFAULT NULL,
  `IsRowLock` int(11) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  `UploadGUID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmAttachmentDBID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmattachmentdb
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmbtn
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmbtn`;
CREATE TABLE `sys_frmbtn` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `Text` text,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `IsView` int(11) DEFAULT NULL,
  `IsEnable` int(11) DEFAULT NULL,
  `BtnType` int(11) DEFAULT NULL,
  `UAC` int(11) DEFAULT NULL,
  `UACContext` text,
  `EventType` int(11) DEFAULT NULL,
  `EventContext` text,
  `MsgOK` varchar(500) DEFAULT NULL,
  `MsgErr` varchar(500) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmBtnID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmbtn
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmele
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmele`;
CREATE TABLE `sys_frmele` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `EleType` varchar(50) DEFAULT NULL,
  `EleID` varchar(50) DEFAULT NULL,
  `EleName` varchar(200) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `H` float DEFAULT NULL,
  `W` float DEFAULT NULL,
  `IsEnable` int(11) DEFAULT NULL,
  `Tag1` varchar(50) DEFAULT NULL,
  `Tag2` varchar(50) DEFAULT NULL,
  `Tag3` varchar(50) DEFAULT NULL,
  `Tag4` varchar(50) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmEleID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmele
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmeledb
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmeledb`;
CREATE TABLE `sys_frmeledb` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `EleID` varchar(50) DEFAULT NULL,
  `RefPKVal` varchar(50) DEFAULT NULL,
  `Tag1` text,
  `Tag2` text,
  `Tag3` text,
  `Tag4` text,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmEleDBID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmeledb
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmevent
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmevent`;
CREATE TABLE `sys_frmevent` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Event` varchar(400) DEFAULT NULL,
  `FK_MapData` varchar(400) DEFAULT NULL,
  `DoType` int(11) DEFAULT NULL,
  `DoDoc` varchar(400) DEFAULT NULL,
  `MsgOK` varchar(400) DEFAULT NULL,
  `MsgError` varchar(400) DEFAULT NULL,
  `MsgCtrl` int(11) DEFAULT NULL,
  `MsgMailEnable` int(11) DEFAULT NULL,
  `MailTitle` varchar(200) DEFAULT NULL,
  `MailDoc` text,
  `SMSEnable` int(11) DEFAULT NULL,
  `SMSDoc` text,
  `MobilePushEnable` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmEventID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmevent
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmimg
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmimg`;
CREATE TABLE `sys_frmimg` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `ImgAppType` int(11) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `H` float DEFAULT NULL,
  `W` float DEFAULT NULL,
  `ImgURL` varchar(200) DEFAULT NULL,
  `ImgPath` varchar(200) DEFAULT NULL,
  `LinkURL` varchar(200) DEFAULT NULL,
  `LinkTarget` varchar(200) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  `Tag0` varchar(500) DEFAULT NULL,
  `SrcType` int(11) DEFAULT NULL,
  `IsEdit` int(11) DEFAULT NULL,
  `Name` varchar(500) DEFAULT NULL,
  `EnPK` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmImgID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmimg
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmimgath
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmimgath`;
CREATE TABLE `sys_frmimgath` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `CtrlID` varchar(200) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `H` float DEFAULT NULL,
  `W` float DEFAULT NULL,
  `IsEdit` int(11) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmImgAthID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmimgath
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmimgathdb
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmimgathdb`;
CREATE TABLE `sys_frmimgathdb` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `FK_FrmImgAth` varchar(50) DEFAULT NULL,
  `RefPKVal` varchar(50) DEFAULT NULL,
  `FileFullName` varchar(700) DEFAULT NULL,
  `FileName` varchar(500) DEFAULT NULL,
  `FileExts` varchar(50) DEFAULT NULL,
  `FileSize` float DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `Rec` varchar(50) DEFAULT NULL,
  `RecName` varchar(50) DEFAULT NULL,
  `MyNote` text,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmImgAthDBID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmimgathdb
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmlab
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmlab`;
CREATE TABLE `sys_frmlab` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `Text` text,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `FontSize` int(11) DEFAULT NULL,
  `FontColor` varchar(50) DEFAULT NULL,
  `FontName` varchar(50) DEFAULT NULL,
  `FontStyle` varchar(50) DEFAULT NULL,
  `FontWeight` varchar(50) DEFAULT NULL,
  `IsBold` int(11) DEFAULT NULL,
  `IsItalic` int(11) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmLabID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmlab
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmline
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmline`;
CREATE TABLE `sys_frmline` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `X1` float DEFAULT NULL,
  `Y1` float DEFAULT NULL,
  `X2` float DEFAULT NULL,
  `Y2` float DEFAULT NULL,
  `BorderWidth` float DEFAULT NULL,
  `BorderColor` varchar(30) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmLineID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmline
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmlink
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmlink`;
CREATE TABLE `sys_frmlink` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `Text` varchar(500) DEFAULT NULL,
  `URL` varchar(500) DEFAULT NULL,
  `Target` varchar(20) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `FontSize` int(11) DEFAULT NULL,
  `FontColor` varchar(50) DEFAULT NULL,
  `FontName` varchar(50) DEFAULT NULL,
  `FontStyle` varchar(50) DEFAULT NULL,
  `IsBold` int(11) DEFAULT NULL,
  `IsItalic` int(11) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmLinkID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmlink
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmrb
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmrb`;
CREATE TABLE `sys_frmrb` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `KeyOfEn` varchar(30) DEFAULT NULL,
  `EnumKey` varchar(30) DEFAULT NULL,
  `Lab` varchar(90) DEFAULT NULL,
  `IntKey` int(11) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmRBID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmrb
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmreportfield
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmreportfield`;
CREATE TABLE `sys_frmreportfield` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `KeyOfEn` varchar(100) DEFAULT NULL,
  `Name` varchar(200) DEFAULT NULL,
  `UIWidth` varchar(100) DEFAULT NULL,
  `UIVisible` int(11) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmRePortFieldID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmreportfield
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmrpt
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmrpt`;
CREATE TABLE `sys_frmrpt` (
  `No` varchar(20) NOT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `PTable` varchar(30) DEFAULT NULL,
  `SQLOfColumn` varchar(300) DEFAULT NULL,
  `SQLOfRow` varchar(300) DEFAULT NULL,
  `RowIdx` int(11) DEFAULT NULL,
  `GroupID` int(11) DEFAULT NULL,
  `IsShowSum` int(11) DEFAULT NULL,
  `IsShowIdx` int(11) DEFAULT NULL,
  `IsCopyNDData` int(11) DEFAULT NULL,
  `IsHLDtl` int(11) DEFAULT NULL,
  `IsReadonly` int(11) DEFAULT NULL,
  `IsShowTitle` int(11) DEFAULT NULL,
  `IsView` int(11) DEFAULT NULL,
  `IsExp` int(11) DEFAULT NULL,
  `IsImp` int(11) DEFAULT NULL,
  `IsInsert` int(11) DEFAULT NULL,
  `IsDelete` int(11) DEFAULT NULL,
  `IsUpdate` int(11) DEFAULT NULL,
  `IsEnablePass` int(11) DEFAULT NULL,
  `IsEnableAthM` int(11) DEFAULT NULL,
  `IsEnableM2M` int(11) DEFAULT NULL,
  `IsEnableM2MM` int(11) DEFAULT NULL,
  `WhenOverSize` int(11) DEFAULT NULL,
  `DtlOpenType` int(11) DEFAULT NULL,
  `DtlShowModel` int(11) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `H` float DEFAULT NULL,
  `W` float DEFAULT NULL,
  `FrmW` float DEFAULT NULL,
  `FrmH` float DEFAULT NULL,
  `MTR` varchar(3000) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `Sys_FrmRptID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmrpt
-- ----------------------------

-- ----------------------------
-- Table structure for sys_frmsln
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmsln`;
CREATE TABLE `sys_frmsln` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Flow` varchar(4) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `FK_MapData` varchar(100) DEFAULT NULL,
  `KeyOfEn` varchar(200) DEFAULT NULL,
  `Name` varchar(500) DEFAULT NULL,
  `EleType` varchar(20) DEFAULT NULL,
  `UIIsEnable` int(11) DEFAULT NULL,
  `UIVisible` int(11) DEFAULT NULL,
  `IsSigan` int(11) DEFAULT NULL,
  `IsNotNull` int(11) DEFAULT NULL,
  `RegularExp` varchar(500) DEFAULT NULL,
  `IsWriteToFlowTable` int(11) DEFAULT NULL,
  `DefVal` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_FrmSlnID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_frmsln
-- ----------------------------

-- ----------------------------
-- Table structure for sys_glovar
-- ----------------------------
DROP TABLE IF EXISTS `sys_glovar`;
CREATE TABLE `sys_glovar` (
  `No` varchar(30) NOT NULL,
  `Name` varchar(120) DEFAULT NULL,
  `Val` varchar(120) DEFAULT NULL,
  `GroupKey` varchar(120) DEFAULT NULL,
  `Note` text,
  PRIMARY KEY (`No`),
  KEY `Sys_GloVarID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_glovar
-- ----------------------------
INSERT INTO `sys_glovar` VALUES ('ColsOfApp', 'SSO界面应用系统列数', '2', 'APP', 'SSO界面应用系统列数。');
INSERT INTO `sys_glovar` VALUES ('ColsOfSSO', 'SSO界面信息列数', '3', 'SSO', 'SSO界面信息列数,单点登陆界面中的列数。');
INSERT INTO `sys_glovar` VALUES ('UnitFullName', '单位全称', '济南驰骋信息技术有限公司', 'Glo', '');
INSERT INTO `sys_glovar` VALUES ('UnitSimpleName', '单位简称', '驰骋软件', 'Glo', '');

-- ----------------------------
-- Table structure for sys_groupenstemplate
-- ----------------------------
DROP TABLE IF EXISTS `sys_groupenstemplate`;
CREATE TABLE `sys_groupenstemplate` (
  `OID` int(11) NOT NULL,
  `EnName` varchar(500) DEFAULT NULL,
  `Name` varchar(500) DEFAULT NULL,
  `EnsName` varchar(90) DEFAULT NULL,
  `OperateCol` varchar(90) DEFAULT NULL,
  `Attrs` varchar(90) DEFAULT NULL,
  `Rec` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `Sys_GroupEnsTemplateID` (`OID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_groupenstemplate
-- ----------------------------

-- ----------------------------
-- Table structure for sys_groupfield
-- ----------------------------
DROP TABLE IF EXISTS `sys_groupfield`;
CREATE TABLE `sys_groupfield` (
  `OID` int(11) NOT NULL,
  `Lab` varchar(500) DEFAULT NULL,
  `EnName` varchar(200) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `Sys_GroupFieldID` (`OID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_groupfield
-- ----------------------------

-- ----------------------------
-- Table structure for sys_m2m
-- ----------------------------
DROP TABLE IF EXISTS `sys_m2m`;
CREATE TABLE `sys_m2m` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(20) DEFAULT NULL,
  `M2MNo` varchar(20) DEFAULT NULL,
  `EnOID` int(11) DEFAULT NULL,
  `DtlObj` varchar(20) DEFAULT NULL,
  `Doc` text,
  `ValsName` text,
  `ValsSQL` text,
  `NumSelected` int(11) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_M2MID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_m2m
-- ----------------------------

-- ----------------------------
-- Table structure for sys_mapattr
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapattr`;
CREATE TABLE `sys_mapattr` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(200) DEFAULT NULL,
  `KeyOfEn` varchar(200) DEFAULT NULL,
  `Name` varchar(200) DEFAULT NULL,
  `DefVal` text,
  `UIContralType` int(11) DEFAULT NULL,
  `MyDataType` int(11) DEFAULT NULL,
  `LGType` int(11) DEFAULT NULL,
  `UIWidth` float DEFAULT NULL,
  `UIHeight` float DEFAULT NULL,
  `MinLen` int(11) DEFAULT NULL,
  `MaxLen` int(11) DEFAULT NULL,
  `UIBindKey` varchar(100) DEFAULT NULL,
  `UIRefKey` varchar(30) DEFAULT NULL,
  `UIRefKeyText` varchar(30) DEFAULT NULL,
  `UIVisible` int(11) DEFAULT NULL,
  `UIIsEnable` int(11) DEFAULT NULL,
  `UIIsLine` int(11) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  `GroupID` int(11) DEFAULT NULL,
  `IsSigan` int(11) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  `Tag` varchar(100) DEFAULT NULL,
  `EditType` int(11) DEFAULT NULL,
  `ColSpan` int(11) DEFAULT NULL,
  `AtPara` text,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_MapAttrID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_mapattr
-- ----------------------------
INSERT INTO `sys_mapattr` VALUES ('112232143_OID', '112232143', 'OID', 'OID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '0', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('112232143_RDT', '112232143', 'RDT', '更新时间', '@RDT', '0', '7', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '0', '0', '5', '5', '', '1', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FK_Dept', 'ND10MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FK_NY', 'ND10MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FlowDaySpan', 'ND10MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FlowEmps', 'ND10MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FlowEnder', 'ND10MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FlowEnderRDT', 'ND10MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FlowEndNode', 'ND10MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FlowStarter', 'ND10MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_FlowStartRDT', 'ND10MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_OID', 'ND10MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1471', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_Title', 'ND10MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1471', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_WFSta', 'ND10MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND10MyRpt_WFState', 'ND10MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1473', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FK_Dept', 'ND11MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FK_NY', 'ND11MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FlowDaySpan', 'ND11MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FlowEmps', 'ND11MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FlowEnder', 'ND11MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FlowEnderRDT', 'ND11MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FlowEndNode', 'ND11MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FlowStarter', 'ND11MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_FlowStartRDT', 'ND11MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_OID', 'ND11MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1487', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_Title', 'ND11MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1487', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_WFSta', 'ND11MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND11MyRpt_WFState', 'ND11MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1489', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FK_Dept', 'ND12MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FK_NY', 'ND12MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FlowDaySpan', 'ND12MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FlowEmps', 'ND12MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FlowEnder', 'ND12MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FlowEnderRDT', 'ND12MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FlowEndNode', 'ND12MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FlowStarter', 'ND12MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_FlowStartRDT', 'ND12MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_OID', 'ND12MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1503', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_Title', 'ND12MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1503', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_WFSta', 'ND12MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND12MyRpt_WFState', 'ND12MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1505', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FK_Dept', 'ND13MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FK_NY', 'ND13MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FlowDaySpan', 'ND13MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FlowEmps', 'ND13MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FlowEnder', 'ND13MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FlowEnderRDT', 'ND13MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FlowEndNode', 'ND13MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FlowStarter', 'ND13MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_FlowStartRDT', 'ND13MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_OID', 'ND13MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1507', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_Title', 'ND13MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1507', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_WFSta', 'ND13MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND13MyRpt_WFState', 'ND13MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1509', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FK_Dept', 'ND14MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FK_NY', 'ND14MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FlowDaySpan', 'ND14MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FlowEmps', 'ND14MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FlowEnder', 'ND14MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FlowEnderRDT', 'ND14MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FlowEndNode', 'ND14MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FlowStarter', 'ND14MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_FlowStartRDT', 'ND14MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_OID', 'ND14MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1511', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_Title', 'ND14MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1511', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_WFSta', 'ND14MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND14MyRpt_WFState', 'ND14MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1513', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FK_Dept', 'ND15MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FK_NY', 'ND15MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FlowDaySpan', 'ND15MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FlowEmps', 'ND15MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FlowEnder', 'ND15MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FlowEnderRDT', 'ND15MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FlowEndNode', 'ND15MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FlowStarter', 'ND15MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_FlowStartRDT', 'ND15MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_OID', 'ND15MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1542', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_Title', 'ND15MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1542', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_WFSta', 'ND15MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND15MyRpt_WFState', 'ND15MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1544', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FK_Dept', 'ND16MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FK_NY', 'ND16MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FlowDaySpan', 'ND16MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FlowEmps', 'ND16MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FlowEnder', 'ND16MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FlowEnderRDT', 'ND16MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FlowEndNode', 'ND16MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FlowStarter', 'ND16MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_FlowStartRDT', 'ND16MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_OID', 'ND16MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1546', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_Title', 'ND16MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1546', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_WFSta', 'ND16MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND16MyRpt_WFState', 'ND16MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1548', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FK_Dept', 'ND1MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FK_NY', 'ND1MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FlowDaySpan', 'ND1MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FlowEmps', 'ND1MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FlowEnder', 'ND1MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FlowEnderRDT', 'ND1MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FlowEndNode', 'ND1MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FlowStarter', 'ND1MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_FlowStartRDT', 'ND1MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_OID', 'ND1MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '635', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_Title', 'ND1MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '635', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_WFSta', 'ND1MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND1MyRpt_WFState', 'ND1MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '637', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FK_Dept', 'ND2MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FK_NY', 'ND2MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FlowDaySpan', 'ND2MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FlowEmps', 'ND2MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FlowEnder', 'ND2MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FlowEnderRDT', 'ND2MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FlowEndNode', 'ND2MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FlowStarter', 'ND2MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_FlowStartRDT', 'ND2MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_OID', 'ND2MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '643', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_Title', 'ND2MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '643', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_WFSta', 'ND2MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND2MyRpt_WFState', 'ND2MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '645', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FK_Dept', 'ND3MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FK_NY', 'ND3MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FlowDaySpan', 'ND3MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FlowEmps', 'ND3MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FlowEnder', 'ND3MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FlowEnderRDT', 'ND3MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FlowEndNode', 'ND3MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FlowStarter', 'ND3MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_FlowStartRDT', 'ND3MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_OID', 'ND3MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '647', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_Title', 'ND3MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '647', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_WFSta', 'ND3MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND3MyRpt_WFState', 'ND3MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '649', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FK_Dept', 'ND4MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FK_NY', 'ND4MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FlowDaySpan', 'ND4MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FlowEmps', 'ND4MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FlowEnder', 'ND4MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FlowEnderRDT', 'ND4MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FlowEndNode', 'ND4MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FlowStarter', 'ND4MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_FlowStartRDT', 'ND4MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_OID', 'ND4MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '712', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_Title', 'ND4MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '712', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_WFSta', 'ND4MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND4MyRpt_WFState', 'ND4MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '714', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FK_Dept', 'ND5MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FK_NY', 'ND5MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FlowDaySpan', 'ND5MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FlowEmps', 'ND5MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FlowEnder', 'ND5MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FlowEnderRDT', 'ND5MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FlowEndNode', 'ND5MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FlowStarter', 'ND5MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_FlowStartRDT', 'ND5MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_OID', 'ND5MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1010', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_Title', 'ND5MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1010', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_WFSta', 'ND5MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND5MyRpt_WFState', 'ND5MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1012', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FK_Dept', 'ND6MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FK_NY', 'ND6MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FlowDaySpan', 'ND6MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FlowEmps', 'ND6MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FlowEnder', 'ND6MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FlowEnderRDT', 'ND6MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FlowEndNode', 'ND6MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FlowStarter', 'ND6MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_FlowStartRDT', 'ND6MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_OID', 'ND6MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1014', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_Title', 'ND6MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1014', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_WFSta', 'ND6MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND6MyRpt_WFState', 'ND6MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1016', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FK_Dept', 'ND7MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FK_NY', 'ND7MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FlowDaySpan', 'ND7MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FlowEmps', 'ND7MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FlowEnder', 'ND7MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FlowEnderRDT', 'ND7MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FlowEndNode', 'ND7MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FlowStarter', 'ND7MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_FlowStartRDT', 'ND7MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_OID', 'ND7MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1390', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_Title', 'ND7MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1390', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_WFSta', 'ND7MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND7MyRpt_WFState', 'ND7MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1392', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FK_Dept', 'ND8MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FK_NY', 'ND8MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FlowDaySpan', 'ND8MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FlowEmps', 'ND8MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FlowEnder', 'ND8MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FlowEnderRDT', 'ND8MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FlowEndNode', 'ND8MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FlowStarter', 'ND8MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_FlowStartRDT', 'ND8MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_OID', 'ND8MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1394', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_Title', 'ND8MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1394', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_WFSta', 'ND8MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND8MyRpt_WFState', 'ND8MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1396', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FK_Dept', 'ND9MyRpt', 'FK_Dept', '操作员部门', '', '1', '1', '2', '100', '23', '0', '100', 'BP.Port.Depts', '', '', '1', '0', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FK_NY', 'ND9MyRpt', 'FK_NY', '年月', '', '1', '1', '2', '100', '23', '0', '7', 'BP.Pub.NYs', '', '', '1', '0', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FlowDaySpan', 'ND9MyRpt', 'FlowDaySpan', '跨度(天)', '', '0', '8', '0', '100', '23', '0', '300', '', '', '', '1', '1', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FlowEmps', 'ND9MyRpt', 'FlowEmps', '参与人', '', '0', '1', '0', '100', '23', '0', '1000', '', '', '', '1', '0', '1', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FlowEnder', 'ND9MyRpt', 'FlowEnder', '结束人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FlowEnderRDT', 'ND9MyRpt', 'FlowEnderRDT', '结束时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FlowEndNode', 'ND9MyRpt', 'FlowEndNode', '结束节点', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FlowStarter', 'ND9MyRpt', 'FlowStarter', '发起人', '', '1', '1', '2', '100', '23', '0', '20', 'BP.Port.Emps', '', '', '1', '0', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_FlowStartRDT', 'ND9MyRpt', 'FlowStartRDT', '发起时间', '', '0', '7', '0', '100', '23', '0', '300', '', '', '', '1', '0', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_OID', 'ND9MyRpt', 'OID', 'WorkID', '0', '0', '2', '0', '100', '23', '0', '300', '', '', '', '0', '0', '0', '999', '1461', '0', '5', '5', '', '', '2', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_Title', 'ND9MyRpt', 'Title', '标题', '', '0', '1', '0', '251', '23', '0', '200', '', '', '', '0', '0', '1', '999', '1461', '0', '174.83', '54.4', '', '', '0', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_WFSta', 'ND9MyRpt', 'WFSta', '状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFSta', '', '', '1', '0', '0', '999', '0', '0', '5', '5', '', '', '1', '1', '');
INSERT INTO `sys_mapattr` VALUES ('ND9MyRpt_WFState', 'ND9MyRpt', 'WFState', '流程状态', '', '1', '2', '1', '100', '23', '0', '1000', 'WFState', '', '', '1', '0', '0', '999', '1463', '0', '5', '5', '', '', '1', '1', '');

-- ----------------------------
-- Table structure for sys_mapdata
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapdata`;
CREATE TABLE `sys_mapdata` (
  `No` varchar(20) NOT NULL,
  `Name` varchar(500) DEFAULT NULL,
  `EnPK` varchar(10) DEFAULT NULL,
  `PTable` varchar(500) DEFAULT NULL,
  `Url` varchar(500) DEFAULT NULL,
  `Dtls` varchar(500) DEFAULT NULL,
  `FrmW` int(11) DEFAULT NULL,
  `FrmH` int(11) DEFAULT NULL,
  `TableCol` int(11) DEFAULT NULL,
  `TableWidth` int(11) DEFAULT NULL,
  `DBURL` int(11) DEFAULT NULL,
  `Tag` varchar(500) DEFAULT NULL,
  `FrmType` int(11) DEFAULT NULL,
  `FK_FrmSort` varchar(500) DEFAULT NULL,
  `FK_FormTree` varchar(500) DEFAULT NULL,
  `AppType` int(11) DEFAULT NULL,
  `Note` varchar(500) DEFAULT NULL,
  `Designer` varchar(500) DEFAULT NULL,
  `DesignerUnit` varchar(500) DEFAULT NULL,
  `DesignerContact` varchar(500) DEFAULT NULL,
  `AtPara` text,
  `Idx` int(11) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  `Ver` varchar(30) DEFAULT NULL,
  `OfficeOpenLab` varchar(50) DEFAULT '打开本地',
  `OfficeOpenEnable` int(11) DEFAULT '0',
  `OfficeOpenTemplateLab` varchar(50) DEFAULT '打开模板',
  `OfficeOpenTemplateEnable` int(11) DEFAULT '0',
  `OfficeSaveLab` varchar(50) DEFAULT '保存',
  `OfficeSaveEnable` int(11) DEFAULT '1',
  `OfficeAcceptLab` varchar(50) DEFAULT '接受修订',
  `OfficeAcceptEnable` int(11) DEFAULT '0',
  `OfficeRefuseLab` varchar(50) DEFAULT '拒绝修订',
  `OfficeRefuseEnable` int(11) DEFAULT '0',
  `OfficeOverLab` varchar(50) DEFAULT '套红按钮',
  `OfficeOverEnable` int(11) DEFAULT '0',
  `OfficeMarksEnable` int(11) DEFAULT '1',
  `OfficePrintLab` varchar(50) DEFAULT '打印按钮',
  `OfficePrintEnable` int(11) DEFAULT '0',
  `OfficeSealLab` varchar(50) DEFAULT '签章按钮',
  `OfficeSealEnable` int(11) DEFAULT '0',
  `OfficeInsertFlowLab` varchar(50) DEFAULT '插入流程',
  `OfficeInsertFlowEnable` int(11) DEFAULT '0',
  `OfficeNodeInfo` int(11) DEFAULT '0',
  `OfficeReSavePDF` int(11) DEFAULT '0',
  `OfficeDownLab` varchar(50) DEFAULT '下载',
  `OfficeDownEnable` int(11) DEFAULT '0',
  `OfficeIsMarks` int(11) DEFAULT '1',
  `OfficeTemplate` varchar(100) DEFAULT '',
  `OfficeIsParent` int(11) DEFAULT '1',
  `OfficeTHEnable` int(11) DEFAULT '0',
  `OfficeTHTemplate` varchar(200) DEFAULT '',
  `FormRunType` int(11) DEFAULT '1',
  `FK_Flow` varchar(50) DEFAULT NULL,
  `ParentMapData` varchar(128) DEFAULT '',
  `RightViewWay` int(11) DEFAULT '0',
  `RightViewTag` text,
  `RightDeptWay` int(11) DEFAULT '0',
  `RightDeptTag` text,
  PRIMARY KEY (`No`),
  KEY `Sys_MapDataID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_mapdata
-- ----------------------------
INSERT INTO `sys_mapdata` VALUES ('ND10MyRpt', '订饭流程报表', '', 'ND10Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-11-14 10:56:10', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '010', 'ND10Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND11MyRpt', '通通通通通通知报表', '', 'ND11Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-11-17 16:41:47', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '011', 'ND11Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND12MyRpt', '企业用户审核报表', '', 'ND12Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-11-24 14:19:55', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '012', 'ND12Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND13MyRpt', '企业帮扶报表', '', 'ND13Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-11-24 14:24:42', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '013', 'ND13Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND14MyRpt', 'test报表', '', 'ND14Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-11-25 15:21:36', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '014', 'ND14Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND15MyRpt', '金融机构申请流程报表', '', 'ND15Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-12-19 16:57:15', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '015', 'ND15Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND16MyRpt', '贴息申请流程报表', '', 'ND16Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-12-20 09:23:07', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '016', 'ND16Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND1MyRpt', '订单报表', '', 'ND1Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-02-22 12:05:32', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '001', 'ND1Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND2MyRpt', 'test报表', '', 'ND2Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-02-22 17:41:00', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '002', 'ND2Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND3MyRpt', '差旅费报销报表', '', 'ND3Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-02-23 16:16:03', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '003', 'ND3Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND4MyRpt', '内部办文报表', '', 'ND4Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-10-12 18:14:19', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '004', 'ND4Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND5MyRpt', '常委会周表报表', '', 'ND5Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-10-17 17:28:42', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '005', 'ND5Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND6MyRpt', '委办周表报表', '', 'ND6Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-10-17 17:33:26', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '006', 'ND6Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND7MyRpt', '来问登记子流程报表', '', 'ND7Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-11-02 14:21:44', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '007', 'ND7Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND8MyRpt', '通知公告子流程报表', '', 'ND8Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-11-02 14:41:07', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '008', 'ND8Rpt', '0', '', '0', '');
INSERT INTO `sys_mapdata` VALUES ('ND9MyRpt', '公告通知测试报表', '', 'ND9Rpt', '', '', '900', '1200', '4', '600', '0', '', '0', '', '', '1', '默认.', '', '', '', '@IsHaveCA=0', '100', '', '2016-11-09 09:52:28', '打开本地', '0', '打开模板', '0', '保存', '1', '接受修订', '0', '拒绝修订', '0', '套红按钮', '0', '1', '打印按钮', '0', '签章按钮', '0', '插入流程', '0', '0', '0', '下载', '0', '1', '', '1', '0', '', '1', '009', 'ND9Rpt', '0', '', '0', '');

-- ----------------------------
-- Table structure for sys_mapdtl
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapdtl`;
CREATE TABLE `sys_mapdtl` (
  `No` varchar(80) NOT NULL,
  `Name` varchar(200) DEFAULT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `PTable` varchar(200) DEFAULT NULL,
  `GroupField` varchar(300) DEFAULT NULL,
  `Model` int(11) DEFAULT NULL,
  `ImpFixTreeSql` varchar(500) DEFAULT NULL,
  `ImpFixDataSql` varchar(500) DEFAULT NULL,
  `RowIdx` int(11) DEFAULT NULL,
  `GroupID` int(11) DEFAULT NULL,
  `RowsOfList` int(11) DEFAULT NULL,
  `IsEnableGroupField` int(11) DEFAULT NULL,
  `IsShowSum` int(11) DEFAULT NULL,
  `IsShowIdx` int(11) DEFAULT NULL,
  `IsCopyNDData` int(11) DEFAULT NULL,
  `IsHLDtl` int(11) DEFAULT NULL,
  `IsReadonly` int(11) DEFAULT NULL,
  `IsShowTitle` int(11) DEFAULT NULL,
  `IsView` int(11) DEFAULT NULL,
  `IsInsert` int(11) DEFAULT NULL,
  `IsDelete` int(11) DEFAULT NULL,
  `IsUpdate` int(11) DEFAULT NULL,
  `IsEnablePass` int(11) DEFAULT NULL,
  `IsEnableAthM` int(11) DEFAULT NULL,
  `IsEnableM2M` int(11) DEFAULT NULL,
  `IsEnableM2MM` int(11) DEFAULT NULL,
  `WhenOverSize` int(11) DEFAULT NULL,
  `DtlOpenType` int(11) DEFAULT NULL,
  `DtlShowModel` int(11) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `H` float DEFAULT NULL,
  `W` float DEFAULT NULL,
  `FrmW` float DEFAULT NULL,
  `FrmH` float DEFAULT NULL,
  `MTR` varchar(3000) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `AtPara` varchar(300) DEFAULT NULL,
  `IsExp` int(11) DEFAULT NULL,
  `IsImp` int(11) DEFAULT NULL,
  `IsEnableSelectImp` int(11) DEFAULT NULL,
  `ImpSQLSearch` varchar(500) DEFAULT NULL,
  `ImpSQLInit` varchar(500) DEFAULT NULL,
  `ImpSQLFull` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `Sys_MapDtlID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_mapdtl
-- ----------------------------

-- ----------------------------
-- Table structure for sys_mapext
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapext`;
CREATE TABLE `sys_mapext` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `ExtType` varchar(30) DEFAULT NULL,
  `DoWay` int(11) DEFAULT NULL,
  `AttrOfOper` varchar(30) DEFAULT NULL,
  `AttrsOfActive` varchar(900) DEFAULT NULL,
  `Doc` text,
  `Tag` varchar(2000) DEFAULT NULL,
  `Tag1` varchar(2000) DEFAULT NULL,
  `Tag2` varchar(2000) DEFAULT NULL,
  `Tag3` varchar(2000) DEFAULT NULL,
  `AtPara` varchar(2000) DEFAULT NULL,
  `DBSrc` varchar(20) DEFAULT NULL,
  `H` int(11) DEFAULT NULL,
  `W` int(11) DEFAULT NULL,
  `PRI` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_MapExtID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_mapext
-- ----------------------------

-- ----------------------------
-- Table structure for sys_mapframe
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapframe`;
CREATE TABLE `sys_mapframe` (
  `MyPK` varchar(100) NOT NULL,
  `NoOfObj` varchar(20) DEFAULT NULL,
  `Name` varchar(200) DEFAULT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `URL` varchar(3000) DEFAULT NULL,
  `W` varchar(20) DEFAULT NULL,
  `H` varchar(20) DEFAULT NULL,
  `IsAutoSize` int(11) DEFAULT NULL,
  `RowIdx` int(11) DEFAULT NULL,
  `GroupID` int(11) DEFAULT NULL,
  `GUID` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_MapFrameID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_mapframe
-- ----------------------------

-- ----------------------------
-- Table structure for sys_mapm2m
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapm2m`;
CREATE TABLE `sys_mapm2m` (
  `MyPK` varchar(100) NOT NULL,
  `FK_MapData` varchar(30) DEFAULT NULL,
  `NoOfObj` varchar(20) DEFAULT NULL,
  `Name` varchar(200) DEFAULT NULL,
  `DBOfLists` text,
  `DBOfObjs` text,
  `DBOfGroups` text,
  `H` float DEFAULT NULL,
  `W` float DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  `ShowWay` int(11) DEFAULT NULL,
  `M2MType` int(11) DEFAULT NULL,
  `RowIdx` int(11) DEFAULT NULL,
  `GroupID` int(11) DEFAULT NULL,
  `Cols` int(11) DEFAULT NULL,
  `IsDelete` int(11) DEFAULT NULL,
  `IsInsert` int(11) DEFAULT NULL,
  `IsCheckAll` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_MapM2MID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_mapm2m
-- ----------------------------

-- ----------------------------
-- Table structure for sys_operation_manual
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_manual`;
CREATE TABLE `sys_operation_manual` (
  `ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `PID` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `TITLE` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作名称',
  `COMMENT` text COLLATE utf8_bin COMMENT '操作内容',
  `ATTACHMENT` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '说明附件',
  `NUM` int(3) DEFAULT NULL COMMENT '顺序',
  `CREATDATE` datetime DEFAULT NULL,
  `LAST_EIDT_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of sys_operation_manual
-- ----------------------------
INSERT INTO `sys_operation_manual` VALUES ('1', 'null', '表单配置', 0x31, '', '1', '2017-05-12 16:31:54', null);
INSERT INTO `sys_operation_manual` VALUES ('33d60c7d-e7a0-4a8a-b31d-b5a4b2f24235', '', '流程配置', null, null, '3', '2017-05-12 16:42:54', null);
INSERT INTO `sys_operation_manual` VALUES ('36706c73-e6af-469a-ad7b-9a9e75b6f8db', '', '列表配置', null, null, '2', '2017-05-12 16:41:28', null);
INSERT INTO `sys_operation_manual` VALUES ('e829c7d0-48b7-47e2-96b7-5fbea77f4325', '', '组件介绍', null, null, '4', '2017-05-12 16:44:38', null);

-- ----------------------------
-- Table structure for sys_questionandanswer
-- ----------------------------
DROP TABLE IF EXISTS `sys_questionandanswer`;
CREATE TABLE `sys_questionandanswer` (
  `ID` varchar(40) COLLATE utf8_bin NOT NULL,
  `TITLE` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SOLUTION` text COLLATE utf8_bin,
  `TYPE` int(5) DEFAULT NULL,
  `LEVEL` int(3) DEFAULT NULL,
  `CLICK_COUNT` int(10) DEFAULT NULL,
  `CREATTIME` datetime DEFAULT NULL,
  `LAST_EDIT_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of sys_questionandanswer
-- ----------------------------

-- ----------------------------
-- Table structure for sys_rptdept
-- ----------------------------
DROP TABLE IF EXISTS `sys_rptdept`;
CREATE TABLE `sys_rptdept` (
  `FK_Rpt` varchar(15) NOT NULL,
  `FK_Dept` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Rpt`,`FK_Dept`),
  KEY `Sys_RptDeptID` (`FK_Rpt`,`FK_Dept`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rptdept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_rptemp
-- ----------------------------
DROP TABLE IF EXISTS `sys_rptemp`;
CREATE TABLE `sys_rptemp` (
  `FK_Rpt` varchar(15) NOT NULL,
  `FK_Emp` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Rpt`,`FK_Emp`),
  KEY `Sys_RptEmpID` (`FK_Rpt`,`FK_Emp`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rptemp
-- ----------------------------

-- ----------------------------
-- Table structure for sys_rptstation
-- ----------------------------
DROP TABLE IF EXISTS `sys_rptstation`;
CREATE TABLE `sys_rptstation` (
  `FK_Rpt` varchar(15) NOT NULL,
  `FK_Station` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Rpt`,`FK_Station`),
  KEY `Sys_RptStationID` (`FK_Rpt`,`FK_Station`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rptstation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_rpttemplate
-- ----------------------------
DROP TABLE IF EXISTS `sys_rpttemplate`;
CREATE TABLE `sys_rpttemplate` (
  `MyPK` varchar(100) NOT NULL,
  `EnsName` varchar(500) DEFAULT NULL,
  `FK_Emp` varchar(20) DEFAULT NULL,
  `D1` varchar(90) DEFAULT NULL,
  `D2` varchar(90) DEFAULT NULL,
  `D3` varchar(90) DEFAULT NULL,
  `AlObjs` varchar(90) DEFAULT NULL,
  `Height` int(11) DEFAULT NULL,
  `Width` int(11) DEFAULT NULL,
  `IsSumBig` int(11) DEFAULT NULL,
  `IsSumLittle` int(11) DEFAULT NULL,
  `IsSumRight` int(11) DEFAULT NULL,
  `PercentModel` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_RptTemplateID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rpttemplate
-- ----------------------------

-- ----------------------------
-- Table structure for sys_seq
-- ----------------------------
DROP TABLE IF EXISTS `sys_seq`;
CREATE TABLE `sys_seq` (
  `SEQ_CODE` varchar(30) NOT NULL,
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_TYPE` smallint(6) NOT NULL DEFAULT '0' COMMENT '0 - 系统流水号，提供本系统内部所使用，不允许用户随便更改；\n            1 - 外部流水号，供用户对一些必要参数的设置，用户流水号的维护统一由用户自身指定和使用，例如收发文流水号等。',
  `SEQ_VALUE` int(11) NOT NULL COMMENT '流水号值初始为1，以+1的速度递增',
  PRIMARY KEY (`SEQ_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统流水号，存储系统或用户定义的流水号';

-- ----------------------------
-- Records of sys_seq
-- ----------------------------

-- ----------------------------
-- Table structure for sys_serial
-- ----------------------------
DROP TABLE IF EXISTS `sys_serial`;
CREATE TABLE `sys_serial` (
  `CfgKey` varchar(100) NOT NULL,
  `IntVal` int(11) DEFAULT NULL,
  PRIMARY KEY (`CfgKey`),
  KEY `Sys_SerialID` (`CfgKey`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_serial
-- ----------------------------
INSERT INTO `sys_serial` VALUES ('BP.GPM.Menu', '105');
INSERT INTO `sys_serial` VALUES ('BP.WF.Template.FlowSort', '106');
INSERT INTO `sys_serial` VALUES ('OID', '1619');
INSERT INTO `sys_serial` VALUES ('Ver', '201505302');
INSERT INTO `sys_serial` VALUES ('WorkID', '7168');

-- ----------------------------
-- Table structure for sys_sfdbsrc
-- ----------------------------
DROP TABLE IF EXISTS `sys_sfdbsrc`;
CREATE TABLE `sys_sfdbsrc` (
  `No` varchar(20) NOT NULL,
  `Name` varchar(30) DEFAULT NULL,
  `UserID` varchar(30) DEFAULT NULL,
  `Password` varchar(30) DEFAULT NULL,
  `IP` varchar(50) DEFAULT NULL,
  `DBName` varchar(30) DEFAULT NULL,
  `DBSrcType` int(11) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `Sys_SFDBSrcID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_sfdbsrc
-- ----------------------------
INSERT INTO `sys_sfdbsrc` VALUES ('local', '应用系统主数据库(默认)', '', '', '', '', '0');

-- ----------------------------
-- Table structure for sys_sftable
-- ----------------------------
DROP TABLE IF EXISTS `sys_sftable`;
CREATE TABLE `sys_sftable` (
  `No` varchar(20) NOT NULL,
  `Name` varchar(30) DEFAULT NULL,
  `FK_Val` varchar(50) DEFAULT NULL,
  `TableDesc` varchar(50) DEFAULT NULL,
  `DefVal` varchar(200) DEFAULT NULL,
  `IsTree` int(11) DEFAULT NULL,
  `SFTableType` int(11) DEFAULT NULL,
  `FK_SFDBSrc` varchar(100) DEFAULT NULL,
  `SrcTable` varchar(50) DEFAULT NULL,
  `ColumnValue` varchar(50) DEFAULT NULL,
  `ColumnText` varchar(50) DEFAULT NULL,
  `ParentValue` varchar(50) DEFAULT NULL,
  `SelectStatement` varchar(1000) DEFAULT NULL,
  `SrcType` int(11) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `Sys_SFTableID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_sftable
-- ----------------------------

-- ----------------------------
-- Table structure for sys_sms
-- ----------------------------
DROP TABLE IF EXISTS `sys_sms`;
CREATE TABLE `sys_sms` (
  `MyPK` varchar(100) NOT NULL,
  `Sender` varchar(200) DEFAULT NULL,
  `SendTo` varchar(200) DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `Mobile` varchar(30) DEFAULT NULL,
  `MobileSta` int(11) DEFAULT NULL,
  `MobileInfo` varchar(1000) DEFAULT NULL,
  `Email` varchar(200) DEFAULT NULL,
  `EmaiSta` int(11) DEFAULT NULL,
  `EmailTitle` varchar(3000) DEFAULT NULL,
  `EmailDoc` text,
  `SendDT` varchar(50) DEFAULT NULL,
  `IsRead` int(11) DEFAULT NULL,
  `IsAlert` int(11) DEFAULT NULL,
  `MsgFlag` varchar(200) DEFAULT NULL,
  `MsgType` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_SMSID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_sms
-- ----------------------------

-- ----------------------------
-- Table structure for sys_userlogt
-- ----------------------------
DROP TABLE IF EXISTS `sys_userlogt`;
CREATE TABLE `sys_userlogt` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Emp` varchar(100) DEFAULT NULL,
  `IP` varchar(200) DEFAULT NULL,
  `LogFlag` varchar(300) DEFAULT NULL,
  `Docs` varchar(300) DEFAULT NULL,
  `RDT` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_UserLogTID` (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_userlogt
-- ----------------------------
INSERT INTO `sys_userlogt` VALUES ('bba9a89921f742d58f8eafc2643c7c2a', '10001', '192.168.1.50', 'SignIn', '登录', '2017-12-19 23:47:38');

-- ----------------------------
-- Table structure for sys_userregedit
-- ----------------------------
DROP TABLE IF EXISTS `sys_userregedit`;
CREATE TABLE `sys_userregedit` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Emp` varchar(30) DEFAULT NULL,
  `CfgKey` varchar(200) DEFAULT NULL,
  `Vals` varchar(2000) DEFAULT NULL,
  `GenerSQL` varchar(2000) DEFAULT NULL,
  `Paras` varchar(2000) DEFAULT NULL,
  `NumKey` varchar(300) DEFAULT NULL,
  `OrderBy` varchar(300) DEFAULT NULL,
  `OrderWay` varchar(300) DEFAULT NULL,
  `SearchKey` varchar(300) DEFAULT NULL,
  `MVals` varchar(300) DEFAULT NULL,
  `IsPic` int(11) DEFAULT NULL,
  `DTFrom` varchar(20) DEFAULT NULL,
  `DTTo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_UserRegeditID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_userregedit
-- ----------------------------

-- ----------------------------
-- Table structure for sys_uservar
-- ----------------------------
DROP TABLE IF EXISTS `sys_uservar`;
CREATE TABLE `sys_uservar` (
  `No` varchar(30) NOT NULL,
  `Name` varchar(120) DEFAULT NULL,
  `Val` varchar(120) DEFAULT NULL,
  `GroupKey` varchar(120) DEFAULT NULL,
  `Note` text,
  PRIMARY KEY (`No`),
  KEY `Sys_UserVarID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_uservar
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wfsealdata
-- ----------------------------
DROP TABLE IF EXISTS `sys_wfsealdata`;
CREATE TABLE `sys_wfsealdata` (
  `MyPK` varchar(100) NOT NULL,
  `OID` varchar(200) DEFAULT NULL,
  `FK_Node` varchar(200) DEFAULT NULL,
  `FK_MapData` varchar(300) DEFAULT NULL,
  `SealData` text,
  `RDT` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `Sys_WFSealDataID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_wfsealdata
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_article
-- ----------------------------
DROP TABLE IF EXISTS `wechat_article`;
CREATE TABLE `wechat_article` (
  `ID` varchar(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '图文消息标题',
  `description` varchar(1024) DEFAULT NULL COMMENT '图文消息描述',
  `picUrl` varchar(255) DEFAULT NULL COMMENT '图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200',
  `url` varchar(255) DEFAULT NULL COMMENT '点击图文消息跳转链接',
  `fkID` varchar(36) DEFAULT NULL COMMENT '关联其他表(wechat_subscribe_reply)',
  `orderNum` tinyint(4) DEFAULT NULL COMMENT '多图文消息时,多个图文的排序',
  `isMore` varchar(1) DEFAULT '0' COMMENT '1为多图文消息,0为单图文消息',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of wechat_article
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_keyword_reply
-- ----------------------------
DROP TABLE IF EXISTS `wechat_keyword_reply`;
CREATE TABLE `wechat_keyword_reply` (
  `ID` varchar(36) NOT NULL,
  `keyword` varchar(50) DEFAULT NULL COMMENT '关键字名',
  `replyType` varchar(2) DEFAULT NULL COMMENT '回复类型',
  `isFuzzy` varchar(1) DEFAULT NULL COMMENT '是否模糊查询,1为是;0为否',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of wechat_keyword_reply
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_menu
-- ----------------------------
DROP TABLE IF EXISTS `wechat_menu`;
CREATE TABLE `wechat_menu` (
  `ID` varchar(36) NOT NULL,
  `name` varchar(20) DEFAULT NULL COMMENT '菜单名',
  `keyValue` varchar(50) DEFAULT NULL COMMENT 'click事件的key值,对应一个关键字',
  `url` varchar(255) DEFAULT NULL COMMENT 'view类型的url',
  `type` varchar(1) DEFAULT NULL COMMENT '响应的类型',
  `orderNum` varchar(2) DEFAULT NULL COMMENT '菜单顺序',
  `media_id` varchar(255) DEFAULT NULL COMMENT '调用新增永久素材接口返回的合法media_id',
  `parentId` varchar(36) DEFAULT NULL COMMENT '如果是子菜单则有父菜单的ID值',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of wechat_menu
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_message_text
-- ----------------------------
DROP TABLE IF EXISTS `wechat_message_text`;
CREATE TABLE `wechat_message_text` (
  `ID` varchar(36) NOT NULL,
  `content` varchar(1024) DEFAULT NULL COMMENT '文本消息内容',
  `fkID` varchar(36) DEFAULT NULL COMMENT '关联的表ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of wechat_message_text
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_subscribe_reply
-- ----------------------------
DROP TABLE IF EXISTS `wechat_subscribe_reply`;
CREATE TABLE `wechat_subscribe_reply` (
  `ID` varchar(36) NOT NULL,
  `replyType` varchar(1) DEFAULT NULL COMMENT '回复类型:0文字消息，1图文素材',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of wechat_subscribe_reply
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_user_location
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user_location`;
CREATE TABLE `wechat_user_location` (
  `ID` varchar(36) COLLATE utf8_bin NOT NULL,
  `openId` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `Latitude` double(12,6) DEFAULT NULL,
  `Longitude` double(12,6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of wechat_user_location
-- ----------------------------

-- ----------------------------
-- Table structure for wf_activityinst
-- ----------------------------
DROP TABLE IF EXISTS `wf_activityinst`;
CREATE TABLE `wf_activityinst` (
  `WorkItemID` int(11) NOT NULL,
  `FlowID` int(11) NOT NULL,
  `ActivityID` int(11) NOT NULL,
  `ActivityName` varchar(50) NOT NULL,
  `ActivityType` smallint(6) NOT NULL,
  `ActivityOrder` int(11) NOT NULL,
  `ActivityContent` text,
  `ImageID` int(11) DEFAULT NULL,
  `ActivityDescribe` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`WorkItemID`,`FlowID`,`ActivityID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_activityinst
-- ----------------------------

-- ----------------------------
-- Table structure for wf_bill
-- ----------------------------
DROP TABLE IF EXISTS `wf_bill`;
CREATE TABLE `wf_bill` (
  `MyPK` varchar(100) NOT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `FID` int(11) DEFAULT NULL,
  `FK_Flow` varchar(4) DEFAULT NULL,
  `FK_BillType` varchar(300) DEFAULT NULL,
  `Title` varchar(900) DEFAULT NULL,
  `FK_Starter` varchar(50) DEFAULT NULL,
  `StartDT` varchar(50) DEFAULT NULL,
  `Url` varchar(2000) DEFAULT NULL,
  `FullPath` varchar(2000) DEFAULT NULL,
  `FK_Emp` varchar(100) DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `FK_Dept` varchar(100) DEFAULT NULL,
  `FK_NY` varchar(100) DEFAULT NULL,
  `Emps` text,
  `FK_Node` varchar(30) DEFAULT NULL,
  `FK_Bill` varchar(500) DEFAULT NULL,
  `MyNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_BillID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_bill
-- ----------------------------

-- ----------------------------
-- Table structure for wf_billtemplate
-- ----------------------------
DROP TABLE IF EXISTS `wf_billtemplate`;
CREATE TABLE `wf_billtemplate` (
  `No` varchar(300) NOT NULL,
  `Name` varchar(200) DEFAULT NULL,
  `Url` varchar(200) DEFAULT NULL,
  `NodeID` int(11) DEFAULT NULL,
  `BillFileType` int(11) DEFAULT NULL,
  `FK_BillType` varchar(4) DEFAULT NULL,
  `IDX` varchar(200) DEFAULT NULL,
  `ExpField` varchar(800) DEFAULT NULL,
  `ReplaceVal` varchar(3000) DEFAULT NULL,
  KEY `WF_BillTemplateID` (`No`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_billtemplate
-- ----------------------------

-- ----------------------------
-- Table structure for wf_billtype
-- ----------------------------
DROP TABLE IF EXISTS `wf_billtype`;
CREATE TABLE `wf_billtype` (
  `No` varchar(2) NOT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `FK_Flow` varchar(50) DEFAULT NULL,
  `IDX` int(11) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `WF_BillTypeID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_billtype
-- ----------------------------

-- ----------------------------
-- Table structure for wf_ccdept
-- ----------------------------
DROP TABLE IF EXISTS `wf_ccdept`;
CREATE TABLE `wf_ccdept` (
  `FK_Node` int(11) NOT NULL,
  `FK_Dept` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Node`,`FK_Dept`),
  KEY `WF_CCDeptID` (`FK_Node`,`FK_Dept`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_ccdept
-- ----------------------------

-- ----------------------------
-- Table structure for wf_ccemp
-- ----------------------------
DROP TABLE IF EXISTS `wf_ccemp`;
CREATE TABLE `wf_ccemp` (
  `FK_Node` int(11) NOT NULL,
  `FK_Emp` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Node`,`FK_Emp`),
  KEY `WF_CCEmpID` (`FK_Node`,`FK_Emp`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_ccemp
-- ----------------------------

-- ----------------------------
-- Table structure for wf_cclist
-- ----------------------------
DROP TABLE IF EXISTS `wf_cclist`;
CREATE TABLE `wf_cclist` (
  `MyPK` varchar(100) NOT NULL,
  `Title` varchar(500) DEFAULT NULL,
  `FK_Flow` varchar(3) DEFAULT NULL,
  `FlowName` varchar(200) DEFAULT NULL,
  `NDFrom` int(11) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `NodeName` varchar(500) DEFAULT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `FID` int(11) DEFAULT NULL,
  `Doc` text,
  `Rec` varchar(50) DEFAULT NULL,
  `RecDept` varchar(50) DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `Sta` int(11) DEFAULT NULL,
  `CCTo` varchar(50) DEFAULT NULL,
  `CCToDept` varchar(50) DEFAULT NULL,
  `CCToName` varchar(50) DEFAULT NULL,
  `CheckNote` varchar(600) DEFAULT NULL,
  `CDT` varchar(50) DEFAULT NULL,
  `PFlowNo` varchar(100) DEFAULT NULL,
  `PWorkID` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_CCListID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_cclist
-- ----------------------------

-- ----------------------------
-- Table structure for wf_ccstation
-- ----------------------------
DROP TABLE IF EXISTS `wf_ccstation`;
CREATE TABLE `wf_ccstation` (
  `FK_Node` int(11) NOT NULL,
  `FK_Station` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Node`,`FK_Station`),
  KEY `WF_CCStationID` (`FK_Node`,`FK_Station`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_ccstation
-- ----------------------------

-- ----------------------------
-- Table structure for wf_cheval
-- ----------------------------
DROP TABLE IF EXISTS `wf_cheval`;
CREATE TABLE `wf_cheval` (
  `MyPK` varchar(100) NOT NULL,
  `Title` varchar(500) DEFAULT NULL,
  `FK_Flow` varchar(7) DEFAULT NULL,
  `FlowName` varchar(100) DEFAULT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `NodeName` varchar(100) DEFAULT NULL,
  `Rec` varchar(50) DEFAULT NULL,
  `RecName` varchar(50) DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `EvalEmpNo` varchar(50) DEFAULT NULL,
  `EvalEmpName` varchar(50) DEFAULT NULL,
  `EvalCent` varchar(20) DEFAULT NULL,
  `EvalNote` varchar(20) DEFAULT NULL,
  `FK_Dept` varchar(50) DEFAULT NULL,
  `DeptName` varchar(100) DEFAULT NULL,
  `FK_NY` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_CHEvalID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_cheval
-- ----------------------------

-- ----------------------------
-- Table structure for wf_cond
-- ----------------------------
DROP TABLE IF EXISTS `wf_cond`;
CREATE TABLE `wf_cond` (
  `MyPK` varchar(100) NOT NULL,
  `CondType` int(11) DEFAULT NULL,
  `DataFrom` int(11) DEFAULT NULL,
  `FK_Flow` varchar(60) DEFAULT NULL,
  `NodeID` int(11) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `FK_Attr` varchar(80) DEFAULT NULL,
  `AttrKey` varchar(60) DEFAULT NULL,
  `AttrName` varchar(500) DEFAULT NULL,
  `FK_Operator` varchar(60) DEFAULT NULL,
  `OperatorValue` text,
  `OperatorValueT` text,
  `ToNodeID` int(11) DEFAULT NULL,
  `ConnJudgeWay` int(11) DEFAULT NULL,
  `MyPOID` int(11) DEFAULT NULL,
  `PRI` int(11) DEFAULT NULL,
  `CondOrAnd` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_CondID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_cond
-- ----------------------------

-- ----------------------------
-- Table structure for wf_dataapply
-- ----------------------------
DROP TABLE IF EXISTS `wf_dataapply`;
CREATE TABLE `wf_dataapply` (
  `MyPK` varchar(100) NOT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `NodeId` int(11) DEFAULT NULL,
  `RunState` int(11) DEFAULT NULL,
  `ApplyDays` int(11) DEFAULT NULL,
  `ApplyData` varchar(50) DEFAULT NULL,
  `Applyer` varchar(100) DEFAULT NULL,
  `ApplyNote1` text,
  `ApplyNote2` text,
  `Checker` varchar(100) DEFAULT NULL,
  `CheckerData` varchar(50) DEFAULT NULL,
  `CheckerDays` int(11) DEFAULT NULL,
  `CheckerNote1` text,
  `CheckerNote2` text,
  PRIMARY KEY (`MyPK`),
  KEY `WF_DataApplyID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_dataapply
-- ----------------------------

-- ----------------------------
-- Table structure for wf_deptflowsearch
-- ----------------------------
DROP TABLE IF EXISTS `wf_deptflowsearch`;
CREATE TABLE `wf_deptflowsearch` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Emp` varchar(50) DEFAULT NULL,
  `FK_Flow` varchar(50) DEFAULT NULL,
  `FK_Dept` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_DeptFlowSearchID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_deptflowsearch
-- ----------------------------

-- ----------------------------
-- Table structure for wf_direction
-- ----------------------------
DROP TABLE IF EXISTS `wf_direction`;
CREATE TABLE `wf_direction` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Flow` varchar(3) DEFAULT NULL,
  `Node` int(11) DEFAULT NULL,
  `ToNode` int(11) DEFAULT NULL,
  `DirType` int(11) DEFAULT NULL,
  `IsCanBack` int(11) DEFAULT NULL,
  `Dots` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_DirectionID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_direction
-- ----------------------------

-- ----------------------------
-- Table structure for wf_emp
-- ----------------------------
DROP TABLE IF EXISTS `wf_emp`;
CREATE TABLE `wf_emp` (
  `No` varchar(50) NOT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `UseSta` int(11) DEFAULT NULL,
  `Tel` varchar(50) DEFAULT NULL,
  `FK_Dept` varchar(100) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `TM` varchar(50) DEFAULT NULL,
  `AlertWay` int(11) DEFAULT NULL,
  `Author` varchar(50) DEFAULT NULL,
  `AuthorDate` varchar(50) DEFAULT NULL,
  `AuthorWay` int(11) DEFAULT NULL,
  `AuthorToDate` varchar(50) DEFAULT NULL,
  `AuthorFlows` varchar(1000) DEFAULT NULL,
  `Stas` varchar(3000) DEFAULT NULL,
  `FtpUrl` varchar(50) DEFAULT NULL,
  `Msg` text,
  `Style` text,
  `Idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `WF_EmpID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_emp
-- ----------------------------
INSERT INTO `wf_emp` VALUES ('10001', 'Ryan Ouyang', '1', '', null, '', '', '3', '', '', '0', '2017-05-17 10:53:59', '', '', '', '', '', '0');

-- ----------------------------
-- Table structure for wf_entryinst
-- ----------------------------
DROP TABLE IF EXISTS `wf_entryinst`;
CREATE TABLE `wf_entryinst` (
  `WEID` int(11) NOT NULL AUTO_INCREMENT,
  `WorkItemID` int(11) NOT NULL,
  `EntryID` int(11) NOT NULL,
  `StateID` smallint(6) DEFAULT NULL,
  `EntryType` smallint(6) NOT NULL,
  `ReciType` smallint(6) DEFAULT NULL,
  `RecipientID` int(11) DEFAULT NULL,
  `Recipients` varchar(50) DEFAULT NULL,
  `ProxyID` int(11) DEFAULT NULL,
  `Proxyor` varchar(50) DEFAULT NULL,
  `RUserID` int(11) DEFAULT NULL,
  `RUserName` varchar(50) DEFAULT NULL,
  `DeptID` int(11) DEFAULT NULL,
  `DeptName` varchar(50) DEFAULT NULL,
  `InceptDate` datetime NOT NULL,
  `ReadDate` datetime DEFAULT NULL,
  `FinishedDate` datetime DEFAULT NULL,
  `OverDate` datetime DEFAULT NULL,
  `AlterDate` datetime DEFAULT NULL,
  `AlterInterval` smallint(6) DEFAULT NULL,
  `AlterNumber` smallint(6) DEFAULT NULL,
  `AlterUsers` varchar(2000) DEFAULT NULL,
  `CallDate` datetime DEFAULT NULL,
  `ParentID` int(11) DEFAULT NULL,
  `OriginalID` int(11) DEFAULT NULL,
  `SameEntryID` int(11) DEFAULT NULL,
  `FlowID` int(11) DEFAULT NULL,
  `ActivityID` int(11) DEFAULT NULL,
  `ActivityName` varchar(50) DEFAULT NULL,
  `RoleID` int(11) DEFAULT NULL,
  `RoleName` varchar(50) DEFAULT NULL,
  `ActivityChoice` smallint(6) DEFAULT NULL,
  `TransferActivityID` int(11) DEFAULT NULL,
  `ChoiceContent` varchar(2000) DEFAULT NULL,
  `ParameterOne` varchar(255) DEFAULT NULL,
  `ParameterTwo` varchar(255) DEFAULT NULL,
  `EntryContent` longblob,
  `DueTransactType` smallint(6) DEFAULT NULL,
  `DueUserNumber` smallint(6) DEFAULT NULL,
  `AlertMaxNumber` smallint(6) DEFAULT NULL,
  `SuperviseNumber` smallint(6) DEFAULT NULL,
  `RemindAddFlag` smallint(6) DEFAULT NULL,
  `WIEntryID` varchar(20) DEFAULT NULL,
  `MobileType` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`WEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_entryinst
-- ----------------------------

-- ----------------------------
-- Table structure for wf_findworkerrole
-- ----------------------------
DROP TABLE IF EXISTS `wf_findworkerrole`;
CREATE TABLE `wf_findworkerrole` (
  `OID` int(11) NOT NULL,
  `Name` varchar(200) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `SortVal0` varchar(200) DEFAULT NULL,
  `SortText0` varchar(200) DEFAULT NULL,
  `SortVal1` varchar(200) DEFAULT NULL,
  `SortText1` varchar(200) DEFAULT NULL,
  `SortVal2` varchar(200) DEFAULT NULL,
  `SortText2` varchar(200) DEFAULT NULL,
  `SortVal3` varchar(200) DEFAULT NULL,
  `SortText3` varchar(200) DEFAULT NULL,
  `TagVal0` varchar(1000) DEFAULT NULL,
  `TagVal1` varchar(1000) DEFAULT NULL,
  `TagVal2` varchar(1000) DEFAULT NULL,
  `TagVal3` varchar(1000) DEFAULT NULL,
  `TagText0` varchar(1000) DEFAULT NULL,
  `TagText1` varchar(1000) DEFAULT NULL,
  `TagText2` varchar(1000) DEFAULT NULL,
  `TagText3` varchar(1000) DEFAULT NULL,
  `IsEnable` int(11) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `WF_FindWorkerRoleID` (`OID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_findworkerrole
-- ----------------------------

-- ----------------------------
-- Table structure for wf_flow
-- ----------------------------
DROP TABLE IF EXISTS `wf_flow`;
CREATE TABLE `wf_flow` (
  `No` varchar(10) NOT NULL,
  `Name` varchar(500) DEFAULT NULL,
  `FK_FlowSort` varchar(100) DEFAULT NULL,
  `FlowRunWay` int(11) DEFAULT NULL,
  `RunObj` varchar(4000) DEFAULT NULL,
  `Note` varchar(100) DEFAULT NULL,
  `RunSQL` varchar(2000) DEFAULT NULL,
  `NumOfBill` int(11) DEFAULT NULL,
  `NumOfDtl` int(11) DEFAULT NULL,
  `FlowAppType` int(11) DEFAULT NULL,
  `ChartType` int(11) DEFAULT NULL,
  `IsCanStart` int(11) DEFAULT NULL,
  `AvgDay` float DEFAULT NULL,
  `IsFullSA` int(11) DEFAULT NULL,
  `IsMD5` int(11) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  `TimelineRole` int(11) DEFAULT NULL,
  `Paras` varchar(400) DEFAULT NULL,
  `PTable` varchar(30) DEFAULT NULL,
  `Draft` int(11) DEFAULT NULL,
  `DataStoreModel` int(11) DEFAULT NULL,
  `TitleRole` varchar(150) DEFAULT NULL,
  `FlowMark` varchar(150) DEFAULT NULL,
  `FlowEventEntity` varchar(150) DEFAULT NULL,
  `HistoryFields` varchar(500) DEFAULT NULL,
  `IsGuestFlow` int(11) DEFAULT NULL,
  `BillNoFormat` varchar(50) DEFAULT NULL,
  `FlowNoteExp` varchar(500) DEFAULT NULL,
  `DRCtrlType` int(11) DEFAULT NULL,
  `StartLimitRole` int(11) DEFAULT NULL,
  `StartLimitPara` varchar(500) DEFAULT NULL,
  `StartLimitAlert` varchar(4000) DEFAULT NULL,
  `StartLimitWhen` int(11) DEFAULT NULL,
  `StartGuideWay` int(11) DEFAULT NULL,
  `StartGuidePara1` varchar(4000) DEFAULT NULL,
  `StartGuidePara2` varchar(500) DEFAULT NULL,
  `StartGuidePara3` varchar(500) DEFAULT NULL,
  `IsResetData` int(11) DEFAULT NULL,
  `IsLoadPriData` int(11) DEFAULT NULL,
  `CFlowWay` int(11) DEFAULT NULL,
  `CFlowPara` varchar(100) DEFAULT NULL,
  `IsBatchStart` int(11) DEFAULT NULL,
  `BatchStartFields` varchar(500) DEFAULT NULL,
  `IsAutoSendSubFlowOver` int(11) DEFAULT NULL,
  `Ver` varchar(20) DEFAULT NULL,
  `DTSWay` int(11) DEFAULT NULL,
  `DTSBTable` varchar(200) DEFAULT NULL,
  `DTSBTablePK` varchar(200) DEFAULT NULL,
  `DTSTime` int(11) DEFAULT NULL,
  `DTSSpecNodes` varchar(200) DEFAULT NULL,
  `DTSField` int(11) DEFAULT NULL,
  `DTSFields` varchar(2000) DEFAULT NULL,
  `DesignerNo` varchar(32) DEFAULT '',
  `DesignerName` varchar(100) DEFAULT '',
  PRIMARY KEY (`No`),
  KEY `WF_FlowID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_flow
-- ----------------------------

-- ----------------------------
-- Table structure for wf_flowdefine
-- ----------------------------
DROP TABLE IF EXISTS `wf_flowdefine`;
CREATE TABLE `wf_flowdefine` (
  `FlowID` int(11) NOT NULL,
  `TemplateID` int(11) DEFAULT NULL,
  `FlowName` varchar(50) NOT NULL,
  `FlowType` smallint(6) NOT NULL,
  `FlowIsValid` smallint(6) NOT NULL,
  `FlowDescribe` varchar(255) DEFAULT NULL,
  `FlowImage` varchar(255) DEFAULT NULL,
  `DefaultFlow` smallint(6) NOT NULL,
  `FlowContent` text,
  PRIMARY KEY (`FlowID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_flowdefine
-- ----------------------------

-- ----------------------------
-- Table structure for wf_flowemp
-- ----------------------------
DROP TABLE IF EXISTS `wf_flowemp`;
CREATE TABLE `wf_flowemp` (
  `FK_Flow` varchar(100) NOT NULL,
  `FK_Emp` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Flow`,`FK_Emp`),
  KEY `WF_FlowEmpID` (`FK_Flow`,`FK_Emp`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_flowemp
-- ----------------------------

-- ----------------------------
-- Table structure for wf_flowformtree
-- ----------------------------
DROP TABLE IF EXISTS `wf_flowformtree`;
CREATE TABLE `wf_flowformtree` (
  `No` varchar(10) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `ParentNo` varchar(100) DEFAULT NULL,
  `TreeNo` varchar(100) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  `IsDir` int(11) DEFAULT NULL,
  `FK_Flow` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `WF_FlowFormTreeID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_flowformtree
-- ----------------------------

-- ----------------------------
-- Table structure for wf_flowinst
-- ----------------------------
DROP TABLE IF EXISTS `wf_flowinst`;
CREATE TABLE `wf_flowinst` (
  `WorkItemID` int(11) NOT NULL,
  `FlowID` int(11) NOT NULL,
  `TemplateID` int(11) DEFAULT NULL,
  `FlowName` varchar(50) NOT NULL,
  `FlowType` smallint(6) NOT NULL,
  `FlowIsValid` smallint(6) NOT NULL,
  `DefaultFlow` smallint(6) NOT NULL,
  `FlowContent` text,
  `FlowDescribe` varchar(255) DEFAULT NULL,
  `FlowImage` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`FlowID`,`WorkItemID`),
  UNIQUE KEY `PK_FLOWINST` (`WorkItemID`,`FlowID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_flowinst
-- ----------------------------

-- ----------------------------
-- Table structure for wf_flownode
-- ----------------------------
DROP TABLE IF EXISTS `wf_flownode`;
CREATE TABLE `wf_flownode` (
  `FK_Flow` varchar(20) NOT NULL,
  `FK_Node` varchar(20) NOT NULL,
  PRIMARY KEY (`FK_Flow`,`FK_Node`),
  KEY `WF_FlowNodeID` (`FK_Flow`,`FK_Node`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_flownode
-- ----------------------------

-- ----------------------------
-- Table structure for wf_flowsort
-- ----------------------------
DROP TABLE IF EXISTS `wf_flowsort`;
CREATE TABLE `wf_flowsort` (
  `No` varchar(10) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `ParentNo` varchar(100) DEFAULT NULL,
  `TreeNo` varchar(100) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  `IsDir` int(11) DEFAULT NULL,
  PRIMARY KEY (`No`),
  KEY `WF_FlowSortID` (`No`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_flowsort
-- ----------------------------
INSERT INTO `wf_flowsort` VALUES ('99', '流程树', '0', '', '0', '1');

-- ----------------------------
-- Table structure for wf_frmnode
-- ----------------------------
DROP TABLE IF EXISTS `wf_frmnode`;
CREATE TABLE `wf_frmnode` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Frm` varchar(32) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `FK_Flow` varchar(20) DEFAULT NULL,
  `OfficeOpenLab` varchar(50) DEFAULT NULL,
  `OfficeOpenEnable` int(11) DEFAULT NULL,
  `OfficeOpenTemplateLab` varchar(50) DEFAULT NULL,
  `OfficeOpenTemplateEnable` int(11) DEFAULT NULL,
  `OfficeSaveLab` varchar(50) DEFAULT NULL,
  `OfficeSaveEnable` int(11) DEFAULT NULL,
  `OfficeAcceptLab` varchar(50) DEFAULT NULL,
  `OfficeAcceptEnable` int(11) DEFAULT NULL,
  `OfficeRefuseLab` varchar(50) DEFAULT NULL,
  `OfficeRefuseEnable` int(11) DEFAULT NULL,
  `OfficeOverLab` varchar(50) DEFAULT NULL,
  `OfficeOverEnable` int(11) DEFAULT NULL,
  `OfficeMarksEnable` int(11) DEFAULT NULL,
  `OfficePrintLab` varchar(50) DEFAULT NULL,
  `OfficePrintEnable` int(11) DEFAULT NULL,
  `OfficeSealLab` varchar(50) DEFAULT NULL,
  `OfficeSealEnable` int(11) DEFAULT NULL,
  `OfficeInsertFlowLab` varchar(50) DEFAULT NULL,
  `OfficeInsertFlowEnable` int(11) DEFAULT NULL,
  `OfficeNodeInfo` int(11) DEFAULT NULL,
  `OfficeReSavePDF` int(11) DEFAULT NULL,
  `OfficeDownLab` varchar(50) DEFAULT NULL,
  `OfficeDownEnable` int(11) DEFAULT NULL,
  `OfficeIsMarks` int(11) DEFAULT NULL,
  `OfficeTemplate` varchar(100) DEFAULT NULL,
  `OfficeIsParent` int(11) DEFAULT NULL,
  `OfficeTHEnable` int(11) DEFAULT NULL,
  `OfficeTHTemplate` varchar(200) DEFAULT NULL,
  `FrmType` varchar(20) DEFAULT '0',
  `IsEdit` int(11) DEFAULT '1',
  `IsPrint` int(11) DEFAULT '0',
  `IsEnableLoadData` int(11) DEFAULT '0',
  `Idx` int(11) DEFAULT '0',
  `FrmSln` int(11) DEFAULT '0',
  `WhoIsPK` int(11) DEFAULT '0',
  PRIMARY KEY (`MyPK`),
  KEY `WF_FrmNodeID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_frmnode
-- ----------------------------

-- ----------------------------
-- Table structure for wf_generfh
-- ----------------------------
DROP TABLE IF EXISTS `wf_generfh`;
CREATE TABLE `wf_generfh` (
  `FID` int(11) NOT NULL,
  `Title` text,
  `GroupKey` varchar(3000) DEFAULT NULL,
  `FK_Flow` varchar(500) DEFAULT NULL,
  `ToEmpsMsg` text,
  `FK_Node` int(11) DEFAULT NULL,
  `WFState` int(11) DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  KEY `WF_GenerFHID` (`FID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_generfh
-- ----------------------------

-- ----------------------------
-- Table structure for wf_generworkerlist
-- ----------------------------
DROP TABLE IF EXISTS `wf_generworkerlist`;
CREATE TABLE `wf_generworkerlist` (
  `WorkID` int(11) NOT NULL,
  `FK_Emp` varchar(255) NOT NULL,
  `FK_Node` int(11) NOT NULL,
  `FID` int(11) DEFAULT NULL,
  `FK_EmpText` varchar(30) DEFAULT NULL,
  `FK_NodeText` varchar(100) DEFAULT NULL,
  `FK_Flow` varchar(3) DEFAULT NULL,
  `FK_Dept` varchar(100) DEFAULT NULL,
  `SDT` varchar(50) DEFAULT NULL,
  `DTOfWarning` varchar(50) DEFAULT NULL,
  `WarningDays` float DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `CDT` varchar(50) DEFAULT NULL,
  `IsEnable` int(11) DEFAULT NULL,
  `IsRead` int(11) DEFAULT NULL,
  `IsPass` int(11) DEFAULT NULL,
  `WhoExeIt` int(11) DEFAULT NULL,
  `Sender` varchar(200) DEFAULT NULL,
  `PRI` int(11) DEFAULT NULL,
  `PressTimes` int(11) DEFAULT NULL,
  `DTOfHungUp` varchar(50) DEFAULT NULL,
  `DTOfUnHungUp` varchar(50) DEFAULT NULL,
  `HungUpTimes` int(11) DEFAULT NULL,
  `GuestNo` varchar(30) DEFAULT NULL,
  `GuestName` varchar(100) DEFAULT NULL,
  `AtPara` text,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`WorkID`,`FK_Emp`,`FK_Node`),
  KEY `WF_GenerWorkerlistID` (`WorkID`,`FK_Emp`,`FK_Node`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_generworkerlist
-- ----------------------------

-- ----------------------------
-- Table structure for wf_generworkflow
-- ----------------------------
DROP TABLE IF EXISTS `wf_generworkflow`;
CREATE TABLE `wf_generworkflow` (
  `WorkID` int(11) NOT NULL,
  `StarterName` varchar(200) DEFAULT NULL,
  `Title` varchar(100) DEFAULT NULL,
  `WFSta` int(11) DEFAULT NULL,
  `NodeName` varchar(100) DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `BillNo` varchar(100) DEFAULT NULL,
  `FlowNote` text,
  `FK_FlowSort` varchar(100) DEFAULT NULL,
  `FK_Flow` varchar(100) DEFAULT NULL,
  `FK_Dept` varchar(100) DEFAULT NULL,
  `FID` int(11) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `MyNum` int(11) DEFAULT NULL,
  `FlowName` varchar(100) DEFAULT '',
  `WFState` int(11) DEFAULT '0',
  `Starter` varchar(200) DEFAULT '',
  `Sender` varchar(200) DEFAULT '',
  `DeptName` varchar(100) DEFAULT '',
  `PRI` int(11) DEFAULT '1',
  `SDTOfNode` varchar(50) DEFAULT NULL,
  `SDTOfFlow` varchar(50) DEFAULT NULL,
  `PFlowNo` varchar(3) DEFAULT '',
  `PWorkID` int(11) DEFAULT '0',
  `PNodeID` int(11) DEFAULT '0',
  `PFID` int(11) DEFAULT '0',
  `PEmp` varchar(32) DEFAULT '',
  `CFlowNo` varchar(3) DEFAULT '',
  `CWorkID` int(11) DEFAULT '0',
  `GuestNo` varchar(100) DEFAULT '',
  `GuestName` varchar(100) DEFAULT '',
  `TodoEmps` text,
  `TodoEmpsNum` int(11) DEFAULT '0',
  `TaskSta` int(11) DEFAULT '0',
  `AtPara` varchar(2000) DEFAULT '',
  `Emps` text,
  `GUID` varchar(36) DEFAULT '',
  `FK_NY` varchar(7) DEFAULT '',
  PRIMARY KEY (`WorkID`),
  KEY `WF_GenerWorkFlowID` (`WorkID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_generworkflow
-- ----------------------------

-- ----------------------------
-- Table structure for wf_hungup
-- ----------------------------
DROP TABLE IF EXISTS `wf_hungup`;
CREATE TABLE `wf_hungup` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `HungUpWay` int(11) DEFAULT NULL,
  `Note` text,
  `Rec` varchar(50) DEFAULT NULL,
  `DTOfHungUp` varchar(50) DEFAULT NULL,
  `DTOfUnHungUp` varchar(50) DEFAULT NULL,
  `DTOfUnHungUpPlan` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_HungUpID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_hungup
-- ----------------------------

-- ----------------------------
-- Table structure for wf_labnote
-- ----------------------------
DROP TABLE IF EXISTS `wf_labnote`;
CREATE TABLE `wf_labnote` (
  `MyPK` varchar(100) NOT NULL,
  `Name` varchar(3000) DEFAULT NULL,
  `FK_Flow` varchar(100) DEFAULT NULL,
  `X` int(11) DEFAULT NULL,
  `Y` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_LabNoteID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_labnote
-- ----------------------------

-- ----------------------------
-- Table structure for wf_listen
-- ----------------------------
DROP TABLE IF EXISTS `wf_listen`;
CREATE TABLE `wf_listen` (
  `OID` int(11) NOT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `Nodes` varchar(400) DEFAULT NULL,
  `NodesDesc` varchar(400) DEFAULT NULL,
  `Title` varchar(400) DEFAULT NULL,
  `Doc` text,
  PRIMARY KEY (`OID`),
  KEY `WF_ListenID` (`OID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_listen
-- ----------------------------

-- ----------------------------
-- Table structure for wf_node
-- ----------------------------
DROP TABLE IF EXISTS `wf_node`;
CREATE TABLE `wf_node` (
  `NodeID` int(11) NOT NULL,
  `SendLab` varchar(50) DEFAULT NULL,
  `SendJS` varchar(999) DEFAULT NULL,
  `JumpWayLab` varchar(50) DEFAULT NULL,
  `JumpWay` int(11) DEFAULT NULL,
  `SaveLab` varchar(50) DEFAULT NULL,
  `SaveEnable` int(11) DEFAULT NULL,
  `ThreadLab` varchar(50) DEFAULT NULL,
  `ThreadEnable` int(11) DEFAULT NULL,
  `SubFlowLab` varchar(50) DEFAULT NULL,
  `SubFlowCtrlRole` int(11) DEFAULT NULL,
  `ReturnLab` varchar(50) DEFAULT NULL,
  `ReturnRole` int(11) DEFAULT NULL,
  `ReturnField` varchar(50) DEFAULT NULL,
  `CCLab` varchar(50) DEFAULT NULL,
  `CCRole` int(11) DEFAULT NULL,
  `ShiftLab` varchar(50) DEFAULT NULL,
  `ShiftEnable` int(11) DEFAULT NULL,
  `DelLab` varchar(50) DEFAULT NULL,
  `DelEnable` int(11) DEFAULT NULL,
  `EndFlowLab` varchar(50) DEFAULT NULL,
  `EndFlowEnable` int(11) DEFAULT NULL,
  `HungLab` varchar(50) DEFAULT NULL,
  `HungEnable` int(11) DEFAULT NULL,
  `PrintDocLab` varchar(50) DEFAULT NULL,
  `PrintDocEnable` int(11) DEFAULT NULL,
  `TrackLab` varchar(50) DEFAULT NULL,
  `TrackEnable` int(11) DEFAULT NULL,
  `SelectAccepterLab` varchar(50) DEFAULT NULL,
  `SelectAccepterEnable` int(11) DEFAULT NULL,
  `SearchLab` varchar(50) DEFAULT NULL,
  `SearchEnable` int(11) DEFAULT NULL,
  `WorkCheckLab` varchar(50) DEFAULT NULL,
  `WorkCheckEnable` int(11) DEFAULT NULL,
  `BatchLab` varchar(50) DEFAULT NULL,
  `BatchEnable` int(11) DEFAULT NULL,
  `AskforLab` varchar(50) DEFAULT NULL,
  `AskforEnable` int(11) DEFAULT NULL,
  `TCLab` varchar(50) DEFAULT NULL,
  `TCEnable` int(11) DEFAULT NULL,
  `WebOffice` varchar(50) DEFAULT NULL,
  `WebOfficeEnable` int(11) DEFAULT NULL,
  `OfficeOpenLab` varchar(50) DEFAULT NULL,
  `OfficeOpenEnable` int(11) DEFAULT NULL,
  `OfficeOpenTemplateLab` varchar(50) DEFAULT NULL,
  `OfficeOpenTemplateEnable` int(11) DEFAULT NULL,
  `OfficeSaveLab` varchar(50) DEFAULT NULL,
  `OfficeSaveEnable` int(11) DEFAULT NULL,
  `OfficeAcceptLab` varchar(50) DEFAULT NULL,
  `OfficeAcceptEnable` int(11) DEFAULT NULL,
  `OfficeRefuseLab` varchar(50) DEFAULT NULL,
  `OfficeRefuseEnable` int(11) DEFAULT NULL,
  `OfficeTHTemplate` varchar(200) DEFAULT NULL,
  `OfficeOverEnable` int(11) DEFAULT NULL,
  `OfficeMarksEnable` int(11) DEFAULT NULL,
  `OfficePrintLab` varchar(50) DEFAULT NULL,
  `OfficePrintEnable` int(11) DEFAULT NULL,
  `OfficeSealLab` varchar(50) DEFAULT NULL,
  `OfficeSealEnable` int(11) DEFAULT NULL,
  `OfficeInsertFlowLab` varchar(50) DEFAULT NULL,
  `OfficeInsertFlowEnable` int(11) DEFAULT NULL,
  `OfficeNodeInfo` int(11) DEFAULT NULL,
  `OfficeReSavePDF` int(11) DEFAULT NULL,
  `OfficeDownLab` varchar(50) DEFAULT NULL,
  `OfficeDownEnable` int(11) DEFAULT NULL,
  `OfficeIsMarks` int(11) DEFAULT NULL,
  `OfficeTemplate` varchar(100) DEFAULT NULL,
  `OfficeIsParent` int(11) DEFAULT NULL,
  `OfficeTHEnable` int(11) DEFAULT NULL,
  `Step` int(11) DEFAULT '0',
  `FK_Flow` varchar(10) DEFAULT NULL,
  `Name` varchar(100) DEFAULT '',
  `DeliveryWay` int(11) DEFAULT '0',
  `DeliveryParas` varchar(500) DEFAULT '',
  `WhoExeIt` int(11) DEFAULT '0',
  `TurnToDeal` int(11) DEFAULT '0',
  `TurnToDealDoc` varchar(1000) DEFAULT '',
  `ReadReceipts` int(11) DEFAULT '0',
  `CondModel` int(11) DEFAULT '0',
  `CancelRole` int(11) DEFAULT '0',
  `BatchRole` int(11) DEFAULT '0',
  `BatchListCount` int(11) DEFAULT '12',
  `BatchParas` varchar(300) DEFAULT '',
  `IsTask` int(11) DEFAULT '1',
  `IsRM` int(11) DEFAULT '1',
  `DTFrom` varchar(50) DEFAULT NULL,
  `DTTo` varchar(50) DEFAULT NULL,
  `FormType` int(11) DEFAULT '1',
  `NodeFrmID` varchar(50) DEFAULT '',
  `FormUrl` varchar(2000) DEFAULT NULL,
  `FocusField` varchar(50) DEFAULT '',
  `SaveModel` int(11) DEFAULT '0',
  `RunModel` int(11) DEFAULT '0',
  `SubThreadType` int(11) DEFAULT '0',
  `PassRate` float DEFAULT '100',
  `SubFlowStartWay` int(11) DEFAULT '0',
  `SubFlowStartParas` varchar(100) DEFAULT '',
  `TodolistModel` int(11) DEFAULT '0',
  `BlockModel` int(11) DEFAULT '0',
  `BlockExp` varchar(700) DEFAULT '',
  `BlockAlert` varchar(700) DEFAULT '',
  `IsAllowRepeatEmps` int(11) DEFAULT '0',
  `IsGuestNode` int(11) DEFAULT '0',
  `AutoJumpRole0` int(11) DEFAULT '0',
  `AutoJumpRole1` int(11) DEFAULT '0',
  `AutoJumpRole2` int(11) DEFAULT '0',
  `WhenNoWorker` int(11) DEFAULT '0',
  `ThreadKillRole` int(11) DEFAULT '0',
  `JumpToNodes` varchar(200) DEFAULT '',
  `IsBackTracking` int(11) DEFAULT '0',
  `CCWriteTo` int(11) DEFAULT '0',
  `WarningDays` float DEFAULT '0',
  `DeductDays` float DEFAULT '1',
  `DeductCent` float DEFAULT '2',
  `MaxDeductCent` float DEFAULT '0',
  `SwinkCent` float DEFAULT '0.1',
  `OutTimeDeal` int(11) DEFAULT '0',
  `DoOutTime` varchar(300) DEFAULT '',
  `DoOutTimeCond` varchar(100) DEFAULT '',
  `CHWay` int(11) DEFAULT '0',
  `Workload` float DEFAULT '0',
  `IsEval` int(11) DEFAULT '0',
  `FWCSta` int(11) DEFAULT '0',
  `FWCShowModel` int(11) DEFAULT '1',
  `FWCType` int(11) DEFAULT '0',
  `FWCNodeName` varchar(100) DEFAULT '',
  `FWCAth` int(11) DEFAULT '0',
  `FWCTrackEnable` int(11) DEFAULT '1',
  `FWCListEnable` int(11) DEFAULT '1',
  `FWCIsShowAllStep` int(11) DEFAULT '0',
  `FWCOpLabel` varchar(50) DEFAULT '审核',
  `FWCDefInfo` varchar(50) DEFAULT '同意',
  `SigantureEnabel` int(11) DEFAULT '0',
  `FWCIsFullInfo` int(11) DEFAULT '1',
  `FWC_H` float DEFAULT '300',
  `FWC_W` float DEFAULT '400',
  `OfficeOverLab` varchar(50) DEFAULT '套红',
  `MPhone_WorkModel` int(11) DEFAULT '0',
  `MPhone_SrcModel` int(11) DEFAULT '0',
  `MPad_WorkModel` int(11) DEFAULT '0',
  `MPad_SrcModel` int(11) DEFAULT '0',
  `SelectorDBShowWay` int(11) DEFAULT '0',
  `SelectorModel` int(11) DEFAULT '0',
  `SelectorP1` text,
  `SelectorP2` text,
  `CheckNodes` varchar(800) DEFAULT '',
  `CCCtrlWay` int(11) DEFAULT '0',
  `CCSQL` varchar(500) DEFAULT '',
  `CCTitle` varchar(500) DEFAULT '',
  `CCDoc` text,
  `ICON` varchar(50) DEFAULT '',
  `NodeWorkType` int(11) DEFAULT '0',
  `FlowName` varchar(100) DEFAULT '',
  `FK_FlowSort` varchar(4) DEFAULT '',
  `FK_FlowSortT` varchar(100) DEFAULT '',
  `FrmAttr` varchar(300) DEFAULT '',
  `Doc` varchar(100) DEFAULT '',
  `IsCanRpt` int(11) DEFAULT '1',
  `IsCanOver` int(11) DEFAULT '0',
  `IsSecret` int(11) DEFAULT '0',
  `IsCanDelFlow` int(11) DEFAULT '0',
  `IsHandOver` int(11) DEFAULT '0',
  `NodePosType` int(11) DEFAULT '0',
  `IsCCFlow` int(11) DEFAULT '0',
  `HisStas` text,
  `HisDeptStrs` text,
  `HisToNDs` varchar(100) DEFAULT '',
  `HisBillIDs` varchar(200) DEFAULT '',
  `HisSubFlows` varchar(50) DEFAULT '',
  `PTable` varchar(100) DEFAULT '',
  `ShowSheets` varchar(100) DEFAULT '',
  `GroupStaNDs` varchar(200) DEFAULT '',
  `X` int(11) DEFAULT '0',
  `Y` int(11) DEFAULT '0',
  `AtPara` varchar(500) DEFAULT '',
  `DocLeftWord` varchar(200) DEFAULT '',
  `DocRightWord` varchar(200) DEFAULT '',
  `FWC_X` float DEFAULT '5',
  `FWC_Y` float DEFAULT '5',
  `Tip` varchar(100) DEFAULT '',
  `IsExpSender` int(11) NOT NULL DEFAULT '1',
  `PRILab` varchar(50) DEFAULT '重要性',
  `PRIEnable` int(11) NOT NULL DEFAULT '0',
  `CHLab` varchar(50) DEFAULT '节点时限',
  `CHEnable` int(11) NOT NULL DEFAULT '0',
  `TSpanDay` float DEFAULT '0',
  `TSpanHour` float DEFAULT '8',
  `WarningDay` float DEFAULT '0',
  `WarningHour` float DEFAULT '0',
  `TCent` float DEFAULT '1',
  `FWCFields` text,
  `SFSta` int(11) NOT NULL DEFAULT '0',
  `SFShowModel` int(11) NOT NULL DEFAULT '1',
  `SFCaption` varchar(50) DEFAULT '',
  `SFDefInfo` varchar(50) DEFAULT '',
  `SFActiveFlows` varchar(50) DEFAULT '',
  `SF_X` float DEFAULT '5',
  `SF_Y` float DEFAULT '5',
  `SF_H` float DEFAULT '300',
  `SF_W` float DEFAULT '400',
  `OfficeOpen` varchar(50) DEFAULT '打开本地',
  `OfficeOpenTemplate` varchar(50) DEFAULT '打开模板',
  `OfficeSave` varchar(50) DEFAULT '保存',
  `OfficeAccept` varchar(50) DEFAULT '接受修订',
  `OfficeRefuse` varchar(50) DEFAULT '拒绝修订',
  `OfficeOver` varchar(50) DEFAULT '套红按钮',
  `OfficeMarks` int(11) NOT NULL DEFAULT '1',
  `OfficeReadOnly` int(11) NOT NULL DEFAULT '0',
  `OfficePrint` varchar(50) DEFAULT '打印按钮',
  `OfficeSeal` varchar(50) DEFAULT '签章按钮',
  `OfficeSealEnabel` int(11) NOT NULL DEFAULT '0',
  `OfficeInsertFlow` varchar(50) DEFAULT '插入流程',
  `OfficeInsertFlowEnabel` int(11) NOT NULL DEFAULT '0',
  `OfficeIsDown` int(11) NOT NULL DEFAULT '0',
  `OfficeIsTrueTH` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`NodeID`),
  KEY `WF_NodeID` (`NodeID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_node
-- ----------------------------

-- ----------------------------
-- Table structure for wf_nodecancel
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodecancel`;
CREATE TABLE `wf_nodecancel` (
  `FK_Node` int(11) NOT NULL,
  `CancelTo` int(11) NOT NULL,
  PRIMARY KEY (`FK_Node`,`CancelTo`),
  KEY `WF_NodeCancelID` (`FK_Node`,`CancelTo`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_nodecancel
-- ----------------------------

-- ----------------------------
-- Table structure for wf_nodedept
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodedept`;
CREATE TABLE `wf_nodedept` (
  `FK_Node` int(11) NOT NULL,
  `FK_Dept` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Node`,`FK_Dept`),
  KEY `WF_NodeDeptID` (`FK_Node`,`FK_Dept`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_nodedept
-- ----------------------------

-- ----------------------------
-- Table structure for wf_nodeemp
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodeemp`;
CREATE TABLE `wf_nodeemp` (
  `FK_Node` int(11) NOT NULL,
  `FK_Emp` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Node`,`FK_Emp`),
  KEY `WF_NodeEmpID` (`FK_Node`,`FK_Emp`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_nodeemp
-- ----------------------------

-- ----------------------------
-- Table structure for wf_nodeflow
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodeflow`;
CREATE TABLE `wf_nodeflow` (
  `FK_Node` int(11) NOT NULL,
  `FK_Flow` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Node`,`FK_Flow`),
  KEY `WF_NodeFlowID` (`FK_Node`,`FK_Flow`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_nodeflow
-- ----------------------------

-- ----------------------------
-- Table structure for wf_nodereturn
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodereturn`;
CREATE TABLE `wf_nodereturn` (
  `FK_Node` int(11) NOT NULL,
  `ReturnTo` int(11) NOT NULL,
  `Dots` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`FK_Node`,`ReturnTo`),
  KEY `WF_NodeReturnID` (`FK_Node`,`ReturnTo`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_nodereturn
-- ----------------------------

-- ----------------------------
-- Table structure for wf_nodestation
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodestation`;
CREATE TABLE `wf_nodestation` (
  `FK_Node` int(11) NOT NULL,
  `FK_Station` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_Node`,`FK_Station`),
  KEY `WF_NodeStationID` (`FK_Node`,`FK_Station`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_nodestation
-- ----------------------------

-- ----------------------------
-- Table structure for wf_nodetoolbar
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodetoolbar`;
CREATE TABLE `wf_nodetoolbar` (
  `OID` int(11) NOT NULL,
  `Title` varchar(100) DEFAULT NULL,
  `Target` varchar(50) DEFAULT NULL,
  `Url` varchar(500) DEFAULT NULL,
  `ShowWhere` int(11) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `MyFileName` varchar(100) DEFAULT NULL,
  `MyFilePath` varchar(100) DEFAULT NULL,
  `MyFileExt` varchar(10) DEFAULT NULL,
  `WebPath` varchar(200) DEFAULT NULL,
  `MyFileH` int(11) DEFAULT NULL,
  `MyFileW` int(11) DEFAULT NULL,
  `MyFileSize` float DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `WF_NodeToolbarID` (`OID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_nodetoolbar
-- ----------------------------

-- ----------------------------
-- Table structure for wf_proxy
-- ----------------------------
DROP TABLE IF EXISTS `wf_proxy`;
CREATE TABLE `wf_proxy` (
  `ID` int(11) NOT NULL,
  `RecipientID` int(11) NOT NULL,
  `ProxyID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`RecipientID`,`ProxyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_proxy
-- ----------------------------

-- ----------------------------
-- Table structure for wf_pushmsg
-- ----------------------------
DROP TABLE IF EXISTS `wf_pushmsg`;
CREATE TABLE `wf_pushmsg` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `FK_Event` varchar(15) DEFAULT NULL,
  `PushWay` int(11) DEFAULT NULL,
  `PushDoc` text,
  `Tag` varchar(500) DEFAULT NULL,
  `MsgMailEnable` int(11) DEFAULT NULL,
  `MailTitle` varchar(200) DEFAULT NULL,
  `MailDoc` text,
  `SMSEnable` int(11) DEFAULT NULL,
  `SMSDoc` text,
  `MobilePushEnable` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_PushMsgID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_pushmsg
-- ----------------------------

-- ----------------------------
-- Table structure for wf_rememberme
-- ----------------------------
DROP TABLE IF EXISTS `wf_rememberme`;
CREATE TABLE `wf_rememberme` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `FK_Emp` varchar(30) DEFAULT NULL,
  `Objs` text,
  `ObjsExt` text,
  `Emps` text,
  `EmpsExt` text,
  PRIMARY KEY (`MyPK`),
  KEY `WF_RememberMeID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_rememberme
-- ----------------------------

-- ----------------------------
-- Table structure for wf_returnwork
-- ----------------------------
DROP TABLE IF EXISTS `wf_returnwork`;
CREATE TABLE `wf_returnwork` (
  `MyPK` varchar(100) NOT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `ReturnNode` int(11) DEFAULT NULL,
  `ReturnNodeName` varchar(200) DEFAULT NULL,
  `Returner` varchar(20) DEFAULT NULL,
  `ReturnerName` varchar(200) DEFAULT NULL,
  `ReturnToNode` int(11) DEFAULT NULL,
  `ReturnToEmp` text,
  `Note` text,
  `RDT` varchar(50) DEFAULT NULL,
  `IsBackTracking` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_ReturnWorkID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_returnwork
-- ----------------------------

-- ----------------------------
-- Table structure for wf_selectaccper
-- ----------------------------
DROP TABLE IF EXISTS `wf_selectaccper`;
CREATE TABLE `wf_selectaccper` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `FK_Emp` varchar(20) DEFAULT NULL,
  `EmpName` varchar(20) DEFAULT NULL,
  `AccType` int(11) DEFAULT NULL,
  `Rec` varchar(20) DEFAULT NULL,
  `Info` varchar(200) DEFAULT NULL,
  `IsRemember` int(11) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  `Tag` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_SelectAccperID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_selectaccper
-- ----------------------------

-- ----------------------------
-- Table structure for wf_selectinfo
-- ----------------------------
DROP TABLE IF EXISTS `wf_selectinfo`;
CREATE TABLE `wf_selectinfo` (
  `MyPK` varchar(100) NOT NULL,
  `AcceptNodeID` int(11) DEFAULT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `InfoLeft` varchar(200) DEFAULT NULL,
  `InfoCenter` varchar(200) DEFAULT NULL,
  `InfoRight` varchar(200) DEFAULT NULL,
  `AccType` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_SelectInfoID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_selectinfo
-- ----------------------------

-- ----------------------------
-- Table structure for wf_shiftwork
-- ----------------------------
DROP TABLE IF EXISTS `wf_shiftwork`;
CREATE TABLE `wf_shiftwork` (
  `MyPK` varchar(100) NOT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `FK_Emp` varchar(40) DEFAULT NULL,
  `FK_EmpName` varchar(40) DEFAULT NULL,
  `ToEmp` varchar(40) DEFAULT NULL,
  `ToEmpName` varchar(40) DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `Note` varchar(2000) DEFAULT NULL,
  `IsRead` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_ShiftWorkID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_shiftwork
-- ----------------------------

-- ----------------------------
-- Table structure for wf_task
-- ----------------------------
DROP TABLE IF EXISTS `wf_task`;
CREATE TABLE `wf_task` (
  `MyPK` varchar(100) NOT NULL,
  `FK_Flow` varchar(200) DEFAULT NULL,
  `Starter` varchar(200) DEFAULT NULL,
  `Paras` text,
  `TaskSta` int(11) DEFAULT NULL,
  `Msg` text,
  `StartDT` varchar(20) DEFAULT NULL,
  `RDT` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_TaskID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_task
-- ----------------------------

-- ----------------------------
-- Table structure for wf_templatedefine
-- ----------------------------
DROP TABLE IF EXISTS `wf_templatedefine`;
CREATE TABLE `wf_templatedefine` (
  `TemplateID` int(11) NOT NULL,
  `TemplateName` varchar(50) NOT NULL,
  `TemplateCode` varchar(50) DEFAULT NULL,
  `TypeID` int(11) NOT NULL,
  `CreateID` int(11) NOT NULL,
  `Creator` varchar(50) NOT NULL,
  `CreateDate` datetime NOT NULL,
  `EditID` int(11) DEFAULT NULL,
  `Editor` varchar(50) DEFAULT NULL,
  `EditDate` datetime DEFAULT NULL,
  `HaveBody` smallint(6) DEFAULT '1',
  `HaveDocument` smallint(6) DEFAULT '1',
  `HaveForm` smallint(6) DEFAULT '1',
  `FirstPage` smallint(6) DEFAULT NULL,
  `FlowFlag` smallint(6) NOT NULL,
  `TemplateDescribe` varchar(255) DEFAULT NULL,
  `TemplateIsValid` smallint(6) NOT NULL,
  `BindImage` varchar(100) DEFAULT NULL,
  `ExtendFields` text,
  `IsPublicUsing` smallint(6) DEFAULT NULL,
  `OrderID` int(11) DEFAULT NULL,
  `COMP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TemplateID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_templatedefine
-- ----------------------------

-- ----------------------------
-- Table structure for wf_templateinst
-- ----------------------------
DROP TABLE IF EXISTS `wf_templateinst`;
CREATE TABLE `wf_templateinst` (
  `WorkItemID` int(11) NOT NULL,
  `TemplateID` int(11) NOT NULL,
  `TemplateName` varchar(50) NOT NULL,
  `Editor` varchar(50) DEFAULT NULL,
  `TemplateDescribe` varchar(255) DEFAULT NULL,
  `CreateDate` datetime NOT NULL,
  `FlowFlag` smallint(6) NOT NULL,
  `Creator` varchar(50) NOT NULL,
  `TypeID` int(11) NOT NULL,
  `TemplateIsValid` smallint(6) NOT NULL,
  `HaveDocument` smallint(6) DEFAULT '1',
  `CreateID` int(11) NOT NULL,
  `EditDate` datetime DEFAULT NULL,
  `HaveForm` smallint(6) DEFAULT '1',
  `EditID` int(11) DEFAULT NULL,
  `FirstPage` smallint(6) DEFAULT NULL,
  `HaveBody` smallint(6) DEFAULT '1',
  `BindImage` varchar(100) DEFAULT NULL,
  `ExtendFields` text,
  `FileSaveType` smallint(6) DEFAULT NULL,
  `FileSaveID` int(11) DEFAULT NULL,
  `FileSaveDate` datetime DEFAULT NULL,
  `COMP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`WorkItemID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_templateinst
-- ----------------------------

-- ----------------------------
-- Table structure for wf_transfercustom
-- ----------------------------
DROP TABLE IF EXISTS `wf_transfercustom`;
CREATE TABLE `wf_transfercustom` (
  `MyPK` varchar(100) NOT NULL,
  `WorkID` int(11) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `Worker` varchar(200) DEFAULT NULL,
  `SubFlowNo` varchar(3) DEFAULT NULL,
  `RDT` varchar(50) DEFAULT NULL,
  `Idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_TransferCustomID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_transfercustom
-- ----------------------------

-- ----------------------------
-- Table structure for wf_turnto
-- ----------------------------
DROP TABLE IF EXISTS `wf_turnto`;
CREATE TABLE `wf_turnto` (
  `MyPK` varchar(100) NOT NULL,
  `TurnToType` int(11) DEFAULT NULL,
  `FK_Flow` varchar(60) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT NULL,
  `FK_Attr` varchar(80) DEFAULT NULL,
  `AttrKey` varchar(80) DEFAULT NULL,
  `AttrT` varchar(80) DEFAULT NULL,
  `FK_Operator` varchar(60) DEFAULT NULL,
  `OperatorValue` varchar(60) DEFAULT NULL,
  `OperatorValueT` varchar(60) DEFAULT NULL,
  `TurnToURL` varchar(700) DEFAULT NULL,
  PRIMARY KEY (`MyPK`),
  KEY `WF_TurnToID` (`MyPK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_turnto
-- ----------------------------

-- ----------------------------
-- Table structure for wf_workflowdeletelog
-- ----------------------------
DROP TABLE IF EXISTS `wf_workflowdeletelog`;
CREATE TABLE `wf_workflowdeletelog` (
  `OID` int(11) NOT NULL,
  `FID` int(11) DEFAULT NULL,
  `FK_Dept` varchar(100) DEFAULT NULL,
  `Title` varchar(100) DEFAULT NULL,
  `FlowStarter` varchar(100) DEFAULT NULL,
  `FlowStartRDT` varchar(50) DEFAULT NULL,
  `FK_NY` varchar(100) DEFAULT NULL,
  `FK_Flow` varchar(100) DEFAULT NULL,
  `FlowEnderRDT` varchar(50) DEFAULT NULL,
  `FlowEndNode` int(11) DEFAULT NULL,
  `FlowDaySpan` int(11) DEFAULT NULL,
  `MyNum` int(11) DEFAULT NULL,
  `FlowEmps` varchar(100) DEFAULT NULL,
  `Oper` varchar(20) DEFAULT NULL,
  `OperDept` varchar(20) DEFAULT NULL,
  `OperDeptName` varchar(200) DEFAULT NULL,
  `DeleteNote` text,
  `DeleteDT` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `WF_WorkFlowDeleteLogID` (`OID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_workflowdeletelog
-- ----------------------------

-- ----------------------------
-- Table structure for wf_workiteminst
-- ----------------------------
DROP TABLE IF EXISTS `wf_workiteminst`;
CREATE TABLE `wf_workiteminst` (
  `WorkItemID` int(11) NOT NULL,
  `WorkItemName` varchar(800) NOT NULL,
  `CreateID` int(11) DEFAULT NULL,
  `Creator` varchar(50) NOT NULL,
  `CreateDate` datetime NOT NULL,
  `DeptID` int(11) DEFAULT NULL,
  `DeptName` varchar(50) DEFAULT NULL,
  `EditID` int(11) DEFAULT NULL,
  `Editor` varchar(50) DEFAULT NULL,
  `EditDate` datetime DEFAULT NULL,
  `WorkItemStatus` smallint(6) NOT NULL,
  `OrignStatus` smallint(6) DEFAULT NULL,
  `IsFinished` smallint(6) DEFAULT '2',
  `FinishedDate` datetime DEFAULT NULL,
  `HaveBody` smallint(6) NOT NULL DEFAULT '1',
  `HaveDocument` smallint(6) NOT NULL,
  `HaveForm` smallint(6) DEFAULT '1',
  `ValidPeriod` smallint(6) DEFAULT NULL,
  `WorkItemSecret` smallint(6) DEFAULT NULL,
  `ExtendOne` smallint(6) DEFAULT NULL,
  `ExtendTwo` int(11) DEFAULT NULL,
  `ExtendThree` varchar(255) DEFAULT NULL,
  `ExtendFour` datetime DEFAULT NULL,
  `ExtendFive` varchar(50) DEFAULT NULL,
  `ExtendSix` varchar(100) DEFAULT NULL,
  `ExtendSeven` varchar(255) DEFAULT NULL,
  `ExtendEight` varchar(800) DEFAULT NULL,
  `ExtendNine` varchar(2000) DEFAULT NULL,
  `ExtendTen` text,
  `WorkItemSource` varchar(255) DEFAULT NULL,
  `LastUpdateType` smallint(6) DEFAULT NULL,
  `LastUpdateDate` datetime DEFAULT NULL,
  `ConfigTransfer` smallint(6) DEFAULT NULL,
  `ResultType` smallint(6) DEFAULT NULL,
  `ResultDescribe` text,
  `WorkItemProp` smallint(6) DEFAULT NULL,
  `FI1` int(11) DEFAULT NULL,
  `FI2` int(11) DEFAULT NULL,
  `FI3` int(11) DEFAULT NULL,
  `FI4` int(11) DEFAULT NULL,
  `FI5` int(11) DEFAULT NULL,
  `FSA1` varchar(255) DEFAULT NULL,
  `FSA2` varchar(255) DEFAULT NULL,
  `FSA3` varchar(255) DEFAULT NULL,
  `FSA4` varchar(255) DEFAULT NULL,
  `FSA5` varchar(255) DEFAULT NULL,
  `FSB1` varchar(512) DEFAULT NULL,
  `FSB2` varchar(512) DEFAULT NULL,
  `FSB3` varchar(512) DEFAULT NULL,
  `FSB4` varchar(512) DEFAULT NULL,
  `FSB5` varchar(512) DEFAULT NULL,
  `COMP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`WorkItemID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_workiteminst
-- ----------------------------

-- ----------------------------
-- Table structure for wf_worktype
-- ----------------------------
DROP TABLE IF EXISTS `wf_worktype`;
CREATE TABLE `wf_worktype` (
  `TypeID` int(11) NOT NULL,
  `TypeName` varchar(50) NOT NULL,
  `ParentID` int(11) DEFAULT NULL,
  `DisplayType` smallint(6) DEFAULT NULL,
  `TypeDescribe` varchar(255) DEFAULT NULL,
  `TypeExter` smallint(6) DEFAULT NULL,
  `AppType` int(11) DEFAULT '1',
  `BindImage` varchar(100) DEFAULT NULL,
  `RightType` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`TypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wf_worktype
-- ----------------------------

-- ----------------------------
-- View structure for port_dept
-- ----------------------------
DROP VIEW IF EXISTS `port_dept`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `port_dept` AS select `p_un_group`.`GROUP_ID` AS `No`,`p_un_group`.`GROUP_CH_NAME` AS `Name`,`p_un_group`.`PARENT_GROUP_ID` AS `ParentNo`,NULL AS `NameOfPath`,NULL AS `TreeNo`,NULL AS `Leader`,NULL AS `Tel`,NULL AS `FK_DeptType`,NULL AS `Idx`,NULL AS `IsDir` from `p_un_group` ;

-- ----------------------------
-- View structure for port_deptempstation
-- ----------------------------
DROP VIEW IF EXISTS `port_deptempstation`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `port_deptempstation` AS select `p_un_user_group`.`USER_GRUOP_ID` AS `MyPK`,`p_un_user_group`.`USER_ID` AS `FK_Emp`,`p_un_user_group`.`GROUP_ID` AS `FK_Dept`,`p_un_user_group`.`POSITION_ID` AS `FK_Station` from `p_un_user_group` ;

-- ----------------------------
-- View structure for port_deptstation
-- ----------------------------
DROP VIEW IF EXISTS `port_deptstation`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `port_deptstation` AS select distinct `p_un_user_group`.`GROUP_ID` AS `FK_Dept`,`p_un_user_group`.`POSITION_ID` AS `FK_Station` from `p_un_user_group` ;

-- ----------------------------
-- View structure for port_emp
-- ----------------------------
DROP VIEW IF EXISTS `port_emp`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `port_emp` AS select distinct `p_un_user_base_info`.`USER_ID_CARD_NUMBER` AS `No`,`p_un_user_base_info`.`NAME` AS `Name`,`p_un_user_base_info`.`USER_PWD` AS `Pass`,`p_un_user_group`.`GROUP_ID` AS `FK_Dept`,`p_un_user_base_info`.`USER_HOUSE_PHONE` AS `SID`,`p_un_user_base_info`.`MOBILE` AS `Tel`,`p_un_user_base_info`.`USER_EMAIL` AS `Email`,1 AS `NumOfDept`,`p_un_user_base_info`.`USER_DOMICILE` AS `Idx` from (`p_un_user_base_info` left join `p_un_user_group` on((`p_un_user_base_info`.`USER_ID` = `p_un_user_group`.`USER_ID`))) ;

-- ----------------------------
-- View structure for port_empdept
-- ----------------------------
DROP VIEW IF EXISTS `port_empdept`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `port_empdept` AS select `p_un_user_group`.`USER_ID` AS `FK_Emp`,`p_un_user_group`.`GROUP_ID` AS `FK_Dept` from `p_un_user_group` ;

-- ----------------------------
-- View structure for port_empstation
-- ----------------------------
DROP VIEW IF EXISTS `port_empstation`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `port_empstation` AS select `p_un_user_group`.`USER_ID` AS `FK_Emp`,`p_un_user_group`.`POSITION_ID` AS `FK_Station` from `p_un_user_group` ;

-- ----------------------------
-- View structure for port_station
-- ----------------------------
DROP VIEW IF EXISTS `port_station`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `port_station` AS select `p_un_position`.`POSITION_ID` AS `No`,`p_un_position`.`NAME` AS `Name`,3 AS `StaGrade` from `p_un_position` ;

-- ----------------------------
-- View structure for v_flowstarter
-- ----------------------------
DROP VIEW IF EXISTS `v_flowstarter`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `v_flowstarter` AS select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`c`.`FK_Emp` AS `FK_Emp` from ((`wf_node` `a` join `wf_nodestation` `b`) join `port_empstation` `c`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`b`.`FK_Station` = `c`.`FK_Station`) and (`a`.`DeliveryWay` = 0)) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`c`.`FK_Emp` AS `FK_Emp` from ((`wf_node` `a` join `wf_nodedept` `b`) join `port_empdept` `c`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`b`.`FK_Dept` = `c`.`FK_Dept`) and (`a`.`DeliveryWay` = 1)) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`b`.`FK_Emp` AS `FK_Emp` from (`wf_node` `a` join `wf_nodeemp` `b`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`a`.`DeliveryWay` = 3)) ;

-- ----------------------------
-- View structure for wf_empworks
-- ----------------------------
DROP VIEW IF EXISTS `wf_empworks`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `wf_empworks` AS select `a`.`PRI` AS `PRI`,`a`.`WorkID` AS `WorkID`,`b`.`IsRead` AS `IsRead`,`a`.`Starter` AS `Starter`,`a`.`StarterName` AS `StarterName`,`a`.`WFState` AS `WFState`,`a`.`FK_Dept` AS `FK_Dept`,`a`.`DeptName` AS `DeptName`,`a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`a`.`PWorkID` AS `PWorkID`,`a`.`PFlowNo` AS `PFlowNo`,`b`.`FK_Node` AS `FK_Node`,`b`.`FK_NodeText` AS `NodeName`,`b`.`FK_Dept` AS `WorkerDept`,`a`.`Title` AS `Title`,`a`.`RDT` AS `RDT`,`b`.`RDT` AS `ADT`,`b`.`SDT` AS `SDT`,`b`.`FK_Emp` AS `FK_Emp`,`b`.`FID` AS `FID`,`a`.`FK_FlowSort` AS `FK_FlowSort`,`a`.`SDTOfNode` AS `SDTOfNode`,`b`.`PressTimes` AS `PressTimes`,`a`.`GuestNo` AS `GuestNo`,`a`.`GuestName` AS `GuestName`,`a`.`BillNo` AS `BillNo`,`a`.`FlowNote` AS `FlowNote`,`a`.`TodoEmps` AS `TodoEmps`,`a`.`TodoEmpsNum` AS `TodoEmpsNum`,`a`.`TaskSta` AS `TaskSta`,0 AS `ListType`,`a`.`Sender` AS `Sender`,((`b`.`AtPara` + _utf8'') + `a`.`AtPara`) AS `AtPara`,1 AS `MyNum` from (`wf_generworkflow` `a` join `wf_generworkerlist` `b`) where ((`b`.`IsEnable` = 1) and (`b`.`IsPass` = 0) and (`a`.`WorkID` = `b`.`WorkID`) and (`a`.`FK_Node` = `b`.`FK_Node`)) union select `a`.`PRI` AS `PRI`,`a`.`WorkID` AS `WorkID`,`b`.`Sta` AS `IsRead`,`a`.`Starter` AS `Starter`,`a`.`StarterName` AS `StarterName`,2 AS `WFState`,`a`.`FK_Dept` AS `FK_Dept`,`a`.`DeptName` AS `DeptName`,`a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`a`.`PWorkID` AS `PWorkID`,`a`.`PFlowNo` AS `PFlowNo`,`b`.`FK_Node` AS `FK_Node`,`b`.`NodeName` AS `NodeName`,`b`.`CCToDept` AS `WorkerDept`,`a`.`Title` AS `Title`,`a`.`RDT` AS `RDT`,`b`.`RDT` AS `ADT`,`b`.`RDT` AS `SDT`,`b`.`CCTo` AS `FK_Emp`,`b`.`FID` AS `FID`,`a`.`FK_FlowSort` AS `FK_FlowSort`,`a`.`SDTOfNode` AS `SDTOfNode`,0 AS `PressTimes`,`a`.`GuestNo` AS `GuestNo`,`a`.`GuestName` AS `GuestName`,`a`.`BillNo` AS `BillNo`,`a`.`FlowNote` AS `FlowNote`,`a`.`TodoEmps` AS `TodoEmps`,`a`.`TodoEmpsNum` AS `TodoEmpsNum`,0 AS `TaskSta`,1 AS `ListType`,`b`.`Rec` AS `Sender`,(_utf8'@IsCC=1' + `a`.`AtPara`) AS `AtPara`,1 AS `MyNum` from (`wf_generworkflow` `a` join `wf_cclist` `b`) where ((`a`.`WorkID` = `b`.`WorkID`) and (`b`.`Sta` <= 1)) ;
