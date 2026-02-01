-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: bookstore_db
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7jv5rwmalxg0a02a3ublrk0j2` (`category_id`),
  CONSTRAINT `FK7jv5rwmalxg0a02a3ublrk0j2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'The Pragmatic Programmer','Andrew Hunt & David Thomas',35.99,'A classic book on software engineering practices.',1,'https://pragprog.com/titles/tpp20/the-pragmatic-programmer-20th-anniversary-edition/tpp20.jpg'),(2,'Clean Code','Robert C. Martin',42.5,'A handbook of agile software craftsmanship.',2,'https://cdn.kobo.com/book-images/eb3afab7-f376-4bdd-8442-33f6d0b89432/1200/1200/False/clean-code-a-handbook-of-agile-software-craftsmanship-1.jpg'),(3,'Design Patterns','Erich Gamma et al.',55,'Elements of reusable object-oriented software.',3,'https://m.media-amazon.com/images/I/51nL96Abi1L._UF1000,1000_QL80_.jpg'),(4,'Java Concurrency in Practice','Brian Goetz',48.75,'Practical guide to Java concurrency.',4,'https://www.oreilly.com/library/cover/0321349601/300w/'),(5,'Effective Java','Joshua Bloch',50,'Best practices for Java programming.',4,'https://m.media-amazon.com/images/I/61nzJ4wHAeL._AC_UF1000,1000_QL80_.jpg'),(6,'Refactoring','Martin Fowler',45.25,'Improving the design of existing code.',2,'https://m.media-amazon.com/images/I/71TGolfP8fL._AC_UF1000,1000_QL80_.jpg'),(7,'Head First Design Patterns','Eric Freeman',39.95,'A brain-friendly guide to design patterns.',3,'https://0.academia-photos.com/attachment_thumbnails/50288619/mini_magick20220701-21703-18de4uu.png?1656731182'),(8,'Introduction to Algorithms','Thomas H. Cormen',85,'The definitive guide to computer algorithms and data structures.',1,'https://cdn.kobo.com/book-images/d76202b5-0140-46a1-8b2c-71754ca21d2e/1200/1200/False/introduction-to-algorithms-fourth-edition.jpg'),(9,'The Mythical Man-Month','Frederick Brooks',32.99,'Classic essays on software engineering and project management.',2,'https://m.media-amazon.com/images/S/compressed.photo.goodreads.com/books/1348430512i/13629.jpg'),(10,'Design Patterns Explained','Alan Shalloway',44.5,'A new perspective on Object-Oriented Design.',3,'https://www.oreilly.com/library/cover/0321247140/300w/'),(11,'Thinking in Java','Bruce Eckel',59.99,'An extraordinary guide to mastering the Java language.',4,'https://m.media-amazon.com/images/S/compressed.photo.goodreads.com/books/1347737121i/71672.jpg'),(12,'Domain-Driven Design','Eric Evans',52,'Tackling complexity in the heart of software.',3,'https://www.oreilly.com/library/cover/0321125215/300w/'),(13,'Code Complete','Steve McConnell',46.25,'A practical handbook of software construction.',1,'https://upload.wikimedia.org/wikipedia/en/5/58/Code_Complete_1st_edition.jpg'),(14,'Clean Architecture','Robert C. Martin',38.5,'A craftsman guide to software structure and design.',3,'https://m.media-amazon.com/images/S/compressed.photo.goodreads.com/books/1471680093i/18043011.jpg'),(15,'Test Driven Development','Kent Beck',41.99,'By example guide to writing clean code that works.',2,'https://archive.org/services/img/est-driven-development-by-example/full/pct:200/0/default.jpg');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Programming'),(2,'Software Engineering'),(3,'Design Patterns'),(4,'Java'),(5,'Web Development');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `book_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `price` double NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,4,3,48.75,NULL),(2,2,4,2,48.75,'Java Concurrency in Practice'),(3,3,4,3,48.75,'Java Concurrency in Practice'),(4,4,2,5,42.5,'Clean Code'),(5,5,5,1,50,'Effective Java');
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `total` double NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,3,146.25,'2026-01-31 07:01:41',''),(2,1,97.5,'2026-01-31 16:09:29','guest'),(3,1,146.25,'2026-01-31 16:10:05','guest'),(4,1,212.5,'2026-01-31 16:48:30','guest'),(5,1,50,'2026-02-01 15:39:56','guest');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin@bookstore.com','$2a$10$N9qo8uLOickgx2ZMRZoMye1J9Z5iQ2sLJxZ3P3SQYp3rZ5iQ2sLJx'),(2,'john_doe','john@example.com','$2a$10$N9qo8uLOickgx2ZMRZoMye1J9Z5iQ2sLJxZ3P3SQYp3rZ5iQ2sLJx'),(3,'svromano','sherwin.v.romano@gmail.com','$2a$10$BG1FbLzRLs6bmFiAxIYCCudh.lavuIlt23ZiIDhRsT0hm5DTjGgem'),(4,'kidy','kidyyyplays@gmail.com','$2a$10$gq4MvwGkkmF47EWcBCPGdOkUnE9WXNc0yWmS1USF/1KKxqHPO3tEK'),(5,'tiffany','tiffany@gmail.com','$2a$10$.YMSicVSmPUnpj5DCki6ieYz8xA9WjaDQez6Dwjja0KPsGWaINZme');
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

-- Dump completed on 2026-02-02  0:13:53
