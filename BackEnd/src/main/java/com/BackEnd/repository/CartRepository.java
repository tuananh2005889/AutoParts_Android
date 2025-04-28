package com.BackEnd.repository;


import com.BackEnd.model.Cart;
import com.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Optional --> tìm tối đa 1 bảng ghi, nếu không có thì trả về rỗng
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByUserAndStatus(User user, Cart.CartStatus status);
    @Query("SELECT ci.id, p.images FROM CartItem ci " +
            "JOIN ci.product p " +
            "WHERE ci.cart.id = :cartId")
    List<Object[]> findCartItemImageUrlsWithIdByCartId(@Param("cartId") Long cartId);

//    @Query("SELECT p.images FROM CartItem ci " +
//            "JOIN ci.product p " +
//            "WHERE ci.cart.id = :cartId")
//    List<String> findCartItemImageUrlsByCartId(@Param("cartId") Long cartId);

    @Query(value = "SELECT MIN(pi.image_url) " +
            "FROM product_images pi " +
            "JOIN cart_item ci ON ci.product_id = pi.product_id " +
            "WHERE ci.cart_id = :cartId " +
            "GROUP BY ci.cart_item_id", nativeQuery = true)
    List<String> findImageUrlPerCartItem(@Param("cartId") Long cartId);



}

