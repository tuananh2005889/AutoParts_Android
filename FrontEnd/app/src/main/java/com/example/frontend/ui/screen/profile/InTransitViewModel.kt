
package com.example.frontend.ui.screen.profile
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.dto.OrderDTO
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.OrderRepository
import com.example.frontend.data.repository.ProductRepository
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InTransitViewModel @Inject constructor(
    val orderRepo: OrderRepository,
    val authManager: AuthManager,
    val productRepo: ProductRepository,
): ViewModel(){
    private val _orderList = mutableStateOf<List<OrderDTO>>(emptyList())
    val orderList = _orderList

    private val _productImageUrlMap = mutableStateMapOf<Long, String>()
    val productImageUrlMap: Map<Long, String>  = _productImageUrlMap

    init{
        getAllShippedOrdersOfUser()
    }
    fun getAllShippedOrdersOfUser(){
        viewModelScope.launch{
            val result = orderRepo.getAllShippedOrdersOfUser(authManager.getUserNameOnce().toString())
            when(result){
                is ApiResponse.Success -> {
                    val orderDTOList: List<OrderDTO> = result.data
                    _orderList.value = result.data

                    orderDTOList.forEach { orderDTO ->
                        orderDTO.orderDetailDTOList.forEach { orderDetailDTO ->
                            loadProductImage(orderDetailDTO.productId)

                        } }

                    Log.d("InTransitVM-getAllShip", "data: ${result.data}")
                }
                is ApiResponse.Error -> {
                    Log.d("InTransitVM-getAllShip", "Error: ${result.message}")
                }
                ApiResponse.Loading -> {}
            }
        }
    }

    fun loadProductImage(productId: Long) {
        if (_productImageUrlMap.containsKey(productId)) return

        viewModelScope.launch {
            Log.d("Order", "Loading image for productId = $productId")
            when (val result = productRepo.getImageUrl(productId)) {
                is ApiResponse.Success -> {
                    _productImageUrlMap[productId] = result.data
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


