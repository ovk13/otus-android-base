package ru.ovk13.otusandroidbase.data.repository

object FavouritesRepository {
    var favouritesIds: MutableList<Int> = mutableListOf()

    fun addFavourite(id: Int) {
        favouritesIds.add(id)
    }

    fun removeFavourite(id: Int) {
        if (isInFavourites(id)) {
            favouritesIds.remove(id)
        } else {
            throw Exception("Фильм не найден в избранных")
        }
    }

    fun isInFavourites(id: Int): Boolean {
        return favouritesIds.contains(id)
    }
}