package com.example.appagenda.model
import kotlinx.datetime.LocalTime
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
    val userId: Int
)
@Serializable
data class Agendamento(
    val id: Int,
    val userId: Int,
    val serviceId: Int,
    val serviceName: String? = null,
    val servicePrice: Double? = null,
    val appointmentDate: String,
    val appointmentTime: LocalTime,
    val usuario: AgendamentoUsuario? = null,
    val status: Int = 1,
    val createdAt: String? = null,
    val updatedAt: String? = null
)