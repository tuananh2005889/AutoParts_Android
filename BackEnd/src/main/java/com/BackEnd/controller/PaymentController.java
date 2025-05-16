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
    @Value("${PAYOS_API_KEY}")
    private String apiKey;
    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;
    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checksumKey;

    private PayOS payOS;

    @PostConstruct
    public void init() {
        payOS = new PayOS(clientId, apiKey, checksumKey);
    }

    @GetMapping("/status")
    public PaymentStatus getPayosPaymentStatus(@RequestParam Long orderCode)  {
        try{
            PaymentLinkData paymentData = payOS.getPaymentLinkInformation(orderCode);
            String status = paymentData.getStatus();
            PaymentStatus paymentStatus = PaymentStatus.valueOf(status);
            return paymentStatus;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("/cancel")
    public PaymentLinkData cancelPayment(@RequestParam Long orderId) throws Exception{
        String cancelReason = "Thich thi huy";
        PaymentLinkData data = payOS.cancelPaymentLink(orderId, cancelReason);
        return data;
    }

    enum PaymentStatus{
        PENDING,
        PAID,
        CANCELLED,
    }
}


