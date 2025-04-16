package com.example.frontend.ui.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*

@HiltViewModel
class ProductDetailViewModel @Inject constructor (private val repository: ProductRepository) : ViewModel(){
    private val _productDetailState = mutableStateOf<ProductDetailUiState>(ProductDetailUiState())
    val productDetailState: State<ProductDetailUiState>  = _productDetailState

    fun getProductById(productId: String){
        viewModelScope.launch{
          _productDetailState.value = ProductDetailUiState()
            when(val result = repository.getProductById(productId)){
                is ApiResponse.Success -> {
                    _productDetailState.value = ProductDetailUiState(product = result.data, isLoading = false)
                }
                is ApiResponse.Error -> {
                    _productDetailState.value = ProductDetailUiState(errorMessage = result.message, isLoading = false)

                }
                ApiResponse.Loading -> {
                    _productDetailState.value = ProductDetailUiState()
                }
            }
        }
    }

}