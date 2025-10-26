package com.example.appagenda.repository

import com.example.appagenda.model.Horario

class HorarioRepository {
    private val horarios = listOf(
        Horario(1, "08h30 - 09h00"),
        Horario(2, "09h00 - 09h30"),
        Horario(3, "09h30 - 10h00"),
        Horario(4, "10h00 - 10h30"),
        Horario(5, "10h30 - 11h00"),
        Horario(6, "11h00 - 11h30"),
        Horario(7, "14h00 - 14h30"),
        Horario(8, "14h30 - 15h00"),
        Horario(9, "15h00 - 15h30"),
        Horario(10, "15h30 - 16h00")
    )

    /** Retorna todos os horários */
    fun getHorarios(): List<Horario> = horarios

    /** Busca um horário pelo ID */
    fun getHorarioById(id: Int): Horario? = horarios.find { it.id == id }

    /** Retorna apenas horários disponíveis */
    fun getHorariosDisponiveis(): List<Horario> = horarios.filter { it.disponivel }
}