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
}
