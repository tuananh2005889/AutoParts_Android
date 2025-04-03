package com.example.frontend.data.remote

import com.example.frontend.data.model.ProductData
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<ProductData>
}