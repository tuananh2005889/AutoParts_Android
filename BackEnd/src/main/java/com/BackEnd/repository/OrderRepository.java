package com.BackEnd.repository;

import com.BackEnd.model.Order;
import com.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findOrderByUser(User user);

    boolean existsByUserAndStatus(User user, Order.OrderStatus status);

    Order findByUserAndStatus(User user, Order.OrderStatus status);

    Order findTopByUserAndStatusOrderByCreatedAtDesc(User user, Order.OrderStatus status);

    Order findByOrderCode(Long orderCode);

}

