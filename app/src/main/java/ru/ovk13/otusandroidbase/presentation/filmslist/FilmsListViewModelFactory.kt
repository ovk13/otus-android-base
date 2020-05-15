package ru.ovk13.otusandroidbase.presentation.filmslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ovk13.otusandroidbase.domain.usecase.FavouritesUseCase
import ru.ovk13.otusandroidbase.domain.usecase.FilmsUseCase
import ru.ovk13.otusandroidbase.domain.usecase.VisitedUseCase


class FilmsListViewModelFactory(
    private val filmsUseCase: FilmsUseCase,
    private val favouritesUseCase: FavouritesUseCase,
    private val visitedUseCase: VisitedUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmsListViewModel::class.java)) {
            return FilmsListViewModel(filmsUseCase, favouritesUseCase, visitedUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}