package ru.ovk13.otusandroidbase.presentation.filmslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ovk13.otusandroidbase.domain.usecase.GetFilmsListUseCase


class FilmsListViewModelFactory(
    private val getFilmsListUseCase: GetFilmsListUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmsListViewModel::class.java)) {
            return FilmsListViewModel(getFilmsListUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}