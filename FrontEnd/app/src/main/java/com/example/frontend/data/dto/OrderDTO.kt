package com.example.frontend.data.dto

import java.time.LocalDateTime

data class OrderDTO(
    val orderId: Long,
    val orderCode: Long,
    val userName: String,
    val createTime: String,
    val totalPrice: Double,
    val status: OrderStatus,
    val qrCodeToCheckout: String,
    val orderDetailDTOList: List<OrderDetailDTO>,
)
