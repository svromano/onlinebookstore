package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Page<Book> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId
    ) {
        return bookService.getBooks(query, categoryId, page, size);
    }
}
