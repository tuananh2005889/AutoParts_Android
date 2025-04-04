package com.example.frontend.compon

import com.example.frontend.data.model.ProductData

sealed class UIState {
    object Loading : UIState()
    data class Success(val products: List<ProductData>) : UIState()
    data class Error(val message: String) : UIState()
}
