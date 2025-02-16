package com.hashem.openmovies.feature.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hashem.openmovies.feature.domain.models.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class MovieData(
    @PrimaryKey
    @SerialName("id")
    val id: Int,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("release_date")
    val releaseDate: String,

    @SerialName("backdrop_path")
    val backdropPath: String,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("video")
    val video: Boolean,

    val sources: Set<String> = emptySet(),
)

fun MovieData.toMovie(): Movie {
    return Movie(
        id = id,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        overview = overview,
        releaseDate = releaseDate,

        backdropPath = backdropPath,
        posterPath = posterPath,
        video = video,
    )
}