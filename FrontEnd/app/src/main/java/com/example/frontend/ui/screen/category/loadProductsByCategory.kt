package com.example.frontend.ui.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.ProductData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// State giữ dữ liệu UI
data class UiState(
    val isLoading: Boolean = false,
    val products: List<ProductData> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            when (val resp = repo.getProductsByCategory(category)) {
                is ApiResponse.Success<List<ProductData>> -> {
                    _uiState.value = UiState(products = resp.data)
                }
                is ApiResponse.Error -> {
                    _uiState.value = UiState(error = resp.message)
                }

                ApiResponse.Loading -> TODO()
            }
        }
    }
}
