package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Category;
import com.bookstore.onlinebookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(); // returns id and name
    }
}
