package com.example.frontend.ui.screen.home

import com.google.gson.annotations.SerializedName



data class Product(
//    val productId: String,
    val name: String = "",
    val brand: String = "",
    val description: String = "",
    val category: String="",
    val compatibleVehicles: String = "",
    val yearOfManufacture: Int? = null,
    val size: String = "",
    val material: String = "",
    val weight: Double? = null,
    @SerializedName("images") val imageUrlList: List<String> = emptyList(),
    val discount: Double? = null,
    val warranty: String = "",
    val price: Double? = null,
    val quantity: Int? = null
)