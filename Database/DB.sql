
DROP TABLE IF EXISTS `Payment`;
CREATE TABLE `Payment` (
  `paymentid` int NOT NULL,
  `clientid` int DEFAULT NULL,
  `paymentdate` date DEFAULT NULL,
  `mois` varchar(255) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`paymentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `QuitHistoric`;
CREATE TABLE `QuitHistoric` (
  `id` date NOT NULL,
  `counter` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `Username` varchar(45) NOT NULL,
  `Password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
LOCK TABLES `admin` WRITE;
INSERT INTO `admin` VALUES ('oussama','soummar'),('soummar','oussama');
UNLOCK TABLES;
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `ClientId` int NOT NULL,
  `FirstName` varchar(45) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `Phone` varchar(45) NOT NULL,
  `Gmail` varchar(45) NOT NULL,
  `Cin` varchar(45) NOT NULL,
  `Gender` varchar(10) NOT NULL,
  `StartDate` date DEFAULT NULL,
  `ExpirationDate` date DEFAULT NULL,
  PRIMARY KEY (`ClientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `PaymentId` int NOT NULL AUTO_INCREMENT,
  `ClientId` int DEFAULT NULL,
  `PaymentDate` date DEFAULT NULL,
  `Mois` int DEFAULT NULL,
  `Amount` int DEFAULT NULL,
  PRIMARY KEY (`PaymentId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

