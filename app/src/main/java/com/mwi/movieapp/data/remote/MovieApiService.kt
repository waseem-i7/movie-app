package com.mwi.movieapp.data.remote

import com.mwi.movieapp.data.remote.dto.MovieResponse
import com.mwi.movieapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET(Constants.TRENDING_MOVIES_ENDPOINT)
    suspend fun fetchTrendingMovies(@Query("language") language: String = "en-US"): MovieResponse

}