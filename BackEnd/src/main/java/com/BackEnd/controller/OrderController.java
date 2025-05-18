package com.BackEnd.controller;

import com.BackEnd.dto.CreateOrderResponse;
import com.BackEnd.dto.OrderDTO;
import com.BackEnd.dto.OrderDetailDTO;
import com.BackEnd.dto.PaymentRequest;
import com.BackEnd.model.Cart;
import com.BackEnd.model.Order;
import com.BackEnd.model.Payment;
import com.BackEnd.model.User;
import com.BackEnd.service.CartService;
import com.BackEnd.service.OrderService;
import com.BackEnd.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/order")
@RequiredArgsConstructor // giup DI, khong can tao constructor
public class OrderController{
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final CartService cartService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${PAYOS_API_KEY}")
    private String apiKey;
    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;

    @PostMapping("/create")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestParam Long cartId) {
        try {
            CreateOrderResponse response = orderService.createOrder(cartId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/check-pending-status")
    public ResponseEntity<Boolean> checkIfUserHasPendingOrder(@RequestParam String userName){
        boolean result =  orderService.checkIfUserHasPendingOrder(userName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pending-order-detail-list")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetailListInPendingOrder(@RequestParam String userName){
        try {
            List<OrderDetailDTO> result = orderService.getOrderDetailListInPendingOrder(userName);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/change-order-status")
    public ResponseEntity<Void> changeOrderStatus(@RequestParam Long orderCode, @RequestParam Order.OrderStatus status){
        orderService.changeOrderStatus(orderCode, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-orders-by-status")
    public List<OrderDTO> getOrder(@RequestParam Order.OrderStatus status){
        return orderService.getOrdersByStatus(status);
    }

    @GetMapping("/get-all-orders")
    public List<OrderDTO> getAllOrder(){
    return orderService.getAllOrders();
    }

    @GetMapping("/get-pending-order-of-user")
    public List<OrderDTO> getPendingOrderOfUser(@RequestParam String userName) throws Exception{
       return orderService.getAllOrdersOfUserByStatusAndName(userName, Order.OrderStatus.PENDING);
    }

    @GetMapping("/get-all-paid-orders-of-user")
    public List<OrderDTO> getAllPaidOrdersOfUser(@RequestParam String userName) throws Exception{
        return orderService.getAllOrdersOfUserByStatusAndName(userName, Order.OrderStatus.PAID);
    }
    @GetMapping("/get-all-submitted-orders-of-user")
    public List<OrderDTO> getAllSubmittedOrdersOfUser(@RequestParam String userName) throws Exception{
        return orderService.getAllOrdersOfUserByStatusAndName(userName, Order.OrderStatus.SUBMITTED);
    }
    @GetMapping("/get-all-shipped-orders-of-user")
    public List<OrderDTO> getAllShippedOrdersOfUser(@RequestParam String userName) throws Exception{
        return orderService.getAllOrdersOfUserByStatusAndName(userName, Order.OrderStatus.SHIPPED);
    }
    @GetMapping("/get-all-dlivered-orders-of-user")
    public List<OrderDTO> getAllDeliveredOrdersOfUser(@RequestParam String userName) throws Exception{
        return orderService.getAllOrdersOfUserByStatusAndName(userName, Order.OrderStatus.DELIVERED);
    }

}