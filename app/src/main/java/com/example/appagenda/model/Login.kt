package com.example.appagenda.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val authToken: String?,
    val safeUser: Usuario?
)

@Serializable
data class UsuarioResponse(
    val user: Usuario
)