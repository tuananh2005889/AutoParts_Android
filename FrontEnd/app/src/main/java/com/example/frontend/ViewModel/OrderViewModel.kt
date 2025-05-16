package com.example.frontend.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.data.dto.OrderStatus
import com.example.frontend.data.dto.PaymentStatus
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.OrderRepository
import com.example.frontend.data.repository.PaymentRepository
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
    private val paymentRepo: PaymentRepository
): ViewModel() {
    private val _orderDetailList = mutableStateOf<List<OrderDetailDTO>>(emptyList())
    val orderDetailList = _orderDetailList
    private val _productImageUrls = mutableStateMapOf<Long, String>()
    val productImageUrls: Map<Long, String> get() = _productImageUrls

    private val _currentQRCode = mutableStateOf<String>("")
    val currentQRCode = _currentQRCode

    private val _orderStatus = mutableStateOf("PENDING")
    val orderStatus = _orderStatus

    var isOrderPaid by mutableStateOf(false)
        private set

    var showPaymentMessage by mutableStateOf(false)
        private set

//     fun checkOrderStatus() {
//        viewModelScope.launch {
//            while(true){
//                try {
//                    val orderCode = authManager.getCurrentPendingOrderCodeOnce()
//                    orderCode?.let{
//                        val status  = paymentRepo.getPaymentStatus(orderCode)
//                        when(status){
//                            is ApiResponse.Success ->{
//                                if (status.data == PaymentStatus.PAID) {
//                                    isOrderPaid = true
//                                    showPaymentMessage = true
//                                }
//                            }
//                            is ApiResponse.Loading -> {
//
//                            }
//                            is ApiResponse.Error -> {
//                                Log.d("OrdVM-checkOrdStt", "Error: ${status.message}")
//                            }
//
//                        }
//
//                    }
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Log.d("OrdVM-checkOrdStatus", "Error ${e.message}")
//                }
//            }
//        }
//    }
fun checkOrderStatus() {
    viewModelScope.launch {
        while (true) {
            try {
                val orderCode = authManager.getCurrentPendingOrderCodeOnce()
                if (orderCode != null) {
                    val status = paymentRepo.getPaymentStatus(orderCode)
                    when (status) {
                        is ApiResponse.Success -> {
                            if (status.data == PaymentStatus.PAID) {
                                isOrderPaid = true
                                showPaymentMessage = true
                                break
                            }
                        }
                        is ApiResponse.Error -> {
                            Log.d("OrdVM-checkOrdStt", "Error: ${status.message}")
                        }
                        is ApiResponse.Loading -> {

                        }
                    }
                }
                kotlinx.coroutines.delay(5000)
            } catch (e: Exception) {
                Log.d("OrdVM-checkOrdStatus", "Error ${e.message}")
                kotlinx.coroutines.delay(5000)
            }
        }
    }
}


//    fun pollOrderStatusUntilPaid(orderId: Long) {
//        viewModelScope.launch {
//            while (true) {
//                try {
//                    val status = paymentRepo.getPaymentStatus(orderId)
//
//                    if (status == PaymentStatus.PAID) {
//                        isOrderPaid = true
//                        showPaymentMessage = true
//                        break // thoát vòng lặp nếu đã thanh toán
//                    }
//                    delay(5000) // chờ 5 giây trước khi kiểm tra lại
//                } catch (e: Exception) {
//                    // xử lý lỗi nếu cần
//                    delay(5000) // thử lại sau 5 giây nếu lỗi
//                }
//            }
//        }
//    }



fun dismissPaymentMessage() {
    viewModelScope.launch {
        val orderCode = authManager.getCurrentPendingOrderCodeOnce()
        if (orderCode != null) {
            val result = orderRepo.changeOrderStatus(orderCode, OrderStatus.PAID)
            if (result is ApiResponse.Success) {
                _orderDetailList.value = emptyList()
                showPaymentMessage = false
                isOrderPaid = false
            } else if (result is ApiResponse.Error) {
                Log.d("OrdVM", "Error changing order status: ${result.message}")
            }
        } else {
            Log.d("OrdVM-dismissPaymentMessage", "Error: orderCode null")
        }
    }
}


    init {
        viewModelScope.launch {
            checkOrderStatus()
            getCurrentQRCode()
            getAllOrderDetailsInPendingOrder()
        }

    }

    suspend fun getCurrentQRCode(){
        val qrCode = authManager.getCurrentQRCodeOnce().toString()
        _currentQRCode.value = qrCode
    }

   suspend fun getAllOrderDetailsInPendingOrder(){
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

