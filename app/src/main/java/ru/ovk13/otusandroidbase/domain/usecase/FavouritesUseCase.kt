package ru.ovk13.otusandroidbase.domain.usecase

import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel.Companion.TYPE_FAVOURITE
import ru.ovk13.otusandroidbase.domain.repository.FavouritesRepository
import java.util.concurrent.Executors

class FavouritesUseCase(
    private val favouritesRepository: FavouritesRepository
) {
    fun getFavourites(callback: GetFavouritesCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                val films = favouritesRepository.getFavouritesListFromDb()
                callback.onSuccess(films)
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun getFavouritesIds(callback: GetFavouritesIdsCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                val filmsIds = favouritesRepository.getFavouritesIdsFromDb()
                callback.onSuccess(filmsIds)
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun addFavourite(film: FilmDataModel, callback: AddFavouritesCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                if (!favouritesRepository.isInFavourites(film.id)) {
                    film.type = TYPE_FAVOURITE
                    favouritesRepository.addFavouriteToDb(film)
                }
                callback.onSuccess(film)
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun removeFavourite(id: Int, callback: RemoveFavouritesCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                if (favouritesRepository.isInFavourites(id)) {
                    favouritesRepository.removeFavouriteFromDb(id)
                    callback.onSuccess()
                } else {
                    throw Exception("Фильм не найден в избранном")
                }
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    interface AddFavouritesCallback {
        fun onSuccess(film: FilmDataModel)
        fun onError(e: Throwable)
    }

    interface RemoveFavouritesCallback {
        fun onSuccess()
        fun onError(e: Throwable)
    }

    interface GetFavouritesCallback {
        fun onSuccess(favouritesList: List<FilmDataModel>)
        fun onError(e: Throwable)
    }

    interface GetFavouritesIdsCallback {
        fun onSuccess(favouritesIds: List<Int>)
        fun onError(e: Throwable)
    }
}