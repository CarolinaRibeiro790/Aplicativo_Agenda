package com.example.appagenda.model
import kotlinx.serialization.Serializable

@Serializable
data class Servico(
    val id: Int,
    val name: String,
    val price: Double,
    val durationMinutes: Int,
    val serviceTypeId: Int? = null,
    val serviceTypeName: String? = null
)

@Serializable
data class ServicesResponse(
    val services: List<Servico>
)

@Serializable
data class ServiceResponse(
    val service: Servico?
)