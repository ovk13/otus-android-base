package ru.ovk13.otusandroidbase.data.repository

import ru.ovk13.otusandroidbase.data.databse.dao.ScheduleDao
import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel
import ru.ovk13.otusandroidbase.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {
    override fun getFutureSchedule(time: Long): List<Int> = scheduleDao.getFutureSchedule(time)

    override fun removePastSchedule(time: Long) = scheduleDao.removePastSchedule(time)

    override fun getFilmSchedule(id: Int): FilmScheduleModel? = scheduleDao.getFilmSchedule(id)

    override fun setFilmSchedule(filmSchedule: FilmScheduleModel) {
        scheduleDao.addFilmSchedule(filmSchedule)
    }

    override fun removeFilmSchedule(id: Int) = scheduleDao.removeFilmSchedule(id)
}