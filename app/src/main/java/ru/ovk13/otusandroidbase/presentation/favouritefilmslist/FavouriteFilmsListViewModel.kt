package ru.ovk13.otusandroidbase.presentation.favouritefilmslist

import androidx.lifecycle.MutableLiveData
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.LoadingErrorModel
import ru.ovk13.otusandroidbase.domain.usecase.FavouritesUseCase
import ru.ovk13.otusandroidbase.domain.usecase.ScheduleUseCase
import ru.ovk13.otusandroidbase.domain.usecase.VisitedUseCase
import ru.ovk13.otusandroidbase.presentation.base.BaseFilmsListViewModel

class FavouriteFilmsListViewModel(
    private val favouritesUseCase: FavouritesUseCase,
    private val visitedUseCase: VisitedUseCase,
    private val scheduleUseCase: ScheduleUseCase
) : BaseFilmsListViewModel(favouritesUseCase, visitedUseCase, scheduleUseCase) {

    fun loadFavourites() {
        favouritesUseCase.getFavourites(object : FavouritesUseCase.GetFavouritesCallback {
            override fun onSuccess(favouritesList: List<FilmDataModel>) {
                errorLiveData.postValue(null)
                val filmsList = favouritesList.toMutableList()
                visitedUseCase.getVisitedIds(object : VisitedUseCase.GetVisitedCallback {
                    override fun onSuccess(visitedIds: List<Int>) {
                        filmsList.map {
                            it.visited = visitedIds.contains(it.id)
                        }

                        filmsLiveData.postValue(filmsList)
                    }

                    override fun onError(e: Throwable) {
                        setError(e)
                    }
                })
            }

            override fun onError(e: Throwable) {
                errorLiveData.postValue(
                    LoadingErrorModel(
                        e.message ?: "",
                        LoadingErrorModel.FULL_RELOAD
                    )
                )
            }

        })
    }

    fun addFilm(film: FilmDataModel, index: Int? = null) {

        favouritesUseCase.addFavourite(film, object : FavouritesUseCase.AddFavouritesCallback {
            override fun onSuccess(film: FilmDataModel) {}

            override fun onError(e: Throwable) {
                errorLiveData.postValue(
                    LoadingErrorModel(
                        e.message ?: "",
                        LoadingErrorModel.FULL_RELOAD
                    )
                )
            }

        })

        val currentFilmsList = filmsLiveData.value ?: mutableListOf()
        if (index == null) {
            currentFilmsList.add(film)
        } else {
            currentFilmsList.add(index, film)
        }
        filmsLiveData.postValue(currentFilmsList)
    }

    fun removeFilm(film: FilmDataModel, index: MutableLiveData<Int?>) {

        favouritesUseCase.removeFavourite(
            film.id,
            object : FavouritesUseCase.RemoveFavouritesCallback {
                override fun onSuccess() {
                    index.postValue(removeFilmById(film.id))
                }

                override fun onError(e: Throwable) {
                    errorLiveData.postValue(
                        LoadingErrorModel(
                            e.message ?: "",
                            LoadingErrorModel.FULL_RELOAD
                        )
                    )
                }

            })
    }

    private fun removeFilmById(id: Int): Int? {
        val currentFilmsList = filmsLiveData.value ?: mutableListOf()
        currentFilmsList.filterNotNull().forEachIndexed { index, film ->
            if (film.id == id) {
                currentFilmsList.remove(film)
                filmsLiveData.postValue(currentFilmsList)
                return index
            }
        }

        return null
    }

    fun isInFavourites(id: Int): Boolean {
        if (filmsLiveData.value === null) {
            return false
        }
        return filmsLiveData.value!!.any { it.id == id }
    }
}