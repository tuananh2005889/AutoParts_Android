package com.example.frontend.data.repository

import com.example.frontend.data.model.UserData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.ApiServiceUser
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiServiceUser
) {

    suspend fun getUser(userName: String): ApiResponse<UserData> =
        try {
            val res = apiService.getUserName(userName)
            if (res.isSuccessful && res.body() != null)
                ApiResponse.Success(res.body()!!)
            else ApiResponse.Error("HTTP ${res.code()}: ${res.message()}")
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Unknown error")
        }
    suspend fun updateAvatar(userName: String, avatarUrl: String): ApiResponse<String> =
        try {
            val res = apiService.updateAvatar(userName, avatarUrl)
            if (res.isSuccessful) ApiResponse.Success(res.body() ?: "")
            else ApiResponse.Error("HTTP ${res.code()}: ${res.message()}")
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Unknown error")
        }

}
