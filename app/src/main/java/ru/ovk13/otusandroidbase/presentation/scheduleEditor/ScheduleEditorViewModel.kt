package ru.ovk13.otusandroidbase.presentation.scheduleEditor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.FilmScheduleModel
import ru.ovk13.otusandroidbase.domain.usecase.FilmsUseCase
import ru.ovk13.otusandroidbase.domain.usecase.ScheduleUseCase
import java.util.*

class ScheduleEditorViewModel(
    private val filmsUseCase: FilmsUseCase,
    private val scheduleUseCase: ScheduleUseCase
) : ViewModel() {

    private val scheduleEditorOpenedLiveData = MutableLiveData<Boolean>()
    private val filmLiveData = MutableLiveData<FilmDataModel>()
    private val filmScheduleLiveData = MutableLiveData<FilmScheduleModel>()
    private val errorLiveData = MutableLiveData<String>()

    val film: LiveData<FilmDataModel>
        get() = filmLiveData
    val filmSchedule: LiveData<FilmScheduleModel>
        get() = filmScheduleLiveData
    val error: LiveData<String>
        get() = errorLiveData
    val scheduleEditorOpened: LiveData<Boolean>
        get() = scheduleEditorOpenedLiveData

    fun setScheduleEditorOpened() {
        scheduleEditorOpenedLiveData.postValue(true)
    }

    fun setScheduleEditorClosed() {
        scheduleEditorOpenedLiveData.postValue(false)
    }

    fun getFilmSchedule(id: Int) {
        scheduleUseCase.getFilmSchedule(id, object : ScheduleUseCase.GetFilmScheduleCallback {
            override fun onSuccess(filmSchedule: FilmScheduleModel?) {
                if (filmSchedule != null) {
                    filmScheduleLiveData.postValue(filmSchedule)
                }
            }

            override fun onError(e: Throwable) {
                errorLiveData.postValue(e.message)
            }

        })
    }

    fun setFilmSchedule(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val watchDate = Calendar.getInstance()
        watchDate.set(year, month, day, hour, minute)
        scheduleUseCase.setFilmSchedule(
            film.value!!,
            watchDate,
            object : ScheduleUseCase.AddFilmScheduleCallback {
                override fun onSuccess() {
                    setScheduleEditorClosed()
                }

                override fun onError(e: Throwable) {
                    errorLiveData.postValue(e.message)
                }

            })
    }

    fun clearError() {
        errorLiveData.value = ""
    }

    fun getFilmData(filmId: Int) {
        filmsUseCase.getFilm(filmId, object : FilmsUseCase.GetFilmCallback {
            override fun onSuccess(film: FilmDataModel) {
                filmLiveData.postValue(film)
            }

            override fun onError(e: Throwable) {
                errorLiveData.postValue(e.message)
            }

        })
    }
}