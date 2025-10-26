package com.example.appagenda.repository
import android.util.Log
import com.example.appagenda.model.Agendamento
import com.example.appagenda.model.AgendamentoData
import com.example.appagenda.network.ApiClient

class AgendamentoRepository {
    private val apiService = ApiClient.apiService

    suspend fun getAgendamentos(): Result<List<Agendamento>> {
        return try {
            val response = apiService.getAgendamentos()

            if (response.isSuccessful) {
                val agendamentos = response.body() ?: emptyList()
                Log.d("AgendamentoRepository", "Agendamentos carregados: ${agendamentos.size}")
                Result.success(agendamentos)
            } else {
                Log.e("AgendamentoRepository", "Erro: ${response.code()}")
                Result.failure(Exception("Erro ao buscar agendamentos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("AgendamentoRepository", "Exception: ${e.message}", e)
            Result.failure(Exception("Erro ao conectar: ${e.message}"))
        }
    }

    suspend fun getAgendamento(id: Int): Result<Agendamento> {
        return try {
            val response = apiService.getAgendamento(id)

            if (response.isSuccessful) {
                val agendamento = response.body()
                if (agendamento != null) {
                    Result.success(agendamento)
                } else {
                    Result.failure(Exception("Agendamento não encontrado"))
                }
            } else {
                Result.failure(Exception("Erro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao conectar: ${e.message}"))
        }
    }

    suspend fun criarAgendamento(agendamento: AgendamentoData): Result<Agendamento> {
        return try {
            val response = apiService.createAgendamento(agendamento)

            if (response.isSuccessful) {
                val novoAgendamento = response.body()
                if (novoAgendamento != null) {
                    Log.d("AgendamentoRepository", "Agendamento criado com sucesso: ${novoAgendamento.id}")
                    Result.success(novoAgendamento)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor"))
                }
            } else {
                Result.failure(Exception("Erro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao conectar: ${e.message}"))
        }
    }

    suspend fun cancelarAgendamento(id: Int): Result<Unit> {
        return try {
            val response = apiService.cancelarAgendamento(id)

            if (response.isSuccessful) {
                Log.d("AgendamentoRepository", "Agendamento $id cancelado")
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao cancelar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao conectar: ${e.message}"))
        }
    }
}