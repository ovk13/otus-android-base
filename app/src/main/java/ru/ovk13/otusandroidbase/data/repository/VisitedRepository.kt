package ru.ovk13.otusandroidbase.data.repository

object VisitedRepository {
    var visitedIds: MutableList<Int> = mutableListOf()

    fun addId(id: Int) {
        visitedIds.add(id)
    }

    fun isVisited(id: Int): Boolean {
        return visitedIds.contains(id)
    }
}