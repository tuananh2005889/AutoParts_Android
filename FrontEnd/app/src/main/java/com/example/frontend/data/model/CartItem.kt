package com.example.frontend.data.model

data class CartItem (
    val cartItemId: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
)