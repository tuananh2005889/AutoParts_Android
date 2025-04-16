package com.example.frontend.ui.screen.home

import com.example.frontend.data.model.ProductData

data class HomeUiState(
    val isLoading: Boolean = true,
    val products: List<ProductData> = emptyList(),
    val errorMessage: String? = null
)
