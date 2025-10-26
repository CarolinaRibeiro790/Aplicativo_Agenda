package com.example.appagenda.repository

import android.util.Log
import com.example.appagenda.model.LoginRequest
import com.example.appagenda.model.LoginResponse
import com.example.appagenda.network.ApiClient
import com.example.appagenda.network.AuthTokenHolder
import com.google.gson.Gson

class AuthRepository {
    private val apiService = ApiClient.apiService
    private val gson = Gson()

    /**
     * Faz login do usuário com e-mail e senha.
     */
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)

            // Captura e loga o corpo de erro se existir
            val errorBody = response.errorBody()?.string()
            if (errorBody != null) {
                Log.e("AuthRepository", "Error body: $errorBody")
            }

            when {
                response.isSuccessful && response.body() != null -> {
                    val loginResponse = response.body()!!

                    // Valida token e usuário retornado
                    if (loginResponse.authToken.isNullOrEmpty()) {
                        return Result.failure(Exception("Erro no servidor: token não foi gerado"))
                    }

                    if (loginResponse.safeUser == null) {
                        return Result.failure(Exception("Erro no servidor: dados do usuário não foram retornados"))
                    }

                    // Salva o token globalmente
                    AuthTokenHolder.token = loginResponse.authToken
                    Log.d("AuthRepository", "Login bem-sucedido! Token salvo.")

                    Result.success(loginResponse)
                }
                response.code() == 400 || response.code() == 401 -> {
                    Result.failure(Exception("Email ou senha incorretos"))
                }
                response.code() == 500 -> {
                    Result.failure(Exception("Erro no servidor. Tente novamente mais tarde."))
                }
                else -> {
                    Result.failure(Exception("Erro ao fazer login. Código: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "EXCEÇÃO CAPTURADA: ${e.message}")
            e.printStackTrace()
            Result.failure(Exception("Erro ao conectar com o servidor: ${e.message}"))
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                AuthTokenHolder.token = null
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao fazer logout"))
            }
        } catch (e: Exception) {
            AuthTokenHolder.token = null
            Result.failure(Exception("Erro ao fazer logout: ${e.message}"))
        }
    }

    fun isAuthenticated(): Boolean = AuthTokenHolder.token != null

    fun clearSession() {
        AuthTokenHolder.token = null
    }
}
