package com.example.frontend.data.remote

import com.example.frontend.data.dto.OrderDetailDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService{
    @POST("/app/order/create")
    suspend fun createOrder(@Query("cartId") cartId: Long): Response<Unit>
    @GET("/app/order/check-pending-status")
    suspend fun checkIfUserHasPendingOrder(@Query("userName") userName: String): Response<Boolean>
    @GET("/app/order/pending-order-detail-list")
    suspend fun getAllOrderDetailsInPendingOrder(@Query("userName") userName: String): Response<List<OrderDetailDTO>>
}