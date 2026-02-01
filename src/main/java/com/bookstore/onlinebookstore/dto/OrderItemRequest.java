package com.bookstore.onlinebookstore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Long bookId;
    private String title;
    private double price;
    private int quantity;
}
