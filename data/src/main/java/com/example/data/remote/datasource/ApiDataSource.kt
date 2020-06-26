package com.example.data.remote.datasource

import com.example.data.models.*
import com.example.domain.entities.DomainEntities
import io.reactivex.Single
import com.example.common.base.Result

interface ApiDataSource {

    fun getAllMatches(date: String): Single<DataMatchResponse>

    fun getAllCompetitions(): Single<DataCompetitionResponse>

    fun getStandings(id: Long): Single<DataStandingResponse>

    fun getSingleMatch(id: Long, date: String): Single<DataMatchResponse>

    fun getTeam(id: Long): Single<DataTeamResponse>

    fun getPlayers(id: Long): Single<DataPlayerResponse>

    //Coroutine

    /**
     * Here we are return Result<DomainEntities.MatchResponse>
     * Unlike in Data Module that we return Response<DomainEntities.MatchResponse>
     * So how do we convert the Response from retrofit to Result that we need???
     * That is the job of one the util classes we called safeApiCall and
     * you will see in the [ApiDataSourceImpl] class
     */
    suspend fun getAllMatchesCoroutine(date: String): Result<DomainEntities.MatchResponse>

}