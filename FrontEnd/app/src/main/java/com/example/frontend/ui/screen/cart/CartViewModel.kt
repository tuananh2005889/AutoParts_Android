package com.example.frontend.ui.screen.cart

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.dto.CartBasicInfoDTO
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.CartRepository
import com.example.frontend.data.repository.ProductRepository
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepository,
    private val authManager: AuthManager,
    private val productRepo: ProductRepository,
) : ViewModel() {

    private val _cart = mutableStateOf<CartBasicInfoDTO?>(null)
    val cart: State<CartBasicInfoDTO?> = _cart

    private val _cartItemDTOs = mutableStateOf<List<CartItemDTO>>(emptyList())
    val cartItemDTOs: State<List<CartItemDTO>> = _cartItemDTOs

    private val _imageUrlMap = mutableStateMapOf<Long, String>()
    val imageUrlMap: Map<Long, String> = _imageUrlMap

    init{
        getCartItems()
    }

    fun getImageUrl(productId: Long) {
        if (_imageUrlMap.containsKey(productId)) return

        viewModelScope.launch {
            val response = productRepo.getImageUrls(productId)
            if (response is ApiResponse.Success) {
                val url = response.data.firstOrNull()
                if (url != null) _imageUrlMap[productId] = url
            }
        }
    }


        fun getCart() {
            viewModelScope.launch {
                val userName = authManager.getUserNameOnce()
                Log.d("CartViewModel", "userName: $userName")
                if (userName != null) {
                    val response = cartRepo.getOrCreateActiveCart(userName)
                    if (response is ApiResponse.Success) {
                        _cart.value = response.data
                    }
                    Log.d("CartViewModel", "Cart: ${_cart.value}")
                }
            }

        }

        fun getCartItems() {
            viewModelScope.launch {
                val cartId = authManager.getCartIdOnce()
                Log.d("CartViewModel-getCartItems", "cartId: $cartId")
                if (cartId != null) {
                    val response = cartRepo.getCartItems(cartId)
                    when (response) {
                        is ApiResponse.Success -> {
                            _cartItemDTOs.value = response.data
                        }

                        is ApiResponse.Error -> {
                            Log.d("CartViewModel-getCartItems", "Error: ${response.message}")

                        }

                        is ApiResponse.Loading -> {
                            Log.d("CartViewModel-getCartItems", "Loading")
                        }
                    }

                }
            }

        }


    }

