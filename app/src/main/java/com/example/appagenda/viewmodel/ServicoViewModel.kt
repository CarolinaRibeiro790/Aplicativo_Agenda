package com.example.appagenda.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appagenda.model.Servico
import com.example.appagenda.repository.ServicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ServicoUiState(
    val servicos: List<Servico> = emptyList(),
    val servicoSelecionado: Servico? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ServicoViewModel(
    private val servicoRepository: ServicoRepository = ServicoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServicoUiState())
    val uiState: StateFlow<ServicoUiState> = _uiState.asStateFlow()

    init { carregarServicos() }

    /** Carrega a lista de serviços */
    private fun carregarServicos() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        try {
            val servicos = servicoRepository.getServicos()
            _uiState.value = _uiState.value.copy(servicos = servicos, isLoading = false)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
        }
    }

    /** Seleciona um serviço */
    fun selecionarServico(servico: Servico) {
        _uiState.value = _uiState.value.copy(servicoSelecionado = servico)
    }

    /** Limpa seleção */
    fun limparSelecao() {
        _uiState.value = _uiState.value.copy(servicoSelecionado = null)
    }
}
