package ru.ovk13.otusandroidbase.data.repository

import ru.ovk13.otusandroidbase.data.databse.dao.ScheduleDao
import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel
import ru.ovk13.otusandroidbase.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {
    override fun getFullSchedule(): List<FilmScheduleModel> = scheduleDao.getFullSchedule()

    override fun getFilmSchedule(id: Int): FilmScheduleModel? = scheduleDao.getFilmSchedule(id)

    override fun setFilmSchedule(filmSchedule: FilmScheduleModel) {
        scheduleDao.addFilmSchedule(filmSchedule)
    }
}