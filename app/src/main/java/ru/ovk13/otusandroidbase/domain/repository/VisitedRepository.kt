package ru.ovk13.otusandroidbase.domain.repository

interface VisitedRepository {
    fun getVisitedIds(): MutableList<Int>
    fun addId(id: Int)
    fun isVisited(id: Int): Boolean
}