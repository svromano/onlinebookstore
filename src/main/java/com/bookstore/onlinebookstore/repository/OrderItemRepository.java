package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}

