package ru.ovk13.otusandroidbase.presentation.filmdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ovk13.otusandroidbase.domain.usecase.FilmsUseCase


class FilmDetailViewModelFactory(
    private val filmsUseCase: FilmsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmDetailViewModel::class.java)) {
            return FilmDetailViewModel(filmsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}