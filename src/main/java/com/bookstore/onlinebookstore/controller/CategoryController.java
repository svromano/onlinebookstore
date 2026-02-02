package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Category;
import com.bookstore.onlinebookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing category-related data.
 * This controller provides endpoints to retrieve classification data (like 'Java', 'Web Development').
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor // Automatically generates constructor for the final CategoryRepository
public class CategoryController {

    // Dependency injection of the repository to interact with the 'categories' table
    private final CategoryRepository categoryRepository;

    /**
     * Endpoint: GET /api/categories
     * Fetches all available categories from the database.
     * This is typically used by the frontend to build filter sidebars or category menus.
     * * @return A list of Category objects, each containing an 'id' and a 'name'.
     */
    @GetMapping
    public List<Category> getAllCategories() {
        // Calls the JPA findAll() method to execute: SELECT * FROM categories;
        return categoryRepository.findAll();
    }
}