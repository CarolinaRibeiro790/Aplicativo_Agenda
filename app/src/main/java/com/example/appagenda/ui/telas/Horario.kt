package com.example.appagenda.ui.telas
import androidx.compose.runtime.Composable
data class Horario(
    val id: Int,
    val hora: String
)

fun getHorario(): List<Horario>{
    return listOf(
        Horario(1, "08h30 - 09h"),
        Horario(2, "09h30 - 10h30"),
    )

}