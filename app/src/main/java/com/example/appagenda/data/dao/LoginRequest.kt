package com.example.appagenda.data.dao

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("authToken")
    val authToken: String?,

    @SerializedName("safeUser")
    val safeUser: SafeUser?
)

data class SafeUser(
    @SerializedName("email")
    val email: String?,

    @SerializedName("name")
    val name: String? = null
)

data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)