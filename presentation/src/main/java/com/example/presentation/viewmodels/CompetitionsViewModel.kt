package com.example.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.toLiveData
import com.example.domain.usecases.competition.GetCompetitionsUseCase
import com.example.domain.usecases.competition.GetTodayFixturesUseCase
import com.example.domain.usecases.competition.GetTodayFixturesUseCaseCoroutine
import com.example.presentation.mappers.map
import com.example.presentation.models.CompetitionResponse
import com.example.presentation.models.MatchResponse
import com.example.presentation.models.Resource
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.functions.Function
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.common.base.Result

class CompetitionsViewModel @Inject constructor(
    private val getTodayFixturesUseCase: GetTodayFixturesUseCase,
    private val getCompetitionsUseCase: GetCompetitionsUseCase,
    private val getTodayFixturesUseCaseCoroutine: GetTodayFixturesUseCaseCoroutine
) : ViewModel() {

    fun getAllMatches(date: String): LiveData<Resource<MatchResponse>> {
        return getTodayFixturesUseCase
            .run(date)
            .map { Resource.success(it.map()) }
            .toObservable()
            .startWith(Resource.loading())
            .onErrorResumeNext(
                Function {
                    Observable.just(Resource.error(msg = "An Error Occurred", data = null))
                }
            )
            .toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
    }

    /**
     * Coroutine style
     *
     * the keyword emit is like the word return
     */
    fun getAllMatchesCoroutine(date: String): LiveData<Resource<MatchResponse>>
            = liveData(Dispatchers.IO) {
        //emit is the word to use when providing a value
        emit(Resource.loading())

        val result = getTodayFixturesUseCaseCoroutine.invoke(date)
        when (result) {
            is Result.Success -> {
                emit(Resource.success(result.data?.map()))
            }
            is Result.Error -> {
                emit(Resource.error(msg = "An Error Occurred", data = null))
            }
        }
    }

    fun getAllCompetitions(): LiveData<Resource<CompetitionResponse>> {
        return getCompetitionsUseCase
            .run()
            .map { Resource.success(it.map()) }
            .toObservable()
            .startWith(Resource.loading())
            .onErrorResumeNext(
                Function {
                    Observable.just(Resource.error(msg = "An Error Occurred", data = null))
                }
            )
            .toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
    }

}
