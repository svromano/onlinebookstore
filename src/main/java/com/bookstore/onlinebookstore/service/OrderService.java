package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.OrderItemRequest;
import com.bookstore.onlinebookstore.entity.Order;
import com.bookstore.onlinebookstore.entity.OrderItem;
import com.bookstore.onlinebookstore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing orders.
 * Handles the business logic of converting a shopping cart into a database record.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * Processes the checkout and saves the order to the database.
     * * @Transactional: This is critical. It ensures "Atomicity." If saving an item fails,
     * the entire order is rolled back so you don't end up with partial data in your DB.
     * * @param userId   The ID of the user placing the order.
     * @param username The name of the user (defaults to 'guest' if null).
     * @param items    The list of items the user is purchasing.
     */
    @Transactional
    public void createOrder(Long userId, String username, List<OrderItemRequest> items) {
        // 1. Initialize a new Order entity
        Order order = new Order();
        order.setUserId(userId);
        order.setUsername(username != null ? username : "guest");

        double total = 0;

        // 2. Loop through the requested items and convert them to OrderItem entities
        for (OrderItemRequest req : items) {
            OrderItem item = new OrderItem();
            item.setBookId(req.getBookId());
            item.setTitle(req.getTitle());
            item.setPrice(req.getPrice());
            item.setQuantity(req.getQuantity());

            // 3. Set the Bi-directional relationship
            // This links the individual item to the parent order
            item.setOrder(order);

            // 4. Add the item to the order's internal list and update the running total
            order.getItems().add(item);
            total += req.getPrice() * req.getQuantity();
        }

        // 5. Finalize the order total and save to the database
        // Because of 'CascadeType.ALL' in the Order entity, saving the 'order'
        // will automatically save all the 'order_items' in one go.
        order.setTotal(total);
        orderRepository.save(order);
    }
}