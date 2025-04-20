package com.example.frontend.data.model

data class Cart (
    val cartId: Long? = 0,
    val userId: Long? = 0,
    val cartItems: List<CartItem>? = emptyList<CartItem>(),
    val status: String? = "CANCELLED",
    )