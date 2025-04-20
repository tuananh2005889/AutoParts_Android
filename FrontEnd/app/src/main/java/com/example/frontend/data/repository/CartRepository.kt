package com.example.frontend.data.repository

import com.example.frontend.data.model.Cart
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.CartApiService
import javax.inject.Inject


class CartRepository @Inject constructor(private val cartApiService: CartApiService){

   suspend fun getOrCreateCart(username: String): ApiResponse<Cart> {
       try {
           val response = cartApiService.getOrCreateCart(username)
           if (response.isSuccessful) {
               val cart = response.body() ?: Cart(0, 0, emptyList(), "")
               return ApiResponse.Success(cart)
           } else {
               return ApiResponse.Error("Failed to fetch cart", response.code())
           }
       } catch (e: Exception) {
           return ApiResponse.Error(e.message ?: "Unknown error")
       }

   }
}