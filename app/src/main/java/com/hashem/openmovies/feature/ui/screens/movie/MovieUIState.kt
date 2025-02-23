package com.hashem.openmovies.feature.ui.screens.movie

import androidx.compose.runtime.Stable
import com.hashem.openmovies.feature.domain.models.Movie

@Stable
sealed interface MovieUIState {
    data object Loading : MovieUIState
    data class Success(val movie: Movie) : MovieUIState
    data class Error(val message: String) : MovieUIState
}
