package com.hashem.openmovies.feature.domain.models

data class Movie(
    val id: Int,
    val originalTitle: String,
    val originalLanguage: String,
    val overview: String,
    val releaseDate: String,

    val backdropPath: String,
    val posterPath: String,
    val video: Boolean,
)