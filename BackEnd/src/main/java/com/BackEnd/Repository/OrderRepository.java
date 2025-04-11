package com.BackEnd.Repository;

import com.BackEnd.model.Order;
import com.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    ;
    Optional<Order> findByUser_UserNameAndStatus(String userName, String status);

}
