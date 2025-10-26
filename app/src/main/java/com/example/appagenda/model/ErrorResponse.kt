package com.example.appagenda.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)