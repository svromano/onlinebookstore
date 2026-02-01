package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    List<Book> findByCategoryId(Long categoryId);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(String title, String author, Long categoryId);
}
