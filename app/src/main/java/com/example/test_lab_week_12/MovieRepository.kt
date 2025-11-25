package com.example.test_lab_week_12.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.model.Movie
import java.lang.Exception
import com.example.test_lab_week_12.model.MovieDetails

class   MovieRepository(private val movieService: MovieService) {
    private val apiKey = "f9d1d23f2d8379704f5448eb7a5404a2"

    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    private val errorLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    suspend fun fetchMovies() {
        try {
            val popularMovies = movieService.getPopularMovies(apiKey)
            movieLiveData.postValue(popularMovies.results)
        } catch (exception: Exception) {
            errorLiveData.postValue("An error occurred: ${exception.message}")
        }
    }

    suspend fun fetchMovieDetails(movieId: Int): MovieDetails? {
        return try {
            // Use the API KEY already defined in the repository
            movieService.getMovieDetails(movieId, apiKey)
        } catch (exception: Exception) {
            errorLiveData.postValue("Error fetching details: ${exception.message}")
            null
        }
    }
}