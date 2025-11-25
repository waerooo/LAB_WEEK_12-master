package com.example.test_lab_week_12.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.test_lab_week_12.model.PopularMoviesResponse
import com.example.test_lab_week_12.model.MovieDetails
import retrofit2.http.Path

interface MovieService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): PopularMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetails
}