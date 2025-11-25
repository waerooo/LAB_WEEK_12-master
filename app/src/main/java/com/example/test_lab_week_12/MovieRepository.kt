package com.example.test_lab_week_12.data

import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.model.MovieDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class MovieRepository(private val movieService: MovieService) {
    private val apiKey = "f9d1d23f2d8379704f5448eb7a5404a2"

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            emit(movieService.getPopularMovies(apiKey).results)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchMovieDetails(movieId: Int): MovieDetails? {
        return try {
            movieService.getMovieDetails(movieId, apiKey)
        } catch (exception: Exception) {
            null
        }
    }
}