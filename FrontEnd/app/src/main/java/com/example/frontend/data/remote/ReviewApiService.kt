package com.example.frontend.data.remote

import com.example.frontend.data.model.AddReviewRequest
import com.example.frontend.data.model.ReviewData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApiService {
    @GET("api/reviews/product/{productId}")
    suspend fun getProductReviews(
        @Path("productId") productId: Long
    ): Response<List<ReviewData>>

    @POST("api/reviews")
    suspend fun addReview(
        @Body req: AddReviewRequest
    ): Response<ReviewData>
}
