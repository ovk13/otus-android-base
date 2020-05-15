package ru.ovk13.otusandroidbase.presentation.filmdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.domain.usecase.FilmsUseCase

class FilmDetailViewModel(
    private val filmsUseCase: FilmsUseCase
) : ViewModel() {
    private val detailFilmLiveData = MutableLiveData<FilmDataModel>()
    private val errorLiveData = MutableLiveData<String>()

    val detailFilm: LiveData<FilmDataModel>
        get() = detailFilmLiveData
    val error: LiveData<String>
        get() = errorLiveData

    fun getFilm(id: Int) {
        filmsUseCase.getFilm(id, object : FilmsUseCase.GetFilmCallback {
            override fun onSuccess(film: FilmDataModel) {
                detailFilmLiveData.postValue(film)
            }

            override fun onError(e: Throwable) {
                errorLiveData.postValue(e.message)
            }

        })
    }
}