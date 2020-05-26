package ru.ovk13.otusandroidbase.domain.usecase

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel.Companion.TYPE_FILM
import ru.ovk13.otusandroidbase.data.model.GetFilmsDataModel
import ru.ovk13.otusandroidbase.domain.CacheValidator
import ru.ovk13.otusandroidbase.domain.repository.FilmsRepository
import java.util.*
import java.util.concurrent.Executors

class FilmsUseCase(
    private val cacheValidator: CacheValidator,
    private val sharedPreferences: SharedPreferences,
    private val filmsRepository: FilmsRepository
) {
    fun getFilms(page: Int = 1, callback: LoadDataCallback) {

        try {
            if ((page == 1 && cacheValidator.isNotValid())) {
                clearDb()
                getFilmsFromApi(page, callback)
            } else {
                getFilmsFromDb(page, callback)
            }
        } catch (e: Exception) {
            Log.d("exception_name", e.message.toString())
        }
    }

    private fun getFilmsFromDb(
        page: Int,
        callback: LoadDataCallback
    ) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                var films = filmsRepository.getFilmsListFromDb(page)
                if (films.isEmpty()) {
                    getFilmsFromApi(page, callback)
                } else {
                    callback.onSuccess(films)
                }
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    private fun getFilmsFromApi(
        page: Int,
        callback: LoadDataCallback
    ) {
        filmsRepository.getFilmsListFromApi(page).enqueue(object : Callback<GetFilmsDataModel> {
            override fun onFailure(call: Call<GetFilmsDataModel>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<GetFilmsDataModel>,
                response: Response<GetFilmsDataModel>
            ) {
                if (response.isSuccessful) {

                    if (page == 1) {
                        cacheValidator.setLastLoadTime(Date())
                    }

                    val filmsList = response.body()!!.results
                    val totalPagesCount = response.body()!!.totalPages

                    saveTotalPagesCount(totalPagesCount)
                    callback.setTotalPages(totalPagesCount)
                    saveFilmsToDb(filmsList)
                    callback.onSuccess(filmsList)

                } else {
                    callback.onError(response.code().toString())
                }
            }

        })
    }

    private fun saveFilmsToDb(filmsList: List<FilmDataModel>) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            filmsList.forEach {
                it.type = TYPE_FILM
            }
            filmsRepository.writeFilmsListToDb(filmsList)
        })
    }

    fun clearDb() {
        Executors.newSingleThreadExecutor().execute(Runnable {
            filmsRepository.clearDb()
        })
    }

    fun saveTotalPagesCount(count: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(SHARED_PREFERENCES_TOTAL_PAGES_COUNT, count)
        editor.apply()
    }

    fun getSavedTotalPagesCount(): Int {
        return sharedPreferences.getInt(SHARED_PREFERENCES_TOTAL_PAGES_COUNT, 0)
    }

    fun getFilm(id: Int, callback: GetFilmCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                val film = filmsRepository.getFilmById(id)
                callback.onSuccess(film)
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    interface GetFilmCallback {
        fun onSuccess(film: FilmDataModel)
        fun onError(e: Throwable)
    }

    interface LoadDataCallback {
        fun onSuccess(data: List<FilmDataModel>)
        fun setTotalPages(count: Int)
        fun onError(error: String)
        fun onError(e: Throwable)
    }

    companion object {
        const val SHARED_PREFERENCES_TOTAL_PAGES_COUNT = "TOTAL_PAGES_COUNT"
    }
}