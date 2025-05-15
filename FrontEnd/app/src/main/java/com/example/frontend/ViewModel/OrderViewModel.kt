package com.example.frontend.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.OrderRepository
import com.example.frontend.data.repository.ProductRepository
import com.example.frontend.ui.common.AuthManager
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepo: OrderRepository,
    private val authManager: AuthManager,
    private val productRepo: ProductRepository,
): ViewModel() {
    private val _orderDetailList = mutableStateOf<List<OrderDetailDTO>>(emptyList())
    val orderDetailList = _orderDetailList
    private val _productImageUrls = mutableStateMapOf<Long, String>()
    val productImageUrls: Map<Long, String> get() = _productImageUrls

    init{
        getAllOrderDetailsInPendingOrder()
    }

    fun getAllOrderDetailsInPendingOrder(){
        viewModelScope.launch {
            val userName = authManager.getUserNameOnce().toString()
            if(userName != null){
                val result = orderRepo.getOrderDetailListInPendingOrder(userName)
                when(result){
                    is ApiResponse.Success ->{
                        _orderDetailList.value = result.data
                        Log.d("Order", "OrderDetails = ${result.data}")

                        result.data.forEach { orderDetail ->
                            loadProductImage(orderDetail.productId)
                        }
                    }
                    is ApiResponse.Error ->{

                    }
                    is ApiResponse.Loading ->{
                    }
                }
            }
        }
    }


    fun loadProductImage(productId: Long) {
        if (_productImageUrls.containsKey(productId)) return

        viewModelScope.launch {
            Log.d("Order", "Loading image for productId = $productId")
            when (val result = productRepo.getImageUrl(productId)) {
                is ApiResponse.Success -> {
                    _productImageUrls[productId] = result.data
                    Log.d("Order", "Loaded image: ${result.data}")
                }
                is ApiResponse.Error -> {
                    Log.e("Order", "Error loading image for productId = $productId: ${result.message}")
                }
                else -> {}
            }
        }
    }


}

