package com.bookstore.onlinebookstore.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Order item request.
 */
@Getter
@Setter
public class OrderItemRequest {

    // Links the request to a specific book in our inventory
    private Long bookId;

    // The name of the book as displayed to the user at the time of purchase
    private String title;

    // The unit price confirmed by the frontend during the checkout step
    private double price;

    // The number of units the customer has added to their cart
    private int quantity;
}