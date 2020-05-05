package ru.ovk13.otusandroidbase.data.databse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFilmSchedule(filmScheduleModel: FilmScheduleModel)

    @Query("SELECT * FROM schedule WHERE film_id = :id")
    fun getFilmSchedule(id: Int): FilmScheduleModel?

    @Query("SELECT * FROM schedule")
    fun getFullSchedule(): List<FilmScheduleModel>
}