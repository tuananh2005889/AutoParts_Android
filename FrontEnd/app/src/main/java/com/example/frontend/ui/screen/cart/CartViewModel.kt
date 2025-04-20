package com.example.frontend.ui.screen.cart

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Cart
import com.example.frontend.data.repository.CartRepository
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import coil.util.CoilUtils.result
import com.example.frontend.data.remote.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _cart = mutableStateOf<Cart?>(null)
    val cart: State<Cart?> = _cart

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    init {
        viewModelScope.launch {
            _userName.value = authManager.getUserNameOnce()

            _userName.value?.let { name ->
                // Gọi API lấy cart
                val cart = cartRepository.getOrCreateCart(name)
                // xử lý cart...
                when(cart){
                    is ApiResponse.Success -> {
                        _cart.value = cart.data
                        _error.value = null
                    }
                    is ApiResponse.Error -> {
                        _error.value = cart.message
                    }
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    fun loadOrCreateCart() {
        viewModelScope.launch {
            val result = cartRepository.getOrCreateCart(_userName.value.toString())
            when(result){
                is ApiResponse.Success -> {
                    _cart.value = result.data
                    _error.value = null
                }
                is ApiResponse.Error -> {
                    _error.value = result.message
                }
                is ApiResponse.Loading -> {}
            }


        }
    }
}


//@HiltViewModel
//class CartViewModel @Inject constructor(
//    private val cartRepository: CartRepository,
//    private val authManager: AuthManager
//    ) : ViewModel() {
//
//    val _cartState = mutableStateOf(Cart())
//    var cartState: State<Cart> = _cartState
//   init{
//       getOrCreateCart()
//   }
//     fun getOrCreateCart(){
//         viewModelScope.launch {
//             val userName=  authManager.getUserNameOnce().toString()
//
//           _cartState.value =   cartRepository.getOrCreateCart(userName)
//         }
//
//    }
//
//
//}