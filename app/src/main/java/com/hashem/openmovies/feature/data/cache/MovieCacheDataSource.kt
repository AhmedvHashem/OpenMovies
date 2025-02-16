package com.hashem.openmovies.feature.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hashem.openmovies.feature.data.models.MovieData

@Dao
interface MovieCacheDataSource {

    @Query("SELECT * FROM MovieData where sources LIKE '%' || :source || '%'")
    suspend fun getMovies(source: String): List<MovieData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieData>)

    @Query("DELETE FROM MovieData")
    suspend fun clearAll()
}