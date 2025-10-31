package com.example.appagenda.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgendamentoUsuario(
    val name: String,
    val birthDate: String,
    val cpf: String,
    val email: String,
    val telephone: String
)

@Serializable
data class AgendamentoData(
    val appointmentDate: String,
    val appointmentTime: String,
    val serviceId: Int,
    val userId: Int,
    @SerialName("scheduleId") val scheduleId: Int
)

@Serializable
data class AgendamentoResponse(
    val appointment: Agendamento?
)

@Serializable
data class AgendamentosResponse(
    val appointments: List<Agendamento>
)

@Serializable
data class UserAppointmentsResponse(
    val userAppointments: List<Agendamento>
)

@Serializable
data class Agendamento(
    val id: Int,
    val userId: Int,
    val serviceId: Int,
    val appointmentDate: String,
    val appointmentTime: String,
    val service: ServiceAgendamento? = null,
    val user: AgendamentoUsuario? = null,
    val status: Int = 1,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val serviceName: String? = null
)

@Serializable
data class ServiceAgendamento(
    val name: String,
    val price: Double,
    val durationMinutes: Int,
    val serviceType: ServiceTypeAgendamento? = null
)

@Serializable
data class ServiceTypeAgendamento(
    val name: String
)