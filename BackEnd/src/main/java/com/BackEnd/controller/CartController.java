package com.BackEnd.controller;

import com.BackEnd.dto.*;
import com.BackEnd.repository.CartRepository;
import com.BackEnd.service.CartService;
import com.BackEnd.model.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/cart")
public class CartController {
  
    private final CartService cartService;
    private final CartRepository cartRepo;
    public CartController(CartService cartService, CartRepository cartRepo) {
        this.cartService = cartService;
        this.cartRepo = cartRepo;
    }

    // done. fetch items tu cart
    @GetMapping("/items")
    public ResponseEntity<List<CartItemDTO>> getAllCartItems(@RequestParam Long cartId) {
        try {
            List<CartItemDTO> items = cartService.getCartItemsInActiveCart(cartId);
            return ResponseEntity.ok(items);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // done. create/get cart
    @PostMapping("/active")
    public ResponseEntity<CartBasicInfoDTO> getOrCreateActiveCart(@RequestParam String userName) {
        try {
            CartBasicInfoDTO dto = cartService.getOrCreateActiveCartDTO(userName);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // done. add item to cart
    @PostMapping("/add")
    public ResponseEntity<CartItemDTO> addItemToCart(@RequestBody AddToCartRequest addToCartRequest) {
        try {
            // Call service to add product to cart
            CartItemDTO dto = cartService.addItemToCart(addToCartRequest);

            // Return response with ItemDTO
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            // Handle specific errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            // Handle other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/imageUrls")
    public ResponseEntity<List<String>> getImageUrlPerCartItem(@RequestParam Long cartId) {
        try{
            List<String> imageUrls =
                    cartService.getImageUrlPerCartItem(cartId);
            if(imageUrls == null || imageUrls.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(imageUrls);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API thanh toán giỏ hàng (cập nhật trạng thái thành PAID)
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam Long cartId) {
        cartService.checkoutCart(cartId);
        return ResponseEntity.ok("Cart successfully checked out and paid.");
    }

    // @GetMapping("/activate")
    // public ResponseEntity<Cart> getActiveCart(@RequestParam String userName) {
    // Cart cart = cartService.createCart(userName); // tạo mới nếu chưa có
    // return ResponseEntity.ok(cart);
    // }
    // API kiểm tra trạng thái giỏ hàng
    @GetMapping("/status")
    public ResponseEntity<Cart> getCartStatus(@RequestParam Long cartId) {
        Cart cart = cartService.getCartStatus(cartId);
        return ResponseEntity.ok(cart); // Trả về trạng thái giỏ hàng
    }
}
