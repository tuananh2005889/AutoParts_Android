package com.BackEnd.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private String imageUrl;
}
