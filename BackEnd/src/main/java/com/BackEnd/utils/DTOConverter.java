package com.BackEnd.utils;

import com.BackEnd.dto.CartBasicInfoDTO;
import com.BackEnd.dto.CartDTO;
import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.dto.BasicCartItemDTO;
import com.BackEnd.model.Cart;
import com.BackEnd.model.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    public static CartItemDTO toCartItemDTO(CartItem cartItem) {
        return new CartItemDTO(
                cartItem.getProduct().getProductId(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity()
        );
    }
    public static CartDTO toCartDTO(Cart cart) {
        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
                .map(DTOConverter::toCartItemDTO)
                .collect(Collectors.toList());

        return new CartDTO(
                cart.getCartId(),
                cart.getUser().getUserId(),
                cartItemDTOs,
                cart.getStatus().name()
        );
    }
    public static CartBasicInfoDTO toCartBasicInfoDTO(Cart cart) {
        Long cartId = cart.getCartId();
        String status = cart.getStatus().name();
        return new CartBasicInfoDTO(cartId, status);
    }

    public static BasicCartItemDTO toBasicCartItemDTO(CartItem cartItem) {
        Long productId = cartItem.getProduct().getProductId();
        int quantity = cartItem.getQuantity();
        return new BasicCartItemDTO(productId, quantity);
    }


}
