package com.bookstore.onlinebookstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


/**
 * The type Order item.
 */
@Entity
@Table(name = "order_items")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    // Unique primary key for each specific item row in the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the original book ID for inventory/tracking purposes
    @Column(name = "book_id")
    private Long bookId;

    // Snapshot data: we store title and price here so the order history
    // remains accurate even if the book's price or title changes later.
    private String title;
    private double price;
    private int quantity;

    // The "Many-to-One" relationship: Many order items belong to one single parent Order.
    // FetchType.LAZY improves performance by only loading the Order data when specifically requested.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference // Prevents infinite loops when converting this object to JSON for the API
    private Order order;
}