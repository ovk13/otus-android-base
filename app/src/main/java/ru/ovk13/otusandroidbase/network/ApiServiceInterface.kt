package ru.ovk13.otusandroidbase.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ovk13.otusandroidbase.data.FilmsResponse

interface ApiServiceInterface {
    @GET("discover/movie")
    fun getFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<FilmsResponse>
}