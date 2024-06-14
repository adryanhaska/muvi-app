package com.example.muvi_app.data.network

import com.example.muvi_app.data.pref.UserPreference
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(userPreference: UserPreference): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val tokenInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val token = runBlocking { userPreference.getToken() }
            val requestBuilder = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestBuilder)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://c24-mr01-server-arxlnabp3q-et.a.run.app/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun getApiServiceMl(): ApiServiceML {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://c24-mr01-ml-arxlnabp3q-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit.create(ApiServiceML::class.java)
    }
}