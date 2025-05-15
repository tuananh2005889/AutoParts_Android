package com.example.frontend.data.repository

import com.example.frontend.data.dto.PaymentStatus
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.PaymentApiService
import javax.inject.Inject

//class PaymentRepository @Inject constructor(val paymentApiService: PaymentApiService){
//    suspend fun getPaymentStatus(orderId: Long): ApiResponse<PaymentStatus>{
//        val result = paymentApiService.getPaymentStatus(orderId)
//        when(result.isSuccessful){
//
//        }
//    }
//}