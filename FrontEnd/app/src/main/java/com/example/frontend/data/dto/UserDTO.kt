package com.example.frontend.data.dto

data class UserDTO(
    val userId: Long,
    val userName: String,
    val fullName: String,
    val gmail: String,
    val phone: String,
    val avatarUrl: String?
)
