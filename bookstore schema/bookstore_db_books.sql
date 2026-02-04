-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bookstore_db
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
  KEY `fk_book_category` (`category_id`),
  CONSTRAINT `fk_book_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'The Pragmatic Programmer','Andrew Hunt & David Thomas',35.99,'A classic book on software engineering practices.',1,'https://pragprog.com/titles/tpp20/the-pragmatic-programmer-20th-anniversary-edition/tpp20.jpg'),(2,'Clean Code','Robert C. Martin',42.5,'A handbook of agile software craftsmanship.',2,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2pOj0p25Ttg1Yu7OfInfZfSBX-3TmgkTwhA&s'),(3,'Design Patterns','Erich Gamma et al.',55,'Elements of reusable object-oriented software.',3,'https://m.media-amazon.com/images/I/51nL96Abi1L._UF1000,1000_QL80_.jpg'),(4,'Java Concurrency in Practice','Brian Goetz',48.75,'Practical guide to Java concurrency.',4,'https://www.oreilly.com/library/cover/0321349601/300w/'),(5,'Effective Java','Joshua Bloch',50,'Best practices for Java programming.',4,'https://m.media-amazon.com/images/I/61nzJ4wHAeL._AC_UF1000,1000_QL80_.jpg'),(6,'Refactoring','Martin Fowler',45.25,'Improving the design of existing code.',2,'https://m.media-amazon.com/images/I/71TGolfP8fL._AC_UF1000,1000_QL80_.jpg'),(7,'Head First Design Patterns','Eric Freeman',39.95,'A brain-friendly guide to design patterns.',3,'https://0.academia-photos.com/attachment_thumbnails/50288619/mini_magick20220701-21703-18de4uu.png?1656731182'),(8,'Introduction to Algorithms','Thomas H. Cormen',85,'The definitive guide to computer algorithms and data structures.',1,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8va0y0Ire1271XTBsLUTEw3dCnG0-F7EnUA&s'),(9,'The Mythical Man-Month','Frederick Brooks',32.99,'Classic essays on software engineering and project management.',2,'https://m.media-amazon.com/images/S/compressed.photo.goodreads.com/books/1348430512i/13629.jpg'),(10,'Design Patterns Explained','Alan Shalloway',44.5,'A new perspective on Object-Oriented Design.',3,'https://www.oreilly.com/library/cover/0321247140/300w/'),(11,'Thinking in Java','Bruce Eckel',59.99,'An extraordinary guide to mastering the Java language.',4,'https://m.media-amazon.com/images/S/compressed.photo.goodreads.com/books/1347737121i/71672.jpg'),(12,'Domain-Driven Design','Eric Evans',52,'Tackling complexity in the heart of software.',3,'https://www.oreilly.com/library/cover/0321125215/300w/'),(13,'Code Complete','Steve McConnell',46.25,'A practical handbook of software construction.',1,'https://upload.wikimedia.org/wikipedia/en/5/58/Code_Complete_1st_edition.jpg'),(14,'Clean Architecture','Robert C. Martin',38.5,'A craftsman guide to software structure and design.',3,'https://m.media-amazon.com/images/S/compressed.photo.goodreads.com/books/1471680093i/18043011.jpg'),(15,'Test Driven Development','Kent Beck',41.99,'By example guide to writing clean code that works.',2,'https://archive.org/services/img/est-driven-development-by-example/full/pct:200/0/default.jpg'),(16,'Test','John Doe',50,'Lorem ipsum',4,'https://timvandevall.com/wp-content/uploads/2014/01/Book-Cover-Template.jpg');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-04 13:32:49
