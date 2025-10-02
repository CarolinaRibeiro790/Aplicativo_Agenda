package com.example.appagenda.data.repository

import android.util.Log
import com.example.appagenda.data.dao.LoginRequest
import com.example.appagenda.data.dao.LoginResponse
import com.google.gson.Gson

/**
 * Repositório responsável por gerenciar a autenticação do usuário
 */
class AuthRepository {
    private val apiService = ApiClient.apiService
    private val gson = Gson()

    /**
     * Realiza o login do usuário
     * @param email Email do usuário
     * @param password Senha do usuário
     * @return Result com LoginResponse em caso de sucesso ou Exception em caso de erro
     */
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            Log.d("AuthRepository", "===== INICIANDO LOGIN =====")
            Log.d("AuthRepository", "Email: $email")

            val response = apiService.login(request)

            Log.d("AuthRepository", "Código HTTP: ${response.code()}")
            Log.d("AuthRepository", "Sucesso: ${response.isSuccessful}")
            Log.d("AuthRepository", "Body existe: ${response.body() != null}")

            // Captura o erro antes de consumir
            val errorBody = response.errorBody()?.string()
            if (errorBody != null) {
                Log.e("AuthRepository", "Error body: $errorBody")
            }

            when {
                response.isSuccessful && response.body() != null -> {
                    val loginResponse = response.body()!!

                    Log.d("AuthRepository", "AuthToken: ${loginResponse.authToken}")
                    Log.d("AuthRepository", "SafeUser: ${loginResponse.safeUser}")
                    Log.d("AuthRepository", "SafeUser.email: ${loginResponse.safeUser?.email}")
                    Log.d("AuthRepository", "SafeUser.name: ${loginResponse.safeUser?.name}")

                    // Valida se o token existe
                    if (loginResponse.authToken.isNullOrEmpty()) {
                        Log.e("AuthRepository", "Token veio vazio ou nulo")
                        return Result.failure(Exception("Erro no servidor: token não foi gerado"))
                    }

                    // Valida se o safeUser existe
                    if (loginResponse.safeUser == null) {
                        Log.e("AuthRepository", "SafeUser veio nulo")
                        return Result.failure(Exception("Erro no servidor: dados do usuário não foram retornados"))
                    }

                    // Salva o token
                    AuthTokenHolder.token = loginResponse.authToken
                    Log.d("AuthRepository", "Login bem-sucedido! Token salvo.")

                    Result.success(loginResponse)
                }
                response.code() == 400 -> {
                    Log.e("AuthRepository", "Credenciais inválidas (400)")
                    Result.failure(Exception("Email ou senha incorretos"))
                }
                response.code() == 401 -> {
                    Log.e("AuthRepository", "Não autorizado (401)")
                    Result.failure(Exception("Email ou senha incorretos"))
                }
                response.code() == 500 -> {
                    Log.e("AuthRepository", "Erro no servidor (500)")
                    Result.failure(Exception("Erro no servidor. Tente novamente mais tarde."))
                }
                else -> {
                    Log.e("AuthRepository", "Erro desconhecido: ${response.code()}")
                    Result.failure(Exception("Erro ao fazer login. Código: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "EXCEÇÃO CAPTURADA")
            Log.e("AuthRepository", "Tipo: ${e.javaClass.simpleName}")
            Log.e("AuthRepository", "Mensagem: ${e.message}")
            Log.e("AuthRepository", "Stack trace completo:")
            e.printStackTrace()
            Result.failure(Exception("Erro ao conectar com o servidor: ${e.message}"))
        }
    }

    /**
     * Realiza o logout do usuário
     * @return Result com Unit em caso de sucesso ou Exception em caso de erro
     */
    suspend fun logout(): Result<Unit> {
        return try {
            val response = apiService.logout()

            if (response.isSuccessful) {
                // Limpa o token armazenado
                AuthTokenHolder.token = null
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao fazer logout"))
            }
        } catch (e: Exception) {
            // Limpa o token mesmo em caso de erro
            AuthTokenHolder.token = null
            Result.failure(Exception("Erro ao fazer logout: ${e.message}"))
        }
    }

    /**
     * Verifica se o usuário está autenticado
     * @return true se houver um token válido, false caso contrário
     */
    fun isAuthenticated(): Boolean {
        return AuthTokenHolder.token != null
    }

    /**
     * Limpa a sessão do usuário localmente
     */
    fun clearSession() {
        AuthTokenHolder.token = null
    }
}