package com.BackEnd.repository;


import com.BackEnd.model.Cart;
import com.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Optional --> tìm tối đa 1 bảng ghi, nếu không có thì trả về rỗng
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndStatus(User user, Cart.CartStatus status);
//    Optional<Cart> findByCartId

}

