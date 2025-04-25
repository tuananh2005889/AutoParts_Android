package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.model.LoginData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.GoogleLoginRequest
import com.example.frontend.data.remote.GoogleLoginResponse
import com.example.frontend.data.remote.LoginApiService
import javax.inject.Inject


class LoginRepository @Inject constructor(private val api: LoginApiService) {
    suspend fun login(user: LoginData): ApiResponse<String> {
        return try{
            val response = api.login(user)
            if(response.isSuccessful){
                Log.d("LoginResponse", "Success: ${response.body()}")
                val string = response.body() ?: "Login successful"
                ApiResponse.Success(string)
            }else{
               ApiResponse.Error(response.errorBody().toString(), response.code())
            }
        }catch(e : Exception){
            ApiResponse.Error(e.message ?: "Unknown error")

        }
    }
    suspend fun loginWithGoogle(idToken: String): ApiResponse<GoogleLoginResponse> {
        return try {
            // gọi qua instance api, không phải gọi tĩnh
            val response = api.loginWithGoogle(GoogleLoginRequest(idToken))
            ApiResponse.Success(response)
        } catch (e: Exception) {
            ApiResponse.Error("Google login failed: ${e.message}")
        }
    }
}