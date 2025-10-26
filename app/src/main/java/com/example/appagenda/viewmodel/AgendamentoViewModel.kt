package com.example.appagenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagenda.model.Agendamento
import com.example.appagenda.model.Horario
import com.example.appagenda.model.Servico
import com.example.appagenda.repository.AgendamentoRepository
import com.example.appagenda.repository.HorarioRepository
import com.example.appagenda.repository.ServicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class AgendamentoUiState(
    val servicos: List<Servico> = emptyList(),
    val agendamentos: List<Agendamento> = emptyList(),
    val agendamentoSelecionado: Agendamento? = null,
    val horarios: List<Horario> = emptyList(),
    val servicoSelecionado: Servico? = null,
    val horarioSelecionado: LocalTime? = null,
    val dataSelecionada: LocalDate? = null,
    val mesAtual: YearMonth = YearMonth.from(LocalDate.now()),
    //val dataSelecionada: LocalDate = LocalDate.now(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val sucessoMensagem: String? = null,
    val mostrarDialog: Boolean = false
)

class AgendamentoViewModel(
    private val agendamentoRepository: AgendamentoRepository = AgendamentoRepository(),
    private val servicoRepository: ServicoRepository = ServicoRepository(),
    private val horarioRepository: HorarioRepository = HorarioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgendamentoUiState())
    val uiState: StateFlow<AgendamentoUiState> = _uiState.asStateFlow()

    init {
        carregarDados()
    }

    /** Carrega serviços e horários disponíveis */
    private fun carregarDados() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        try {
            val servicos = servicoRepository.getServicos()
            val horarios = horarioRepository.getHorarios()
            val agendamentos = agendamentoRepository.getAgendamentos().getOrNull() ?: emptyList()
            _uiState.value = _uiState.value.copy(
                servicos = servicos,
                horarios = horarios,
                agendamentos = agendamentos.filter { it.status == 1 },
                isLoading = false
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Erro ao carregar dados: ${e.message}"
            )
        }
    }

    // Recarregar apenas agendamentos
    fun recarregarAgendamentos() = viewModelScope.launch {
        try {
            val agendamentos = agendamentoRepository.getAgendamentos().getOrNull() ?: emptyList()
            atualizarUi {
                it.copy(
                    agendamentos = agendamentos.filter { it.status == 1 }
                )
            }
        } catch (e: Exception) {
            mostrarErro("Erro ao recarregar agendamentos: ${e.message}")
        }
    }

    /** Atualiza seleção de data, serviço ou horário */
    fun selecionarData(data: LocalDate) =
        atualizarUi { it.copy(dataSelecionada = data) }

    fun selecionarServico(servico: Servico) =
        atualizarUi { it.copy(servicoSelecionado = servico) }

    fun limparSelecoes() =
        atualizarUi { it.copy(servicoSelecionado = null, dataSelecionada = null) }

    /** Controle de calendário */
    fun mesAnterior() = atualizarUi { it.copy(mesAtual = it.mesAtual.minusMonths(1)) }
    fun proximoMes() = atualizarUi { it.copy(mesAtual = it.mesAtual.plusMonths(1)) }
    fun irParaHoje() {
        val hoje = LocalDate.now()
        atualizarUi { it.copy(mesAtual = YearMonth.from(hoje), dataSelecionada = hoje) }
    }

    /** Exibe / oculta o diálogo */
    fun mostrarDialog() {
        val data = _uiState.value.dataSelecionada
        if (data == null) {
            mostrarErro("Por favor, selecione uma data no calendário")
        } else {
            atualizarUi { it.copy(mostrarDialog = true) }
        }
    }

    fun esconderDialog() =
        atualizarUi {
            it.copy(
                mostrarDialog = false,
                servicoSelecionado = null,
                horarioSelecionado = null
            )
        }

    // Cancelar agendamento específico
    fun cancelarAgendamento(agendamentoId: Int) = viewModelScope.launch {
        atualizarUi { it.copy(isLoading = true) }
        try {
            agendamentoRepository.cancelarAgendamento(agendamentoId).onSuccess {
                atualizarUi {
                    it.copy(
                        sucessoMensagem = "Agendamento cancelado com sucesso!",
                        isLoading = false
                    )
                }
                recarregarAgendamentos()
            }.onFailure { erro ->
                mostrarErro("Erro ao cancelar: ${erro.message}")
                atualizarUi { it.copy(isLoading = false) }
            }
        } catch (e: Exception) {
            mostrarErro("Erro ao cancelar agendamento: ${e.message}")
        }
    }

    /** Confirma o agendamento */
    fun confirmarAgendamento() {
        val (servico, horario, data) = with(_uiState.value) {
            Triple(servicoSelecionado, horarioSelecionado, dataSelecionada)
        }

        when {
            servico == null -> return mostrarErro("Selecione um serviço")
            horario == null -> return mostrarErro("Selecione um horário")
            data == null -> return mostrarErro("Selecione uma data")
            data.isBefore(LocalDate.now()) -> return mostrarErro("Não é possível agendar em datas passadas")
        }

        viewModelScope.launch {
            atualizarUi { it.copy(isLoading = true) }
            try {
                kotlinx.coroutines.delay(1000) // Simula chamada de API
                atualizarUi {
                    it.copy(
                        isLoading = false,
                        sucessoMensagem = "Agendamento realizado com sucesso!\n" +
                                "${servico.name} - ${horario}\n" +
                                "Data: ${formatarData(data)}",
                        mostrarDialog = false,
                        servicoSelecionado = null,
                        horarioSelecionado = null
                    )
                }
            } catch (e: Exception) {
                mostrarErro("Erro ao realizar agendamento: ${e.message}")
            }
        }
    }

    /** Utilitários */
    private fun atualizarUi(block: (AgendamentoUiState) -> AgendamentoUiState) {
        _uiState.value = block(_uiState.value)
    }

    fun selecionarHorario(horario: LocalTime?) =
        atualizarUi { it.copy(horarioSelecionado = horario) }

    fun limparMensagens() = atualizarUi { it.copy(error = null, sucessoMensagem = null) }
    private fun mostrarErro(msg: String) = atualizarUi { it.copy(error = msg) }

    private fun formatarData(data: LocalDate): String =
        data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

}
