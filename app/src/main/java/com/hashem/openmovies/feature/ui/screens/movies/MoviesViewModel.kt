package com.hashem.openmovies.feature.ui.screens.movies

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import androidx.paging.map
import com.hashem.openmovies.feature.data.DefaultMovieRepository
import com.hashem.openmovies.feature.domain.GetNowPlayingMoviesUseCase
import com.hashem.openmovies.feature.domain.GetPopularMoviesUseCase
import com.hashem.openmovies.feature.domain.GetUpcomingMoviesUseCase
import com.hashem.openmovies.feature.framework.database.OpenMoviesDatabase
import com.hashem.openmovies.feature.framework.network.OpenMoviesNetwork
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MoviesViewModel(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application

                val db = OpenMoviesDatabase.getInstance(context)
                val network = OpenMoviesNetwork.getInstance()

                val cache = db.dataSource()
                val remote = network.dataSource()
                val repo = DefaultMovieRepository(db, cache, remote)

                MoviesViewModel(
                    GetNowPlayingMoviesUseCase(repo),
                    GetPopularMoviesUseCase(repo),
                    GetUpcomingMoviesUseCase(repo)
                )
            }
        }
    }

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
