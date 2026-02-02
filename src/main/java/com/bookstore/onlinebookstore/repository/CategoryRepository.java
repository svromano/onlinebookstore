package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data Access Layer for Categories.
 * By extending JpaRepository, this interface automatically provides methods
 * for saving, deleting, and finding Categories without writing any SQL.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Standard methods like findById(), findAll(), and save() are already included!
}