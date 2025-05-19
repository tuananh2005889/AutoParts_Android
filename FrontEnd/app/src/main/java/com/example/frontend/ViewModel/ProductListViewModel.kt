package com.example.frontend.ViewModel

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

/**
 * State của màn ProductList theo Category
 */
data class CategoryUiState(
    val isLoading: Boolean = false,
    val products: List<ProductData> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class CategoryProductListViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState(isLoading = true))
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    /**
     * Tải sản phẩm dựa trên category và cập nhật state
     */
    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            // đặt giao diện vào trạng thái loading
            _uiState.value = CategoryUiState(isLoading = true)

            when (val resp = repo.getProductsByCategory(category)) {
                is ApiResponse.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    val list = resp.data as? List<ProductData> ?: emptyList()
                    _uiState.value = CategoryUiState(products = list)
                }
                is ApiResponse.Error -> {
                    _uiState.value = CategoryUiState(error = resp.message)
                }
                ApiResponse.Loading -> {
                    // Nếu cần xử lý loading thêm
                }
            }
        }
    }
}
