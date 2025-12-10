package com.example.lab_week_13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_week_13.viewmodel.MovieViewModel
import com.example.lab_week_13.model.Movie
import androidx.databinding.DataBindingUtil
import com.example.lab_week_13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val recyclerView: RecyclerView = binding.movieList

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

        binding.viewModel = movieViewModel

        binding.lifecycleOwner = this


    }
}