package ru.ovk13.otusandroidbase.presentation.favouritefilmslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class FavouriteFilmsListViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteFilmsListViewModel::class.java)) {
            return FavouriteFilmsListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}