package com.BackEnd.dto;

import com.BackEnd.model.Product;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductRequest {
    private Product product;
    private List<String> imageUrls;

}
