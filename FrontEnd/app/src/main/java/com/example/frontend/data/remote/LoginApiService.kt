package com.example.frontend.data.remote

import com.example.frontend.data.model.LoginData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


//  Request cho Google login
data class GoogleLoginRequest(val idToken: String)
//  Response chứa JWT token và info cơ bản
data class GoogleLoginResponse(val token: String, val userName: String)

interface LoginApiService {
    @POST("/auth/login")
    suspend fun login(@Body loginData: LoginData): Response<String>

    @POST("/auth/google")
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): GoogleLoginResponse
}