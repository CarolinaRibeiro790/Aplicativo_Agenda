package com.example.appagenda.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("authToken") val authToken: String?,
    @SerializedName("safeUser") val safeUser: Usuario?
)
data class UsuarioResponse(
    val user: Usuario
)