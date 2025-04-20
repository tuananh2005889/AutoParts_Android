package com.example.frontend.data.remote

import com.example.frontend.data.model.ProductData
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path

interface ProductApiService {
    @GET("/app/product/all")
    suspend fun getAllProducts(): Response<List<ProductData>>

    @GET("/app/product/get/{id}")
    suspend fun getProductById(@Path("id") productId: Long): Response<ProductData>
}
