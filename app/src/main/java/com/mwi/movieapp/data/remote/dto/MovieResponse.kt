package com.mwi.movieapp.data.remote.dto

import com.mwi.movieapp.domain.model.Movie

data class MovieResponse(
    val results: List<Movie>
)