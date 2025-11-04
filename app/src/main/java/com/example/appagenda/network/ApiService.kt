package com.example.appagenda.network

import com.example.appagenda.model.AgendamentoData
import com.example.appagenda.model.AgendamentoResponse
import com.example.appagenda.model.AgendamentosResponse
import com.example.appagenda.model.LoginRequest
import com.example.appagenda.model.LoginResponse
import com.example.appagenda.model.Servico
import com.example.appagenda.model.UserAppointmentsResponse
import com.example.appagenda.model.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
        @POST("auth/login")
        suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

        @POST("auth/logout")
        suspend fun logout(): Response<Unit>

        @GET("users/me")
        suspend fun getUserProfile(): Response<UsuarioResponse>

        // ✅ CORRIGIDO: Usa ServicesResponse
        @GET("services")
        suspend fun getServices(): Response<ServicesResponse>

        // ✅ CORRIGIDO: Usa AgendamentosResponse
        @GET("appointments")
        suspend fun getAgendamentos(): Response<AgendamentosResponse>

        // ✅ CORRIGIDO: Usa UserAppointmentsResponse
        @GET("appointments/{userId}")
        suspend fun getAgendamentosUsuario(@Path("userId") userId: Int?): Response<UserAppointmentsResponse>

        // ✅ CORRIGIDO: Usa AgendamentoResponse
        @GET("appointments/{id}")
        suspend fun getAgendamento(@Path("id") id: Int): Response<AgendamentoResponse>

        // ✅ CORRIGIDO: Usa AgendamentoResponse
        @POST("appointments")
        suspend fun createAgendamento(@Body agendamento: AgendamentoData): Response<AgendamentoResponse>

        // ✅ CORRIGIDO: Usa AgendamentoResponse
        @PUT("appointments/{id}")
        suspend fun updateAgendamento(
                @Path("id") id: Int,
                @Body agendamento: AgendamentoData
        ): Response<AgendamentoResponse>

        // Deletar agendamento
        @DELETE("appointments/{id}")
        suspend fun cancelarAgendamento(@Path("id") id: Int): Response<Unit>
}
// Adicione estas classes de resposta

data class ServicesResponse(
        val services: List<Servico>
)