package com.hashem.openmovies.feature.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hashem.openmovies.feature.data.cache.MovieCacheDataSource
import com.hashem.openmovies.feature.data.paging.NowPlayingMoviesSource
import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import com.hashem.openmovies.feature.domain.models.Movie
import com.hashem.openmovies.feature.domain.models.MovieSource
import com.hashem.openmovies.feature.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class DefaultMovieRepository(
    private val cacheDataSource: MovieCacheDataSource,
    private val remoteDataSource: MovieRemoteDataSource
) : MovieRepository {

    override fun getMovies(source: MovieSource): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                when (source) {
                    MovieSource.NowPlaying -> NowPlayingMoviesSource(remoteDataSource)
                    MovieSource.Popular -> TODO()
                    MovieSource.Upcoming -> TODO()
                }
            }
        ).flow
    }
}