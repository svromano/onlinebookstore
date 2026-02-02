package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing book-related HTTP requests.
 * @RestController: Combines @Controller and @ResponseBody, meaning the returned
 * data is serialized directly into JSON for the client.
 * @RequestMapping: Defines the base URL path for all endpoints in this class.
 * @RequiredArgsConstructor: A Lombok annotation that generates a constructor
 * for all final fields (enabling constructor injection).
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    // The repository interface used to perform CRUD operations on the 'books' table
    private final BookRepository bookRepository;

    /**
     * Endpoint: GET /api/books
     * Retrieves a full list of all books in the database.
     * @return List of Book entities.
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Endpoint: GET /api/books/search
     * Handles filtered searches based on text queries and/or category selection.
     * * @param query (Optional) Text to match against book titles or authors.
     * @param categoryId (Optional) The ID of the category to filter by.
     * @return A filtered list of books based on the provided parameters.
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