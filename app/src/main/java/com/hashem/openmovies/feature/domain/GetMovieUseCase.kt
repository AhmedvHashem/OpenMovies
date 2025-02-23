package com.hashem.openmovies.feature.domain

import com.hashem.openmovies.feature.domain.repository.MovieRepository

class GetMovieUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Int) = repository.getMovie(movieId)
}