package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Data Access Layer for Customer Orders.
 * This repository manages the retrieval of complete order histories from the database.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * CUSTOM QUERY: This JPQL (Java Persistence Query Language) statement
     * retrieves all orders for a specific user.
     * 1. FILTER: It looks for orders matching the provided 'userId'.
     * 2. SORT: It uses 'ORDER BY o.createdAt DESC' so that the customer's
     * most recent purchases appear at the top of their history page.
     */
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUserId(@Param("userId") Long userId);
}