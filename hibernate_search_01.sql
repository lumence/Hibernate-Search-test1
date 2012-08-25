/*
MySQL Data Transfer
Source Host: localhost
Source Database: hibernate_search_01
Target Host: localhost
Target Database: hibernate_search_01
Date: 2012/8/25 19:24:45
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `_name` varchar(255) DEFAULT NULL,
  `familyName` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `_age` int(11) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;