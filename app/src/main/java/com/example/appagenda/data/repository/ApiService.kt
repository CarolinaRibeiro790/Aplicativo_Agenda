package com.example.appagenda.data.repository

import com.example.appagenda.data.dao.LoginRequest
import com.example.appagenda.data.dao.LoginResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
        @POST("auth/login")
        suspend fun login(@Body loginData: LoginRequest): Response<LoginResponse>

        @POST("auth/logout")
        suspend fun logout(): Response<Unit>
}
