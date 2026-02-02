package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.OrderItemRequest;
import com.bookstore.onlinebookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling shopping cart and checkout operations.
 * @RestController: Marks this as a RESTful web service endpoint.
 * @RequestMapping("/api/cart"): Sets the base path for all cart-related actions.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    // Injecting the OrderService to handle the business logic of order creation
    private final OrderService orderService;

    /**
     * Endpoint: POST /api/cart/checkout
     * Finalizes the purchase by converting cart items into a permanent order record.
     * * @param userId (Query Param) The unique ID of the user placing the order.
     * @param username (Query Param) The name of the user for display/logging purposes.
     * @param items (Request Body) A JSON list of books, quantities, and prices from the cart.
     * @return ResponseEntity containing a success message or an error status.
     */
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(
            @RequestParam Long userId,
            @RequestParam String username,
            @RequestBody List<OrderItemRequest> items) {

        try {
            // Delegates the complex work (validating stock, calculating totals,
            // saving to 'orders' and 'order_items' tables) to the service layer.
            orderService.createOrder(userId, username, items);

            // HTTP 200 OK
            return ResponseEntity.ok("Order placed successfully!");
        } catch (Exception e) {
            // Logs the full stack trace to the server console for debugging
            e.printStackTrace();

            // HTTP 500 Internal Server Error
            // Returns a detailed message so the frontend can inform the user what went wrong
            return ResponseEntity.internalServerError()
                    .body("Failed to create order: " + e.getMessage());
        }
    }
}