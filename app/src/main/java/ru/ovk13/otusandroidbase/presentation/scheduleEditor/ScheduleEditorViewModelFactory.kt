package ru.ovk13.otusandroidbase.presentation.scheduleEditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ovk13.otusandroidbase.domain.usecase.FilmsUseCase
import ru.ovk13.otusandroidbase.domain.usecase.ScheduleUseCase


class ScheduleEditorViewModelFactory(
    private val filmsUseCase: FilmsUseCase,
    private val scheduleUseCase: ScheduleUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleEditorViewModel::class.java)) {
            return ScheduleEditorViewModel(filmsUseCase, scheduleUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}