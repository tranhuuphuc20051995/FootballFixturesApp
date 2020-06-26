package com.example.domain.repository

import com.example.domain.entities.DomainEntities
import io.reactivex.Single
import com.example.common.base.Result

interface CompetitionsRepository {

    fun getTodayMatches(date: String): Single<DomainEntities.MatchResponse>

    fun getAllCompetitions(): Single<DomainEntities.CompetitionResponse>

    fun getStandings(id: Long): Single<DomainEntities.StandingResponse>

    fun getSingleMatch(id: Long, date: String): Single<DomainEntities.MatchResponse>

    fun getTeam(id: Long): Single<DomainEntities.TeamResponse>

    fun getPlayers(id: Long): Single<DomainEntities.PlayerResponse>

    /**
     * Here we are return Result<DomainEntities.MatchResponse>
     * Unlike in Data Module that we return Response<DomainEntities.MatchResponse>
     * So how do we convert the Response from retrofit to Result that we need???
     * That is the job of one the util classes we called safeApiCall and
     * you will see in the [ApiDataSourceImpl] class
     */
    suspend fun getAllMatchesCoroutine(date: String): Result<DomainEntities.MatchResponse>

}