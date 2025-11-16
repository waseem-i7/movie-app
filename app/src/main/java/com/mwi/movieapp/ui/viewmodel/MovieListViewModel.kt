package com.mwi.movieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mwi.movieapp.domain.model.Movie
import com.mwi.movieapp.domain.usecase.GetTrendingMoviesUseCase
import com.mwi.movieapp.domain.usecase.SearchMoviesUseCase
import com.mwi.movieapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<Response<List<Movie>>>(Response.Loading())
    val movies: StateFlow<Response<List<Movie>>> = _movies

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        observeSearchQuery()
        fetchTrendingMovies()
    }

    private fun observeSearchQuery() {
        searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isEmpty()) {
                    fetchTrendingMovies()
                } else {
                    searchMovies(query)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            try {
                val movies = getTrendingMoviesUseCase()
                _movies.value = Response.Success(movies)
            } catch (e: Exception) {
                _movies.value = Response.Error(message = e.message ?: "An error occurred")
            }
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                val movies = searchMoviesUseCase(query)
                _movies.value = Response.Success(movies)
            } catch (e: Exception) {
                _movies.value = Response.Error(message = e.message ?: "An error occurred")
            }
        }
    }

    fun onRetry() {
        if (searchQuery.value.isEmpty()) {
            fetchTrendingMovies()
        } else {
            searchMovies(searchQuery.value)
        }
    }
}