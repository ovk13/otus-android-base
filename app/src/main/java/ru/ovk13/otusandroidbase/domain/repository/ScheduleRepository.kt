package ru.ovk13.otusandroidbase.domain.repository

import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel

interface ScheduleRepository {
    fun getFullSchedule(): List<FilmScheduleModel>
    fun getFilmSchedule(id: Int): FilmScheduleModel?
    fun setFilmSchedule(filmSchedule: FilmScheduleModel)
}