package com.example.frontend.data.model

import java.time.LocalDateTime

data class OrderData(
    val orderId: String,
    val orderDate: LocalDateTime,
    val totalAmount: Double,
    val orderStatus: String,
    val orderDetails: List<OrderDetails>

    )