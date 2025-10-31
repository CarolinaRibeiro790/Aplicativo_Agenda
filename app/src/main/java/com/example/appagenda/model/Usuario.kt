package com.example.appagenda.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val email: String,
    val name: String,
    val birthDate: String? = null,
    val cpf: String,
    val telephone: String,
    val street: String,
    val state: String,
    val city: String,
    val cep: String,
    val country: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)