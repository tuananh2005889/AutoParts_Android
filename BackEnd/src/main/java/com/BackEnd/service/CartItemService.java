package com.BackEnd.service;

import com.BackEnd.dto.BasicCartItemDTO;
import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.model.CartItem;
import com.BackEnd.repository.CartItemRepository;
import com.BackEnd.utils.DTOConverter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private CartItemRepository cartItemRepo;

    public CartItemService(CartItemRepository cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    public BasicCartItemDTO increaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepo.save(cartItem);
        return DTOConverter.toBasicCartItemDTO(cartItem);
    }

    public BasicCartItemDTO decreaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        }
        cartItemRepo.save(cartItem);
        return DTOConverter.toBasicCartItemDTO(cartItem);
    }

}
