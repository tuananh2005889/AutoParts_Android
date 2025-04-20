package com.example.frontend.data.dto

data class AddToCartRequest(
    val cartId: Long,
    val productId: Long,
    val quantity: Int,
)
