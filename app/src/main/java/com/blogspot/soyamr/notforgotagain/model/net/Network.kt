package com.blogspot.soyamr.notforgotagain.model.net

import android.util.Log
import com.blogspot.soyamr.notforgotagain.model.net.pojo.UserToken
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Network {
    private var token: String = ""

    fun updateToken(token: UserToken) {
        this.token = token.apiToken
        Log.i("betweok", "updated token " + token.apiToken)
    }


    val retrofit: TaskApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://practice.mobile.kreosoft.ru/api/")
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                var request = chain.request()
                if (request.header("No-Authentication") == null) {
                    if (!token.isNullOrEmpty()) {
                        val finalToken = "Bearer $token"
                        Log.e(" ttooken ", " " + finalToken)
                        request = request.newBuilder()
                            .addHeader("Authorization", finalToken)
                            .build()
                        Log.e(" request header ", " " + request.headers)
                    }

                }
                chain.proceed(request)
            }.build())
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build().create(TaskApiService::class.java)
    }
}