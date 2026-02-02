package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The Book Repository is the Data Access Layer.
 * It uses "Method Name Queries," where Spring automatically generates the SQL
 * based on the name of the method.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // SEARCH: Finds books where the title OR author matches a search term,
    // ignoring whether the text is uppercase or lowercase.
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    // FILTER: Retrieves all books belonging to a specific category ID.
    List<Book> findByCategoryId(Long categoryId);

    // ADVANCED: Combines search and filter. Finds books matching a name/author
    // ONLY within a specific category (e.g., searching for "Java" inside "Programming").
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(String title, String author, Long categoryId);
}