package com.example.frontend.data.remote

import com.google.gson.annotations.SerializedName


sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String, val statusCode: Int? = null ) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}


data class AuthResponse(
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int? = null
)