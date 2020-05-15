package ru.ovk13.otusandroidbase.data.repository

import ru.ovk13.otusandroidbase.data.databse.dao.VisitedDao
import ru.ovk13.otusandroidbase.data.model.VisitedFilmModel
import ru.ovk13.otusandroidbase.domain.repository.VisitedRepository

class VisitedRepositoryImpl(
    private val visitedDao: VisitedDao
) : VisitedRepository {

    override fun getVisitedIds() = visitedDao.getVisitedIds()

    override fun addId(id: Int) = visitedDao.addVisited(VisitedFilmModel(id))

    override fun isVisited(id: Int) = visitedDao.isVisited(id)
}