package com.example.lab_week_13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_week_13.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import com.example.lab_week_13.model.Movie
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.movie_list)

        movieAdapter = MovieAdapter(
            object : MovieAdapter.MovieClickListener {
                override fun onMovieClick(movie: Movie) {
                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    intent.putExtra("MOVIE_ID", movie.id)
                    startActivity(intent)
                }
            }
        )
        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel = ViewModelProvider(
            this, object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return MovieViewModel(movieRepository) as T
                }
            })[MovieViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    movieViewModel.popularMovies.collect { popularMovies ->
                        movieAdapter.addMovies(popularMovies)
                    }
                }
                launch {
                    movieViewModel.error.collect { error ->
                        if (error.isNotEmpty()) Snackbar
                            .make(
                                recyclerView, error, Snackbar.LENGTH_LONG
                            ).show()
                    }
                }
            }
        }
    }
}