package com.hashem.openmovies.feature.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hashem.openmovies.feature.data.models.MovieData

@Dao
interface MovieCacheDataSource {

    @Query("SELECT * FROM MovieData where sources LIKE '%' || :source || '%'")
    fun getMovies(source: String): PagingSource<Int, MovieData>

    @Upsert
    suspend fun insertMovies(movies: List<MovieData>)

    @Query("DELETE FROM MovieData where sources LIKE '%' || :source || '%'")
    suspend fun clearAll(source: String)
}