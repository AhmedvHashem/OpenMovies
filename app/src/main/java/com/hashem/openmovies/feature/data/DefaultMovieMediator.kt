package com.hashem.openmovies.feature.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.hashem.openmovies.feature.data.cache.MovieCacheDataSource
import com.hashem.openmovies.feature.data.models.MovieData
import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class DefaultMovieMediator(
    private val source: String,
    private val db: RoomDatabase,
    private val cacheDataSource: MovieCacheDataSource,
    private val remoteDataSource: MovieRemoteDataSource
) : RemoteMediator<Int, MovieData>() {

    private var nextPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieData>
    ): MediatorResult {
        return try {
            nextPage = when (loadType) {
                LoadType.REFRESH -> {
                    1
                }

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

            val remoteData = remoteDataSource.getNowPlayerMovies(
                page = nextPage,
            ).results.map { it.copy(sources = setOf(source)) }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    cacheDataSource.clearAll(source)
                }
                cacheDataSource.insertMovies(remoteData)
            }

            MediatorResult.Success(
                endOfPaginationReached = remoteData.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}