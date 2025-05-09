package com.example.frontend.data.model

data class ReviewData(
    val reviewId: Long,
    val userName: String,
    val userId: Long,
    val productId: Long,
    val rating: Int,
    val comment: String,
    val userAvatarUrl: String?,
    val createdAt: String
)