package com.hashem.openmovies.feature.ui.screens.movie

import androidx.compose.runtime.Immutable
import com.hashem.openmovies.feature.domain.models.Movie

@Immutable
data class MovieUIModel(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,

    val posterPath: String,

    val runtime: Int = 0,
    val genres: List<String>
)

fun Movie.toMovieUIModel() = MovieUIModel(
    id = id,
    title = title,
    overview = overview,
    releaseDate = releaseDate,

    posterPath = posterPath,

    runtime = runtime,
    genres = genres
)