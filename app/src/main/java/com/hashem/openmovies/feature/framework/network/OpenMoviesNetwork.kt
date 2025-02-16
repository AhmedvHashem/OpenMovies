package com.hashem.openmovies.feature.framework.network

import com.hashem.openmovies.Constants
import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

abstract class OpenMoviesNetwork {
    abstract fun dataSource(): MovieRemoteDataSource

    companion object {
        private const val BASE_URL = Constants.BASE_URL

        @Volatile
        private var INSTANCE: OpenMoviesNetwork? = null
        fun getInstance(): OpenMoviesNetwork {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: provideNetwork().also { INSTANCE = it }
            }
        }


        private fun provideNetwork(): OpenMoviesNetwork {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
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