package ru.ovk13.otusandroidbase.presentation.favouritefilmslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ovk13.otusandroidbase.domain.usecase.FavouritesUseCase
import ru.ovk13.otusandroidbase.domain.usecase.VisitedUseCase


class FavouriteFilmsListViewModelFactory(
    private val favouritesUseCase: FavouritesUseCase,
    private val visitedUseCase: VisitedUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteFilmsListViewModel::class.java)) {
            return FavouriteFilmsListViewModel(favouritesUseCase, visitedUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}