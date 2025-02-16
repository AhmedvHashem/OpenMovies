package com.hashem.openmovies.feature.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hashem.openmovies.feature.data.DefaultMovieRepository
import com.hashem.openmovies.feature.domain.GetNowPlayingMoviesUseCase
import com.hashem.openmovies.feature.domain.GetPopularMoviesUseCase
import com.hashem.openmovies.feature.domain.GetUpcomingMoviesUseCase
import com.hashem.openmovies.feature.framework.database.OpenMoviesDatabase
import com.hashem.openmovies.feature.framework.network.OpenMoviesNetwork

class MainViewModel(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application

                val db = OpenMoviesDatabase.getInstance(context)
                val network = OpenMoviesNetwork.getInstance()

                val cache = db.dataSource()
                val remote = network.dataSource()
                val repo = DefaultMovieRepository(cache, remote)

                MainViewModel(
                    GetNowPlayingMoviesUseCase(repo),
                    GetPopularMoviesUseCase(repo),
                    GetUpcomingMoviesUseCase(repo)
                )
            }
        }
    }

    fun getNowPlayingMovies() = getNowPlayingMoviesUseCase()
}
