package com.hashem.openmovies.feature.data.remote

import com.hashem.openmovies.feature.domain.repository.MovieError
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface NetworkErrorHandler {
    suspend fun <T> handle(block: suspend () -> T): Result<T>
}

class DefaultNetworkErrorHandler @Inject constructor() : NetworkErrorHandler {

    override suspend fun <T> handle(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: IOException) {
            Result.failure(MovieError.NetworkError)
        } catch (e: HttpException) {
            if (e.code() == 404)
                Result.failure(MovieError.NotFoundError)
            else
                Result.failure(MovieError.ApiError)
        } catch (e: SerializationException) {
            Result.failure(MovieError.ApiError)
        } catch (e: Exception) {
            Result.failure(MovieError.UnknownError(e.message.toString()))
        }
    }
}

class DefaultNetworkErrorHandler2 @Inject constructor() : NetworkErrorHandler {

    override suspend fun <T> handle(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: IOException) {
            Result.failure(MovieError.NetworkError)
        } catch (e: HttpException) {
            if (e.code() == 404)
                Result.failure(MovieError.NotFoundError)
            else
                Result.failure(MovieError.ApiError)
        } catch (e: SerializationException) {
            Result.failure(MovieError.ApiError)
        } catch (e: Exception) {
            Result.failure(MovieError.UnknownError(e.message.toString()))
        }
    }
}