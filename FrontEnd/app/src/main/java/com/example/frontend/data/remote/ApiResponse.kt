package com.example.frontend.data.remote


sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String, val statusCode: Int? = null ) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}