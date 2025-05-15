package com.BackEnd.service;

import com.BackEnd.dto.PaymentRequest;
import com.BackEnd.utils.SignatureUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
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

    public String createOrderInPayOS(PaymentRequest request) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("amount", String.valueOf(request.getAmount()));
        params.put("cancelUrl", "autoparts://payment-cancel");
        params.put("description", request.getDescription());
        params.put("orderCode", String.valueOf(request.getOrderCode()));
        params.put("returnUrl", "autoparts://payment-return");

        String signature = SignatureUtil.createSignature(params, checksumKey);

        PaymentData paymentData = PaymentData.builder()
                .orderCode(request.getOrderCode())
                .amount(request.getAmount())
                .description(request.getDescription())
                .cancelUrl("autoparts://payment-cancel")
                .returnUrl("autoparts://payment-return")
                .signature(signature)
                .build();

        CheckoutResponseData checkoutResponse= payOS.createPaymentLink(paymentData);
        String qrCode = checkoutResponse.getQrCode();
        return qrCode;
    }
}
