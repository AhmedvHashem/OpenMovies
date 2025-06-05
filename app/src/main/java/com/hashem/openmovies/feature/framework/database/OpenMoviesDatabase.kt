package com.hashem.openmovies.feature.framework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hashem.openmovies.Constants
import com.hashem.openmovies.feature.data.cache.MovieCacheDataSource
import com.hashem.openmovies.feature.data.models.MovieData


@Database(entities = [MovieData::class], exportSchema = false, version = Constants.DATABASE_VERSION)
@TypeConverters(Converter::class)
abstract class OpenMoviesDatabase : RoomDatabase() {
    abstract fun dataSource(): MovieCacheDataSource

    companion object {
        private const val DATABASE_NAME = Constants.DATABASE_NAME

        @Volatile
        private var INSTANCE: OpenMoviesDatabase? = null
        fun getInstance(context: Context): OpenMoviesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: provideDatabase(context).also { INSTANCE = it }
            }
        }

        private fun provideDatabase(context: Context): OpenMoviesDatabase {
            return Room.databaseBuilder(
                context,
                OpenMoviesDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}