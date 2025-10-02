package com.example.appagenda.data.dao
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

suspend fun verificarLogin(email: String, senha: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()

            // Criar JSON para enviar
            val json = JSONObject()
            json.put("email", email)
            json.put("password", senha)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("http://192.168.3.9:8000/api/auth/login")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()

            println("Código de resposta da API: ${response.code}")

            // Verifica se a resposta foi bem-sucedida
            if (response.isSuccessful) {
                true
            } else {
                // Se o servidor respondeu com um erro (como 500), log o corpo da resposta
                println("Corpo do erro: ${response.body?.string()}")
                false
            }

        } catch (e: Exception) {
            false // Se der erro, retorna false
        }
    }
}