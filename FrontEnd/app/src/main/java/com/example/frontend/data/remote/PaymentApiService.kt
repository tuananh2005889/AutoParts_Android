package com.example.frontend.data.remote

import com.example.frontend.data.dto.CreateOrderResponse
import com.example.frontend.data.dto.PaymentStatus
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentApiService {
    @FormUrlEncoded
    @POST("/app/order/create")
    suspend fun createOrder(
        @Field("ammount") amount: Double
    ): Response<CreateOrderResponse>

    @GET("/app/payment/status")
    suspend fun getPaymentStatus(@Query("orderId") orderId: Long): Response<PaymentStatus>
}

