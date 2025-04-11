package com.BackEnd.Repository;

import com.BackEnd.model.Order;
import com.BackEnd.model.OrderDetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);
}
