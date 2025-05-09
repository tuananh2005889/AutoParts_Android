package com.example.frontend.data.remote

import com.example.frontend.data.dto.OrderDetailDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderApiService{
    @GET("/app/order/create")
    suspend fun createOrder(@Path("cartId") cartId: Long): Response<List<OrderDetailDTO>>
}