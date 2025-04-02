package com.BackEnd.Controller;

import com.BackEnd.Service.OrderService;
import com.BackEnd.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            Long productId = Long.valueOf(payload.get("productId").toString());
            int quantity = Integer.parseInt(payload.get("quantity").toString());

            // Giả sử ta có user từ ID (cần có UserService để lấy User từ userId)
            User user = new User();
            user.setUserID(userId);

            boolean added = orderService.addToCart(user, productId, quantity);
            if (added) {
                return ResponseEntity.ok("Product added to cart successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to add product to cart");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
