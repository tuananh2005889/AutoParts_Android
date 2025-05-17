package com.BackEnd.dto;

import com.BackEnd.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long orderCode;
    private String userName;
    private LocalDateTime createTime;
    private Double totalPrice;
    private Order.OrderStatus status;
    private String qrCodeToCheckout;

}
