package com.BackEnd.Repository;

import com.BackEnd.model.Order;
import com.BackEnd.model.Product;
import com.BackEnd.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}
