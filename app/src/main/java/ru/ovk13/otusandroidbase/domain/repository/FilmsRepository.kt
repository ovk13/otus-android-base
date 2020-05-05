package ru.ovk13.otusandroidbase.domain.repository

import retrofit2.Call
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.GetFilmsDataModel

interface FilmsRepository {
    fun getFilmsListFromApi(page: Int): Call<GetFilmsDataModel>
    fun getFilmsListFromDb(page: Int): MutableList<FilmDataModel>
    fun writeFilmsListToDb(filmsList: List<FilmDataModel>)
    fun clearDb()
    fun getFilmById(id: Int): FilmDataModel
}