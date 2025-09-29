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
                .url("http://10.0.2.2:3000/auth/login") // Ajuste a URL conforme necessário
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()

            // Se a resposta for 200, login válido
            response.isSuccessful

        } catch (e: Exception) {
            false // Se der erro, retorna false
        }
    }
}