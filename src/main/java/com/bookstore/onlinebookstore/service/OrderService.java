package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.OrderItemRequest;
import com.bookstore.onlinebookstore.entity.Order;
import com.bookstore.onlinebookstore.entity.OrderItem;
import com.bookstore.onlinebookstore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(Long userId, String username, List<OrderItemRequest> items) {
        Order order = new Order();
        order.setUserId(userId);
        order.setUsername(username != null ? username : "guest");

        double total = 0;
        for (OrderItemRequest req : items) {
            OrderItem item = new OrderItem();
            item.setBookId(req.getBookId());
            item.setTitle(req.getTitle());
            item.setPrice(req.getPrice());
            item.setQuantity(req.getQuantity());
            item.setOrder(order);

            order.getItems().add(item);
            total += req.getPrice() * req.getQuantity();
        }

        order.setTotal(total);
        orderRepository.save(order);
    }
}