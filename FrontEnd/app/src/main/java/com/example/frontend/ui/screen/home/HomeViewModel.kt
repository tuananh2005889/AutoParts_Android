package com.example.frontend.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.ProductData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _productState =
        MutableStateFlow<ApiResponse<List<ProductData>>>(ApiResponse.Loading)
    val productState: StateFlow<ApiResponse<List<ProductData>>> = _productState

    init{
        getAllProducts()
    }

    fun getAllProducts(){
        viewModelScope.launch {
            _productState.value = ApiResponse.Loading

            when(val result = repository.getAllProducts()){
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