package com.hashem.openmovies.feature.framework.network

import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import retrofit2.Retrofit

abstract class OpenMoviesNetwork {
    abstract fun dataSource(): MovieRemoteDataSource

    companion object {
        fun provideNetwork(retrofit: Retrofit): OpenMoviesNetwork {
            return object : OpenMoviesNetwork() {
                override fun dataSource(): MovieRemoteDataSource {
                    return retrofit.create(MovieRemoteDataSource::class.java)
                }
            }
        }
    }
}