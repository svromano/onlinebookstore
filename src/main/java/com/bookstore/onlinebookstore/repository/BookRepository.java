package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Book repository.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find by title containing ignore case or author containing ignore case list.
     *
     * @param title  the title
     * @param author the author
     * @return the list
     */
// SEARCH: Finds books where the title OR author matches a search term,
    // ignoring whether the text is uppercase or lowercase.
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    /**
     * Find by category id list.
     *
     * @param categoryId the category id
     * @return the list
     */
// FILTER: Retrieves all books belonging to a specific category ID.
    List<Book> findByCategoryId(Long categoryId);

    /**
     * Find by title containing ignore case or author containing ignore case and category id list.
     *
     * @param title      the title
     * @param author     the author
     * @param categoryId the category id
     * @return the list
     */
// ADVANCED: Combines search and filter. Finds books matching a name/author
    // ONLY within a specific category (e.g., searching for "Java" inside "Programming").
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(String title, String author, Long categoryId);
}