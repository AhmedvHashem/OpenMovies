package com.hashem.openmovies.feature.domain.repository

import androidx.paging.PagingData
import com.hashem.openmovies.feature.domain.models.Movie
import com.hashem.openmovies.feature.domain.models.MovieSource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(source: MovieSource): Flow<PagingData<Movie>>
}