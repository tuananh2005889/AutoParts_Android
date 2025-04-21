package com.example.frontend.data.repository

import com.example.frontend.data.dto.AddToCartRequest
import com.example.frontend.data.dto.CartBasicInfoDTO
import com.example.frontend.data.dto.CartDTO
import com.example.frontend.data.model.Cart
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.CartApiService
import javax.inject.Inject


class CartRepository @Inject constructor(private val cartApiService: CartApiService){

   suspend fun getOrCreateCart(username: String): ApiResponse<CartBasicInfoDTO> {
       return try {
           val response = cartApiService.getOrCreateCart(username)

           if (response.isSuccessful) {
               val body = response.body()
               if (body != null) {
                   ApiResponse.Success(body)
               } else {
                   ApiResponse.Error("Empty response body", response.code())
               }
           } else {
               val errorMsg = response.errorBody()?.string() ?: "Failed to fetch cart"
               ApiResponse.Error(errorMsg, response.code())
           }
       } catch (e: Exception) {
           ApiResponse.Error(e.localizedMessage ?: "Unknown error")
       }

   }

    suspend fun addProductToCart(cartId: Long, productId: Long, quantity: Int): ApiResponse<String> {
        try {
            val addToCartRequest = AddToCartRequest(cartId, productId, quantity)
            val response = cartApiService.addProductToCart(addToCartRequest)

            if (response.isSuccessful) {
                return ApiResponse.Success(response.body() ?: "Product added to cart successfully")
            } else {
                return ApiResponse.Error(
                    message = response.errorBody()?.string() ?: "Unknown error",
                    statusCode = response.code()
                )
            }

        } catch (e: Exception) {
            return ApiResponse.Error(e.message ?: "Error occurred")
        }
    }

}