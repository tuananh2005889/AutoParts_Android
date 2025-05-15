package com.example.frontend.data.dto

data class PayosResponse(
    val code: String,
    val desc: String,
    val payosData: PayosData,
)
data class PayosData(
    val bin: String,
    val accountNumber: String,
    val accountName: String,
    val currency: String,
    val paymentLinkId: String,
    val amount: Int,
    val description: String,
    val orderCode: Int,
    val status: String,
    val checkoutUrl: String,
    val qrCode: String,
)