package ru.ovk13.otusandroidbase.domain.usecase

import ru.ovk13.otusandroidbase.domain.repository.VisitedRepository
import java.util.concurrent.Executors

class VisitedUseCase(
    private val visitedRepository: VisitedRepository
) {
    fun getVisitedIds(callback: GetVisitedCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                callback.onSuccess(visitedRepository.getVisitedIds())
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    fun addVisited(id: Int, callback: AddVisitedCallback) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            try {
                if (!visitedRepository.isVisited(id)) {
                    visitedRepository.addId(id)
                }
            } catch (e: Throwable) {
                callback.onError(e)
            }
        })
    }

    interface AddVisitedCallback {
        fun onError(e: Throwable)
    }

    interface GetVisitedCallback {
        fun onSuccess(visitedIds: List<Int>)
        fun onError(e: Throwable)
    }
}