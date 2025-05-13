package com.example.frontend.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.OrderRepository
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepo: OrderRepository,
    private val authManager: AuthManager,
): ViewModel() {
    private val _orderDetailList = mutableStateOf<List<OrderDetailDTO>>(emptyList())
    val orderDetailList = _orderDetailList

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
                    }
                    is ApiResponse.Error ->{
                    }
                    is ApiResponse.Loading ->{
                    }
                }
            }
        }
    }


}

