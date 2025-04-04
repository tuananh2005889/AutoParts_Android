package com.example.frontend.data.model

data class OrderDetails(
    val OrderDetailsId: Long,
    val product: ProductData,
    val quantity: Int,
    val price: Double,
    val order: OrderData
)