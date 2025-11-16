package com.mwi.movieapp.domain.usecase

import com.mwi.movieapp.domain.model.Movie
import com.mwi.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend operator fun invoke(query: String): List<Movie> {
        return repository.searchMovies(query)
    }
}