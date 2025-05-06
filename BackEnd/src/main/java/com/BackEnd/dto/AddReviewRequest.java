package com.BackEnd.dto;

import lombok.Data;

@Data
public class AddReviewRequest {
    private Long productId;
    private Long userId;
    private Integer rating;
    private String comment;
}
