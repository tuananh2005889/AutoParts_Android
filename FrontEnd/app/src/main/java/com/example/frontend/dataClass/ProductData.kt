package com.example.frontend.dataClass


data class ProductData(
    val category_id: String? = null,
    val name: String = "",
    val brand: String = "",
    val description: String = "",
    val compatibleVehicles: String = "",
    val yearOfManufacture: Int? = null,
    val size: String = "",
    val material: String = "",
    val weight: Double? = null,
    val image: String = "",
    val discount: Double? = null,
    val warranty: String = "",
    val price: Double? = null,
    val quantity: Int? = null
)