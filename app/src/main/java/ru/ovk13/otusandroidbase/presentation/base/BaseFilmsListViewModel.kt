package ru.ovk13.otusandroidbase.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.LoadingErrorModel
import ru.ovk13.otusandroidbase.domain.usecase.FavouritesUseCase
import ru.ovk13.otusandroidbase.domain.usecase.VisitedUseCase

abstract class BaseFilmsListViewModel(
    private val favouritesUseCase: FavouritesUseCase,
    private val visitedUseCase: VisitedUseCase
) : ViewModel() {
    protected val filmsLiveData = MutableLiveData<MutableList<FilmDataModel>>()
    protected val errorLiveData = MutableLiveData<LoadingErrorModel?>()

    val films: LiveData<MutableList<FilmDataModel>>
        get() = filmsLiveData

    val error: LiveData<LoadingErrorModel?>
        get() = errorLiveData

    fun addVisited(id: Int) {
        visitedUseCase.addVisited(id, object : VisitedUseCase.AddVisitedCallback {
            override fun onError(e: Throwable) {
                setError(e)
            }
        })
        val currentFilmsList = filmsLiveData.value ?: mutableListOf()
        currentFilmsList.filterNotNull().forEach {
            if (it.id == id) {
                it.visited = true
            }
        }
        filmsLiveData.postValue(currentFilmsList)
    }

    protected fun setError(e: Throwable, page: Int = 0) {
        errorLiveData.postValue(
            LoadingErrorModel(
                e.message ?: "",
                if (page > 1) LoadingErrorModel.LOAD_PAGE else LoadingErrorModel.FULL_RELOAD
            )
        )
    }

    fun clearError() {
        errorLiveData.value = null
    }
}