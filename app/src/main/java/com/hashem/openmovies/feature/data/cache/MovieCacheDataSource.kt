package com.hashem.openmovies.feature.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hashem.openmovies.feature.data.models.MovieData

@Dao
interface MovieCacheDataSource {

    @Query("SELECT * FROM MovieData")
    suspend fun getWords(): List<MovieData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWord(movieData: MovieData)
}