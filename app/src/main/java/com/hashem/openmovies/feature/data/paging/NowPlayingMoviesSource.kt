package com.hashem.openmovies.feature.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hashem.openmovies.feature.data.models.toMovie
import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import com.hashem.openmovies.feature.domain.models.Movie
import retrofit2.HttpException
import java.io.IOException

class NowPlayingMoviesSource(private val api: MovieRemoteDataSource) :
    PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val nowPlayingMovies = api.getNowPlayerMovies(currentPage)
            LoadResult.Page(
                data = nowPlayingMovies.results.map { it.toMovie() },
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (nowPlayingMovies.results.isEmpty()) null else nowPlayingMovies.page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}