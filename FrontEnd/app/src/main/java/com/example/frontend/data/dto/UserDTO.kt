package com.example.frontend.data.dto

data class UserDTO(
    val userId: Long?,
    val fullName: String,
    val userName: String,
    val password: String,
    val gmail: String,
    val address: String?,
    val role: String?,
    val phone: String?,
    val carts: List<CartDTO>?,
    val avatarUrl: String?
)
