package com.hashem.openmovies.feature.framework.network

import com.hashem.openmovies.Constants
import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import com.hashem.openmovies.feature.framework.network.interceptors.AuthInterceptor
import com.hashem.openmovies.feature.framework.network.interceptors.LogInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class OpenMoviesNetwork {
    abstract fun dataSource(): MovieRemoteDataSource

    companion object {

        @Volatile
        private var INSTANCE: OpenMoviesNetwork? = null
        fun getInstance(): OpenMoviesNetwork {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: provideNetwork().also { INSTANCE = it }
            }
        }


        private fun provideNetwork(): OpenMoviesNetwork {
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(LogInterceptor.debug())
                .addInterceptor(AuthInterceptor(Constants.API_TOKEN))
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(Converter.json())
                .build()

            return object : OpenMoviesNetwork() {
                override fun dataSource(): MovieRemoteDataSource {
                    return retrofit.create(MovieRemoteDataSource::class.java)
                }
            }
        }
    }
}