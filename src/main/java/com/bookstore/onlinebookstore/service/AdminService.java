package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.CreateBookRequest;
import com.bookstore.onlinebookstore.dto.CreateUserRequest;
import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.entity.Category;
import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.BookRepository;
import com.bookstore.onlinebookstore.repository.CategoryRepository;
import com.bookstore.onlinebookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(CreateUserRequest request) {
        // Validate username doesn't exist
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Validate email doesn't exist
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set role from request, default to USER if not provided
        user.setRole(request.getRole() != null ? request.getRole() : "USER");

        userRepository.save(user);
    }

    public Book createBook(CreateBookRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setImageUrl(request.getImageUrl());
        book.setCategory(category);

        return bookRepository.save(book);
    }
}