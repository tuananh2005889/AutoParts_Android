package com.example.frontend.data.remote

import com.example.frontend.data.dto.CreateOrderResponse
import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.data.dto.OrderStatus
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService{
    @POST("/app/order/create")
    suspend fun createOrder(@Query("cartId") cartId: Long): Response<CreateOrderResponse>
    @GET("/app/order/check-pending-status")
    suspend fun checkIfUserHasPendingOrder(@Query("userName") userName: String): Response<Boolean>
    @GET("/app/order/pending-order-detail-list")
    suspend fun getAllOrderDetailsInPendingOrder(@Query("userName") userName: String): Response<List<OrderDetailDTO>>
    @PUT("/app/order/change-order-status")
    suspend fun changeOrderStatus(@Query("orderCode") orderCode: Long, @Query("status") status: OrderStatus): Response<Unit>
}