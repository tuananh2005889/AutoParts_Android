package com.example.frontend.data.model

data class Cart (
    val cartId: Long,
    val userId: Long,
    val cartItems: List<CartItem>,
    val status: String
    )