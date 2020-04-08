package ru.ovk13.otusandroidbase

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ovk13.otusandroidbase.data.repository.FilmsRepositoryImpl
import ru.ovk13.otusandroidbase.data.retrofit.service.FilmsRetrofitService
import ru.ovk13.otusandroidbase.domain.usecase.GetFilmsListUseCase
import ru.ovk13.otusandroidbase.network.Api
import java.util.concurrent.TimeUnit

class FilmsApplication : Application() {

    lateinit var filmsRetrofitService: FilmsRetrofitService
    lateinit var getFilmsListUseCase: GetFilmsListUseCase

    override fun onCreate() {
        super.onCreate()

        instance = this
        initRetrofit()
        initUseCase()
    }

    private fun initRetrofit() {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val httpClient = OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        filmsRetrofitService = Retrofit.Builder()
            .baseUrl(Api.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(FilmsRetrofitService::class.java)
    }

    private fun initUseCase() {
        getFilmsListUseCase = GetFilmsListUseCase(FilmsRepositoryImpl(filmsRetrofitService))
    }

    companion object {
        const val API_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "ee92a90466363d9efc0eecb85b392cfe"
        var instance: FilmsApplication? = null
            private set
    }
}