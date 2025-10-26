package com.example.appagenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagenda.network.ApiClient
import com.example.appagenda.network.AuthTokenHolder
import com.example.appagenda.model.LoginRequest
import com.example.appagenda.model.Usuario
import kotlinx.coroutines.launch
import android.util.Log
import com.example.appagenda.cache.UserCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val usuario: Usuario? = null,
    val error: String? = null
)

class LoginViewModel : ViewModel() {
      private val _uiState = MutableStateFlow(LoginUiState())
    var uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, senha: String, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = ApiClient.apiService.login(LoginRequest(email, senha))

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.authToken != null && loginResponse.safeUser != null) {
                        val token = loginResponse.authToken
                        val usuario = loginResponse.safeUser

                        AuthTokenHolder.token = loginResponse.authToken
                        //Salvar no cache
                        UserCache.salvar(usuario, token)

                        _uiState.value = _uiState.value.copy(
                            isLoggedIn = true,
                            usuario = loginResponse.safeUser
                        )

                        onSuccess(token)
                    } else {
                        _uiState.value = _uiState.value.copy(error = "Resposta inválida do servidor")
                    }
                } else {
                    _uiState.value = _uiState.value.copy(error = "Login falhou: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Erro: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            try{
                ApiClient.apiService.logout()
            }catch (e: Exception){

            }finally {
                UserCache.limpar()
                AuthTokenHolder.token = null
                _uiState.value = LoginUiState()
            }
        }
    }
}