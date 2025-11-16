package com.mwi.movieapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mwi.movieapp.domain.model.Movie
import com.mwi.movieapp.domain.usecase.GetMovieByIdUseCase
import com.mwi.movieapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movie = MutableStateFlow<Response<Movie>>(Response.Loading())
    val movie: StateFlow<Response<Movie>> = _movie

    init {
        fetchMovieById()
    }

    private fun fetchMovieById() {
        val movieId = savedStateHandle.get<Int>("movieId")
        movieId?.let {
            viewModelScope.launch {
                try {
                    val movie = getMovieByIdUseCase(it)
                    _movie.value = Response.Success(movie)
                } catch (e: Exception) {
                    _movie.value = Response.Error(message = e.message ?: "An error occurred")
                }
            }
        }
    }

    fun onRetry() {
        fetchMovieById()
    }
}