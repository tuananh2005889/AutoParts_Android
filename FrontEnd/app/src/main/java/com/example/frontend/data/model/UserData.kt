package com.example.frontend.data.model

data class UserData(
    val userId: Long?,
    val userName: String,
    val fullName: String,
    val gmail: String,
    val phone: String,
    val password: String? = null,
    val isActive: Boolean = true,
    val avatarUrl: String? = null,
)
