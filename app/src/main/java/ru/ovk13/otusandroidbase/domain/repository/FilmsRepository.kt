package ru.ovk13.otusandroidbase.domain.repository

import retrofit2.Call
import ru.ovk13.otusandroidbase.data.model.GetFilmsDataModel

interface FilmsRepository {
    fun getFilmsList(page: Int): Call<GetFilmsDataModel>
}