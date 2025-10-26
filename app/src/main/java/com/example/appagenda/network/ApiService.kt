package com.example.appagenda.network

import com.example.appagenda.model.Agendamento
import com.example.appagenda.model.AgendamentoData
import com.example.appagenda.model.LoginRequest
import com.example.appagenda.model.LoginResponse
import com.example.appagenda.model.Servico
import com.example.appagenda.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
        @POST("auth/login")
        suspend fun login(
                @Body loginRequest: LoginRequest
        ): Response<LoginResponse>

        @POST("auth/logout")
        suspend fun logout(): Response<Unit>

        @GET("users/me")
        suspend fun getUserProfile(
                @retrofit2.http.Header("Authorization") token: String
        ): Response<Usuario>


        @GET("services")
        suspend fun getServices(): Response<List<Servico>>

        //Buscar todos os agendamentos do usuário
        @GET("appointments")
        suspend fun getAgendamentos(): Response<List<Agendamento>>

        // Buscar agendamento específico
        @GET("appointments/{id}")
        suspend fun getAgendamento(@Path("id") id: Int): Response<Agendamento>

        // Criar novo agendamento
        @POST("appointments")
        suspend fun createAgendamento(
                @Body agendamento: AgendamentoData
        ): Response<Agendamento>

        //Cancelar agendamento
        @DELETE("appointments/{id}")
        suspend fun cancelarAgendamento(@Path("id") id: Int): Response<Unit>

}
data class UsuarioResponse(
        val user: Usuario
)