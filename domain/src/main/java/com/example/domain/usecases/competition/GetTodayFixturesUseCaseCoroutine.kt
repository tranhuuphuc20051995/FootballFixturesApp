package com.example.domain.usecases.competition

import com.example.domain.entities.DomainEntities
import com.example.domain.repository.CompetitionsRepository
import javax.inject.Inject
import com.example.common.base.Result

/**
 * This is the usecase sample.. no extend base class or inject Background thread or whatsoever
 */
class GetTodayFixturesUseCaseCoroutine @Inject constructor(
    private val competitionsRepository: CompetitionsRepository
) {
    suspend operator fun invoke(input: String): Result<DomainEntities.MatchResponse> {
        return competitionsRepository.getAllMatchesCoroutine(input)
    }
}