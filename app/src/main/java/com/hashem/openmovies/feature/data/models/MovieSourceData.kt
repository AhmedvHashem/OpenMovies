package com.hashem.openmovies.feature.data.models

import com.hashem.openmovies.feature.domain.models.MovieSource

enum class MovieSourceData {
    NowPlaying,
    Popular,
    Upcoming
}

fun MovieSource.toMovieSourceData(): MovieSourceData {
    return when (this) {
        MovieSource.NowPlaying -> MovieSourceData.NowPlaying
        MovieSource.Popular -> MovieSourceData.Popular
        MovieSource.Upcoming -> MovieSourceData.Upcoming
    }
}