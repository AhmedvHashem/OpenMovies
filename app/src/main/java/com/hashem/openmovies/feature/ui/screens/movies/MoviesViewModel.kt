package com.hashem.openmovies.feature.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hashem.openmovies.feature.domain.GetNowPlayingMoviesUseCase
import com.hashem.openmovies.feature.domain.GetPopularMoviesUseCase
import com.hashem.openmovies.feature.domain.GetUpcomingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getNowPlayingMovies()
        getPopularMovies()
        getUpcomingMovies()
    }

    private fun getNowPlayingMovies() {
        _uiState.value = _uiState.value.copy(
            nowPlayingMovies = getNowPlayingMoviesUseCase().map { pagingData ->
                pagingData.map { it.toMoviesUIModel() }
            }.cachedIn(viewModelScope)
        )
    }

    private fun getPopularMovies() {
        _uiState.value = _uiState.value.copy(
            popularMovies = getPopularMoviesUseCase().map { pagingData ->
                pagingData.map { it.toMoviesUIModel() }
            }.cachedIn(viewModelScope)
        )
    }

    private fun getUpcomingMovies() {
        _uiState.value = _uiState.value.copy(
            upcomingMovies = getUpcomingMoviesUseCase().map { pagingData ->
                pagingData.map { it.toMoviesUIModel() }
            }.cachedIn(viewModelScope)
        )
    }
}
