package com.BackEnd.controller;

import com.BackEnd.dto.BasicCartItemDTO;
import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.model.CartItem;
import com.BackEnd.repository.CartItemRepository;
import com.BackEnd.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/cart-item")
public class CartItemController {
    private CartItemService cartItemService;
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PutMapping("/increase")
    public ResponseEntity<BasicCartItemDTO> increaseQuantity(@RequestParam Long cartItemId) {
        BasicCartItemDTO updatedItem =
                cartItemService.increaseQuantity(cartItemId);
        return ResponseEntity.ok(updatedItem);
    }

    @PutMapping("/decrease")
    public ResponseEntity<BasicCartItemDTO> decreaseQuantity(@RequestParam Long cartItemId) {
        BasicCartItemDTO updatedItem = cartItemService.decreaseQuantity(cartItemId);
        return ResponseEntity.ok(updatedItem);
    }
}
