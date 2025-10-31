package com.example.appagenda.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appagenda.model.Horario
import com.example.appagenda.repository.HorarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HorarioUiState(
    val horarios: List<Horario> = emptyList(),
    val horarioSelecionado: Horario? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HorarioViewModel(
    private val horarioRepository: HorarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HorarioUiState())
    val uiState: StateFlow<HorarioUiState> = _uiState.asStateFlow()

    /** Carrega horários disponíveis */
    suspend fun carregarHorarios() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        try {
            val horarios = horarioRepository.getHorariosDisponiveis()
            _uiState.value = _uiState.value.copy(horarios = horarios, isLoading = false)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
        }
    }

    /** Seleciona um horário */
    fun selecionarHorario(horario: Horario) {
        _uiState.value = _uiState.value.copy(horarioSelecionado = horario)
    }

    /** Limpa seleção */
    fun limparSelecao() {
        _uiState.value = _uiState.value.copy(horarioSelecionado = null)
    }
}
