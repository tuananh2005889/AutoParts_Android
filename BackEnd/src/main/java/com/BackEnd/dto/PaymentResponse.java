package com.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponse {
    String code;
    String desc;
    PayosData data;
    String signature;

}
class PayosData{
    String bin;
    String accountNumber;
    String acountName;
    String currency;
    String paymentLinkId;
    int amount;
    String description;
    int orderCode;
    String status;
    String checkoutUrl;
    String qrCode;

}
