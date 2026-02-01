package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.BookDTO;
import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/search")
    public List<Book> searchBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId
    ) {
        if (query != null && !query.isEmpty() && categoryId != null) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(query, query, categoryId);
        } else if (query != null && !query.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
        } else if (categoryId != null) {
            return bookRepository.findByCategoryId(categoryId);
        } else {
            return bookRepository.findAll();
        }
    }
}

