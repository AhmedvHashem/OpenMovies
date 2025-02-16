package com.hashem.openmovies.feature.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.hashem.openmovies.feature.ui.theme.OpenMoviesTheme
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>(factoryProducer = {
        MainViewModel.Factory
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenMoviesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )

                    val items = viewModel.getNowPlayingMovies().collectAsLazyPagingItems()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        items(items.itemCount) {
                            val item = items[it]
                            if (item != null) {
                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    text = item.originalTitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }

                        items.loadState.let { loadState ->
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillParentMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            CircularProgressIndicator(
                                                strokeWidth = 2.dp,
                                            )
                                        }
                                    }
                                }

                                loadState.refresh is LoadState.NotLoading && items.itemCount < 1 -> {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillParentMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
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
                                            modifier = Modifier
                                                .fillParentMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
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
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
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
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
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
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenMoviesTheme {
        Greeting("Android")
    }
}