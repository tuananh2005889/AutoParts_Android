package com.example.frontend.data.model

data class CartItemData(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val price: Double,
    val imageUrl: String? = null
)
