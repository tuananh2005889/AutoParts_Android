package com.example.frontend.data.repository

import com.example.frontend.data.model.ProductData
import com.example.frontend.data.remote.ApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(): List<ProductData> {
        return apiService.getProducts()
    }
}
