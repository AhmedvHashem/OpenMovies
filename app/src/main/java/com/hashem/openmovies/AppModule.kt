package com.hashem.openmovies

import android.content.Context
import com.hashem.openmovies.feature.data.DefaultMovieRepository
import com.hashem.openmovies.feature.data.cache.MovieCacheDataSource
import com.hashem.openmovies.feature.data.remote.DefaultNetworkErrorHandler
import com.hashem.openmovies.feature.data.remote.MovieRemoteDataSource
import com.hashem.openmovies.feature.data.remote.NetworkErrorHandler
import com.hashem.openmovies.feature.domain.DefaultDispatchers
import com.hashem.openmovies.feature.domain.Dispatchers
import com.hashem.openmovies.feature.domain.repository.MovieRepository
import com.hashem.openmovies.feature.framework.database.OpenMoviesDatabase
import com.hashem.openmovies.feature.framework.network.Converter
import com.hashem.openmovies.feature.framework.network.OpenMoviesNetwork
import com.hashem.openmovies.feature.framework.network.interceptors.AuthInterceptor
import com.hashem.openmovies.feature.framework.network.interceptors.LogInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindMovieRepository(
        defaultMovieRepository: DefaultMovieRepository
    ): MovieRepository

    @Binds
    abstract fun bindDispatchers(
        defaultDispatchers: DefaultDispatchers
    ): Dispatchers

    @Binds
    abstract fun bindNetworkErrorHandler(
        defaultNetworkErrorHandler: DefaultNetworkErrorHandler
    ): NetworkErrorHandler
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    fun providesGetMovieUseCase(repository: MovieRepository): GetMovieUseCase {
//        return GetMovieUseCase(repository)
//    }
//
//    @Provides
//    @Singleton
//    fun providesMovieRepository(
//        db: OpenMoviesDatabase,
//        cacheDataSource: MovieCacheDataSource,
//        remoteDataSource: MovieRemoteDataSource,
//        networkErrorHandler: NetworkErrorHandler
//    ): MovieRepository {
//        return DefaultMovieRepository(db, cacheDataSource, remoteDataSource, networkErrorHandler)
//    }
//
//    @Provides
//    @Singleton
//    fun providesNetworkErrorHandler(): NetworkErrorHandler {
//        return DefaultNetworkErrorHandler()
//    }


    @Provides
    @Singleton
    fun providesMovieCacheDataSource(database: OpenMoviesDatabase): MovieCacheDataSource {
        return database.dataSource()
    }

    @Provides
    @Singleton
    fun providesOpenMoviesDatabase(
        @ApplicationContext
        context: Context
    ): OpenMoviesDatabase {
        return OpenMoviesDatabase.provideDatabase(context)
    }

    @Provides
    @Singleton
    fun providesMovieRemoteDataSource(openMoviesNetwork: OpenMoviesNetwork): MovieRemoteDataSource {
        return openMoviesNetwork.dataSource()
    }

    @Provides
    fun providesOpenMoviesNetwork(retrofit: Retrofit): OpenMoviesNetwork {
        return OpenMoviesNetwork.provideNetwork(retrofit)
    }

    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Converter.json())
            .build()
    }

    @Provides
    fun providesOkHttpClient(
        logInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .addInterceptor(authInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    fun providesAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(Constants.API_KEY)
    }

    @Provides
    fun providesLogInterceptor(): HttpLoggingInterceptor {
        return LogInterceptor.debug()
    }
}