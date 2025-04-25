package com.example.frontend.data.dto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class GoogleLoginRequest(val idToken: String)
data class GoogleLoginResponse(val token: String, val userName: String)

interface GoogleAuthApi {
    @POST("auth/google")  // endpoint bên Spring Boot
    suspend fun loginWithGoogle(@Body req: GoogleLoginRequest): Response<GoogleLoginResponse>
}