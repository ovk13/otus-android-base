package ru.ovk13.otusandroidbase.data.repository

import ru.ovk13.otusandroidbase.FilmsApplication.Companion.API_KEY
import ru.ovk13.otusandroidbase.data.retrofit.service.FilmsRetrofitService
import ru.ovk13.otusandroidbase.domain.repository.FilmsRepository

class FilmsRepositoryImpl(
    private val filmsRetrofitService: FilmsRetrofitService
) : FilmsRepository {
    override fun getFilmsList(page: Int) =
        filmsRetrofitService.getFilms(API_KEY, "ru-Ru", page)
}