package ru.ovk13.otusandroidbase.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
class FilmScheduleModel(
    @PrimaryKey
    @ColumnInfo(name = "film_id")
    val filmId: Int,
    @ColumnInfo(name = "watch_timestamp")
    val watchTimestamp: Long
) {
}