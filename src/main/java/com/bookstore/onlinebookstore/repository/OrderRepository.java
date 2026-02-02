package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * The interface Order repository.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUserId(@Param("userId") Long userId);
}