package com.blogspot.soyamr.notforgotagain.model.net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Network {
    private var token: String = ""

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor { chain ->
            var request = chain.request()
            if (request.header("No-Authentication") == null) {
                if (token.isNotEmpty()) {
                    val finalToken = "Bearer $token"
                    println(" AAmr finalToken: $finalToken")
                    request = request.newBuilder()
                        .addHeader("Authorization", finalToken)
                        .build()
                    println("AAmr request header  ${request.headers}")
                }
            }
            chain.proceed(request)
        }.build()
    }

    fun updateToken(token: String) {
        this.token = token
        println("AAmr netweok updated token ${token}")
    }


    val retrofit: TaskApiService by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://practice.mobile.kreosoft.ru/api/")
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build().create(TaskApiService::class.java)
    }
}