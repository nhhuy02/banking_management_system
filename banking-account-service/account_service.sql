-- MySQL dump 10.13  Distrib 9.0.1, for Linux (x86_64)
--
-- Host: localhost    Database: account_service
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `account_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `account_name` varchar(100) DEFAULT NULL,
  `balance` decimal(15,2) DEFAULT NULL,
  `status` enum('active','inactive','suspended') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`account_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,5,'TKTT KHACH HANG ',NULL,'active',NULL,'2024-09-16 13:48:13'),(2,6,'TKTT KHACH HANG ',0.00,'active','2024-09-16 13:57:46','2024-09-16 13:57:46'),(3,7,'TKTT KHACH HANG ',0.00,'active','2024-09-16 14:38:33','2024-09-16 14:38:33'),(4,8,'TKTT KHACH HANG ',0.00,'active','2024-09-16 15:30:18','2024-09-16 15:30:18'),(5,9,'TKTT KHACH HANG ',0.00,'active','2024-09-16 15:43:02','2024-09-16 15:43:02'),(6,10,'TKTT KHACH HANG ',0.00,'active','2024-09-16 16:06:23','2024-09-16 16:06:23'),(7,11,'TKTT KHACH HANG ',0.00,'active','2024-09-16 16:13:27','2024-09-16 16:13:27'),(8,12,'TKTT KHACH HANG ',0.00,'active','2024-09-16 16:30:57','2024-09-16 16:30:57'),(9,13,'TKTT KHACH HANG ',10000000000.00,'active','2024-09-16 16:40:21','2024-09-17 08:06:45');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card_types`
--

DROP TABLE IF EXISTS `card_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card_types` (
  `card_type_id` int NOT NULL AUTO_INCREMENT,
  `card_type_name` varchar(100) NOT NULL,
  `nfc` tinyint(1) DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`card_type_id`),
  UNIQUE KEY `card_type_name` (`card_type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card_types`
--

LOCK TABLES `card_types` WRITE;
/*!40000 ALTER TABLE `card_types` DISABLE KEYS */;
INSERT INTO `card_types` VALUES (1,'Napas',0,'Th thanh ton ni a ca Vit Nam.',1,'2024-09-17 10:06:58','2024-09-17 10:06:58'),(2,'MasterCard',1,'Th tn dng quc t do MasterCard pht hnh.',1,'2024-09-17 10:06:58','2024-09-17 10:06:58'),(3,'Visa',1,'Th tn dng quc t do Visa pht hnh.',1,'2024-09-17 10:06:58','2024-09-17 10:06:58'),(4,'American Express',1,'Th tn dng quc t do American Express pht hnh.',1,'2024-09-17 10:06:58','2024-09-17 10:06:58'),(5,'JCB',1,'Th tn dng quc t do Japan Credit Bureau pht hnh.',1,'2024-09-17 10:06:58','2024-09-17 10:06:58'),(6,'Discover',1,'Th tn dng quc t do Discover Financial Services pht hnh.',1,'2024-09-17 10:06:58','2024-09-17 10:06:58'),(7,'UnionPay',1,'Th tn dng quc t do China UnionPay pht hnh.',1,'2024-09-17 10:06:58','2024-09-17 10:06:58');
/*!40000 ALTER TABLE `card_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cards`
--

DROP TABLE IF EXISTS `cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cards` (
  `card_id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL,
  `card_type_id` int NOT NULL,
  `card_number` varchar(20) NOT NULL,
  `balance` decimal(15,2) DEFAULT '0.00',
  `status` enum('active','inactive','closed') DEFAULT 'active',
  `opened_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `closed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`card_id`),
  UNIQUE KEY `card_number` (`card_number`),
  KEY `account_id` (`account_id`),
  KEY `card_type_id` (`card_type_id`),
  CONSTRAINT `cards_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
  CONSTRAINT `cards_ibfk_2` FOREIGN KEY (`card_type_id`) REFERENCES `card_types` (`card_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cards`
--

LOCK TABLES `cards` WRITE;
/*!40000 ALTER TABLE `cards` DISABLE KEYS */;
/*!40000 ALTER TABLE `cards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `savings_accounts`
--

DROP TABLE IF EXISTS `savings_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `savings_accounts` (
  `savings_account_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `account_name` varchar(100) DEFAULT NULL,
  `balance` decimal(15,2) DEFAULT '0.00',
  `status` enum('active','inactive','closed') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`savings_account_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `savings_accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `savings_accounts`
--

LOCK TABLES `savings_accounts` WRITE;
/*!40000 ALTER TABLE `savings_accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `savings_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(50) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `unique_phone_number` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','$2a$10$ijEH1raoeHkKFs2t17hDse3JdKHC5MMf22GGLEPAvB5SVR4BnimiO','admin',NULL,NULL,'2024-09-16 03:59:05'),(2,'ahihi','$2a$10$sHZ6f57T94G6BVAKm676lOmaM4NU/grOrRtcKqY0UQDuLRqk5rBtK','customer',NULL,'2024-09-16 02:47:53',NULL),(3,'0352364178','$2a$10$YdFJzWZjS6EHzSCSYb9y1uqIqLmrGCNUm5TETvsYnK5sGNqg5RG/e','admin','0352364178','2024-09-16 03:30:46','2024-09-16 07:04:24'),(5,'0965278419','$2a$10$kLfpZXGKQMP075bTG7uMWu2aVGMauVJYMN3sqSXZjOGYi2XUcOwQG','customer','0965278419','2024-09-16 06:48:13',NULL),(6,'0559483491','$2a$10$kLHEeiV99oD76dI7UD4FM.8dG.QHCMHdFxY5BT8VR82DQVIWM21g.','customer','0559483491','2024-09-16 06:57:46','2024-09-17 09:46:00'),(7,'0349145640','$2a$10$D5BWWcTu35lQJ05Sg4XkIelSgr3BQU7xceGJvfvNhOzs25LlYrtzW','customer','0349145640','2024-09-16 14:38:33','2024-09-16 14:38:33'),(8,'0912379780','$2a$10$pXJWFtk5l.rPJyyMjIJjTeWS8e5sn0Y9Cvp8k7IYJNNfUOy1/Hs9.','customer','0912379780','2024-09-16 15:30:18','2024-09-16 15:30:18'),(9,'0977082278','$2a$10$y83NDIAA5CdrfPQxv87GD.qdTAulfDL0CEsgIE03suQDG6Eq/AaC.','customer','0977082278','2024-09-16 15:43:02','2024-09-16 15:43:02'),(10,'0987561422','$2a$10$ZMAuBCksZCS/1/w9zdbxkuJSf58hShpUWHPP3mNkaj0m56VVvKw8y','customer','0987561422','2024-09-16 16:06:22','2024-09-16 16:06:23'),(11,'0916463568','$2a$10$0LNvB8bFtgUufgY799RS7u6anh9sp88Tmc2ZTmog0MVmXRg8E3MbS','customer','0916463568','2024-09-16 16:13:27','2024-09-16 16:13:27'),(12,'0748651530','$2a$10$gMREp5WEVrjWI5/2r6t3dOQtE67MAJGADyolFLkBvgod/bTITzf1m','customer','0748651530','2024-09-16 16:30:57','2024-09-16 16:30:57'),(13,'0485626563','$2a$10$DCkfAG94bZtSoTw0HG6v2e/z6QELEbJCNT8ANtQ6HmS.ebgLd/h42','employee','0485626563','2024-09-16 16:40:21','2024-09-16 09:43:53');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-18  3:55:08
