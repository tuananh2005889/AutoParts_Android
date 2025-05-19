package com.BackEnd.repository;

import com.BackEnd.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(Long productId);

    List<Product> findByCategory(String category);

    @Query("SELECT p.images FROM Product p WHERE p.productId = :productId")
    Optional<List<String>> findImageByProductId(@Param("productId") Long productId);

}
