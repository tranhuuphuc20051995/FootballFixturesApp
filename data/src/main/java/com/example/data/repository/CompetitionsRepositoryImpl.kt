package com.example.data.repository

import com.example.data.local.mapper.map
import com.example.data.remote.datasource.ApiDataSource
import com.example.domain.entities.DomainEntities
import com.example.domain.repository.CompetitionsRepository
import io.reactivex.Single
import com.example.common.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompetitionsRepositoryImpl (private val apiDataSource: ApiDataSource): CompetitionsRepository {

    override fun getTodayMatches(date: String): Single<DomainEntities.MatchResponse> {
        return apiDataSource.getAllMatches(date).map { it.map() }
    }

    override fun getAllCompetitions(): Single<DomainEntities.CompetitionResponse> {
        return apiDataSource.getAllCompetitions().map { it.map() }
    }

    override fun getStandings(id: Long): Single<DomainEntities.StandingResponse> {
        return apiDataSource.getStandings(id).map { it.map() }
    }

    override fun getSingleMatch(id: Long, date: String): Single<DomainEntities.MatchResponse> {
        return apiDataSource.getSingleMatch(id, date).map { it.map() }
    }

    override fun getTeam(id: Long): Single<DomainEntities.TeamResponse> {
        return apiDataSource.getTeam(id).map { it.map() }
    }

    override fun getPlayers(id: Long): Single<DomainEntities.PlayerResponse> {
        return apiDataSource.getPlayers(id).map { it.map() }
    }

    /**
     * This is the connection between the Data module and domain module
     * So at this point you tell coroutine how you want the call done..
     *  Dispatchers.Default
     *  Dispatchers.IO
     *  Dispatchers.Main
     *  Dispatchers.Unconfined
     *  Please read them up, but since we want it on the background we use IO
     *
     *   withContext is the CoroutineScope you using, we have diff types to
     *   withContext
     *   launch
     *   viewModelScope
     *   Read up too
     *
     *   we would have done .map() or .map{ } but coroutine does not support that...
     *   that is why we using the DomainEntities straight
     */
    override suspend fun getAllMatchesCoroutine(date: String): Result<DomainEntities.MatchResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext apiDataSource.getAllMatchesCoroutine(date)
        }
    }

}