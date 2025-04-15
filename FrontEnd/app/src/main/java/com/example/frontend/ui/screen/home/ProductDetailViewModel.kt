package com.example.frontend.ui.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.ProductData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor (private val repository: ProductRepository) : ViewModel(){
    private val _productState = mutableStateOf<ApiResponse<ProductData>>(ApiResponse.Loading)
    val productState  = _productState

    fun getProductById(productId: String){
        viewModelScope.launch{
          _productState.value = ApiResponse.Loading
            when(val result = repository.getProductById(productId)){
                is ApiResponse.Success -> {
                    _productState.value = ApiResponse.Success(result.data)
                }
                is ApiResponse.Error -> {
                    _productState.value = ApiResponse.Error(result.message)

                }
                ApiResponse.Loading -> {}
            }
        }
    }

}