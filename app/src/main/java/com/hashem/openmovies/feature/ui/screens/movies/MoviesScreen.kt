package com.hashem.openmovies.feature.ui.screens.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hashem.openmovies.Constants
import com.hashem.openmovies.feature.ui.AppRoute
import retrofit2.HttpException
import java.io.IOException

@Composable
fun MoviesScreen(
    route: AppRoute.Movies,
    viewModel: MoviesViewModel = viewModel<MoviesViewModel>(factory = MoviesViewModel.Factory),
    onMovieClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val movies = when (route) {
        AppRoute.Movies.NowPlaying -> uiState.nowPlayingMovies.collectAsLazyPagingItems()
        AppRoute.Movies.Popular -> uiState.popularMovies.collectAsLazyPagingItems()
        AppRoute.Movies.Upcoming -> uiState.upcomingMovies.collectAsLazyPagingItems()
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(movies.itemCount) {
            val movie = movies[it]
            if (movie != null) {
                Card(
                    modifier = Modifier
                        .height(250.dp)
                        .padding(8.dp)
                        .clickable { onMovieClick(movie.id) },
                ) {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(Constants.IMAGE_BASE_URL + movie.posterPath).crossfade(true)
                                .build(),
                            contentDescription = movie.originalTitle,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .fillMaxWidth()
                                .height(250.dp),
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            Pair(0.3f, Color.Transparent), Pair(
                                                1.5f, MaterialTheme.colorScheme.background
                                            )
                                        )
                                    )
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = movie.originalTitle,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                                Text(
                                    text = movie.releaseDate,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                )
                            }
                        }
                    }
                }
            }
        }

        movies.loadState.let { loadState ->
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                            )
                        }
                    }
                }

                loadState.refresh is LoadState.NotLoading && movies.itemCount < 1 -> {
                    item {
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "No data available",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    item {
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = when ((loadState.refresh as LoadState.Error).error) {
                                    is HttpException -> {
                                        "Oops, something went wrong!"
                                    }

                                    is IOException -> {
                                        "Couldn't reach server, check your internet connection!"
                                    }

                                    else -> {
                                        "Unknown error occurred"
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.Center),
                                strokeWidth = 2.dp,
                            )
                        }
                    }
                }

                loadState.append is LoadState.Error -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "An error occurred",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}
