package com.hashem.openmovies.feature.data.remote

import com.hashem.openmovies.feature.data.models.MovieData
import com.hashem.openmovies.feature.data.remote.models.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieRemoteDataSource {

    @GET("movie/now_playing")
    suspend fun getNowPlayerMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en",
    ): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en",
    ): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en",
    ): MoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") id: Int,
        @Query("language") language: String = "en"
    ): MovieData
}