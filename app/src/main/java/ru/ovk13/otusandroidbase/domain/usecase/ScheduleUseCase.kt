package ru.ovk13.otusandroidbase.domain.usecase

import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel
import ru.ovk13.otusandroidbase.domain.repository.ScheduleRepository
import java.util.*
import java.util.concurrent.Executors

class ScheduleUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    fun getFilmSchedule(id: Int, callback: GetFilmScheduleCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                val filmSchedule = scheduleRepository.getFilmSchedule(id)
                callback.onSuccess(filmSchedule)
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun setFilmSchedule(
        film: FilmDataModel,
        watchDate: Calendar,
        callback: AddFilmScheduleCallback
    ) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                val filmSchedule = FilmScheduleModel(film.id, watchDate.timeInMillis)
                scheduleRepository.setFilmSchedule(filmSchedule)
                callback.onSuccess()
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    interface GetFilmScheduleCallback {
        fun onSuccess(filmSchedule: FilmScheduleModel?)
        fun onError(e: Throwable)
    }

    interface AddFilmScheduleCallback {
        fun onSuccess()
        fun onError(e: Throwable)
    }
}