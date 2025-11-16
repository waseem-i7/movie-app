package com.mwi.movieapp.domain.repository

import com.mwi.movieapp.domain.model.Movie

interface MovieRepository {
    suspend fun getTrendingMovies(): List<Movie>
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun getMovieById(movieId: Int): Movie
}