package com.example.appagenda.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagenda.model.Agendamento
import com.example.appagenda.model.AgendamentoData
import com.example.appagenda.model.Horario
import com.example.appagenda.model.Servico
import com.example.appagenda.network.ApiClient
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
    private val horarioRepository: HorarioRepository = HorarioRepository(ApiClient.apiService)
) : ViewModel() {

    private val TAG = "AgendamentoVM"
    private val repository = AgendamentoRepository()

    private val _uiState = MutableStateFlow(AgendamentoUiState())
    val uiState: StateFlow<AgendamentoUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "ViewModel inicializado -> carregando dados iniciais...")
        carregarDados()
    }

    /** Carrega serviços e horários disponíveis */
    private fun carregarDados() = viewModelScope.launch {
        Log.d(TAG, "Iniciando carregamento de dados...")
        _uiState.value = _uiState.value.copy(isLoading = true)
        try {
            val servicos = servicoRepository.getServicos()
            Log.d(TAG, "Serviços carregados: ${servicos?.size ?: 0}")

            val horarios = horarioRepository.getHorarios()
            Log.d(TAG, "Horários carregados: ${horarios?.size ?: 0}")

            val agendamentos = agendamentoRepository.getAgendamentos().getOrNull() ?: emptyList()
            Log.d(TAG, "Agendamentos carregados: ${agendamentos.size}")

            _uiState.value = _uiState.value.copy(
                servicos = servicos,
                horarios = horarios,
                agendamentos = agendamentos.filter { it.status == 1 },
                isLoading = false
            )
            Log.d(TAG, "Carregamento concluído com sucesso!")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao carregar dados: ${e.message}", e)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Erro ao carregar dados: ${e.message}"
            )
        }
    }



    /** Recarrega apenas os agendamentos */
    fun recarregarAgendamentos() = viewModelScope.launch {
        Log.d(TAG, "Recarregando agendamentos...")
        try {
            val agendamentos = agendamentoRepository.getAgendamentos().getOrNull() ?: emptyList()
            Log.d(TAG, "Agendamentos recarregados: ${agendamentos.size}")
            atualizarUi {
                it.copy(agendamentos = agendamentos.filter { a -> a.status == 1 })
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao recarregar agendamentos: ${e.message}", e)
            mostrarErro("Erro ao recarregar agendamentos: ${e.message}")
        }
    }

    /** Atualiza seleção de data, serviço ou horário */
    fun selecionarData(data: LocalDate) {
        Log.d(TAG, "Data selecionada: $data")
        atualizarUi { it.copy(dataSelecionada = data) }
    }

    fun selecionarServico(servico: Servico) {
        Log.d(TAG, "Serviço selecionado: ${servico.name}")
        atualizarUi { it.copy(servicoSelecionado = servico) }
    }

    fun selecionarHorario(horario: LocalTime?) {
        Log.d(TAG, "Horário selecionado: $horario")
        atualizarUi { it.copy(horarioSelecionado = horario) }
    }

    fun limparSelecoes() {
        Log.d(TAG, "Limpando seleções...")
        atualizarUi { it.copy(servicoSelecionado = null, dataSelecionada = null) }
    }

    /** Controle de calendário */
    fun mesAnterior() {
        Log.d(TAG, "Indo para o mês anterior")
        atualizarUi { it.copy(mesAtual = it.mesAtual.minusMonths(1)) }
    }

    fun proximoMes() {
        Log.d(TAG, "Indo para o próximo mês")
        atualizarUi { it.copy(mesAtual = it.mesAtual.plusMonths(1)) }
    }

    fun irParaHoje() {
        val hoje = LocalDate.now()
        Log.d(TAG, "Voltando para hoje: $hoje")
        atualizarUi { it.copy(mesAtual = YearMonth.from(hoje), dataSelecionada = hoje) }
    }

    /** Exibe / oculta o diálogo */
    fun mostrarDialog() {
        val data = _uiState.value.dataSelecionada
        if (data == null) {
            Log.w(TAG, "Tentou abrir diálogo sem data selecionada")
            mostrarErro("Por favor, selecione uma data no calendário")
        } else {
            Log.d(TAG, "Mostrando diálogo de agendamento")
            atualizarUi { it.copy(mostrarDialog = true) }
        }
    }

    fun esconderDialog() {
        Log.d(TAG, "Fechando diálogo e limpando seleções")
        atualizarUi {
            it.copy(
                mostrarDialog = false,
                servicoSelecionado = null,
                horarioSelecionado = null
            )
        }
    }

    /** Cancela um agendamento */
    fun cancelarAgendamento(agendamentoId: Int) = viewModelScope.launch {
        Log.d(TAG, "Cancelando agendamento com ID: $agendamentoId")
        atualizarUi { it.copy(isLoading = true) }
        try {
            agendamentoRepository.cancelarAgendamento(agendamentoId).onSuccess {
                Log.d(TAG, "Agendamento cancelado com sucesso!")
                atualizarUi {
                    it.copy(
                        sucessoMensagem = "Agendamento cancelado com sucesso!",
                        isLoading = false
                    )
                }
                recarregarAgendamentos()
            }.onFailure { erro ->
                Log.e(TAG, "Erro ao cancelar: ${erro.message}", erro)
                mostrarErro("Erro ao cancelar: ${erro.message}")
                atualizarUi { it.copy(isLoading = false) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro inesperado ao cancelar agendamento: ${e.message}", e)
            mostrarErro("Erro ao cancelar agendamento: ${e.message}")
        }
    }

    /** Confirma o agendamento */
    fun confirmarAgendamento(userId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val horarioStr = _uiState.value.horarioSelecionado?.toString()?.substring(0,5) ?: ""
            val scheduleId = horarioRepository.obterScheduleId(
                _uiState.value.horarioSelecionado?.toString()
            )

            val agendamentoData = AgendamentoData(
                appointmentDate = _uiState.value.dataSelecionada?.toString() ?: "",
                appointmentTime = _uiState.value.horarioSelecionado?.toString() ?: "",
                serviceId = _uiState.value.servicoSelecionado?.id ?: 0,
                userId = userId,
                scheduleId = scheduleId
            )


            val resultado = repository.criarAgendamento(agendamentoData)

            resultado.onSuccess {
                _uiState.value = _uiState.value.copy(
                    sucessoMensagem = "Agendamento realizado com sucesso!",
                    isLoading = false,
                    dataSelecionada = null,
                    servicoSelecionado = null,
                    horarioSelecionado = null,
                    mostrarDialog = false
                )
            }.onFailure {
                Log.d("--- DEV", "confirmarAgendamento: ${it.message} data $agendamentoData ")
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao agendar: ${it.message}",
                    isLoading = false
                )
            }
        }
    }

    private fun atualizarUi(block: (AgendamentoUiState) -> AgendamentoUiState) {
        _uiState.value = block(_uiState.value)
    }

    fun limparMensagens() = atualizarUi { it.copy(error = null, sucessoMensagem = null) }

    private fun mostrarErro(msg: String) {
        Log.w(TAG, msg)
        atualizarUi { it.copy(error = msg) }
    }

    private fun formatarData(data: LocalDate): String =
        data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}