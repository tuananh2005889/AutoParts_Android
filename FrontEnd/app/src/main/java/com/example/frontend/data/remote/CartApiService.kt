package com.example.frontend.data.remote

import com.example.frontend.data.dto.AddToCartRequest
import com.example.frontend.data.dto.CartBasicInfoDTO
import com.example.frontend.data.dto.CartDTO
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.data.model.Cart
import com.example.frontend.data.model.CartItem
import com.example.frontend.ui.common.AuthPreferencesKeys.userName
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface CartApiService {
    @GET("/app/cart/items")
    suspend fun getCartItems(
        @Query("cartId") cartId: Long
    ): Response<List<CartItemDTO>>

    @POST("/app/cart/add")
    suspend fun addProductToCart(
        @Body addToCartRequest: AddToCartRequest
    ): Response<CartItemDTO>

    @POST("/app/cart/active")
    suspend fun getOrCreateActiveCart(@Query("userName") userName: String): Response<CartBasicInfoDTO>
}