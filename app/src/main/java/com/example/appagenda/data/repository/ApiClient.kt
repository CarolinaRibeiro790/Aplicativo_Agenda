package com.example.appagenda.data.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthTokenHolder {
    var token: String? = null
}

object ApiClient {
    private fun getRetrofitInstance(): Retrofit {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val originalRequest = chain.request()
            // Adiciona o token ao cabeçalho se estiver disponível
            AuthTokenHolder.token?.let { token ->
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            chain.proceed(originalRequest)
        }

        return Retrofit.Builder()
            .baseUrl("http://192.168.3.9:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    val apiService: ApiService by lazy {
        getRetrofitInstance().create(ApiService::class.java)
    }
}