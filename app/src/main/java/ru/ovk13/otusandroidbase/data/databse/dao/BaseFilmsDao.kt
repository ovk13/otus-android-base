package ru.ovk13.otusandroidbase.data.databse.dao

import androidx.room.*
import ru.ovk13.otusandroidbase.data.model.FilmDataModel

@Dao
interface BaseFilmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filmsList: List<FilmDataModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: FilmDataModel)

    @Delete
    fun delete(film: FilmDataModel)

    @Query("SELECT * FROM films WHERE id=:id LIMIT 1")
    fun getFilm(id: Int): FilmDataModel
}