package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Order;
import com.bookstore.onlinebookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for retrieving order history.
 * Provides endpoints to view past transactions for a specific user.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    // Repository to access the 'orders' table in the database
    private final OrderRepository orderRepository;

    /**
     * Endpoint: GET /api/orders?userId=3
     * Retrieves all orders placed by a specific user.
     * * @param userId The ID of the user whose order history is being requested.
     * @return A list of Order entities, including their nested OrderItems.
     */
    @GetMapping
    public List<Order> getOrders(@RequestParam Long userId) {
        // 1. Fetch orders matching the userId from the 'orders' table
        List<Order> orders = orderRepository.findByUserId(userId);

        /**
         * 2. Force Lazy Loading:
         * In JPA, the 'items' collection inside the Order entity is likely marked as FetchType.LAZY.
         * This means the items aren't fetched from the database until they are accessed.
         * * By calling .size(), we trigger a database hit for each order's items while the
         * Hibernate session is still open. This prevents a 'LazyInitializationException'
         * when Spring tries to convert the list to JSON later.
         */
        orders.forEach(order -> order.getItems().size());

        return orders;
    }
}