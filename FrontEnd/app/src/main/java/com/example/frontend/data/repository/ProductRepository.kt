package com.example.frontend.data.repository

import android.util.Log.e
import com.example.frontend.data.model.ProductData
import javax.inject.Inject
import com.example.frontend.data.getUserData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.ProductApiService

class ProductRepository @Inject constructor(private val productApiService: ProductApiService) {

    suspend fun getAllProducts(): ApiResponse<List<ProductData>> {
        return try{
            val response = productApiService.getAllProducts()
            if(response.isSuccessful){
                val list = response.body() ?: emptyList()
                ApiResponse.Success(list)
            }else{
                ApiResponse.Error("Failed to fetch all products", response.code())
            }
        }catch(e: Exception){
            ApiResponse.Error(e.message ?: "Unknown error")
        }
    }
}
