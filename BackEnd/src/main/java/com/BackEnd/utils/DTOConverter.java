package com.BackEnd.utils;

import com.BackEnd.dto.*;
import com.BackEnd.model.Cart;
import com.BackEnd.model.CartItem;
import com.BackEnd.model.OrderDetail;

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
    public static BasicCartInfoDto toCartBasicInfoDTO(Cart cart) {
        Long cartId = cart.getCartId();
        String status = cart.getStatus().name();
        Double totalPrice = cart.getTotalPrice();
        return new BasicCartInfoDto(cartId, status, totalPrice);
    }

    public static BasicCartItemDTO toBasicCartItemDTO(CartItem cartItem) {
        Long productId = cartItem.getProduct().getProductId();
        int quantity = cartItem.getQuantity();
        return new BasicCartItemDTO(productId, quantity);
    }

    public static OrderDetailDTO toOrderDetailDTO(OrderDetail orderDetail){
        return new OrderDetailDTO(orderDetail.getProduct().getName(), orderDetail.getQuantity(), orderDetail.getTotalPrice());
    }

}
