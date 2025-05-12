package com.BackEnd.service;

import com.BackEnd.dto.BasicCartItemDTO;
import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.model.Cart;
import com.BackEnd.model.CartItem;
import com.BackEnd.repository.CartItemRepository;
import com.BackEnd.repository.CartRepository;
import com.BackEnd.utils.DTOConverter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private CartItemRepository cartItemRepo;
    private CartRepository cartRepo;
    public CartItemService(CartItemRepository cartItemRepo, CartRepository cartRepo) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
    }

    public BasicCartItemDTO increaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        //increase cart totalPrice
        Cart cart = cartItem.getCart();
        Double cartTotalPrice = cart.getTotalPrice();
        cart.setTotalPrice(cartTotalPrice + cartItem.getProduct().getPrice());
        cartRepo.save(cart);

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepo.save(cartItem);
        return DTOConverter.toBasicCartItemDTO(cartItem);
    }

    public BasicCartItemDTO decreaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);

            Cart cart = cartItem.getCart();
            Double cartTotalPrice = cart.getTotalPrice();
            cart.setTotalPrice(cartTotalPrice - cartItem.getProduct().getPrice());
            cartRepo.save(cart);
        }
        cartItemRepo.save(cartItem);
        return DTOConverter.toBasicCartItemDTO(cartItem);
    }

}
