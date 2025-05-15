package com.BackEnd.controller;

import com.BackEnd.dto.OrderDetailDTO;
import com.BackEnd.dto.PaymentRequest;
import com.BackEnd.model.Cart;
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

import java.util.List;

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

//    @PostMapping("/create")
//    public ResponseEntity<?> createOrder(@RequestParam Double ammount) {
//        String orderCode = UUID.randomUUID().toString();
//        Double amount = ammount;
//
//        Map<String, Object> body = Map.of(
//                "orderCode", orderCode,
//                "amount", amount,
//                "description", "Thanh toán đơn hàng " + orderCode,
//                "returnUrl", "autoparts://payment-result",
//                "cancelUrl", "autoparts://payment-cancel"
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("x-client-id", "YOUR_CLIENT_ID");
//        headers.set("x-api-key", "YOUR_API_KEY");
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
//
//        ResponseEntity<Map> res = restTemplate.postForEntity("https://api.payos.vn/v1/payment-requests", entity, Map.class);
//
//        if (res.getStatusCode().is2xxSuccessful()) {
//            String checkoutUrl = (String) ((Map<String, Object>) res.getBody().get("data")).get("checkoutUrl");
//            // Lưu trạng thái đơn hàng là "pending" trong DB
//            return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl, "orderCode", orderCode));
//        }
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tạo đơn hàng thất bại");
//    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestParam Long cartId) {
        try {
            List<OrderDetailDTO> orderDetailDTOList = orderService.createOrder(cartId);
            Integer totalPrice = 0;
            for(OrderDetailDTO orderDetailDTO : orderDetailDTOList){
                totalPrice +=  orderDetailDTO.getTotalPrice().intValue();
            }


            User user =  cartService.getUserByCartId(cartId);
            Long orderId =  orderService.getPendingOrderId(user.getUserName());
            PaymentRequest paymentRequest = new PaymentRequest(orderId, totalPrice,"Checkout AutoParts Order");
            String qrCode =  paymentService.createOrderInPayOS(paymentRequest);

            return ResponseEntity.ok(qrCode);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
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

}