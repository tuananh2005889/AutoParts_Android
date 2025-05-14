package com.example.frontend.data.remote

import com.example.frontend.data.dto.AddToCartRequest
import com.example.frontend.data.dto.BasicCartItemDTO
import com.example.frontend.data.dto.CartBasicInfoDTO
import com.example.frontend.data.dto.CartItemDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface CartApiService {
    @GET("/app/cart/items")
    suspend fun getAllCartItems(
        @Query("cartId") cartId: Long
    ): Response<List<CartItemDTO>>

    @POST("/app/cart/add")
    suspend fun addProductToCart(
        @Body addToCartRequest: AddToCartRequest
    ): Response<CartItemDTO>

    @POST("/app/cart/active")
    suspend fun getOrCreateActiveCart(@Query("userName") userName: String): Response<CartBasicInfoDTO>

    @GET("/app/cart/imageUrls")
    suspend fun getImageUrlPerCartItem(@Query("cartId") cartId: Long): Response<List<String>>

    @PUT("/app/cart-item/increase")
    suspend fun increaseCartItemQuantity(@Query("cartItemId") cartItemId: Long): Response<BasicCartItemDTO>

    @PUT("/app/cart-item/decrease")
    suspend fun decreaseCartItemQuantity(@Query("cartItemId") cartItemId: Long): Response<BasicCartItemDTO>

    @GET("/app/cart/total-price")
    suspend fun getTotalPrice(@Query("cartId") cartId: Long): Response<Double>

}