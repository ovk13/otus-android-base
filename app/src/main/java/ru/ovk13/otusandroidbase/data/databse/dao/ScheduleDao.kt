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

    @Query("SELECT film_id FROM schedule WHERE watch_timestamp > :time")
    fun getFutureSchedule(time: Long): List<Int>

    @Query("DELETE FROM schedule WHERE watch_timestamp < :time")
    fun removePastSchedule(time: Long)

    @Query("DELETE FROM schedule WHERE film_id = :id")
    fun removeFilmSchedule(id: Int)
}