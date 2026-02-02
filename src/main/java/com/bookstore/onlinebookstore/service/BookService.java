package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that handles business logic for book management.
 * @Service: Registers this class as a Spring-managed service bean.
 * @RequiredArgsConstructor: Generates a constructor for the final BookRepository dependency.
 */
@Service
@RequiredArgsConstructor
public class BookService {

    // Dependency on the repository to perform database queries
    private final BookRepository bookRepository;

    /**
     * Retrieves every book in the database.
     * This is usually the default view when a user first opens the catalog.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Filters books based on their category (e.g., Programming, Java).
     * @param categoryId The ID of the category to filter by.
     * @return A list of books belonging to the specified category.
     */
    public List<Book> getBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    /**
     * Performs a text-based search across both book titles and author names.
     * @param keyword The search term entered by the user.
     * @return A list of books matching the keyword (case-insensitive).
     */
    public List<Book> searchBooks(String keyword) {
        // We pass the same keyword twice because the repository method
        // checks both the title OR the author field.
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }
}