package com.example.frontend.data.repository

import com.example.frontend.data.model.LoginData
import com.example.frontend.data.model.UserData
import com.example.frontend.data.remote.*
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val api: LoginApiService,
    private val userApi: ApiServiceUser      // ← đã có trong project
) {

    /*---- login thường ----*/
    suspend fun login(data: LoginData): ApiResponse<Pair<String, UserData?>> {
        return try {
            val tokenRes = api.login(data)          // token dưới dạng String

            if (!tokenRes.isSuccessful || tokenRes.body() == null) {
                ApiResponse.Error("HTTP ${tokenRes.code()}: ${tokenRes.message()}")
            } else {
                val token = tokenRes.body()!!

                /* Lấy hồ sơ bằng userName (username chính là data.userName) */
                val userRes = userApi.getUserName(data.userName)   // cần thêm hàm này
                val userData = if (userRes.isSuccessful) userRes.body() else null

                ApiResponse.Success(token to userData)  // Pair(token, userData?)
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Unknown error")
        }
    }

    /*---- login google ----*/
    suspend fun loginWithGoogle(idToken: String): ApiResponse<GoogleLoginResponse> =
        try {
            ApiResponse.Success(api.loginWithGoogle(GoogleLoginRequest(idToken)))
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Unknown error")
        }
}
