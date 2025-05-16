package com.BackEnd.controller;

import com.BackEnd.dto.*;
import com.BackEnd.repository.CartRepository;
import com.BackEnd.service.CartService;
import com.BackEnd.model.Cart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            List<CartItemDTO> items = cartService.getAllCartItemDTOsInActiveCart(cartId);
            return ResponseEntity.ok(items);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // done. create/get cart
    @PostMapping("/active")
    public ResponseEntity<BasicCartInfoDto> getOrCreateActiveCart(@RequestParam String userName) {
        try {
            BasicCartInfoDto dto = cartService.getOrCreateActiveCartDTO(userName);
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
            e.printStackTrace();
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

    @GetMapping("/status")
    public ResponseEntity<Cart> getCartStatus(@RequestParam Long cartId) {
        Cart cart = cartService.getCartStatus(cartId);
        return ResponseEntity.ok(cart); // Trả về trạng thái giỏ hàng
    }

//    @PostMapping("/pendingStatus")
//    public void changeCartStatusToPending(@RequestParam Long cartId){
//   Cart cart =     cartService.getCartByCartId(cartId);
//   cart.setStatus(Cart.CartStatus.PENDING);
//   cartRepo.save(cart);
//    }
//
    @PostMapping("/activeStatus")
    public void changeCartStatusToActive(@RequestParam Long cartId){
        Cart cart =     cartService.getCartByCartId(cartId);
        cart.setStatus(Cart.CartStatus.ACTIVE);
        cartRepo.save(cart);
    }

    @GetMapping("/total-price")
    public ResponseEntity<Double> getCartTotalPrice(@RequestParam Long cartId){
        Double price = cartService.getTotalPrice(cartId);
        if(price == null ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(price);
    }
}
