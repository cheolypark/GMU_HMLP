CREATE DATABASE  IF NOT EXISTS `test_heater_for_experiment_discrete` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `test_heater_for_experiment_discrete`;

-- MySQL dump 10.13  Distrib 5.7.12, for Win32 (AMD64)
--
-- Host: localhost    Database: test_heater_for_experiment_discrete
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `heateractuator_item`
--

DROP TABLE IF EXISTS `heateractuator_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `heateractuator_item` (
  `TimeID` char(45) NOT NULL,
  `energy` char(45) DEFAULT NULL,
  PRIMARY KEY (`TimeID`),
  CONSTRAINT `heateractuator_item_ibfk_1` FOREIGN KEY (`TimeID`) REFERENCES `time` (`TimeID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `heateractuator_item`
--

LOCK TABLES `heateractuator_item` WRITE;
/*!40000 ALTER TABLE `heateractuator_item` DISABLE KEYS */;
INSERT INTO `heateractuator_item` VALUES ('0','118256.45941032615'),('1','118859.23429451065'),('10','118256.25941032615'),('11','118859.23429451065'),('12','118460.2244688102'),('13','118317.27180783986'),('14','118313.59579440192'),('15','118135.46695848434'),('16','118206.47842493427'),('17','118636.2648115293'),('2','118460.5244688102'),('22','120009.2'),('23','120010.2'),('3','118317.97180783986'),('4','118313.69579440192'),('5','118135.36695848434'),('6','118206.67842493427'),('7','118636.5648115293'),('8','119071.31340800396'),('9','119071.31340800396');
/*!40000 ALTER TABLE `heateractuator_item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-11 17:11:51
