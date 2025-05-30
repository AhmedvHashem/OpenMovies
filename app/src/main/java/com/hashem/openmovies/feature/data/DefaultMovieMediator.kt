package com.hashem.openmovies.feature.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.hashem.openmovies.feature.data.cache.MovieCacheDataSource
import com.hashem.openmovies.feature.data.models.MovieData
import com.hashem.openmovies.feature.data.models.MovieSourceData
import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import com.hashem.openmovies.feature.data.remote.NetworkErrorHandler
import java.lang.IllegalStateException

@OptIn(ExperimentalPagingApi::class)
class DefaultMovieMediator(
    private val source: MovieSourceData,
    private val db: RoomDatabase,
    private val cacheDataSource: MovieCacheDataSource,
    private val remoteDataSource: MovieRemoteDataSource,
    private val networkErrorHandler: NetworkErrorHandler
) : RemoteMediator<Int, MovieData>() {

    private var nextPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieData>
    ): MediatorResult {
        return try {
            nextPage = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = false
                        )

                    ++nextPage
                }
            }

            val remoteData = networkErrorHandler.handle {
                when (source) {
                    MovieSourceData.NowPlaying -> remoteDataSource.getNowPlayerMovies(
                        page = nextPage,
                    )

                    MovieSourceData.Popular -> remoteDataSource.getPopularMovies(
                        page = nextPage,
                    )

                    MovieSourceData.Upcoming -> remoteDataSource.getUpcomingMovies(
                        page = nextPage,
                    )
                }.results.map { it.copy(sources = setOf(source.toString())) }
            }.getOrThrow()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    cacheDataSource.clearAll(source.toString())
                }
                cacheDataSource.insertMovies(remoteData)
            }

            MediatorResult.Success(
                endOfPaginationReached = remoteData.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}