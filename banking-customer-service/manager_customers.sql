mysqldump: [Warning] Using a password on the command line interface can be insecure.
-- MySQL dump 10.13  Distrib 9.0.1, for Linux (x86_64)
--
-- Host: localhost    Database: manager_customers
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
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `permanent_address` tinytext,
  `current_address` tinytext,
  `kyc_id` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_id` (`account_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone_number` (`phone_number`),
  KEY `fk_kyc_id` (`kyc_id`),
  KEY `idx_account_id` (`account_id`),
  CONSTRAINT `fk_kyc_id` FOREIGN KEY (`kyc_id`) REFERENCES `kyc` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,1001,'Nguyen Van A','1990-01-15','MALE','nguyenvana@example.com','0901234567','123 Main St, Hanoi','456 Secondary St, Hanoi',1,'2024-08-30 04:16:12','2024-09-11 03:36:01'),(2,1002,'Nguyen Van Z','1985-06-20','MALE','nguyenvanz@gmail.com','0912345678','Viet Nam','Viet Nam',26,'2024-08-30 04:16:12','2024-09-11 03:36:01'),(3,1003,'Le Van C','1992-03-05','MALE','levanc@example.com','0923456789','345 Main St, Hanoi','678 Secondary St, Hanoi',3,'2024-08-30 04:16:12','2024-09-11 03:36:01'),(4,1004,'Nguyen Minh Tam','2002-07-10','MALE','mintamhy2002@gmail.com','0352364178','123 Main St, Hanoi','456 Secondary St, Hanoi',4,'2024-08-30 06:43:39','2024-09-11 03:36:01'),(5,1010,'Nguyen Minh Tam','2002-05-12','MALE','minhtamhy2002@gmail.com','0559483491','Hung Yen','Ha Noi',5,'2024-08-30 06:51:27','2024-09-11 03:36:01'),(6,1978,'Chu Thị Doan','1978-09-01','FEMALE','chuthidoan@gmail.com','965278419','Hưng Yên','Hưng Yên',19,'2024-09-06 08:04:28','2024-09-11 03:36:01');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers_status_history`
--

DROP TABLE IF EXISTS `customers_status_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers_status_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `status` enum('active','suspended','closed') DEFAULT 'active',
  `changed_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `customers_status_history_ibfk_1` (`customer_id`),
  CONSTRAINT `customers_status_history_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers_status_history`
--

LOCK TABLES `customers_status_history` WRITE;
/*!40000 ALTER TABLE `customers_status_history` DISABLE KEYS */;
INSERT INTO `customers_status_history` VALUES (11,1,'active','2024-08-30 07:48:45'),(12,2,'active','2024-08-30 07:48:45'),(13,3,'active','2024-08-30 07:48:45'),(14,4,'active','2024-08-30 07:48:45'),(15,5,'active','2024-08-30 07:48:45'),(16,1,'active','2024-09-01 10:15:30'),(17,1,'suspended','2024-09-02 14:22:45'),(18,1,'closed','2024-09-03 09:05:20'),(19,1,'suspended','2024-09-05 06:50:50'),(20,5,'suspended','2024-09-05 06:51:25'),(25,6,'active','2024-09-09 01:58:04'),(26,6,'active','2024-09-09 02:06:17'),(27,2,'active','2024-09-09 09:08:00'),(28,2,'active','2024-09-09 09:08:01'),(29,2,'active','2024-09-09 09:08:02'),(30,2,'active','2024-09-09 09:13:45'),(31,2,'active','2024-09-09 09:13:46'),(32,2,'active','2024-09-09 09:13:47'),(33,2,'active','2024-09-09 09:14:49');
/*!40000 ALTER TABLE `customers_status_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kyc`
--

DROP TABLE IF EXISTS `kyc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kyc` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `verification_status` enum('verified','pending') NOT NULL DEFAULT 'pending',
  `verification_date` timestamp NULL DEFAULT NULL,
  `document_type` enum('id_card','passport','driver_license') DEFAULT NULL,
  `document_number` varchar(255) DEFAULT NULL,
  `document_image_url` tinytext,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `document_type` (`document_type`,`document_number`),
  KEY `FK2qhciw6flmi7mamir7g88jv0q` (`customer_id`),
  CONSTRAINT `FK2qhciw6flmi7mamir7g88jv0q` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kyc`
--

LOCK TABLES `kyc` WRITE;
/*!40000 ALTER TABLE `kyc` DISABLE KEYS */;
INSERT INTO `kyc` VALUES (1,'verified','2024-09-09 04:28:54','passport','17485962123',NULL,'2024-08-30 04:14:39','2024-08-30 04:14:39',NULL),(2,'verified','2024-09-05 09:46:46','passport',NULL,NULL,'2024-08-30 04:14:39','2024-08-30 04:14:39',NULL),(3,'verified','2024-09-05 09:58:41','id_card','12345678912345',NULL,'2024-08-30 04:14:39','2024-08-30 04:14:39',NULL),(4,'pending',NULL,NULL,NULL,NULL,'2024-08-30 06:43:28','2024-08-30 06:43:28',NULL),(5,'pending',NULL,NULL,NULL,NULL,'2024-08-30 06:46:31','2024-08-30 06:46:31',NULL),(19,'pending',NULL,NULL,NULL,NULL,'2024-09-09 02:06:17',NULL,NULL),(20,'pending',NULL,NULL,NULL,NULL,'2024-09-09 09:08:00',NULL,NULL),(21,'pending',NULL,NULL,NULL,NULL,'2024-09-09 09:08:01',NULL,NULL),(22,'pending',NULL,NULL,NULL,NULL,'2024-09-09 09:08:02',NULL,NULL),(23,'pending',NULL,NULL,NULL,NULL,'2024-09-09 09:13:45',NULL,NULL),(24,'pending',NULL,NULL,NULL,NULL,'2024-09-09 09:13:46',NULL,NULL),(25,'pending',NULL,NULL,NULL,NULL,'2024-09-09 09:13:47',NULL,NULL),(26,'pending',NULL,NULL,NULL,NULL,'2024-09-09 09:14:49',NULL,NULL);
/*!40000 ALTER TABLE `kyc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_codes`
--

DROP TABLE IF EXISTS `verification_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verification_codes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `expires_at` timestamp NULL DEFAULT NULL,
  `is_verified` tinyint(1) DEFAULT '0',
  `used_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`,`code`),
  KEY `idx_customer_id` (`customer_id`),
  CONSTRAINT `verification_codes_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_codes`
--

LOCK TABLES `verification_codes` WRITE;
/*!40000 ALTER TABLE `verification_codes` DISABLE KEYS */;
INSERT INTO `verification_codes` VALUES (1,1,'408012','2024-08-30 06:40:00','2024-08-30 06:45:00',0,NULL),(2,4,'828846','2024-08-30 06:45:25','2024-08-30 06:50:25',0,NULL),(3,5,'013044','2024-08-30 06:52:40','2024-08-30 06:57:40',0,NULL),(4,5,'482961','2024-08-30 07:05:20','2024-08-30 07:10:20',1,'2024-08-30 07:07:03'),(5,5,'511740','2024-08-30 07:08:40','2024-08-30 07:13:40',1,'2024-08-30 07:09:01'),(6,5,'078687','2024-09-04 01:49:38','2024-09-04 01:54:38',1,'2024-09-04 01:51:11'),(7,5,'560115','2024-09-05 01:19:20','2024-09-05 01:24:20',1,'2024-09-05 01:20:05');
/*!40000 ALTER TABLE `verification_codes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-11  4:31:12
