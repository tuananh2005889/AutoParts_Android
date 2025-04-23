package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.dto.AddToCartRequest
import com.example.frontend.data.dto.CartBasicInfoDTO
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.CartApiService
import javax.inject.Inject


class CartRepository @Inject constructor(private val cartApiService: CartApiService){

   suspend fun getOrCreateActiveCart(username: String): ApiResponse<CartBasicInfoDTO> {
       return try {
           val response = cartApiService.getOrCreateActiveCart(username)

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

    suspend fun addProductToCart(cartId: Long, productId: Long, quantity: Int): ApiResponse<CartItemDTO> {
        Log.d("HomeViewModel", "addProductToCart called")
        try {
            val addToCartRequest = AddToCartRequest(cartId, productId, quantity)
            val response = cartApiService.addProductToCart(addToCartRequest)

            if (response.isSuccessful) {
                Log.d("HomeViewModel", "API call successful")
                return ApiResponse.Success(response.body() ?: throw Exception("Empty response body"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Log.e("API Error", "Error: $errorMessage")
                return ApiResponse.Error("Error: $errorMessage", response.code())
            }
        } catch (e: Exception) {
            Log.e("API Exception", "Exception: ${e.message}")
            return ApiResponse.Error("Exception: ${e.message ?: "Unknown error"}")
        }
    }

    suspend fun getCartItems(cartId: Long): ApiResponse<List<CartItemDTO>>{
       return try{
           val response = cartApiService.getCartItems(cartId)
           if(response.isSuccessful){
               ApiResponse.Success(response.body() ?: emptyList())
           }else{
               ApiResponse.Error("Failed to fetch cart items", response.code())
           }
       }catch(e: Exception){
           ApiResponse.Error(e.message ?: "Unknown error")
       }
    }


}