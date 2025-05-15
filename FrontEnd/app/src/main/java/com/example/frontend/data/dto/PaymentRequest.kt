package com.example.frontend.data.dto

data class PaymentRequest(
    val orderCode: Int,
    val amount: Int,
    val description: String,

)
