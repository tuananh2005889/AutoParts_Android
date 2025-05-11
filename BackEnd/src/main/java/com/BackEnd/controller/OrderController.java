package com.BackEnd.controller;

import com.BackEnd.dto.OrderDetailDTO;
import com.BackEnd.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/app/order")
@RequiredArgsConstructor
public class OrderController{
    private final OrderService orderService;
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
    public ResponseEntity<List<OrderDetailDTO>> createOrder(@RequestParam Long cartId ){
        List<OrderDetailDTO> orderDetailDTOList = orderService.createOrder(cartId);
        return ResponseEntity.ok(orderDetailDTOList);
    }

}