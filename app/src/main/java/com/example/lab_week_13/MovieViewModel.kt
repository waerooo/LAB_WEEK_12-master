package com.example.lab_week_13.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_week_13.data.MovieRepository
import com.example.lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _popularMovies = MutableStateFlow(
        emptyList<Movie>()
    )
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        // Get the current year once
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                .map { movieList ->
                    // 1. Filter movies released in the current year
                    movieList.filter { movie ->
                        movie.releaseDate?.startsWith(currentYear) == true
                    }
                }
                .map { filteredList ->
                    // 2. Sort the filtered list by popularity in descending order
                    filteredList.sortedByDescending { it.popularity }
                }
                .catch {
                    _error.value = "An exception occurred: ${it.message}"
                }
                .collect {
                    // 3. Emit the final, processed list to the StateFlow
                    _popularMovies.value = it
                }
        }
    }
}