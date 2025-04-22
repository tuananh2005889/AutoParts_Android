package com.example.frontend.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.data.model.ProductData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.CartRepository
import com.example.frontend.data.repository.ProductRepository
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


    private fun observeUserName(){
        viewModelScope.launch {
            val userName= authManager.getUserNameOnce()
            Log.d("HomeViewModel-obserUserName", "userName: $userName")
                if(userName != null){
                createCart(userName)
                }
            }
    }

    fun createCart(userName: String) {
        viewModelScope.launch {
            when (val response = cartRepo.getOrCreateActiveCart(userName)) {
                is ApiResponse.Success -> {
                    val cartId = response.data.cartId
                    Log.d("HomeViewModel-create cart", "Cart created with ID: $cartId")
                    if (cartId != null) {
                        authManager.saveLoginStatus(true, userName, cartId)
                    }
                }
                is ApiResponse.Error -> {
                    Log.d("HomeViewModel-create cart", "Error creating cart: ${response.message}")
                }
                is ApiResponse.Loading -> {
                }
            }
        }
    }

    suspend fun addOneProductToCart(productId: Long
    ): CartItemDTO? {
            val cartId: Long? = authManager.getCartIdOnce()

            if (cartId == null) {
                Log.e("HomeViewModel", "Cannot get cartId to add product to cart")
                return null
            }

            val result = cartRepo.addProductToCart(
                productId = productId,
                quantity = 1,
                cartId = cartId
            )

            return when (result) {
                is ApiResponse.Success -> {
                    Log.d("HomeViewModel - addOneProductToCart", "Add to cart successfully: ${result.data}")
                     result.data
                }
                is ApiResponse.Error -> {
                    Log.e("HomeViewModel - addOneProductToCart", "Error when add item to cart: ${result.message}")
                    null  
                }

                ApiResponse.Loading -> null
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