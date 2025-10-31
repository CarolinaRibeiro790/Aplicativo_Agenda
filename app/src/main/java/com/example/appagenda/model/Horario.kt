package com.example.appagenda.model
import kotlinx.serialization.Serializable

@Serializable
data class Horario(
    val id: Int,
    val hora: String,
    val disponivel: Boolean = true
)
