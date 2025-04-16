package com.example.frontend.ui.screen.home

import com.example.frontend.data.model.ProductData

data class ProductDetailUiState(
    val isLoading: Boolean = true,
    val product: ProductData? = null,
    val errorMessage: String? = null,
)