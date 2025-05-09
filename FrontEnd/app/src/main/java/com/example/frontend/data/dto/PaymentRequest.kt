package com.example.frontend.data.dto

data class PaymentRequest(
    val amount: Int,
    val description: String,
    val orderCode: Long
)