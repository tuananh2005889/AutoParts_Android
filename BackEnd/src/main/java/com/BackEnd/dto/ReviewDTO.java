package com.BackEnd.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long reviewId;
    private String comment;
    private Integer rating;
    private String createdAt;
    private Long productId;
    private Long userId;
    private String userName;
    private String userAvatarUrl;
}
