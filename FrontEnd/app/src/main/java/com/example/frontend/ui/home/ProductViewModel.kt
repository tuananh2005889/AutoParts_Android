package com.example.frontend.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

//    private val _uiState = MutableLiveData<UIState>()
//    val uiState: LiveData<UIState> = _uiState
//    init{
//        fetchProducts()
//    }
//
//    fun fetchProducts() {
//        _uiState.value = UIState.Loading
//        viewModelScope.launch {
//            try {
//                val products = productRepository.getProducts()
//                _uiState.value = UIState.Success(products)
//            } catch (e: Exception) {
//                _uiState.value = UIState.Error(e.message ?: "Unknown Error")
//            }
//        }
//    }
}
