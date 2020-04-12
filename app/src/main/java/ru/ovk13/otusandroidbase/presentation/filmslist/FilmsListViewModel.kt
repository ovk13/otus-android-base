package ru.ovk13.otusandroidbase.presentation.filmslist

import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.GetFilmsDataModel
import ru.ovk13.otusandroidbase.data.model.LoadingErrorModel
import ru.ovk13.otusandroidbase.domain.usecase.GetFilmsListUseCase
import ru.ovk13.otusandroidbase.presentation.base.BaseFilmsListViewModel

class FilmsListViewModel(
    private val getFilmsListUseCase: GetFilmsListUseCase
) : BaseFilmsListViewModel() {
    private var page = 1
    private var totalPages: Int = 0


    fun loadPage(reload: Boolean = false) {
        getFilmsListUseCase.getFilms(page, object : GetFilmsListUseCase.LoadDataCallback {
            override fun onSuccess(data: GetFilmsDataModel) {
                totalPages = data.totalPages
                errorLiveData.postValue(null)

                if (reload)
                    setFilms(data.results)
                else
                    addFilms(data.results)
            }

            override fun onError(error: String) {
                errorLiveData.postValue(
                    LoadingErrorModel(
                        error,
                        if (page > 1) LoadingErrorModel.LOAD_PAGE else LoadingErrorModel.FULL_RELOAD
                    )
                )
            }

            override fun onError(e: Throwable) {
                errorLiveData.postValue(
                    LoadingErrorModel(
                        e.message ?: "",
                        if (page > 1) LoadingErrorModel.LOAD_PAGE else LoadingErrorModel.FULL_RELOAD
                    )
                )
            }

        })
    }

    private fun setFilms(filmsList: MutableList<FilmDataModel?>) {
        setMarkers(filmsList)
        filmsLiveData.postValue(filmsList)
    }

    private fun addFilms(filmsList: MutableList<FilmDataModel?>) {
        setMarkers(filmsList)
        val currentFilmsList = filmsLiveData.value ?: mutableListOf()
        currentFilmsList.addAll(filmsList)
        filmsLiveData.postValue(currentFilmsList)
    }

    fun loadNextPage() {
        val nextPage = page + 1
        if (nextPage > totalPages) {
            return
        }
        page = nextPage
        loadPage()
    }

    fun reloadAll() {
        page = 1
        totalPages = 0
        loadPage(true)
    }
}