package ru.ovk13.otusandroidbase.data.databse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ovk13.otusandroidbase.data.model.VisitedFilmModel

@Dao
interface VisitedDao {
    @Query("SELECT film_id FROM visited")
    fun getVisitedIds(): MutableList<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addVisited(visited: VisitedFilmModel)

    @Query("DELETE FROM visited WHERE film_id = :id")
    fun deleteVisitedId(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM visited WHERE film_id = :id)")
    fun isVisited(id: Int): Boolean
}