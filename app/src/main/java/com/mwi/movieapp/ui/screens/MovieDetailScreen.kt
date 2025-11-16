package com.mwi.movieapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mwi.movieapp.R
import com.mwi.movieapp.ui.screens.components.ErrorScreen
import com.mwi.movieapp.ui.screens.components.PlaceholderMovieDetail
import com.mwi.movieapp.ui.viewmodel.MovieDetailViewModel
import com.mwi.movieapp.utils.Constants
import com.mwi.movieapp.utils.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val movieState by viewModel.movie.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Sharp.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        }
    ) {

        Column(modifier = Modifier.padding(it)) {
            when (val response = movieState) {
                is Response.Success -> {
                    if (response.data != null) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .height(361.dp)
                            ) {
                                AsyncImage(
                                    model = Constants.BASE_IMAGE_URL + response.data.poster_path,
                                    contentDescription = response.data.title,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Text(
                                text = response.data.title,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(16.dp),
                                fontSize = 24.sp
                            )
                            Text(
                                text = response.data.overview,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                is Response.Error -> {
                    ErrorScreen(message = response.message, onRetry = { viewModel.onRetry() })
                }

                is Response.Loading -> {
                    PlaceholderMovieDetail()
                }
            }
        }
    }
}