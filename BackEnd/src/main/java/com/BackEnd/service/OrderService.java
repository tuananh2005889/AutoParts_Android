package com.BackEnd.service;

import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.dto.OrderDetailDTO;
import com.BackEnd.model.*;
import com.BackEnd.repository.*;
import com.BackEnd.repository.OrderDetailRepository;
import com.BackEnd.utils.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final UserService userService;
    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;


    public List<OrderDetailDTO> createOrder(Long cartId){
        //Change Cart Status active -> pending
        cartService.changeCartStatus(cartId, Cart.CartStatus.PENDING);

        //Create Order
        User user = cartService.getUserByCartId(cartId);
        LocalDateTime now = LocalDateTime.now();

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(now);
        order.setShippingAddress(user.getAddress());
        orderRepo.save(order);

        //thieu total amount, orderDetail                ,
        List<CartItem> cartItemList = cartService.getAllCartItemsInActiveCart(cartId);
        List<OrderDetail> orderDetailList = cartItemList.stream()
                .map(cartItem -> {
                    //product
                    Product product = cartItem.getProduct();
                    //quantity
                    int quantity = cartItem.getQuantity();
                    //totalPrice
                    Double totalPrice = quantity * product.getPrice();

                    return new OrderDetail(order, product, quantity, totalPrice);
                })
                .collect(Collectors.toList());

        order.setOrderDetails(orderDetailList);

        Double cartTotalPrice = orderDetailList.stream()
                        .mapToDouble(orderDetail -> orderDetail.getTotalPrice())
                                .sum();

        order.setTotalPrice(cartTotalPrice);

        orderRepo.save(order);

        List<OrderDetailDTO> orderDetailDTOList = orderDetailList.stream()
                .map(orderDetail -> {
                    return DTOConverter.toOrderDetailDTO(orderDetail);
        }).collect(Collectors.toList());


         return orderDetailDTOList;
    }
}