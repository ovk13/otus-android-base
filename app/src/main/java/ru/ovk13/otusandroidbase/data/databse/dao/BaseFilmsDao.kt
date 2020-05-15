package ru.ovk13.otusandroidbase.data.databse.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.ovk13.otusandroidbase.data.model.FilmDataModel

@Dao
interface BaseFilmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filmsList: List<FilmDataModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: FilmDataModel)

    @Delete
    fun delete(film: FilmDataModel)
}