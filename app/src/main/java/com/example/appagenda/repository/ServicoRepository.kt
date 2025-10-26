package com.example.appagenda.repository

import com.example.appagenda.model.Servico

class ServicoRepository {
    private val servicos = listOf(
        Servico(1, "Acupuntura", 100.0, 30, 1, "Tratamento com agulhas finas"),
        Servico(2, "Auriculoterapia", 100.0, 30, 2, "Terapia auricular"),
        Servico(3, "Desbloqueio emocional", 100.0, 30, 3, "Liberação emocional"),
        Servico(4, "Drenagem linfática", 100.0, 45, 4, "Massagem drenante"),
        Servico(5, "Iridologia", 100.0, 30, 5, "Análise pela íris"),
        Servico(6, "Massagem terapêutica", 100.0, 60, 6, "Massagem relaxante")
    )

    /** Retorna todos os serviços */
    fun getServicos(): List<Servico> = servicos

    /** Busca um serviço por ID */
    fun getServicoById(id: Int): Servico? = servicos.find { it.id == id }
}