package com.example.appagenda.ui.telas

import androidx.compose.runtime.Composable

data class Servico(

    val id: Int,
    val nome: String,
    val duracao: String,
)

@Composable
fun getServico(): List<Servico> {
    return listOf(
        Servico(1, "Acupuntura", "30 min"),
        Servico(2, "Auriculoterapia", "30 min"),
        Servico(3, "Desbloqueio emocional", "1h"),
        Servico(4, "Drenagem ", "1h"),
        Servico(5, "Iridologia", "1h"),
        Servico(6, "Massagem terapêutica", "1h")
    )
}
