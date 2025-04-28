package com.example.frontend.ui.screen.cart

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.dto.BasicCartItemDTO
import com.example.frontend.data.dto.CartBasicInfoDTO
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.CartRepository
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _cart = mutableStateOf<CartBasicInfoDTO?>(null)
    val cart: State<CartBasicInfoDTO?> = _cart

    private val _cartItemDTOs = mutableStateOf<List<CartItemDTO>>(emptyList())
    val cartItemDTOs: State<List<CartItemDTO>> = _cartItemDTOs


    private val _imageUrlPerCartItem = mutableStateOf<List<String>>(emptyList())
    val imageUrlPerCartItem = _imageUrlPerCartItem

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init{
        getCartItems()
        getImageUrlPerCartItem()
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
                        _errorMessage.value = "Failed to load cart items: ${response.message}"
                    }
                    is ApiResponse.Loading -> {
                        Log.d("CartViewModel-getCartItems", "Loading")
                    }
                }
            }
        }
    }




    fun getImageUrlPerCartItem() {
        viewModelScope.launch {
            try{
                val cartId = authManager.getCartIdOnce()
                if (cartId != null) {
                    val response = cartRepo.getImageUrlPerCartItem(cartId)
                    when(response){
                        is ApiResponse.Success -> {
                            _imageUrlPerCartItem.value = response.data
                        }
                        is ApiResponse.Error ->{
                           _errorMessage.value = "Failed to fetch image urls: ${response.message}"
                        }
                        is ApiResponse.Loading -> {}
                    }
                }else{
                    _errorMessage.value = "Cart ID is null"
                }
            }catch(e: Exception){
                _errorMessage.value = "Failed to fetch image URLs: ${e.message}"
            }
        }
    }


//    fun getImageUrl(productId: Long) {
//        if (_imageUrlMap.containsKey(productId)) return
//
//
//        viewModelScope.launch {
//            val response = productRepo.getImageUrl(productId)
//            if (response is ApiResponse.Success) {
//                val url = response.data
//                if (url.isNotBlank()) {
//                    _imageUrlMap[productId] = url
//                }
//            } else if (response is ApiResponse.Error) {
//                Log.e("CartViewModel", "Error fetching image URL: ${response.message}")
//            }
//        }
//    }


//    fun getImageUrs(productId: Long) {
//        if (_imageUrlMap.containsKey(productId)) return
//
//        viewModelScope.launch {
//            val response = productRepo.getImageUrl(productId)
//            if (response is ApiResponse.Success) {
//                val url = response.data.firstOrNull()
//                if (url != null) _imageUrlMap[productId] = url
//            }
//        }
//    }


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

    fun increaseQuantity(cartItemId: Long) {
        viewModelScope.launch {
            when (val response = cartRepo.increaseCartItemQuantity(cartItemId)) {
                is ApiResponse.Success -> {
                    val updatedItem = response.data
                    updateCartItem(updatedItem)
                }
                is ApiResponse.Error -> {
                    _errorMessage.value = "Failed to increase quantity: ${response.message}"
                }
                is ApiResponse.Loading -> {}
            }
        }
    }

    fun decreaseQuantity(cartItemId: Long) {
        viewModelScope.launch {
            when (val response = cartRepo.decreaseCartItemQuantity(cartItemId)) {
                is ApiResponse.Success -> {
                    val updatedItem = response.data
                    updateCartItem(updatedItem)
                }
                is ApiResponse.Error -> {
                    _errorMessage.value = "Failed to decrease quantity: ${response.message}"
                }
                is ApiResponse.Loading -> {}
            }
        }
    }

    private fun updateCartItem(updatedItem: BasicCartItemDTO) {
        _cartItemDTOs.value = _cartItemDTOs.value.map { item ->
            if (item.productId == updatedItem.productId) {
                item.copy(quantity = updatedItem.quantity) // 👈 chỉ update quantity
            } else {
                item
            }
        }
    }




}

