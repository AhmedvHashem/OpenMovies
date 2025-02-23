package com.hashem.openmovies.feature.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector


sealed class AppRoute(
    open val route: String, open val label: String, open val icon: ImageVector
) {
    sealed class Movies(
        override val route: String, override val label: String, override val icon: ImageVector
    ) : AppRoute(route, label, icon) {
        data object NowPlaying : Movies("NowPlayingMovies", "Now Playing", Icons.Filled.PlayArrow)
        data object Popular : Movies("PopularMovies", "Popular", Icons.Filled.Favorite)
        data object Upcoming : Movies("UpcomingMovies", "Upcoming", Icons.Filled.DateRange)
    }

    data object Movie : AppRoute("Movie", "Movie Details", Icons.Filled.PlayArrow)
}

val navigationBarRoutes = listOf(
    AppRoute.Movies.NowPlaying, AppRoute.Movies.Popular, AppRoute.Movies.Upcoming
)

val appRoutes = navigationBarRoutes + AppRoute.Movie

