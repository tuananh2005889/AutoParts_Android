package com.example.frontend.data.dto

data class OrderDetailDTO(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val totalPrice: Double,
)
