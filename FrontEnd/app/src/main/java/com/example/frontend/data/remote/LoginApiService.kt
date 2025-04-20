package com.example.frontend.data.remote

import com.example.frontend.data.model.LoginData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("/auth/login")
    suspend fun login(@Body loginData: LoginData): Response<String>
}