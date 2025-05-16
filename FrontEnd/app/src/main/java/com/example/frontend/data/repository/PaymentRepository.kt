package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.dto.PaymentStatus
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.PaymentApiService
import javax.inject.Inject

class PaymentRepository @Inject constructor(val paymentApiService: PaymentApiService){
    suspend fun getPaymentStatus(orderCode: Long): ApiResponse<PaymentStatus>{
        return try{
            val result = paymentApiService.getPaymentStatus(orderCode)
            if(result.isSuccessful){
                val body = result.body()
                if(body != null){
                     ApiResponse.Success(body)
                }else{
                    ApiResponse.Error("Empty response body", result.code())
                }
            }else{
                val errorMessage = result.errorBody()?.string() ?: "Unknown error"
                val code = result.code()
                ApiResponse.Error(errorMessage, code )
            }
        }catch(e: Exception){
            e.printStackTrace()
            Log.d("ordRepo-createOrd", "Exception: ${e.message ?: "Unknown error"}")
            ApiResponse.Error("Exception occurred: ${e.message ?: "Unknown error"}", null)
        }
    }
}