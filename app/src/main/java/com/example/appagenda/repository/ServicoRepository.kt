package com.example.appagenda.repository
import android.util.Log
import com.example.appagenda.model.Servico
import com.example.appagenda.model.ServicesResponse
import com.example.appagenda.network.ApiClient

class ServicoRepository {
    private val apiService = ApiClient.apiService
    private val TAG = "ServicoRepository"

    /** Retorna todos os serviços do backend */
    suspend fun getServicos(): List<Servico> {
        return try {
            val response = apiService.getServices()
            if (response.isSuccessful) {
                val servicesResponse = response.body()
                servicesResponse?.services ?: emptyList()
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Erro ao carregar serviços: ${response.code()} - $errorBody")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception ao carregar serviços: ${e.message}", e)
            emptyList()
        }
    }

    /** Busca um serviço por ID */
    suspend fun getServicoById(id: Int): Servico? {
        return try {
            val servicos = getServicos()
            servicos.find { it.id == id }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar serviço por ID: ${e.message}")
            null
        }
    }
}