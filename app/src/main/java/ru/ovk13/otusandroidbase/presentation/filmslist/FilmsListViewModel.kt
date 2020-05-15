package ru.ovk13.otusandroidbase.presentation.filmslist

import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.LoadingErrorModel
import ru.ovk13.otusandroidbase.domain.usecase.FavouritesUseCase
import ru.ovk13.otusandroidbase.domain.usecase.FilmsUseCase
import ru.ovk13.otusandroidbase.domain.usecase.ScheduleUseCase
import ru.ovk13.otusandroidbase.domain.usecase.VisitedUseCase
import ru.ovk13.otusandroidbase.presentation.base.BaseFilmsListViewModel

class FilmsListViewModel(
    private val filmsUseCase: FilmsUseCase,
    private val favouritesUseCase: FavouritesUseCase,
    private val visitedUseCase: VisitedUseCase,
    private val scheduleUseCase: ScheduleUseCase
) : BaseFilmsListViewModel(favouritesUseCase, visitedUseCase, scheduleUseCase) {
    private var page = 1
    private var totalPagesCount: Int? = null


    fun loadPage(reload: Boolean = false) {
        filmsUseCase.getFilms(page, object : FilmsUseCase.LoadDataCallback {

            override fun onSuccess(data: List<FilmDataModel>) {
                errorLiveData.postValue(null)
                val filmsList = data.toMutableList()
                favouritesUseCase.getFavouritesIds(object :
                    FavouritesUseCase.GetFavouritesIdsCallback {
                    override fun onSuccess(favouritesIds: List<Int>) {
                        visitedUseCase.getVisitedIds(object : VisitedUseCase.GetVisitedCallback {
                            override fun onSuccess(visitedIds: List<Int>) {
                                scheduleUseCase.getFutureScheduledFilmsIds(object :
                                    ScheduleUseCase.GetScheduledFilmsIdsCallback {
                                    override fun onSuccess(scheduledFilmsIds: List<Int>) {
                                        filmsList.map {
                                            it.inFavourites = favouritesIds.contains(it.id)
                                            it.visited = visitedIds.contains(it.id)
                                            it.scheduled = scheduledFilmsIds.contains(it.id)
                                        }

                                        if (reload)
                                            setFilms(filmsList)
                                        else
                                            addFilms(filmsList)
                                    }

                                    override fun onError(e: Throwable) {
                                        setError(e, page)
                                    }

                                })
                            }

                            override fun onError(e: Throwable) {
                                setError(e, page)
                            }
                        })

                    }

                    override fun onError(e: Throwable) {
                        setError(e)
                    }

                })

            }

            override fun setTotalPages(count: Int) {
                totalPagesCount = count
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
                setError(e, page)
            }

        })
    }

    private fun setFilms(filmsList: MutableList<FilmDataModel>) {
        filmsLiveData.postValue(filmsList)
    }

    private fun addFilms(filmsList: MutableList<FilmDataModel>) {
        val currentFilmsList = filmsLiveData.value ?: mutableListOf()
        currentFilmsList.addAll(filmsList)
        filmsLiveData.postValue(currentFilmsList)
    }

    fun loadNextPage() {
        val nextPage = page + 1
        if (totalPagesCount === null) {
            totalPagesCount = filmsUseCase.getSavedTotalPagesCount()
        }
        if (nextPage > (totalPagesCount as Int)) {
            return
        }
        page = nextPage
        loadPage()
    }

    fun reloadAll() {
        page = 1
        loadPage(true)
    }

    fun setFavouriteStatus(id: Int, inFavourites: Boolean) {
        val filmsList = filmsLiveData.value
        filmsList?.find { it.id == id }?.inFavourites = inFavourites
        filmsLiveData.postValue(filmsList)
    }
}