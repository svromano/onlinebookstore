package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.OrderItemRequest;
import com.bookstore.onlinebookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(
            @RequestParam Long userId,
            @RequestParam String username,
            @RequestBody List<OrderItemRequest> items) {

        try {
            orderService.createOrder(userId, username, items);
            return ResponseEntity.ok("Order placed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Failed to create order: " + e.getMessage());
        }
    }
}