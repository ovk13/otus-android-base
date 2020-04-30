package ru.ovk13.otusandroidbase.data.repository

import ru.ovk13.otusandroidbase.FilmsApplication.Companion.API_KEY
import ru.ovk13.otusandroidbase.data.databse.dao.FilmsDao
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.retrofit.service.FilmsRetrofitService
import ru.ovk13.otusandroidbase.domain.repository.FilmsRepository

class FilmsRepositoryImpl(
    private val filmsDao: FilmsDao,
    private val filmsRetrofitService: FilmsRetrofitService
) : FilmsRepository {

    private var pageSize: Int = DEFAULT_PAGE_SIZE

    override fun getFilmsListFromApi(page: Int) =
        filmsRetrofitService.getFilms(API_KEY, "ru-Ru", page)

    override fun getFilmsListFromDb(page: Int): MutableList<FilmDataModel> {
        val limit = pageSize
        val offset = (page - 1) * pageSize
        return filmsDao.getFilms(limit, offset)
    }

    override fun writeFilmsListToDb(filmsList: List<FilmDataModel>) {
        filmsDao.insert(filmsList)
    }

    override fun clearDb() {
        filmsDao.deleteAllFilms()
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}