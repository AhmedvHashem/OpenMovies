package com.hashem.openmovies.feature.ui.screens.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hashem.openmovies.feature.domain.GetMovieUseCase
import com.hashem.openmovies.feature.domain.repository.MovieError
import com.hashem.openmovies.feature.ui.components.toAppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUIState>(MovieUIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            val result = getMovieUseCase(movieId)
            result.onSuccess { movie ->
                _uiState.value = MovieUIState.Success(movie.toMovieUIModel())
            }.onFailure {
                it as MovieError
                _uiState.value = MovieUIState.Error(it.toAppError())
            }
        }
    }
}
