package com.hashem.openmovies.feature.domain

import com.hashem.openmovies.feature.domain.models.MovieSource
import com.hashem.openmovies.feature.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetPopularMoviesUseCase(
    private val repository: MovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke() = repository.getMovies(MovieSource.Popular)
        .flowOn(dispatcher)
}