package com.example.appagenda.repository
/*
import com.example.appagenda.model.Horario

class HorarioRepository {
    private val horarios = listOf(
        Horario(1, "13h00 - 14h00"),
        Horario(2, "14h00 - 15h00"),
        Horario(3, "15h00 - 16h00"),
        Horario(4, "16h00 - 17h00"),
        Horario(5, "17h00 - 18h00"),
        Horario(6, "18h00 - 19h00")
    )

    *//** Retorna todos os horários *//*
    fun getHorarios(): List<Horario> = horarios

    *//** Busca um horário pelo ID *//*
   fun getHorarioById(id: Int): Horario? = horarios.find { it.id == id }

   *//** Retorna apenas horários disponíveis *//*
   fun getHorariosDisponiveis(): List<Horario> = horarios.filter { it.disponivel }
}*/

import com.example.appagenda.model.Horario
import com.example.appagenda.network.ApiService
class HorarioRepository(private val api: ApiService) {
    private val horarios = listOf(
        Horario(1, "13h00 - 14h00"),
        Horario(2, "14h00 - 15h00"),
        Horario(3, "15h00 - 16h00"),
        Horario(4, "16h00 - 17h00"),
        Horario(5, "17h00 - 18h00"),
        Horario(6, "18h00 - 19h00")
    )
    fun listarHorarios(): List<Horario> = horarios

    fun getHorarios(): List<Horario> = horarios
    fun getHorarioById(id: Int): Horario? = horarios.find { it.id == id }
    fun getHorariosDisponiveis(): List<Horario> = horarios.filter { it.disponivel }

    // Corrigido: recebe string "14:00" ou "14:00:00"
    fun obterScheduleId(horarioSelecionado: String?): Int {
        if (horarioSelecionado.isNullOrBlank()) return 0

        val horarioEncontrado = horarios.find { h ->
            val partes = h.hora.split(" - ")         // "15h00 - 16h00"
            val inicio = partes[0].replace("h", ":00").trim() // "15:00"
            inicio.startsWith(horarioSelecionado.trim())      // compara com "15:00"
        }

        return horarioEncontrado?.id ?: 0
    }

}

