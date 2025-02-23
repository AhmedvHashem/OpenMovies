package com.hashem.openmovies.feature.ui.screens.movies

import androidx.paging.PagingData
import com.hashem.openmovies.feature.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MoviesUIState(
    val upcomingMovies: Flow<PagingData<Movie>> = emptyFlow(),
    val nowPlayingMovies: Flow<PagingData<Movie>> = emptyFlow(),
    val popularMovies: Flow<PagingData<Movie>> = emptyFlow(),
)
