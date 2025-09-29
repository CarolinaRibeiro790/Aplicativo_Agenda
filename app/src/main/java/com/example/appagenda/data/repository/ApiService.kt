package com.example.appagenda.data.repository

import com.example.appagenda.data.dao.LoginRequest
import com.example.appagenda.data.dao.LoginResponse
import retrofit2.Response
import retrofit2.http.*

class ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginData: LoginRequest): Response<LoginResponse> {
        return TODO("Provide the return value")
    }

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit> {
        return TODO("Provide the return value")
    }
}