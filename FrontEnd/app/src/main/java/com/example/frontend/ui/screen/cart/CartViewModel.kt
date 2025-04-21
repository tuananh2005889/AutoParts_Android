package com.example.frontend.ui.screen.cart

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Cart
import com.example.frontend.data.repository.CartRepository
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.frontend.data.dto.CartBasicInfoDTO
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.ui.common.AuthPreferencesKeys.userName
import com.example.frontend.ui.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _cart = mutableStateOf<CartBasicInfoDTO?>(null)
    val cart: State<CartBasicInfoDTO?> = _cart

//    private val _userName = MutableStateFlow<String?>(null)
//    val userName: StateFlow<String?> = _userName

    fun getCart() {
        viewModelScope.launch {
            val userName = authManager.getUserNameOnce()
            Log.d("CartViewModel", "userName: $userName")
            if (userName != null) {
                val response = cartRepo.getOrCreateCart(userName)
                if (response is ApiResponse.Success) {
                    _cart.value = response.data
                }
                Log.d("CartViewModel", "Cart: ${_cart.value}")
            }
        }


    }

    init {
        getCart()
    }
}

