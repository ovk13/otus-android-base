package ru.ovk13.otusandroidbase.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.LoadingErrorModel
import ru.ovk13.otusandroidbase.data.repository.FavouritesRepository
import ru.ovk13.otusandroidbase.data.repository.VisitedRepository

abstract class BaseFilmsListViewModel() : ViewModel() {
    protected val filmsLiveData = MutableLiveData<MutableList<FilmDataModel?>>()
    protected val errorLiveData = MutableLiveData<LoadingErrorModel?>()

    val films: LiveData<MutableList<FilmDataModel?>>
        get() = filmsLiveData

    val error: LiveData<LoadingErrorModel?>
        get() = errorLiveData


    protected fun setMarkers(filmsList: MutableList<FilmDataModel?>) {
        filmsList.filterNotNull().forEach {
            it.visited = VisitedRepository.isVisited(it.id)
            it.inFavourites = FavouritesRepository.isInFavourites(it.id)
        }
    }

    fun addVisited(id: Int) {
        if (VisitedRepository.isVisited(id)) {
            return
        }
        VisitedRepository.addId(id)

        val currentFilmsList = filmsLiveData.value ?: mutableListOf()
        currentFilmsList.filterNotNull().forEach {
            if (it.id == id) {
                it.visited = true
            }
        }
    }

    fun clearError() {
        errorLiveData.value = null
    }
}