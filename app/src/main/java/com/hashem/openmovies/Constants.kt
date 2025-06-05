package com.hashem.openmovies

import com.hashem.constants.Constants

object Constants {
    val API_KEY = Constants.getApiKey()

    const val DATABASE_NAME = "open-movies-database"
    const val DATABASE_VERSION = 1

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
}
