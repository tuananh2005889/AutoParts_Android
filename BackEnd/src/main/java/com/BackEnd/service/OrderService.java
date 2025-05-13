package com.BackEnd.service;

import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.dto.OrderDetailDTO;
import com.BackEnd.model.*;
import com.BackEnd.repository.*;
import com.BackEnd.repository.OrderDetailRepository;
import com.BackEnd.utils.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        cartService.changeCartStatus(cartId, Cart.CartStatus.SUBMITTED);

        //Create Order
        Order order = new Order();
        order.setUser(cartService.getUserByCartId(cartId));
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        order.setShippingAddress("test");

        // orderDetail,
        List<CartItem> cartItemList = cartService.getAllCartItemsInActiveCart(cartId);
        List<OrderDetail> orderDetailList = cartItemList.stream()
                .map(cartItem -> {
                    //product
                    Product product = cartItem.getProduct();
                    if (product == null || product.getPrice() == null) {
                        throw new IllegalArgumentException("CartItem contains invalid product or price");
                    }
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

         return orderDetailList.stream()
                 .map(DTOConverter::toOrderDetailDTO)
                 .collect(Collectors.toList());
    }

    public Boolean checkIfUserHasPendingOrder(String userName){
        User user = userService.getUserByName(userName);
        return orderRepo.existsByUserAndStatus(user, Order.OrderStatus.PENDING);
    }

    @Transactional
    public List<OrderDetailDTO> getOrderDetailListInPendingOrder(String userName){
        User user = userService.getUserByName(userName);
        Order order = orderRepo.findByUserAndStatus(user, Order.OrderStatus.PENDING);
        List<OrderDetail> orderDetailList = order.getOrderDetails();
        return orderDetailList.stream()
                .map(DTOConverter::toOrderDetailDTO)
                .collect(Collectors.toList());
    }
}