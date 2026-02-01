package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Order;
import com.bookstore.onlinebookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping
    public List<Order> getOrders(@RequestParam Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        // Force lazy loading of items before returning
        orders.forEach(order -> order.getItems().size());
        return orders;
    }
}