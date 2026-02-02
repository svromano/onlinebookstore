package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data Access Layer for individual Order Items.
 * This repository handles the storage and retrieval of the specific books
 * linked to a parent Order, ensuring that every "line item" on a receipt
 * is correctly persisted in the 'order_items' table.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Inherits all standard CRUD operations (save, find, delete) from JpaRepository.
}