package ru.ovk13.otusandroidbase.domain.repository

import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel

interface ScheduleRepository {
    fun getFilmSchedule(id: Int): FilmScheduleModel?
    fun setFilmSchedule(filmSchedule: FilmScheduleModel)
    fun removeFilmSchedule(id: Int)
    fun getFutureSchedule(time: Long): List<Int>
    fun removePastSchedule(time: Long)
}