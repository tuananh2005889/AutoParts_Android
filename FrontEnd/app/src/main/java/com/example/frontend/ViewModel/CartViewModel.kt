package com.example.frontend.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.frontend.data.dto.BasicCartItemDTO
import com.example.frontend.data.dto.CartBasicInfoDTO
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.data.dto.CreateOrderResponse
import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.CartApiService
import com.example.frontend.data.repository.CartRepository
import com.example.frontend.data.repository.OrderRepository
import com.example.frontend.ui.common.AuthManager
import com.example.frontend.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepository,
    private val authManager: AuthManager,
    private val orderRepo: OrderRepository,
) : ViewModel() {

    private val _cart = mutableStateOf<CartBasicInfoDTO?>(null)
    val cart: State<CartBasicInfoDTO?> = _cart

    val _cartId: StateFlow<Long?> = authManager.cartIdFlow
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            null
        )
    private val _cartTotalPrice = mutableDoubleStateOf(0.0)
    val cartTotalPrice: State<Double> = _cartTotalPrice

    private val _cartItemDTOList = mutableStateOf<List<CartItemDTO>>(emptyList())
    val cartItemDTOList: State<List<CartItemDTO>> = _cartItemDTOList

    private val _imageUrlPerCartItemList = mutableStateOf<List<String>>(emptyList())
    val imageUrlPerCartItemList = _imageUrlPerCartItemList

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _hasPendingOrder = mutableStateOf<Boolean>(false)
    val hasPendingOrder: State<Boolean> = _hasPendingOrder

    init{
        viewModelScope.launch {
            checkIfUserHasPendingOrder()
            _cartId
                .filterNotNull()
                .collect { cartId ->
                    getAllCartItems(cartId)
                    getImageUrlPerCartItem(cartId)
                    getTotalPrice(cartId)
                }
        }
    }

    fun getAllCartItems(cartId: Long) {
        viewModelScope.launch {
            val cartId = _cartId.value
            Log.d("CartVM-getAllCartItems", "cartId: $cartId")
            if (cartId != null) {
                val response = cartRepo.getAllCartItems(cartId)
                when (response) {
                    is ApiResponse.Success -> {
                        _cartItemDTOList.value = response.data
                    }
                    is ApiResponse.Error -> {
                        Log.d("CartVM-getCartItems", "Error: ${response.message}")
                        _errorMessage.value = "Failed to load cart items: ${response.message}"
                    }
                    is ApiResponse.Loading -> {
                        Log.d("CartVM-getCartItems", "Loading")
                    }
                }
            }
        }
    }

    fun getImageUrlPerCartItem(cartId: Long) {
        viewModelScope.launch {
            try{
                if (cartId != null) {
                    val response = cartRepo.getImageUrlPerCartItem(cartId)
                    when(response){
                        is ApiResponse.Success -> {
                            _imageUrlPerCartItemList.value = response.data
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

                    // update cart total price
                    _cartId.filterNotNull().collect{
                        cartId -> getTotalPrice(cartId)
                    }

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

                    // update cart total price
                    _cartId.filterNotNull().collect{
                            cartId -> getTotalPrice(cartId)
                    }
                }
                is ApiResponse.Error -> {
                    _errorMessage.value = "Failed to decrease quantity: ${response.message}"
                }
                is ApiResponse.Loading -> {}
            }
        }
    }

    private fun updateCartItem(updatedItem: BasicCartItemDTO) {
        _cartItemDTOList.value = _cartItemDTOList.value.map { item ->
            if (item.productId == updatedItem.productId) {
                item.copy(quantity = updatedItem.quantity)
            } else {
                item
            }
        }
    }

    private fun getTotalPrice(cartId: Long){
        viewModelScope.launch {
            when(val response = cartRepo.getTotalPrice(cartId)){
                is ApiResponse.Success ->{
                    _cartTotalPrice.doubleValue = response.data
                }
                is ApiResponse.Error ->{
                    _errorMessage.value = "Failed to get total price: ${response.message}"
                    Log.d("CartVM-getTotalPrice", "Error: ${response.message}")

                }
                is ApiResponse.Loading -> {}
            }
        }
    }
    fun removeItemFromCart(cartItemId: Long) {
        viewModelScope.launch {
            when (val response = cartRepo.removeItemFromCart(cartItemId)) {
                is ApiResponse.Success -> {
                    // Sau khi backend xóa thành công hãy reload
                    _cartId.value?.let { id ->
                        getAllCartItems(id)
                        getTotalPrice(id)
                    }
                }
                is ApiResponse.Error -> {
                    _errorMessage.value = "Failed to remove item: ${response.message}"
                }
                else -> {}
            }
        }
    }


     fun clickOrderNow(navController: NavHostController){
        val cartId = _cartId.value

        if(cartId != null){
            viewModelScope.launch{
               val result  = orderRepo.createOrder(cartId)
                when(result){
                    is ApiResponse.Success ->{
                        val data: CreateOrderResponse = result.data
                        authManager.saveCurrentQRCode(data.qrCode)
                        authManager.saveCurrentPendingOrderCode(data.orderCode)

                        navController.navigate(Route.Order.route)
                    }
                    is ApiResponse.Error ->{

                    }
                    is ApiResponse.Loading ->{

                    }
                }
            }
        }

    }

    suspend fun checkIfUserHasPendingOrder(){
        val userName:String = authManager.getUserNameOnce().toString()
        val result = orderRepo.checkIfUserHasPendingOrder(userName)
        when(result){
            is ApiResponse.Success -> {
                _hasPendingOrder.value = result.data
            }
            is ApiResponse.Error -> {
                _errorMessage.value = "Failed to check pending order: ${result.message}"
            }
            is ApiResponse.Loading -> {

            }
        }
    }





}