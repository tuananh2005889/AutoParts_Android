package com.BackEnd.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BackEnd.model.CartItem;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.product.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
