package ru.ovk13.otusandroidbase.presentation.filmdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ovk13.otusandroidbase.data.model.FilmDataModel

class FilmDetailViewModel : ViewModel() {
    private val detailFilmLiveData = MutableLiveData<FilmDataModel?>()

    val detailFilm: LiveData<FilmDataModel?>
        get() = detailFilmLiveData
}