package com.hashem.openmovies.feature.ui.screens.movie

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hashem.openmovies.feature.data.DefaultMovieRepository
import com.hashem.openmovies.feature.domain.GetMovieUseCase
import com.hashem.openmovies.feature.framework.database.OpenMoviesDatabase
import com.hashem.openmovies.feature.framework.network.OpenMoviesNetwork
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel(
    private val getMovieUseCase: GetMovieUseCase,
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

                MovieViewModel(
                    GetMovieUseCase(repo),
                )
            }
        }
    }

    private val _uiState = MutableStateFlow<MovieUIState>(MovieUIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            val result = getMovieUseCase(movieId)
            result.onSuccess { movie ->
                _uiState.value = MovieUIState.Success(movie)
            }.onFailure {
                _uiState.value = MovieUIState.Error(it.message ?: "Unknown error")
            }
        }
    }
}
