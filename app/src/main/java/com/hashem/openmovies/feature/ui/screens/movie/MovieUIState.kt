package com.hashem.openmovies.feature.ui.screens.movie

import androidx.compose.runtime.Stable

@Stable
sealed interface MovieUIState {
    data object Loading : MovieUIState
    data class Success(val movie: MovieUIModel) : MovieUIState
    data class Error(val message: String) : MovieUIState
}
