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

    private val _homeUiState =
        MutableStateFlow<HomeUiState>(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    init{
        getAllProducts()
    }

    fun getAllProducts(){
        viewModelScope.launch {
            _homeUiState.value = HomeUiState()

            when(val result = repository.getAllProducts()){
                is ApiResponse.Success -> {
                    _homeUiState.value = HomeUiState(products = result.data, isLoading = false)
                }
                is ApiResponse.Error -> {
                    _homeUiState.value = HomeUiState(errorMessage = result.message, isLoading = false)
                }
                is ApiResponse.Loading -> {
                    _homeUiState.value = HomeUiState(isLoading = true)
                }
            }
        }
    }

}