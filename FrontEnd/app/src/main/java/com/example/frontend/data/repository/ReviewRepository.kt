package com.example.frontend.data.repository

import com.example.frontend.data.model.AddReviewRequest
import com.example.frontend.data.model.ReviewData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.ReviewApiService
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val api: ReviewApiService
) {
    suspend fun getReviews(productId: Long): ApiResponse<List<ReviewData>> = try {
        val res = api.getProductReviews(productId)
        if (res.isSuccessful) ApiResponse.Success(res.body()!!)
        else ApiResponse.Error("HTTP ${res.code()}")
    } catch(e: Exception) { ApiResponse.Error(e.message ?: "") }

    suspend fun postReview(req: AddReviewRequest): ApiResponse<ReviewData> = try {
        val res = api.addReview(req)
        if (res.isSuccessful) ApiResponse.Success(res.body()!!)
        else ApiResponse.Error("HTTP ${res.code()}")
    } catch(e: Exception) { ApiResponse.Error(e.message ?: "") }
}
