package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Category repository.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Standard methods like findById(), findAll(), and save() are already included!
}