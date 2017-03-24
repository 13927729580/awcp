/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.6.19 : Database - platformcloud
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`platformcloud` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `platformcloud`;

/*Table structure for table `fw_mm_metamodel` */

DROP TABLE IF EXISTS `fw_mm_metamodel`;

CREATE TABLE `fw_mm_metamodel` (
  `modelClassId` bigint(20) DEFAULT NULL,
  `modelName` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `modelCode` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `modelDesc` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `tableName` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `projectName` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `modelType` int(11) DEFAULT NULL,
  `modelSynchronization` int(11) DEFAULT NULL,
  `modelValid` int(11) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `SYSTEM_ID` bigint(20) DEFAULT NULL COMMENT '系统ID',
  PRIMARY KEY (`id`),
  KEY `modelName` (`modelName`)
) ENGINE=InnoDB AUTO_INCREMENT=1751 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `fw_mm_metamodel` */

insert  into `fw_mm_metamodel`(`modelClassId`,`modelName`,`modelCode`,`modelDesc`,`tableName`,`projectName`,`modelType`,`modelSynchronization`,`modelValid`,`id`,`SYSTEM_ID`) values (1,'模型','model','描述','model','model',NULL,0,0,1708,NULL),(1,'hao','hao','hao','ahoa','hao',1,1,0,1731,NULL),(1,'写','writer','write','writer','write',1,1,0,1742,NULL),(NULL,'树','tree','自关联','tree','大项目',1,1,0,1745,NULL),(NULL,'国家','country','对应关系','country','大项目',1,1,0,1746,NULL),(NULL,'城市','chengshi','对应关系','cityc','0',1,0,0,1747,NULL),(83,'请假申请表','ask','请假申请表','ask_table','waiting',1,1,0,1748,NULL),(2,'test','test','  ','test','22',2,1,0,1749,NULL),(83,'人员信息','person','无','myPerson','hello',1,1,0,1750,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
