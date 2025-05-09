package com.example.frontend.data.model

data class AddReviewRequest(
    val productId: Long,
    val userId: Long,
    val rating: Int,
    val comment: String
)