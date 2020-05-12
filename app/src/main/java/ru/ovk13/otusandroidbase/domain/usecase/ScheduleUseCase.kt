package ru.ovk13.otusandroidbase.domain.usecase

import ru.ovk13.otusandroidbase.FilmsApplication
import ru.ovk13.otusandroidbase.R
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel
import ru.ovk13.otusandroidbase.domain.notification.NotificationScheduler
import ru.ovk13.otusandroidbase.domain.repository.ScheduleRepository
import java.util.*
import java.util.concurrent.Executors

class ScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val notificationScheduler: NotificationScheduler
) {
    fun getFilmSchedule(id: Int, callback: GetFilmScheduleCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                removePastSchedule(object : RemoveFilmScheduleCallback {
                    override fun onSuccess() {
                        val filmSchedule = scheduleRepository.getFilmSchedule(id)
                        callback.onSuccess(filmSchedule)
                    }

                    override fun onError(e: Throwable) {
                        throw e
                    }

                })

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
                notificationScheduler.scheduleNotification(
                    notificationScheduler.getNotification(
                        FilmsApplication.instance!!.getString(R.string.scheduleNotificationTitle) + " " + film.title,
                        FilmsApplication.instance!!.getString(R.string.scheduleNotificationText) + " " + film.title
                    ),
                    film.id,
                    watchDate.timeInMillis
                )
                callback.onSuccess()
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun removeFilmSchedule(film: FilmDataModel, callback: RemoveFilmScheduleCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                scheduleRepository.removeFilmSchedule(film.id)
                callback.onSuccess()
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun getFutureScheduledFilmsIds(callback: GetScheduledFilmsIdsCallback) {
        try {
            removePastSchedule(object : RemoveFilmScheduleCallback {
                override fun onSuccess() {
                    val calendar = Calendar.getInstance()
                    callback.onSuccess(scheduleRepository.getFutureSchedule(calendar.timeInMillis))
                }

                override fun onError(e: Throwable) {
                    throw e
                }
            })

        } catch (e: Throwable) {
            callback.onError(e)
        }
    }

    private fun removePastSchedule(callback: RemoveFilmScheduleCallback) {
        try {
            val calendar = Calendar.getInstance()
            scheduleRepository.removePastSchedule(calendar.timeInMillis)
            callback.onSuccess()
        } catch (e: Throwable) {
            callback.onError(e)
        }
    }

    interface GetScheduledFilmsIdsCallback {
        fun onSuccess(scheduledFilmsIds: List<Int>)
        fun onError(e: Throwable)
    }

    interface GetFilmScheduleCallback {
        fun onSuccess(filmSchedule: FilmScheduleModel?)
        fun onError(e: Throwable)
    }

    interface AddFilmScheduleCallback {
        fun onSuccess()
        fun onError(e: Throwable)
    }

    interface RemoveFilmScheduleCallback {
        fun onSuccess()
        fun onError(e: Throwable)
    }
}