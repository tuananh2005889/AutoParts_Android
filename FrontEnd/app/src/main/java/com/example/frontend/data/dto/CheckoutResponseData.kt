package com.example.frontend.data.dto
data class CheckoutResponseData(
    val bin: String,
    val accountNumber: String,
    val accountName: String,
    val amount: Int,
    val description: String,
    val orderCode: Long,
    val currency: String,
    val paymentLinkId: String,
    val status: String,
    val expiredAt: Long?,
    val checkoutUrl: String,
    val qrCode: String
)
