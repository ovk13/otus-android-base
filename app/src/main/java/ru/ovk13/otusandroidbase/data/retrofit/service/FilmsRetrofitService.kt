package ru.ovk13.otusandroidbase.data.retrofit.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ovk13.otusandroidbase.data.model.GetFilmsDataModel

interface FilmsRetrofitService {
    @GET("discover/movie")
    fun getFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<GetFilmsDataModel>
}