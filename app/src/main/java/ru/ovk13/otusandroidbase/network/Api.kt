package ru.ovk13.otusandroidbase.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Api {
    const val API_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "ee92a90466363d9efc0eecb85b392cfe"

    val service: ApiServiceInterface by lazy {

        val httpClient = OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        return@lazy retrofit.create(ApiServiceInterface::class.java)
    }
}