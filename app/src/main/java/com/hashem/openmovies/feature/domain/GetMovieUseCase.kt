package com.hashem.openmovies.feature.domain

import com.hashem.openmovies.feature.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Int) = repository.getMovie(movieId)
}