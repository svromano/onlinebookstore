package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> getBooks(
            String query,
            Long categoryId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        if (query != null && !query.isEmpty() && categoryId != null) {
            return bookRepository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(
                            query, query, categoryId, pageable
                    );
        }
        else if (query != null && !query.isEmpty()) {
            return bookRepository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                            query, query, pageable
                    );
        }
        else if (categoryId != null) {
            return bookRepository.findByCategoryId(categoryId, pageable);
        }
        else {
            return bookRepository.findAll(pageable);
        }
    }
}
