package com.BackEnd.dto;

import com.BackEnd.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long orderCode;
    private String userName;
    @JsonFormat(pattern = "HH:mm:ss_dd/MM/yyyy")
    private LocalDateTime createTime;
    private Double totalPrice;
    private Order.OrderStatus status;
    private String qrCodeToCheckout;
    private List<OrderDetailDTO> orderDetailDTOList;
}
