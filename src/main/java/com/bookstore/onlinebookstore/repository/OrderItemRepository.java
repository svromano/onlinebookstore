package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Order item repository.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Inherits all standard CRUD operations (save, find, delete) from JpaRepository.
}