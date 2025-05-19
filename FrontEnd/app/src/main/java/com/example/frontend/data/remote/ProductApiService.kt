package com.example.frontend.data.remote

import com.example.frontend.data.model.ProductData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("/app/product/all")
    suspend fun getAllProducts(): Response<List<ProductData>>

    @GET("/app/product/get/{id}")
    suspend fun getProductById(@Path("id") productId: Long): Response<ProductData>

    @GET("/app/product/imageUrls")
    suspend fun getImageUrls(@Query("productId") productId: Long): Response<List<String>>

    @GET("/app/product/imageUrl")
    suspend fun getImageUrl(@Query("productId") productId: Long): Response<String>

    @GET("/app/product/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): Response<List<ProductData>>
}
