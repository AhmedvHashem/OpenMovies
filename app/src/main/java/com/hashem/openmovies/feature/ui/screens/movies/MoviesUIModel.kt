package com.hashem.openmovies.feature.ui.screens.movies

import androidx.compose.runtime.Immutable
import com.hashem.openmovies.feature.domain.models.Movie

@Immutable
data class MoviesUIModel(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
)

fun Movie.toMoviesUIModel() = MoviesUIModel(
    id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = posterPath,
)