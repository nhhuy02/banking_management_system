-- Drop tables if they exist
DROP TABLE IF EXISTS `customers`;
DROP TABLE IF EXISTS `customers_status_history`;
DROP TABLE IF EXISTS `kyc`;
DROP TABLE IF EXISTS `verification_codes`;

-- Create tables
CREATE TABLE `kyc`
(
    `id`                  bigint                      NOT NULL AUTO_INCREMENT,
    `verification_status` enum ('verified','pending') NOT NULL         DEFAULT 'pending',
    `verification_date`   datetime                    NULL             DEFAULT NULL,
    `document_type`       enum ('id_card','passport','driver_license') DEFAULT NULL,
    `document_number`     varchar(255)                                 DEFAULT NULL,
    `document_image_url`  tinytext,
    `created_at`          datetime                    NULL             DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          datetime                    NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `document_type` (`document_type`, `document_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `customers`
(
    `id`                bigint   NOT NULL AUTO_INCREMENT,
    `account_id`        bigint   NOT NULL,
    `full_name`         varchar(255)                   DEFAULT NULL,
    `date_of_birth`     date                           DEFAULT NULL,
    `gender`            enum ('MALE','FEMALE','OTHER') DEFAULT NULL,
    `email`             varchar(255)                   DEFAULT NULL,
    `phone_number`      varchar(20)                    DEFAULT NULL,
    `permanent_address` tinytext,
    `current_address`   tinytext,
    `kyc_id`            bigint                         DEFAULT NULL,
    `created_at`        datetime NULL                  DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        datetime NULL                  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `account_id` (`account_id`),
    UNIQUE KEY `email` (`email`),
    UNIQUE KEY `phone_number` (`phone_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `customers_status_history`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `customer_id` bigint   NOT NULL,
    `status`      enum ('active','suspended','closed') DEFAULT 'active',
    `changed_at`  datetime NULL                        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `customers_status_history_ibfk_1` (`customer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `verification_codes`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `customer_id` bigint        DEFAULT NULL,
    `code`        varchar(10)   DEFAULT NULL,
    `created_at`  datetime NULL DEFAULT CURRENT_TIMESTAMP,
    `expires_at`  datetime NULL DEFAULT NULL,
    `is_verified` tinyint(1)    DEFAULT '0',
    `used_at`     datetime NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `customer_id` (`customer_id`, `code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Add foreign key constraints after table creation
ALTER TABLE `customers`
    ADD CONSTRAINT `fk_kyc_id` FOREIGN KEY (`kyc_id`) REFERENCES `kyc` (`id`);

ALTER TABLE `customers_status_history`
    ADD CONSTRAINT `customers_status_history_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);

ALTER TABLE `verification_codes`
    ADD CONSTRAINT `verification_codes_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);

-- Insert data into tables
INSERT INTO `customers`
VALUES (1, 1001, 'Nguyen Van A', '1990-01-15', 'MALE', 'nguyenvana@example.com', '0901234567', '123 Main St, Hanoi',
        '456 Secondary St, Hanoi', 1, '2024-08-30 04:16:12', '2024-09-11 03:36:01'),
       (2, 1002, 'Nguyen Van Z', '1985-06-20', 'MALE', 'nguyenvanz@gmail.com', '0912345678', 'Viet Nam', 'Viet Nam', 26,
        '2024-08-30 04:16:12', '2024-09-11 03:36:01'),
       (3, 1003, 'Le Van C', '1992-03-05', 'MALE', 'levanc@example.com', '0923456789', '345 Main St, Hanoi',
        '678 Secondary St, Hanoi', 3, '2024-08-30 04:16:12', '2024-09-11 03:36:01'),
       (4, 1004, 'Nguyen Minh Tam', '2002-07-10', 'MALE', 'mintamhy2002@gmail.com', '0352364178', '123 Main St, Hanoi',
        '456 Secondary St, Hanoi', 4, '2024-08-30 06:43:39', '2024-09-11 03:36:01'),
       (5, 1010, 'Nguyen Minh Tam', '2002-05-12', 'MALE', 'minhtamhy2002@gmail.com', '0559483491', 'Hung Yen', 'Ha Noi',
        5, '2024-08-30 06:51:27', '2024-09-11 03:36:01'),
       (6, 1978, 'Chu Thị Doan', '1978-09-01', 'FEMALE', 'chuthidoan@gmail.com', '965278419', 'Hưng Yên', 'Hưng Yên',
        19, '2024-09-06 08:04:28', '2024-09-11 03:36:01'),
       (8, 8, NULL, NULL, NULL, NULL, '0748651530', NULL, NULL, 27, NULL, NULL'),
(9,9,'Louis Nguyễn','2002-05-12','MALE','tamshino2002@gmail.com','0485626563','Hưng Yên','Hưng Yên',28,NULL,'2024-09-17 08:01:48');

INSERT INTO `customers_status_history` VALUES
(11,1,'active','2024-08-30 07:48:45'),
(12,2,'active','2024-08-30 07:48:45'),
(13,3,'active','2024-08-30 07:48:45'),
(14,4,'active','2024-08-30 07:48:45'),
(15,5,'active','2024-08-30 07:48:45'),
(16,1,'active','2024-09-01 10:15:30'),
(17,1,'suspended','2024-09-02 14:22:45'),
(18,1,'closed','2024-09-03 09:05:20'),
(19,1,'suspended','2024-09-05 06:50:50'),
(20,5,'suspended','2024-09-05 06:51:25'),
(25,6,'active','2024-09-09 01:58:04'),
(26,6,'active','2024-09-09 02:06:17'),
(27,2,'active','2024-09-09 09:08:00'),
(28,2,'active','2024-09-09 09:08:01'),
(29,2,'active','2024-09-09 09:08:02'),
(30,2,'active','2024-09-09 09:13:45'),
(31,2,'active','2024-09-09 09:13:46'),
(32,2,'active','2024-09-09 09:13:47'),
(33,2,'active','2024-09-09 09:14:49'),
(35,9,'active','2024-09-17 07:55:05'),
(36,9,'active','2024-09-17 08:00:27'),
(37,9,'active','2024-09-17 08:01:38');
