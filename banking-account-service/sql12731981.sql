-- phpMyAdmin SQL Dump
-- version 4.7.1
-- https://www.phpmyadmin.net/
--
-- Host: sql12.freesqldatabase.com
-- Generation Time: Oct 03, 2024 at 02:30 AM
-- Server version: 5.5.62-0ubuntu0.14.04.1
-- PHP Version: 7.0.33-0ubuntu0.16.04.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sql12731981`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `account_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `account_name` varchar(100) DEFAULT NULL,
  `account_number` varchar(20) DEFAULT NULL,
  `balance` decimal(15,2) DEFAULT NULL,
  `status` enum('active','suspended','closed') NOT NULL DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`account_id`, `user_id`, `account_name`, `account_number`, `balance`, `status`, `created_at`, `updated_at`) VALUES
(1, 5, 'TKTT KHACH HANG ', '8969745980', NULL, 'active', NULL, '2024-09-19 02:27:24'),
(2, 6, 'TKTT KHACH HANG ', '5986682898', '0.00', 'active', '2024-09-16 21:57:46', '2024-09-19 02:27:24'),
(3, 7, 'TKTT KHACH HANG ', '2024176828', '0.00', 'active', '2024-09-16 22:38:33', '2024-09-19 02:27:24'),
(4, 8, 'TKTT KHACH HANG ', '9160835720', '0.00', 'active', '2024-09-16 23:30:18', '2024-09-19 02:27:24'),
(5, 9, 'TKTT KHACH HANG ', '2731648399', '0.00', 'active', '2024-09-16 23:43:02', '2024-09-19 02:27:24'),
(6, 10, 'TKTT KHACH HANG ', '3175735107', '0.00', 'active', '2024-09-17 00:06:23', '2024-09-19 02:27:24'),
(8, 12, 'TKTT KHACH HANG ', '4891448041', '0.00', 'active', '2024-09-17 00:30:57', '2024-09-19 02:27:24'),
(9, 13, 'TKTT KHACH HANG ', '3406048634', '10000000000.00', 'active', '2024-09-17 00:40:21', '2024-09-19 02:27:24'),
(10, 14, 'TKTT KHACH HANG ', '1355899322', '0.00', 'active', '2024-09-19 01:14:25', '2024-09-19 02:27:24'),
(11, 15, 'TKTT KHACH HANG ', '4561350983', '0.00', 'active', '2024-09-19 01:27:11', '2024-09-19 02:27:24'),
(14, 20, 'TKTT KHACH HANG ', '8234477962', '0.00', 'active', '2024-09-19 17:56:13', '2024-09-19 17:56:13'),
(17, 23, 'TKTT KHACH HANG ', '1079743598', '0.00', 'active', '2024-09-19 18:33:45', '2024-09-19 18:33:45'),
(18, 24, 'TKTT KHACH HANG ', '9052993272', '0.00', 'active', '2024-09-19 18:56:09', '2024-09-19 18:56:09'),
(20, 26, 'TKTT KHACH HANG ', '6139718715', '0.00', 'active', '2024-09-19 19:18:54', '2024-09-19 19:18:54'),
(21, 27, 'TKTT KHACH HANG ', '7116927949', '0.00', 'active', '2024-09-19 19:21:05', '2024-09-19 19:21:05'),
(22, 28, 'TKTT KHACH HANG ', '2359843907', '0.00', 'active', '2024-09-23 23:44:50', '2024-09-23 23:44:50'),
(23, 29, 'TKTT KHACH HANG ', '9532235271', '0.00', 'active', '2024-09-26 21:48:03', '2024-09-26 21:48:03'),
(24, 30, 'TKTT KHACH HANG ', '9970222080', '0.00', 'active', '2024-09-26 22:09:17', '2024-09-26 22:09:17'),
(25, 31, 'TKTT KHACH HANG ', '1465085005', '0.00', 'active', '2024-09-26 22:13:19', '2024-09-26 22:13:19'),
(31, 37, 'TKTT KHACH HANG ', '2479284332', '0.00', 'active', '2024-09-27 01:03:35', '2024-09-27 01:03:35'),
(32, 38, 'TKTT KHACH HANG ', '7493476316', '0.00', 'active', '2024-09-27 17:05:12', '2024-09-27 17:05:12'),
(33, 39, 'TKTT KHACH HANG ', '5766955651', '0.00', 'active', '2024-09-27 17:10:36', '2024-09-27 17:10:36'),
(34, 40, 'TKTT KHACH HANG ', '5285486823', '0.00', 'active', '2024-09-27 17:22:36', '2024-09-27 17:22:36'),
(35, 41, 'TKTT KHACH HANG ', '7659216863', '0.00', 'active', '2024-09-27 17:31:53', '2024-09-27 17:31:53'),
(36, 42, 'TKTT KHACH HANG ', '9966309651', '0.00', 'active', '2024-09-27 17:33:19', '2024-09-27 17:33:19'),
(37, 43, 'TKTT KHACH HANG ', '5710332079', '0.00', 'active', '2024-09-27 17:40:26', '2024-09-27 17:40:26'),
(38, 44, 'TKTT KHACH HANG ', '4316623976', '0.00', 'active', '2024-09-27 17:52:14', '2024-09-27 17:52:14'),
(39, 45, 'TKTT KHACH HANG ', '1041170727', '0.00', 'active', '2024-09-27 18:02:36', '2024-09-27 18:02:36'),
(40, 46, 'TKTT KHACH HANG ', '4248887108', '0.00', 'active', '2024-09-27 18:08:06', '2024-09-27 18:08:06'),
(41, 47, 'TKTT KHACH HANG ', '1091550403', '0.00', 'active', '2024-09-27 18:23:50', '2024-09-27 18:23:50'),
(42, 48, 'TKTT KHACH HANG ', '7274070477', '0.00', 'active', '2024-09-27 18:43:57', '2024-09-27 18:43:57'),
(43, 49, 'TKTT KHACH HANG ', '4298927295', '0.00', 'active', '2024-09-27 18:44:39', '2024-09-27 18:44:39'),
(44, 50, 'TKTT KHACH HANG ', '5261476195', '0.00', 'active', '2024-09-27 18:45:10', '2024-09-27 18:45:10'),
(45, 51, 'TKTT KHACH HANG ', '9202500351', '0.00', 'active', '2024-09-27 18:46:36', '2024-09-27 18:46:36'),
(46, 52, 'TKTT KHACH HANG ', '8407892446', '0.00', 'active', '2024-09-27 18:47:05', '2024-09-27 18:47:05'),
(47, 53, 'TKTT KHACH HANG ', '7447672533', '0.00', 'active', '2024-09-27 18:47:28', '2024-09-27 18:47:28'),
(48, 54, 'TKTT KHACH HANG ', '7524272235', '0.00', 'active', '2024-09-27 18:47:50', '2024-09-27 18:47:50'),
(49, 55, 'TKTT KHACH HANG ', '7263052483', '0.00', 'active', '2024-09-27 18:48:18', '2024-09-27 18:48:18'),
(50, 56, 'TKTT KHACH HANG ', '2012243917', '0.00', 'active', '2024-09-27 18:48:45', '2024-09-27 18:48:45'),
(51, 57, 'TKTT KHACH HANG ', '9411171865', '0.00', 'active', '2024-09-27 19:08:42', '2024-09-27 19:08:42'),
(52, 58, 'TKTT KHACH HANG ', '8629336759', '0.00', 'active', '2024-09-27 22:31:30', '2024-09-27 22:31:30'),
(56, 62, 'TKTT KHACH HANG ', '3419006557', '0.00', 'active', '2024-09-28 00:32:52', '2024-09-28 00:32:52'),
(57, 63, 'TKTT KHACH HANG ', '2243903144', '0.00', 'active', '2024-09-28 00:34:16', '2024-09-28 00:34:16'),
(58, 64, 'TKTT KHACH HANG ', '9369466244', '0.00', 'active', '2024-09-30 22:11:56', '2024-09-30 22:11:56'),
(61, 65, 'TKTT KHACH HANG ', '6982893386', '0.00', 'active', '2024-10-02 18:12:40', '2024-10-02 18:12:40'),
(62, 66, 'GIAI NGAN KHOAN VAY NGAN HANG', '0123456789', '1000000000000.00', 'active', '2024-10-02 18:18:50', '2024-10-02 03:42:28'),
(63, 67, 'THU HOI KHOAN VAY NGAN HANG', '1234567890', '40000000000.00', 'active', '2024-10-02 18:19:07', '2024-10-02 03:42:41'),
(64, 68, 'TKTT KHACH HANG ', '5858824364', '0.00', 'active', '2024-10-03 17:18:00', '2024-10-03 17:18:00');

