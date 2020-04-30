package ru.ovk13.otusandroidbase

import android.app.Application
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ovk13.otusandroidbase.data.databse.AppDatabase
import ru.ovk13.otusandroidbase.data.repository.FavouritesRepositoryImpl
import ru.ovk13.otusandroidbase.data.repository.FilmsRepositoryImpl
import ru.ovk13.otusandroidbase.data.repository.VisitedRepositoryImpl
import ru.ovk13.otusandroidbase.data.retrofit.service.FilmsRetrofitService
import ru.ovk13.otusandroidbase.domain.CacheValidator
import ru.ovk13.otusandroidbase.domain.usecase.FavouritesUseCase
import ru.ovk13.otusandroidbase.domain.usecase.FilmsUseCase
import ru.ovk13.otusandroidbase.domain.usecase.VisitedUseCase
import ru.ovk13.otusandroidbase.network.Api
import java.util.concurrent.TimeUnit

class FilmsApplication : Application() {

    lateinit var filmsRetrofitService: FilmsRetrofitService
    lateinit var filmsUseCase: FilmsUseCase
    lateinit var favouritesUseCase: FavouritesUseCase
    lateinit var visitedUseCase: VisitedUseCase

    override fun onCreate() {
        super.onCreate()

        instance = this
        initRetrofit()
        initUseCases()
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

    private fun initUseCases() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val cacheValidator = CacheValidator(sharedPreferences)
        val filmsDao = AppDatabase.getDatabase(this).filmsDao
        val favouritesDao = AppDatabase.getDatabase(this).favouritesDao
        val visitedDao = AppDatabase.getDatabase(this).visitedDao
        filmsUseCase = FilmsUseCase(
            cacheValidator,
            sharedPreferences,
            FilmsRepositoryImpl(filmsDao, filmsRetrofitService)
        )
        favouritesUseCase = FavouritesUseCase(FavouritesRepositoryImpl(favouritesDao))
        visitedUseCase = VisitedUseCase(VisitedRepositoryImpl(visitedDao))
    }

    companion object {
        const val API_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "ee92a90466363d9efc0eecb85b392cfe"
        const val SHARED_PREFERENCES_KEY = "MOVIE_DB"
        var instance: FilmsApplication? = null
            private set
    }
}