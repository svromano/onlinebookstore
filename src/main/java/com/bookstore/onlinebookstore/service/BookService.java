package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Book service.
 */
@Service
@RequiredArgsConstructor
public class BookService {

    // Dependency on the repository to perform database queries
    private final BookRepository bookRepository;

    /**
     * Gets all books.
     *
     * @return the all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    public List<Book> searchBooks(String keyword) {

        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }
}