package com.hashem.openmovies.feature.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hashem.openmovies.feature.ui.components.AppNavigationBar
import com.hashem.openmovies.feature.ui.screens.movie.MovieScreen
import com.hashem.openmovies.feature.ui.screens.movies.MoviesScreen
import com.hashem.openmovies.feature.ui.theme.OpenMoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenMoviesTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                val showBottomBar = currentRoute in navigationBarRoutes.map { it.route }
                val onMovieClick: (Int) -> Unit = { movieId ->
                    navController.navigate("${AppRoute.Movie.route}/$movieId")
                }
                val onBackClick: () -> Unit = {
                    navController.popBackStack()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AnimatedVisibility(
                            visible = showBottomBar,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            CenterAlignedTopAppBar(title = {
                                Text(
                                    text = "Open Movies",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            })
                        }
                    },
                    bottomBar = {
                        AnimatedVisibility(
                            visible = showBottomBar,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            AppNavigationBar(
                                navController = navController,
                                currentRoute = currentRoute,
                                routes = navigationBarRoutes
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AppRoute.Movies.NowPlaying.route,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable(route = AppRoute.Movies.NowPlaying.route) {
                            MoviesScreen(
                                route = AppRoute.Movies.NowPlaying, onMovieClick = onMovieClick
                            )
                        }
                        composable(route = AppRoute.Movies.Popular.route) {
                            MoviesScreen(
                                route = AppRoute.Movies.Popular, onMovieClick = onMovieClick
                            )
                        }
                        composable(route = AppRoute.Movies.Upcoming.route) {
                            MoviesScreen(
                                route = AppRoute.Movies.Upcoming, onMovieClick = onMovieClick
                            )
                        }
                        composable(
                            route = "${AppRoute.Movie.route}/{movieId}",
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) {
                            val movieId: Int = backStackEntry?.arguments?.getInt("movieId") ?: -1

                            MovieScreen(
                                movieId = movieId,
                                onBackClick = onBackClick
                            )
                        }
                    }
                }
            }
        }
    }
}
