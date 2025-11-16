package com.mwi.movieapp.data.repository

import com.mwi.movieapp.data.local.MovieDao
import com.mwi.movieapp.data.remote.MovieApiService
import com.mwi.movieapp.domain.model.Movie
import com.mwi.movieapp.domain.repository.MovieRepository
import com.mwi.movieapp.utils.NetworkMonitor
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    private val movieDao: MovieDao,
    private val networkMonitor: NetworkMonitor
) : MovieRepository {

    override suspend fun getTrendingMovies(): List<Movie> {
        return if (networkMonitor.isConnected()) {
            val remoteMovies = movieApiService.fetchTrendingMovies().results
            movieDao.insertMovies(remoteMovies)
            remoteMovies
        } else {
            movieDao.getMovies()
        }
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        return movieDao.searchMovies(query)
    }

    override suspend fun getMovieById(movieId: Int): Movie {
        return movieDao.getMovieById(movieId)
    }
}