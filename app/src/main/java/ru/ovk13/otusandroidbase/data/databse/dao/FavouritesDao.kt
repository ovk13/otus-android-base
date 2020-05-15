package ru.ovk13.otusandroidbase.data.databse.dao

import androidx.room.Dao
import androidx.room.Query
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel.Companion.TYPE_FAVOURITE

@Dao
interface FavouritesDao : BaseFilmsDao {
    @Query("SELECT * FROM films WHERE type = \"$TYPE_FAVOURITE\"")
    fun getFavourites(): MutableList<FilmDataModel>

    @Query("SELECT id FROM films WHERE type = \"$TYPE_FAVOURITE\"")
    fun getFavouritesIds(): MutableList<Int>

    @Query("DELETE FROM films WHERE type = \"$TYPE_FAVOURITE\"")
    fun deleteAllFavourites()

    @Query("DELETE FROM films WHERE id = :id AND type = \"$TYPE_FAVOURITE\"")
    fun deleteByFilmId(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM films WHERE id = :id AND type = \"$TYPE_FAVOURITE\" LIMIT 1)")
    fun isInFavourites(id: Int): Boolean
}