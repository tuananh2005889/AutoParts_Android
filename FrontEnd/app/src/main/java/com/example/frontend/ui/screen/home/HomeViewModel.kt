package com.example.frontend.ui.screen.home

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.example.frontend.data.model.ProductData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.CartRepository
import com.example.frontend.data.repository.ProductRepository
import com.example.frontend.ui.Controller.ProductController.Companion.getAllProducts
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val products: List<ProductData> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository,
    private val authManager: AuthManager,
    ) : ViewModel() {

    private val _homeUiState =
        MutableStateFlow<HomeUiState>(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    init{
        observeUserName()
        getAllProducts()
    }


//
//   private val _userName = MutableStateFlow<String?>(null)
//    val userName: StateFlow<String?> = _userName

    private fun observeUserName(){
        viewModelScope.launch {
            val userName= authManager.getUserNameOnce()

            if(userName != null){
                createCart(userName)
            }
            }



    }

    fun createCart(userName: String) {
        viewModelScope.launch {
            val response = cartRepo.getOrCreateCart(userName)
            if (response is ApiResponse.Success) {
                authManager.saveLoginStatus(true, userName, response.data.cartId.toString())
            }
        }
    }

    fun getAllProducts(){
        viewModelScope.launch {
            _homeUiState.value = HomeUiState()

            when(val result = productRepo.getAllProducts()){
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