package com.example.frontend.data.model

data class UserData(
    val userName: String,
    val fullName: String,
    val gmail: String,
    val phone: String,
    val password: String,
    val isActive: Boolean = true
)
