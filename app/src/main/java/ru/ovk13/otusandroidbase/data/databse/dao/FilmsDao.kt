package ru.ovk13.otusandroidbase.data.databse.dao

import androidx.room.Dao
import androidx.room.Query
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel.Companion.TYPE_FILM

@Dao
interface FilmsDao : BaseFilmsDao {
    @Query("SELECT * FROM films WHERE type=\"$TYPE_FILM\" LIMIT :offset, :limit")
    fun getFilms(limit: Int, offset: Int): MutableList<FilmDataModel>

    @Query("DELETE FROM films WHERE type=\"$TYPE_FILM\"")
    fun deleteAllFilms()
}