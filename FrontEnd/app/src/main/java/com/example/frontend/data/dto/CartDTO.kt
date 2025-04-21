package com.example.frontend.data.dto

data class CartDTO(
    val cartId: Long,
    val cartItemDTO: List<CartItemDTO>,
    val status: String
)