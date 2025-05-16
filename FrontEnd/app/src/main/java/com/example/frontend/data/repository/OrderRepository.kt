package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.dto.CreateOrderResponse
import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.data.dto.OrderStatus
import com.example.frontend.data.model.OrderDetails
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.OrderApiService
import com.example.frontend.ui.screen.login.LoginScreen
import retrofit2.Response
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderApiService: OrderApiService) {

    suspend fun createOrder(cartId: Long): ApiResponse<CreateOrderResponse>{
        return try{
            val response = orderApiService.createOrder(cartId)
            if(response.isSuccessful){
                val body = response.body()
                if (body != null) {
                    ApiResponse.Success(body)
                } else {
                    ApiResponse.Error("Empty response body", response.code())
                }
            }else{
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                val code = response.code()
                ApiResponse.Error(errorMessage, code )
            }
        }catch(e: Exception) {
            e.printStackTrace()
            Log.d("ordRepo-createOrd", "Exception: ${e.message ?: "Unknown error"}")
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

    suspend fun changeOrderStatus(orderCode: Long, status: OrderStatus): ApiResponse<Unit>{
        return try{
            val result: Response<Unit> = orderApiService.changeOrderStatus(orderCode, status)
            if(result.isSuccessful){
                ApiResponse.Success(Unit)
            }else{
                ApiResponse.Error(result.message(), result.code())
            }
        }catch(e: Exception){
            e.printStackTrace()
            ApiResponse.Error("Exception: ${e.message}", null)
        }

    }

}