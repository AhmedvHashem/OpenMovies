package com.hashem.openmovies.feature.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hashem.openmovies.feature.data.cache.MovieCacheDataSource
import com.hashem.openmovies.feature.data.models.toMovie
import com.hashem.openmovies.feature.data.models.toMovieSourceData
import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import com.hashem.openmovies.feature.data.remote.NetworkErrorHandler
import com.hashem.openmovies.feature.domain.models.Movie
import com.hashem.openmovies.feature.domain.models.MovieSource
import com.hashem.openmovies.feature.domain.repository.MovieRepository
import com.hashem.openmovies.feature.framework.database.OpenMoviesDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultMovieRepository @Inject constructor(
    private val db: OpenMoviesDatabase,
    private val cacheDataSource: MovieCacheDataSource,
    private val remoteDataSource: MovieRemoteDataSource,
    private val networkErrorHandler: NetworkErrorHandler
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(source: MovieSource): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = DefaultMovieMediator(
                source.toMovieSourceData(),
                db,
                cacheDataSource,
                remoteDataSource,
                networkErrorHandler
            ),
            pagingSourceFactory = { cacheDataSource.getMovies(source.toString()) }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
        }
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        return networkErrorHandler.handle {
            remoteDataSource.getMovie(id).toMovie()
        }
    }
}