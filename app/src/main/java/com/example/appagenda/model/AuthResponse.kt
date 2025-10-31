package com.example.appagenda.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val authToken: String,
    val user: Usuario
)