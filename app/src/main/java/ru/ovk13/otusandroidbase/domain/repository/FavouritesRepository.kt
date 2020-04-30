package ru.ovk13.otusandroidbase.domain.repository

import ru.ovk13.otusandroidbase.data.model.FilmDataModel

interface FavouritesRepository {
    fun getFavouritesListFromDb(): MutableList<FilmDataModel>
    fun addFavouriteToDb(film: FilmDataModel)
    fun removeFavouriteFromDb(id: Int)
    fun isInFavourites(id: Int): Boolean
    fun getFavouritesIdsFromDb(): List<Int>
}