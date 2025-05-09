package com.example.frontend.data.dto

data class CreateOrderResponse(
    val checkoutUrl: String,
    val orderCode: String
)
