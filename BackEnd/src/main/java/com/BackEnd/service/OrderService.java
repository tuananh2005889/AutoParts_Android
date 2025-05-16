package com.BackEnd.service;

import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.dto.CreateOrderResponse;
import com.BackEnd.dto.OrderDetailDTO;
import com.BackEnd.dto.PaymentRequest;
import com.BackEnd.model.*;
import com.BackEnd.repository.*;
import com.BackEnd.repository.OrderDetailRepository;
import com.BackEnd.utils.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;

//    public List<OrderDetailDTO> createOrder(Long cartId){
//        cartService.changeCartStatus(cartId, Cart.CartStatus.SUBMITTED);
//
//        //Create Order
//        Order order = new Order();
//        order.setUser(cartService.getUserByCartId(cartId));
//        order.setStatus(Order.OrderStatus.PENDING);
//        order.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
//        order.setShippingAddress("test");
//
//        // orderDetail,
//        List<CartItem> cartItemList = cartService.getAllCartItemsInActiveCart(cartId);
//        List<OrderDetail> orderDetailList = cartItemList.stream()
//                .map(cartItem -> {
//                    //product
//                    Product product = cartItem.getProduct();
//                    if (product == null || product.getPrice() == null) {
//                        throw new IllegalArgumentException("CartItem contains invalid product or price");
//                    }
//                    //quantity
//                    int quantity = cartItem.getQuantity();
//                    //totalPrice
//                    Double totalPrice = quantity * product.getPrice();
//
//                    return new OrderDetail(order, product, quantity, totalPrice);
//                })
//                .collect(Collectors.toList());
//
//        order.setOrderDetails(orderDetailList);
//
//        Double cartTotalPrice = orderDetailList.stream()
//                        .mapToDouble(orderDetail -> orderDetail.getTotalPrice())
//                                .sum();
//
//        order.setTotalPrice(cartTotalPrice);
//
//        orderRepo.save(order);
//
//         return orderDetailList.stream()
//                 .map(DTOConverter::toOrderDetailDTO)
//                 .collect(Collectors.toList());
//    }
//public List<OrderDetailDTO> createOrder(Long cartId) {
//    // Đổi trạng thái cart sang SUBMITTED
//    cartService.changeCartStatus(cartId, Cart.CartStatus.SUBMITTED);
//
//    // Lấy dữ liệu user và cart item trước
//    User user = cartService.getUserByCartId(cartId);
//    List<CartItem> cartItemList = cartService.getAllCartItemsInActiveCart(cartId);
//
//    // Tạo danh sách OrderDetail (chưa gán vào Order vội)
//    List<OrderDetail> orderDetailList = cartItemList.stream()
//            .map(cartItem -> {
//                Product product = cartItem.getProduct();
//                if (product == null || product.getPrice() == null) {
//                    throw new IllegalArgumentException("CartItem contains invalid product or price");
//                }
//                int quantity = cartItem.getQuantity();
//                double totalPrice = quantity * product.getPrice();
//                return new OrderDetail(null, product, quantity, totalPrice); // chưa gán Order
//            })
//            .collect(Collectors.toList());
//
//    // Tính tổng giá
//    double cartTotalPrice = orderDetailList.stream()
//            .mapToDouble(OrderDetail::getTotalPrice)
//            .sum();
//
//    // Tạo orderCode an toàn (thử nhiều lần nếu trùng)
//    Long orderCode;
//    String qrUrl = null;
//    int retry = 0;
//    boolean success = false;
//    while (!success && retry < 5) {
//        try {
//            orderCode =  System.currentTimeMillis() +  + new Random().nextInt(9999);
//            PaymentRequest paymentRequest = new PaymentRequest(orderCode, (int) cartTotalPrice, "AutoParts Checkout");
//            qrUrl = paymentService.createOrderInPayOS(paymentRequest);
//            success = true;
//
//            Order order = new Order();
//            order.setUser(user);
//            order.setStatus(Order.OrderStatus.PENDING);
//            order.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
//            order.setShippingAddress("test");
//            order.setOrderCode(orderCode);
//            order.setTotalPrice(cartTotalPrice);
//
//            for (OrderDetail od : orderDetailList) {
//                od.setOrder(order);
//            }
//
//            order.setOrderDetails(orderDetailList);
//            orderRepo.save(order); // chỉ lưu khi đã có qrUrl hợp lệ
//
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode() == HttpStatus.CONFLICT) {
//                retry++;
//                System.out.println("Duplicate ordercode, please try again");
//            } else {
//                throw e;
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    if (!success) {
//        throw new RuntimeException("Không thể tạo đơn hàng vì orderCode bị trùng quá nhiều lần.");
//    }
//
//    return orderDetailList.stream()
//            .map(DTOConverter::toOrderDetailDTO)
//            .collect(Collectors.toList());
//}
public CreateOrderResponse createOrder(Long cartId) {
    cartService.changeCartStatus(cartId, Cart.CartStatus.SUBMITTED);

    User user = cartService.getUserByCartId(cartId);
    List<CartItem> cartItemList = cartService.getAllCartItemsInActiveCart(cartId);

    List<OrderDetail> orderDetailList = cartItemList.stream()
            .map(cartItem -> {
                Product product = cartItem.getProduct();
                if (product == null || product.getPrice() == null) {
                    throw new IllegalArgumentException("CartItem contains invalid product or price");
                }
                int quantity = cartItem.getQuantity();
                double totalPrice = quantity * product.getPrice();
                return new OrderDetail(null, product, quantity, totalPrice);
            })
            .collect(Collectors.toList());

    double cartTotalPrice = orderDetailList.stream()
            .mapToDouble(OrderDetail::getTotalPrice)
            .sum();

    Long orderCode =0L;
    String qrCode = null ;
    int retry = 0;
    boolean success = false;
    while (!success && retry < 5) {
        try {
            orderCode =  System.currentTimeMillis() +  new Random().nextInt(9999);
            PaymentRequest paymentRequest = new PaymentRequest(orderCode, (int) cartTotalPrice, "AutoParts Checkout");
            qrCode = paymentService.createOrderInPayOS(paymentRequest);
            success = true;

            Order order = new Order();
            order.setUser(user);
            order.setStatus(Order.OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            order.setShippingAddress("test");
            order.setOrderCode(orderCode);
            order.setTotalPrice(cartTotalPrice);

            for (OrderDetail od : orderDetailList) {
                od.setOrder(order);
            }
            order.setOrderDetails(orderDetailList);
            orderRepo.save(order);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                retry++;
                System.out.println("OrderCode is duplicate, please try again");
            } else {
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    if (!success) {
        throw new RuntimeException("Cannot create order because duplicate orderCode too much");
    }

    return new CreateOrderResponse(qrCode, orderCode);
}



    public Boolean checkIfUserHasPendingOrder(String userName){
        User user = userService.getUserByName(userName);
        return orderRepo.existsByUserAndStatus(user, Order.OrderStatus.PENDING);
    }

    public Long getPendingOrderId(String userName){
        User user = userService.getUserByName(userName);
        Order order = orderRepo.findTopByUserAndStatusOrderByCreatedAtDesc(user, Order.OrderStatus.PENDING);
        return order.getOrderId();
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

    public void changeOrderStatus(Long orderCode, Order.OrderStatus status){
        try{
            Order order = orderRepo.findByOrderCode(orderCode);
            order.setStatus(status);
            orderRepo.save(order);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}