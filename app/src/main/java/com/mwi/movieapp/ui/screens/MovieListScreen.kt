package com.mwi.movieapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mwi.movieapp.R
import com.mwi.movieapp.domain.model.Movie
import com.mwi.movieapp.ui.screens.components.EmptyScreen
import com.mwi.movieapp.ui.screens.components.ErrorScreen
import com.mwi.movieapp.ui.screens.components.PlaceholderMovieItem
import com.mwi.movieapp.ui.viewmodel.MovieListViewModel
import com.mwi.movieapp.utils.Constants
import com.mwi.movieapp.utils.Response

@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val moviesState by viewModel.movies.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold {
        Column(Modifier.padding(it)) {
            SearchBar(
                searchQuery = searchQuery,
                onQueryChanged = { viewModel.onSearchQueryChanged(it) }
            )
            when (val response = moviesState) {
                is Response.Success -> {
                    if (response.data.isNullOrEmpty()) {
                        EmptyScreen(message = stringResource(id = R.string.no_movies_found))
                    } else {
                        MovieList(movies = response.data, navController = navController)
                    }
                }

                is Response.Error -> {
                    ErrorScreen(message = response.message, onRetry = { viewModel.onRetry() })
                }

                is Response.Loading -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    ) {
                        items(10) {
                            PlaceholderMovieItem()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchQuery: String, onQueryChanged: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 13.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFE3E8EF)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onQueryChanged,
            placeholder = { Text(stringResource(id = R.string.search_movies)) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        onQueryChanged("")
                        focusManager.clearFocus()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                focusedLeadingIconColor = Color.Gray,
                unfocusedLeadingIconColor = Color.Gray,
                focusedTrailingIconColor = Color.Gray,
                unfocusedTrailingIconColor = Color.Gray
            )
        )
    }
}

@Composable
fun MovieList(movies: List<Movie>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        items(movies) {
            MovieItem(movie = it, navController = navController)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieItem(movie: Movie, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(top = 17.dp, start = 8.dp, end = 8.dp)
            .clickable { navController.navigate("movie_detail/${movie.id}") }
    ) {
        Card {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp),
                model = Constants.BASE_IMAGE_URL + movie.poster_path,
                contentScale = ContentScale.FillWidth,
                contentDescription = movie.title
            )
        }
        Text(
            text = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .basicMarquee(),
            maxLines = 1
        )
    }
}