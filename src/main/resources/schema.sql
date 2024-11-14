CREATE TABLE IF NOT EXISTS `accounts` (
   `account_id` int AUTO_INCREMENT  PRIMARY KEY,
   `account_number` varchar(30) NOT NULL,
   `balance` double NOT NULL,
   `account_type` varchar(200) NOT NULL,
   `customer_id` varchar(30) NOT NULL,
   `created_at` date NOT NULL,
   `created_by` varchar(20) NOT NULL,
   `updated_at` date DEFAULT NULL,
   `updated_by` varchar(20) DEFAULT NULL
);
