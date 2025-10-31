import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagenda.cache.UserCache
import com.example.appagenda.model.Usuario
import com.example.appagenda.network.ApiClient
import com.example.appagenda.network.AuthTokenHolder
import kotlinx.coroutines.launch

class UsuarioViewModel(): ViewModel() {

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set


    fun carregarDados(context: Context) {
        //Carregar os dados da cache
        val usuarioCache = UserCache.obter()

        if (usuarioCache != null) {
            usuario = usuarioCache
            Log.d("UsuarioViewModel", "Usuário carregado do cache: ${usuarioCache.name}")
        }

        //Caso não tenha dados na cache, busca no servidor
        atualizarPerfil(context)
    }

    fun atualizarPerfil(context: Context) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val token = AuthTokenHolder.token

                if(token == null){
                    errorMessage = "Token não disponível"
                    return@launch
                }
                val response = ApiClient.apiService.getUserProfile()

                if(response.isSuccessful){
                    val usuarioServidor = response.body()
                    if(usuarioServidor != null){
                        usuario = usuarioServidor.user
                        UserCache.salvar(usuario!!, token, context)
                    }
                }else{
                    errorMessage = "Erro ao carregar perfil"
                }
            }catch (e: Exception){
                errorMessage = "Erro: ${e.message}"
            }finally {
                isLoading = false
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            try{
                ApiClient.apiService.logout()
            }catch (e: Exception){
                Log.e("UsuarioViewModel", "Erro ao fazer logout: ${e.message}")
            }finally {
                UserCache.limpar()
                AuthTokenHolder.token = null
                usuario = null
            }
        }
    }
}