package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * The type Book controller.
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    // The repository interface used to perform CRUD operations on the 'books' table
    private final BookRepository bookRepository;


    /**
     * Gets all books.
     *
     * @return the all books
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    /**
     * Search books list.
     *
     * @param query      the query
     * @param categoryId the category id
     * @return the list
     */
    @GetMapping("/search")
    public List<Book> searchBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId
    ) {
        // Case 1: Searching by both text AND category
        if (query != null && !query.isEmpty() && categoryId != null) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(query, query, categoryId);
        }
        // Case 2: Searching by text only (matches title or author name)
        else if (query != null && !query.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
        }
        // Case 3: Filtering by category only
        else if (categoryId != null) {
            return bookRepository.findByCategoryId(categoryId);
        }
        // Case 4: No parameters provided, fallback to returning everything
        else {
            return bookRepository.findAll();
        }
    }
}