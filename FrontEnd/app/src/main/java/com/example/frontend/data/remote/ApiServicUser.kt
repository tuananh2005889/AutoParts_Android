package com.example.frontend.data.remote

import com.example.frontend.data.model.UserData
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceUser {


    @GET("api/user/name/{userName}")
    suspend fun getUserName(
        @Path("userName") userName: String
    ): Response<UserData>


    @FormUrlEncoded
    @POST("api/user/update-avatar")
    suspend fun updateAvatar(
        @Field("userName") userName: String,
        @Field("avatarUrl") avatarUrl: String
    ): Response<String>

    @POST("api/user/forgot-password")
    suspend fun forgotPassword(@Body body: Map<String,String>): Response<String>

    @POST("api/user/verify-code")
    suspend fun verifyCode(@Body body: Map<String,String>): Response<String>

    @POST("api/user/reset-password")
    suspend fun resetPassword(@Body body: Map<String,String>): Response<String>

}
