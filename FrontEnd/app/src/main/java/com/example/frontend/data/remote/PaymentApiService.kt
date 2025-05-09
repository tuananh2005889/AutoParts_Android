package com.example.frontend.data.remote

import com.example.frontend.data.dto.CreateOrderResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PaymentApiService {
    @FormUrlEncoded
    @POST("/app/order/create")
    suspend fun createOrder(
        @Field("ammount") amount: Double
    ): Response<CreateOrderResponse>
}