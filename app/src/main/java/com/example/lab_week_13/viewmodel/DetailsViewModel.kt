package com.example.lab_week_13.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lab_week_13.data.MovieRepository
import com.example.lab_week_13.model.MovieDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DetailsViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails>
        get() = _movieDetails

    fun fetchDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val details = movieRepository.fetchMovieDetails(movieId)
            details?.let {
                _movieDetails.postValue(it)
            }
        }
    }

    companion object {
        fun Factory(repository: MovieRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return DetailsViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}