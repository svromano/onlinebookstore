package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Order;
import com.bookstore.onlinebookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Order controller.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    // Repository to access the 'orders' table in the database
    private final OrderRepository orderRepository;

    @GetMapping
    public List<Order> getOrders(@RequestParam Long userId) {
        // 1. Fetch orders matching the userId from the 'orders' table
        List<Order> orders = orderRepository.findByUserId(userId);

        orders.forEach(order -> order.getItems().size());

        return orders;
    }
}