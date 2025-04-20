package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.model.LoginData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.LoginApiService
import javax.inject.Inject


class LoginRepository @Inject constructor(private val loginApiService: LoginApiService) {
    suspend fun login(user: LoginData): ApiResponse<String> {
        return try{
            val response = loginApiService.login(user)
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
}