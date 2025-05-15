package com.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private Double totalPrice;
}
