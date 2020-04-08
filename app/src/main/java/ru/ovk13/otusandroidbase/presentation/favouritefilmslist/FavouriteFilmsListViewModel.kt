package ru.ovk13.otusandroidbase.presentation.favouritefilmslist

import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.repository.FavouritesRepository
import ru.ovk13.otusandroidbase.presentation.base.BaseFilmsListViewModel

class FavouriteFilmsListViewModel() : BaseFilmsListViewModel() {

    fun addFilm(film: FilmDataModel, index: Int? = null) {
        val currentFilmsList = filmsLiveData.value ?: mutableListOf()
        if (index == null) {
            currentFilmsList.add(film)
        } else {
            currentFilmsList.add(index, film)
        }
        filmsLiveData.postValue(currentFilmsList)

        FavouritesRepository.addFavourite(film.id)
    }

    fun removeFilm(film: FilmDataModel): Int? {
        val index = removeFilmById(film.id)

        if (FavouritesRepository.isInFavourites(film.id)) {
            FavouritesRepository.removeFavourite(film.id)
        }

        return index
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
}