-- --------------------------------------------------------

--
-- Table structure for table `cards`
--

CREATE TABLE `cards` (
  `card_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `card_type_id` int(11) NOT NULL,
  `card_number` varchar(20) NOT NULL,
  `balance` decimal(15,2) DEFAULT '0.00',
  `status` enum('active','inactive','closed') DEFAULT 'active',
  `image_url` varchar(255) DEFAULT NULL,
  `opened_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `closed_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cards`
--

INSERT INTO `cards` (`card_id`, `account_id`, `card_type_id`, `card_number`, `balance`, `status`, `image_url`, `opened_at`, `closed_at`) VALUES
(1, 8, 1, '9774877435', '0.00', 'active', NULL, NULL, NULL),
(2, 9, 1, '3112584905', '0.00', 'active', NULL, '2024-09-20 19:04:54', NULL),
(3, 9, 3, '1878402956', '0.00', 'active', NULL, '2024-09-23 18:39:10', NULL),
(4, 1, 1, '7809630329', '0.00', 'active', NULL, '2024-09-28 00:13:08', NULL),
(5, 1, 1, '2073794415', '0.00', 'active', NULL, '2024-09-28 00:13:46', NULL),
(6, 1, 1, '2730238185', '0.00', 'active', NULL, '2024-09-28 00:13:49', NULL),
(7, 1, 1, '1749946680', '0.00', 'active', NULL, '2024-09-28 00:41:00', NULL),
(8, 3, 1, '4609812144', '0.00', 'active', NULL, '2024-09-28 00:41:28', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `card_registration_requests`
--

CREATE TABLE `card_registration_requests` (
  `id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `card_type_id` int(11) NOT NULL,
  `request_status` enum('pending','approved','rejected') DEFAULT 'pending',
  `request_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `review_date` timestamp NULL DEFAULT NULL,
  `notes` tinytext
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `card_registration_requests`
--

INSERT INTO `card_registration_requests` (`id`, `account_id`, `card_type_id`, `request_status`, `request_date`, `review_date`, `notes`) VALUES
(1, 9, 3, 'approved', '2024-09-20 21:43:30', '2024-09-23 18:39:10', 'Good profile and handsome :>'),
(2, 9, 2, 'pending', '2024-09-20 23:36:57', NULL, NULL),
(3, 8, 2, 'pending', '2024-09-20 23:37:30', NULL, NULL),
(4, 11, 3, 'pending', '2024-09-20 23:47:57', NULL, NULL),
(6, 8, 5, 'rejected', '2024-09-23 21:59:04', '2024-09-23 22:00:05', 'Fail');

-- --------------------------------------------------------

--
-- Table structure for table `card_types`
--

CREATE TABLE `card_types` (
  `card_type_id` int(11) NOT NULL,
  `card_type_name` varchar(100) NOT NULL,
  `nfc` tinyint(1) DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `card_types`
--

INSERT INTO `card_types` (`card_type_id`, `card_type_name`, `nfc`, `description`, `is_active`, `created_at`, `updated_at`) VALUES
(1, 'Napas', 0, 'Thẻ thanh toán nội địa của Việt Nam.', 1, '2024-09-17 18:06:58', '2024-09-17 18:06:58'),
(2, 'MasterCard', 1, 'Thẻ tín dụng quốc tế do MasterCard phát hành.', 1, '2024-09-17 18:06:58', '2024-09-17 18:06:58'),
(3, 'Visa', 1, 'Thẻ tín dụng quốc tế do Visa phát hành.', 1, '2024-09-17 18:06:58', '2024-09-17 18:06:58'),
(4, 'American Express', 1, 'Thẻ tín dụng quốc tế do American Express phát hành.', 1, '2024-09-17 18:06:58', '2024-09-17 18:06:58'),
(5, 'JCB', 1, 'Thẻ tín dụng quốc tế do Japan Credit Bureau phát hành.', 1, '2024-09-17 18:06:58', '2024-09-17 18:06:58'),
(6, 'Discover', 1, 'Thẻ tín dụng quốc tế do Discover Financial Services phát hành.', 1, '2024-09-17 18:06:58', '2024-09-17 18:06:58'),
(7, 'UnionPay', 1, 'Thẻ tín dụng quốc tế do China UnionPay phát hành.', 0, '2024-09-17 18:06:58', '2024-09-18 22:25:06');

-- --------------------------------------------------------

--
-- Table structure for table `savings_accounts`
--

CREATE TABLE `savings_accounts` (
  `savings_account_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `account_name` varchar(100) DEFAULT NULL,
  `balance` decimal(15,2) DEFAULT '0.00',
  `status` enum('active','inactive','closed') DEFAULT 'active',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `savings_accounts`
--

INSERT INTO `savings_accounts` (`savings_account_id`, `user_id`, `account_name`, `balance`, `status`, `created_at`, `updated_at`) VALUES
(1, 13, 'Savings Account', '0.00', 'active', '2024-09-24 00:18:00', '2024-09-24 00:18:00'),
(2, 46, 'Savings Account', '0.00', 'active', '2024-09-27 19:07:15', '2024-09-27 19:07:15'),
(3, 9, 'Savings Account', '0.00', 'active', '2024-09-27 22:28:54', '2024-09-27 22:28:54'),
(4, 52, 'Savings Account', '0.00', 'active', '2024-09-27 22:42:18', '2024-09-27 22:42:18'),
(5, 1, 'Savings Account', '0.00', 'active', '2024-09-27 23:25:10', '2024-09-27 23:25:10'),
(6, 2, 'Savings Account', '0.00', 'active', '2024-09-27 23:43:28', '2024-09-27 23:43:28'),
(7, 3, 'Savings Account', '0.00', 'active', '2024-09-27 23:46:58', '2024-09-27 23:46:58');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(50) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `role`, `phone_number`, `created_at`, `updated_at`) VALUES
(1, 'admin', '$2a$10$ijEH1raoeHkKFs2t17hDse3JdKHC5MMf22GGLEPAvB5SVR4BnimiO', 'admin', NULL, NULL, '2024-09-16 11:59:05'),
(2, 'ahihi', '$2a$10$sHZ6f57T94G6BVAKm676lOmaM4NU/grOrRtcKqY0UQDuLRqk5rBtK', 'customer', NULL, '2024-09-16 10:47:53', NULL),
(3, '0352852369', '$2a$10$YdFJzWZjS6EHzSCSYb9y1uqIqLmrGCNUm5TETvsYnK5sGNqg5RG/e', 'admin', '0485626548', '2024-09-16 11:30:46', '2024-09-30 10:37:08'),
(5, '0965278419', '$2a$10$kLfpZXGKQMP075bTG7uMWu2aVGMauVJYMN3sqSXZjOGYi2XUcOwQG', 'customer', '0965278419', '2024-09-16 14:48:13', NULL),
(6, '0559483491', '$2a$10$fdaq3adWV/Q0/OLsN2cELODeIV5S4qYywhdP2NNmgSwpWphFd5U76', 'customer', '0559483491', '2024-09-16 14:57:46', '2024-10-01 00:23:38'),
(7, '0349145640', '$2a$10$D5BWWcTu35lQJ05Sg4XkIelSgr3BQU7xceGJvfvNhOzs25LlYrtzW', 'customer', '0349145640', '2024-09-16 22:38:33', '2024-09-16 22:38:33'),
(8, '0912379780', '$2a$10$pXJWFtk5l.rPJyyMjIJjTeWS8e5sn0Y9Cvp8k7IYJNNfUOy1/Hs9.', 'customer', '0912379780', '2024-09-16 23:30:18', '2024-09-16 23:30:18'),
(9, '0977082278', '$2a$10$y83NDIAA5CdrfPQxv87GD.qdTAulfDL0CEsgIE03suQDG6Eq/AaC.', 'customer', '0977082278', '2024-09-16 23:43:02', '2024-09-16 23:43:02'),
(10, '0987561422', '$2a$10$ZMAuBCksZCS/1/w9zdbxkuJSf58hShpUWHPP3mNkaj0m56VVvKw8y', 'customer', '0987561422', '2024-09-17 00:06:22', '2024-09-17 00:06:23'),
(12, '0748651530', '$2a$10$I.W1dXvB1yFsFYv8YxwKseFkJcxmuBTIz6O5JbgIReq1hgYkMizuy', 'customer', '0748651530', '2024-09-17 00:30:57', '2024-10-01 16:40:48'),
(13, '0352364178', '$2a$10$mBrpRiL18dFbUvvs51SYkOK1sdkPzOwilASI/gyaAP6.WfiVrJ6bi', 'employee', '0352364178', '2024-09-17 00:40:21', '2024-09-30 10:37:32'),
(14, '0974158963', '$2a$10$xgYSl1SQ6KLNdqMqBZKP3uotzQJLUZZlrkcKmn1d8/rAyow2z79cC', 'customer', '0974158963', '2024-09-19 01:14:25', '2024-09-19 01:14:25'),
(15, '0356987563', '$2a$10$gXYld7x.FumAEECVxsR5Z.uB9ZFaNp61Ib36OtGBYEv52r7SvP8CK', 'customer', '0356987563', '2024-09-19 01:27:11', '2024-09-19 01:27:11'),
(20, '0789654123', '$2a$10$16ypjPCwLYQ2j4h85q/5GOF0yxC01vBw8zN0nAb766875Z/JFrfcy', 'customer', '0789654123', '2024-09-19 17:56:13', '2024-09-19 17:56:13'),
(23, '0294787561', '$2a$10$tzESLzrKudKJczRbwvN0..h8.V18bPapTiDN5B1myfEd11rZOtHxO', 'customer', '0294787561', '2024-09-19 18:33:45', '2024-09-19 18:33:45'),
(24, '0891461544', '$2a$10$BIuYw21j7CmQlguhwfPSHu5M5yoTeEvPqBStbgIDejhNxR.X6CHbm', 'customer', '0891461544', '2024-09-19 18:56:09', '2024-09-19 18:56:09'),
(26, '0596934135', '$2a$10$.ZqlrgNzpP0Qb1XRtI9CR.v2QOmr7l4S9kUKte5up5n9Fg3zSXVgq', 'customer', '0596934135', '2024-09-19 19:18:54', '2024-09-19 19:18:54'),
(27, '0596777135', '$2a$10$EP6wzfVz7.Q7qOMQdkgsE.NVTIhj9QYuJpHf50GaC0s5Jfn/Avk5C', 'customer', '0596777135', '2024-09-19 19:21:04', '2024-09-19 19:21:04'),
(28, '0777775621', '$2a$10$0DmjC9mxzvOshVzSSKkCz.y.HjJejbhNfh1lFQAtfxK6MfNK..hFm', 'customer', '0777775621', '2024-09-23 23:44:50', '2024-09-23 23:44:50'),
(29, '0581264717', '$2a$10$2DTrIYUM3WPnzC6bw6Jgbecxn4vTMn4qstmMIVDvCBnshMmANHwZa', 'customer', '0581264717', '2024-09-26 21:48:03', '2024-09-26 21:48:03'),
(30, '0856189465', '$2a$10$ygbJ8jR44kaa.wMl42sgPeo6SEZdUlLontISlk70e8wRKiI10sdBC', 'customer', '0856189465', '2024-09-26 22:09:17', '2024-09-26 22:09:17'),
(31, '0958563247', '$2a$10$FqD/FLX6CtZ4CoEJYDSxmuimrBTS8RdRhbAl/bK7Vx/5jRNphzpf.', 'customer', '0958563247', '2024-09-26 22:13:19', '2024-09-26 22:13:19'),
(37, '0123456789', '$2a$10$wyraHmciXKobKIJ0sZijX.c8GZxNtXveEOsgnd7ba9Y87p9HK8uU.', 'customer', '0123456789', '2024-09-27 01:03:35', '2024-09-27 01:03:35'),
(38, '0123698745', '$2a$10$Yd6uPc1zDs3/5sE2x323muVnf4E/0VMll6qdoYlA2zGXyExlgjUpK', 'customer', '0123698745', '2024-09-27 17:05:12', '2024-09-27 17:05:12'),
(39, '5746786876', '$2a$10$TLWBcS5PQMM32BTHjNA7YOaA/1PY/Ap4OVuzGblET52Qx/zW6H2B6', 'customer', '5746786876', '2024-09-27 17:10:36', '2024-09-27 17:10:36'),
(40, '0789456123', '$2a$10$jxJR1zalS66qQT/Rl3Q6dOFAHBneRRzeX4nZcYgJpwngeYHInyhzS', 'customer', '0789456123', '2024-09-27 17:22:36', '2024-09-27 17:22:36'),
(41, '0147852369', '$2a$10$puJXbXvpTi7ZvpxmupVdXe3Oy/T9e6.HJ5.NoLqtvGXxCvplj9SrC', 'customer', '0147852369', '2024-09-27 17:31:52', '2024-09-27 17:31:52'),
(42, '0321654987', '$2a$10$VWULH97JJH8kh5EB2tDegu2v.ULFVZFlSe4QBBSs6WUxzbMP5trt2', 'customer', '0321654987', '2024-09-27 17:33:19', '2024-09-27 17:33:19'),
(43, '0897564516', '$2a$10$SlT4KV3SHbiF4vCVOf4QdOmL2XoMoY0qbogHiAxvEL8XK4/PT9bXi', 'customer', '0897564516', '2024-09-27 17:40:26', '2024-09-27 17:40:26'),
(44, '0897564517', '$2a$10$u84jCQNd0ETW44vMkJJhieVH1m72/.nd3.ZAmh9Uv4el6C08pu.7m', 'customer', '0897564517', '2024-09-27 17:52:14', '2024-09-27 17:52:14'),
(45, '0849456156', '$2a$10$2k5Ybcoi148f5XRrp7GNTed7dEa5H42Ot.NTVGUFjrA0QK27DWdNW', 'customer', '0849456156', '2024-09-27 18:02:36', '2024-09-27 18:02:36'),
(46, '0741258963', '$2a$10$ikzhnhpbpyKvffLzBPVMee2WQ/IcHQwrDfaeIntgq/2le0rpNS9NC', 'customer', '0741258963', '2024-09-27 18:08:06', '2024-09-27 18:08:06'),
(47, '0258741369', '$2a$10$fpDsVBtoo0hJMkHDR32bvePA2qwVLUXxfeXpMFl1t8D0TZiRrVMG2', 'customer', '0258741369', '2024-09-27 18:23:50', '2024-09-27 18:23:50'),
(48, '0523421141', '$2a$10$NeKkXFE7Y8WHfE6I02JOFO7So.iNhQ7pwb3fJaq4Q6yYMlAIUDamO', 'customer', '0523421141', '2024-09-27 18:43:57', '2024-09-27 18:43:57'),
(49, '0523421142', '$2a$10$R/hh7fAeOqTYK8QzQFQkm.Ik/rJ6Taqx//NDHuWa.nT4HvTOr6avW', 'customer', '0523421142', '2024-09-27 18:44:39', '2024-09-27 18:44:39'),
(50, '0523421144', '$2a$10$whYm3ZLlxj9UYcP86BDZXutsO2wVxWd9bhBbvurALnTU7WMf6CfoC', 'customer', '0523421144', '2024-09-27 18:45:10', '2024-09-27 18:45:10'),
(51, '0523421177', '$2a$10$FztY3AKh8f4EzqVP0S/bSehr9.znalHBJI1ehDd0EwPqcLeacukne', 'customer', '0523421177', '2024-09-27 18:46:36', '2024-09-27 18:46:36'),
(52, '0523221177', '$2a$10$XkIaL00RkonowRQyFQ5agOuGLdDTxxbLQbDc3FeUdhe5Sp2.G9C9K', 'customer', '0523221177', '2024-09-27 18:47:04', '2024-10-01 00:42:07'),
(53, '0511221177', '$2a$10$5/kYsOklnsylqfYaf759eechB.sA5iaxSef0jSNtGKhT6gqj6H.Gm', 'customer', '0511221177', '2024-09-27 18:47:28', '2024-09-27 18:47:28'),
(54, '0511290177', '$2a$10$.KQrVmfr71ZLAm6lXWuPNewVHotIGnBdO6PG6HEieGgBEdaRyZ5Ky', 'customer', '0511290177', '2024-09-27 18:47:50', '2024-09-27 18:47:50'),
(55, '0511211177', '$2a$10$nLy2g6LwNPUlLF06Z/tv1e0AvCgMMKPYi9voyvotGmJK94EMmyKcG', 'customer', '0511211177', '2024-09-27 18:48:18', '2024-09-27 18:48:18'),
(56, '0512211177', '$2a$10$SgwNq7XBUiIGoyknyQa4LO0YQIbMU5FYVvPCUCvWxl7GEBU8FINk6', 'customer', '0512211177', '2024-09-27 18:48:45', '2024-09-27 18:48:45'),
(57, '0812351177', '$2a$10$SJ8QwM4g7goLnJ7LFF5AmOm0c3YCXEFz96hZK.ArPWNRswr50Kx7G', 'customer', '0812351177', '2024-09-27 19:08:42', '2024-09-27 19:08:42'),
(58, '0000000000', '$2a$10$WDLaFKcnmVcqdHK5aSZxB.TRY2yAr3fFAWBv/FDF.qsCzxHM4GcqK', 'customer', '0000000000', '2024-09-27 22:31:30', '2024-09-27 22:31:30'),
(62, '0123456783', '$2a$10$dAp6Ug8OU5OE71aSrFp3vediajN2LQYNv05Xu1LiTtMTECReKHCVq', 'customer', '0123456783', '2024-09-28 00:32:52', '2024-09-28 00:32:52'),
(63, '1234567890', '$2a$10$DQbWDEae1CxRaZAI2yleue23B2WLo0swWjRONgUprihcp4RjoXD76', 'customer', '1234567890', '2024-09-28 00:34:16', '2024-09-28 00:34:16'),
(64, '0987412563', '$2a$10$zbumCbIQoB/x2/1Y/ek6juSqYzj/EVjrdi9SNDtONIRDHNs34C8VK', 'customer', '0987412563', '2024-09-30 22:11:56', '2024-09-30 22:11:56'),
(65, '0999999999', '$2a$10$Ybe7ymRRFe6gJkyLOl2Kae.5npMlFlxAQhptvbNPCNfgd93PRQvbO', 'customer', '0999999999', '2024-10-02 18:12:40', '2024-10-02 18:12:40'),
(66, '0999999998', '$2a$10$FC5jZNDDNRPV1A72w6YGPuq/5GlmeaWhcFJo.J0y933rTkqtUOLjO', 'customer', '0999999998', '2024-10-02 18:18:50', '2024-10-02 18:18:50'),
(67, '0999999997', '$2a$10$AgcDsE71W9oEDTg4DZaRruU3I5fy67Yp1/sduCcZTU//jZ2UqN8Rm', 'customer', '0999999997', '2024-10-02 18:19:07', '2024-10-02 18:19:07'),
(68, '0987654321', '$2a$10$uqC5c2IauYbpj9MjWyqKOeZQB.DsX0nPZyyrymfy4Isvc64zAiCkC', 'customer', '0987654321', '2024-10-03 17:18:00', '2024-10-03 17:18:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`account_id`),
  ADD UNIQUE KEY `account_number` (`account_number`),
  ADD UNIQUE KEY `account_number_2` (`account_number`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `cards`
--
ALTER TABLE `cards`
  ADD PRIMARY KEY (`card_id`),
  ADD UNIQUE KEY `card_number` (`card_number`),
  ADD KEY `account_id` (`account_id`),
  ADD KEY `card_type_id` (`card_type_id`);

--
-- Indexes for table `card_registration_requests`
--
ALTER TABLE `card_registration_requests`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_account_card_type` (`account_id`,`card_type_id`),
  ADD KEY `account_id` (`account_id`),
  ADD KEY `card_type_id` (`card_type_id`);

--
-- Indexes for table `card_types`
--
ALTER TABLE `card_types`
  ADD PRIMARY KEY (`card_type_id`),
  ADD UNIQUE KEY `card_type_name` (`card_type_name`);

--
-- Indexes for table `savings_accounts`
--
ALTER TABLE `savings_accounts`
  ADD PRIMARY KEY (`savings_account_id`),
  ADD UNIQUE KEY `unique_user_id` (`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `unique_phone_number` (`phone_number`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `account_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=65;
--
-- AUTO_INCREMENT for table `cards`
--
ALTER TABLE `cards`
  MODIFY `card_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `card_registration_requests`
--
ALTER TABLE `card_registration_requests`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `card_types`
--
ALTER TABLE `card_types`
  MODIFY `card_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `savings_accounts`
--
ALTER TABLE `savings_accounts`
  MODIFY `savings_account_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `cards`
--
ALTER TABLE `cards`
  ADD CONSTRAINT `cards_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
  ADD CONSTRAINT `cards_ibfk_2` FOREIGN KEY (`card_type_id`) REFERENCES `card_types` (`card_type_id`);

--
-- Constraints for table `card_registration_requests`
--
ALTER TABLE `card_registration_requests`
  ADD CONSTRAINT `card_registration_requests_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
  ADD CONSTRAINT `card_registration_requests_ibfk_2` FOREIGN KEY (`card_type_id`) REFERENCES `card_types` (`card_type_id`);

--
-- Constraints for table `savings_accounts`
--
ALTER TABLE `savings_accounts`
  ADD CONSTRAINT `savings_accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
