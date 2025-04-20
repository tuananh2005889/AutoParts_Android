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
import com.example.frontend.data.model.ProductData

data class ProductDetailUiState(
    val isLoading: Boolean = true,
    val product: ProductData? = null,
    val errorMessage: String? = null,
    val quantity: Int = 1,
    val initialPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor (private val repository: ProductRepository) : ViewModel(){
    private val _productDetailState = mutableStateOf<ProductDetailUiState>(ProductDetailUiState())
    val productDetailState: State<ProductDetailUiState>  = _productDetailState

    val quantity  = mutableStateOf(1)
    val totalPrice: State<Double> = derivedStateOf {
        val initialPrice = _productDetailState.value.product?.price ?: 0.0
        initialPrice * quantity.value

    }

    fun increaseQuantity(){
        quantity.value++
    }
    fun decreaseQuantity(){
        if(quantity.value > 1){
            quantity.value--
        }
    }

    fun getProductById(productId: Long){
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

