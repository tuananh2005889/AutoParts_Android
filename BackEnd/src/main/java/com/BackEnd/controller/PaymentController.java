package com.BackEnd.controller;
import com.BackEnd.dto.PaymentRequest;
import com.BackEnd.utils.SignatureUtil;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.PaymentData;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/payment")
@CrossOrigin
public class PaymentController {
//    @Value("${PAYOS_API_KEY}")
//    private String apiKey;
//    @Value("${PAYOS_CLIENT_ID}")
//    private String clientId;
//    @Value("${PAYOS_CHECKSUM_KEY}")
//    private String checksumKey;
//
//    private PayOS payOS;
//
//    @PostConstruct
//    public void init() {
//        payOS = new PayOS(clientId, apiKey, checksumKey);
//    }

//    @PostMapping("/create-payment")
//    public String createPaymentLink(@RequestBody PaymentRequest request) throws Exception {
//        Map<String, String> params = new HashMap<>();
//        params.put("amount", String.valueOf(request.getAmount()));
//        params.put("cancelUrl", "autoparts://payment-cancel");
//        params.put("description", request.getDescription());
//        params.put("orderCode", String.valueOf(request.getOrderCode()));
//        params.put("returnUrl", "autoparts://payment-return");
//
//        String signature = SignatureUtil.createSignature(params, checksumKey);
//
//        PaymentData paymentData = PaymentData.builder()
//                .orderCode(request.getOrderCode())
//                .amount(request.getAmount())
//                .description(request.getDescription())
//                .cancelUrl("autoparts://payment-cancel")
//                .returnUrl("autoparts://payment-return")
//                .signature(signature)
//                .build();
//
//        CheckoutResponseData  checkoutResponse= payOS.createPaymentLink(paymentData);
//        String qrCode = checkoutResponse.getQrCode();
//        return qrCode;
//    }

//    @GetMapping("/status")
//    public PaymentStatus getPaymentStatus(@RequestParam Long orderId)  {
//        try{
//            PaymentLinkData paymentData = payOS.getPaymentLinkInformation(orderId);
//            String status = paymentData.getStatus();
//            PaymentStatus paymentStatus = PaymentStatus.valueOf(status);
//            return paymentStatus;
//        }catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
    enum PaymentStatus{
        PENDING,
        PAID,
        CANCELLED,
    }
}


