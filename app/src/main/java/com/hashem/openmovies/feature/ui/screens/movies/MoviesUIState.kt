package com.hashem.openmovies.feature.ui.screens.movies

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class MoviesUIState(
    val upcomingMovies: Flow<PagingData<MoviesUIModel>> = emptyFlow(),
    val nowPlayingMovies: Flow<PagingData<MoviesUIModel>> = emptyFlow(),
    val popularMovies: Flow<PagingData<MoviesUIModel>> = emptyFlow(),
)
