package com.BackEnd.controller;


import com.BackEnd.dto.AddToCartRequest;
import com.BackEnd.dto.CartBasicInfoDTO;
import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.service.CartService;
import com.BackEnd.dto.CartDTO;
import com.BackEnd.model.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    //done. fetch items tu cart
    @GetMapping("/items")
    public ResponseEntity<List<CartItemDTO>> getAllCartItems(@RequestParam Long cartId) {
        try {
            List<CartItemDTO> items = cartService.getAllItemsInActiveCart(cartId);
            return ResponseEntity.ok(items);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    //done. create/get cart
    @PostMapping("/active")
    public ResponseEntity<CartBasicInfoDTO> getOrCreateCart(@RequestParam String userName) {
        try{
            CartBasicInfoDTO dto = cartService.getOrCreateActiveCartDTO(userName);
            return ResponseEntity.ok(dto);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //done. add item to cart
    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody AddToCartRequest addToCartRequest) {
        try {
             cartService.addItemToCart(addToCartRequest);
            return ResponseEntity.ok("Add product successfully");

        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Invalid quantity " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Server error " + e.getMessage());
        }
    }




    // API thanh toán giỏ hàng (cập nhật trạng thái thành PAID)
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam Long cartId) {
        cartService.checkoutCart(cartId);
        return ResponseEntity.ok("Cart successfully checked out and paid.");
    }

//    @GetMapping("/activate")
//    public ResponseEntity<Cart> getActiveCart(@RequestParam String userName) {
//        Cart cart = cartService.createCart(userName); // tạo mới nếu chưa có
//        return ResponseEntity.ok(cart);
//    }
    // API kiểm tra trạng thái giỏ hàng
    @GetMapping("/status")
    public ResponseEntity<Cart> getCartStatus(@RequestParam Long cartId) {
        Cart cart = cartService.getCartStatus(cartId);
        return ResponseEntity.ok(cart); // Trả về trạng thái giỏ hàng
    }
}

