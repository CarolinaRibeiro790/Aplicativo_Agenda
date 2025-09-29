package com.example.appagenda.data.dao

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val authToken: String,
    val safeUser: SafeUser
)

data class SafeUser(
    val email: String,
    val name: String? = null
)

data class ErrorResponse(
    val success: Boolean,
    val message: String
)