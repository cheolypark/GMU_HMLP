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
-- Table structure for table `slabinput_item`
--

DROP TABLE IF EXISTS `slabinput_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slabinput_item` (
  `TimeID` char(45) NOT NULL,
  `temperature` char(45) DEFAULT NULL,
  `grade` char(45) DEFAULT NULL,
  `volume` char(45) DEFAULT NULL,
  PRIMARY KEY (`TimeID`),
  CONSTRAINT `slabinput_item_ibfk_1` FOREIGN KEY (`TimeID`) REFERENCES `time` (`TimeID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slabinput_item`
--

LOCK TABLES `slabinput_item` WRITE;
/*!40000 ALTER TABLE `slabinput_item` DISABLE KEYS */;
INSERT INTO `slabinput_item` VALUES ('0','17.435405917816027','G1','small'),('1','11.407656721134089','G2','large'),('10','17.135405917816027','G1','small'),('11','11.207656721134089','G2','large'),('12','15.694755308639896','G1','small'),('13','16.120282220750023','G1','large'),('14','16.163041822018737','G1','large'),('15','18.346329805131433','G2','small'),('16','17.13321585835428','G2','small'),('17','13.834351845896946','G2','large'),('18','9.486865619619798','G2','large'),('19','19.156530947317002','G1','small'),('2','15.394755308639896','G1','small'),('20','17.22','G1','small'),('21','16.02','G2','small'),('3','16.820282220750023','G1','large'),('4','16.863041822018737','G1','large'),('5','18.646329805131433','G2','small'),('6','17.93321585835428','G2','small'),('7','13.634351845896946','G2','large'),('8','9.286865619619798','G2','large'),('9','18.156530947317002','G1','small');
/*!40000 ALTER TABLE `slabinput_item` ENABLE KEYS */;
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
