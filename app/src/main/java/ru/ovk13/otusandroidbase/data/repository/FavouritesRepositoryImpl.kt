package ru.ovk13.otusandroidbase.data.repository

import ru.ovk13.otusandroidbase.data.databse.dao.FavouritesDao
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.domain.repository.FavouritesRepository

class FavouritesRepositoryImpl(
    private val favouritesDao: FavouritesDao
) : FavouritesRepository {
    override fun getFavouritesListFromDb() = favouritesDao.getFavourites()

    override fun addFavouriteToDb(film: FilmDataModel) {
        favouritesDao.insert(film)
    }

    override fun removeFavouriteFromDb(id: Int) {
        if (isInFavourites(id)) {
            favouritesDao.deleteByFilmId(id)
        } else {
            throw Exception("Фильм не найден в избранных")
        }
    }

    override fun isInFavourites(id: Int): Boolean {
        return favouritesDao.isInFavourites(id)
    }

    override fun getFavouritesIdsFromDb() = favouritesDao.getFavouritesIds()
}