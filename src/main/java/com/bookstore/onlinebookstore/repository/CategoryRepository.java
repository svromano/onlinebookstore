package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
