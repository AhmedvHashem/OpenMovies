package com.hashem.openmovies.feature.domain

import com.hashem.openmovies.feature.domain.models.MovieSource
import com.hashem.openmovies.feature.domain.repository.MovieRepository
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val dispatchers: Dispatchers = DefaultDispatchers()
) {
    operator fun invoke() = repository.getMovies(MovieSource.Popular)
        .flowOn(dispatchers.io())
}