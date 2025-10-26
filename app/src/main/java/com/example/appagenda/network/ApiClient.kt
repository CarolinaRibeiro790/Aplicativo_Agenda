package com.example.appagenda.network

import com.example.appagenda.network.ApiService
import com.example.appagenda.network.AuthTokenHolder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    private fun getRetrofitInstance(): Retrofit {

        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val originalRequest = chain.request()
            AuthTokenHolder.token?.let { token ->
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            chain.proceed(originalRequest)
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    val apiService: ApiService by lazy {
        getRetrofitInstance().create(ApiService::class.java)
    }
}