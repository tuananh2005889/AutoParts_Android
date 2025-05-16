package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.dto.AddToCartRequest
import com.example.frontend.data.dto.BasicCartItemDTO
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

    suspend fun addProductToCart(cartId: Long,
                                 productId: Long,
                                 quantity: Int,
                                 price: Double?): ApiResponse<CartItemDTO> {
        Log.d("HomeViewModel", "addProductToCart called")

        try {
            val addToCartRequest = AddToCartRequest(
                cartId    = cartId,
                productId = productId,
                price     = price,
                quantity  = quantity
            )
            val response = cartApiService.addProductToCart(addToCartRequest)

            if (response.isSuccessful) {
                Log.d("HomeViewModel", "API call successful")
                return ApiResponse.Success(response.body() ?: throw Exception("Empty response body"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Log.e("CartRepo", "Error: $errorMessage, Message Code: ${response.code()}")
                return ApiResponse.Error("Error: $errorMessage", response.code())
            }
        } catch (e: Exception) {
            Log.e("CartRepo-exception", "Exception: ${e.message}")
            return ApiResponse.Error("Exception: ${e.message ?: "Unknown error"}")
        }
    }
    suspend fun removeItemFromCart(cartItemId: Long): ApiResponse<Unit> {
        return try {
            val response = cartApiService.removeItemFromCart(cartItemId)
            if (response.isSuccessful) {
                ApiResponse.Success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to remove item"
                ApiResponse.Error(errorMsg, response.code())
            }
        } catch (e: Exception) {
            Log.e("CartRepo-exception", "Exception: ${e.message}")
            ApiResponse.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    suspend fun getAllCartItems(cartId: Long): ApiResponse<List<CartItemDTO>>{
       return try{
           val response = cartApiService.getAllCartItems(cartId)
           if(response.isSuccessful){
               ApiResponse.Success(response.body() ?: emptyList())
           }else{
               ApiResponse.Error("Failed to fetch cart items", response.code())
           }
       }catch(e: Exception){
           ApiResponse.Error(e.message ?: "Unknown error")
       }
    }

    suspend fun getImageUrlPerCartItem(cartId: Long): ApiResponse<List<String>>{
        return try{
            val response = cartApiService.getImageUrlPerCartItem(cartId)
            if(response.isSuccessful){
                ApiResponse.Success(response.body() ?: emptyList())
            }else{
                val errorDetail = response.errorBody().toString()
                ApiResponse.Error("Failed to get image url per cart item: ${response.message()} - $errorDetail", response.code())
            }

        }catch(e: Exception){
            ApiResponse.Error(e.message ?: "Unknown error")

        }
    }

    suspend fun increaseCartItemQuantity(cartItemId: Long): ApiResponse<BasicCartItemDTO> {
        return try {
            val response = cartApiService.increaseCartItemQuantity(cartItemId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResponse.Success(body)
                } else {
                    ApiResponse.Error("Empty response body")
                }
            } else {
                ApiResponse.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResponse.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
        }
    }


    suspend fun decreaseCartItemQuantity(cartItemId: Long): ApiResponse<BasicCartItemDTO> {
        return try {
            val response = cartApiService.decreaseCartItemQuantity(cartItemId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResponse.Success(body)
                } else {
                    ApiResponse.Error("Empty response body")
                }
            } else {
                ApiResponse.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResponse.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    suspend fun getTotalPrice(cartId: Long): ApiResponse<Double>{
        return try{
            val response = cartApiService.getTotalPrice(cartId)
            if(response.isSuccessful){
                ApiResponse.Success(response.body() ?: 0.0)
            }else{
                ApiResponse.Error("Failed to fetch total price", response.code())
            }
        }catch(e: Exception){
            ApiResponse.Error("Unknown error: ${e.message}")
        }

    }



}