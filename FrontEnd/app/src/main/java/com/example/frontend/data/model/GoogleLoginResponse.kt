package com.example.frontend.data.model

data class GoogleLoginResponse(
    val userId: Long,
    val token: String,
    val userName: String
)
