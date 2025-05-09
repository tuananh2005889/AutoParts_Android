package com.example.frontend.data.repository

import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.data.model.OrderDetails
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.OrderApiService
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderApiService: OrderApiService) {

    suspend fun createOrder(cartId: Long): ApiResponse<List<OrderDetailDTO>>{
        return try{
           val response = orderApiService.createOrder(cartId)
            if(response.isSuccessful){
                val orderDetailDTOList:  List<OrderDetailDTO> = response.body() ?: emptyList()
                ApiResponse.Success(orderDetailDTOList)
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
}