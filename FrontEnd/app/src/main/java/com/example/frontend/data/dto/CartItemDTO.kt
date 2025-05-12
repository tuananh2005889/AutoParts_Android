package com.example.frontend.data.dto

data class CartItemDTO(
    val cartItemId: Long,
    val productId: Long,
    val productName: String,
    val price: Double,
    val quantity: Int,
)

