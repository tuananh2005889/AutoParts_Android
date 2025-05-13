package com.example.frontend.data.repository

import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.data.model.OrderDetails
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.OrderApiService
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderApiService: OrderApiService) {

    suspend fun createOrder(cartId: Long): ApiResponse<Unit>{
        return try{
           val response = orderApiService.createOrder(cartId)
            if(response.isSuccessful){
                ApiResponse.Success(Unit)
            }else{
                val errorMessage: String = response.errorBody().toString()
                val code = response.code()
                ApiResponse.Error(errorMessage, code )
            }
        }catch(e: Exception){
            e.printStackTrace()
            ApiResponse.Error("Exception occurred: ${e.message ?: "Unknown error"}", null)
        }
    }

    suspend fun checkIfUserHasPendingOrder(userName: String): ApiResponse<Boolean> {
        return try {
            val result = orderApiService.checkIfUserHasPendingOrder(userName)
            if (result.isSuccessful) {
                ApiResponse.Success(result.body() ?: false)
            } else {
                ApiResponse.Error("Failed to get pending order status", result.code())
            }
        } catch (e: Exception) {
            ApiResponse.Error("Exception occurred: ${e.message ?: "Unknown error"}")
        }
    }

    suspend fun getOrderDetailListInPendingOrder(userName: String): ApiResponse<List<OrderDetailDTO>>{
        return try{
            val response = orderApiService.getAllOrderDetailsInPendingOrder(userName)
            if(response.isSuccessful){
                ApiResponse.Success(response.body() ?: emptyList())
            }else{
                ApiResponse.Error("Failed to get order detail list", response.code())
            }
        }catch(e: Exception){
            ApiResponse.Error("Exception occurred: ${e.message ?: "Unknown error"}")

        }
    }

}