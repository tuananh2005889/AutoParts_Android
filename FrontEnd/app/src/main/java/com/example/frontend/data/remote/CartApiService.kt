package com.example.frontend.data.remote

import com.example.frontend.data.dto.AddToCartRequest
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.data.model.Cart
import com.example.frontend.data.model.CartItem
import com.example.frontend.ui.common.AuthPreferencesKeys.userName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface CartApiService {
    @GET("/cart/items")
    suspend fun getCartItems(
        @Query("cartId") cartId: Long
    ): Response<List<CartItemDTO>>

    @POST("/cart/add")
    suspend fun addItemToCart(
        @Body addToCartRequest: AddToCartRequest
    ): Response<Cart>

    @POST("/cart/active")
    suspend fun getOrCreateCart(@Query("userName") userName: String): Response<Cart>
}