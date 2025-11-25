package com.example.test_lab_week_12

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.test_lab_week_12.model.MovieDetails
import com.example.test_lab_week_12.viewmodel.DetailsViewModel
import com.example.test_lab_week_12.viewmodel.DetailsViewModel.Companion.Factory

class DetailsActivity : AppCompatActivity() {

    // Corrected IDs based on your XML (movie_poster, title_text, release_text, overview_text)
    private val posterImageView: ImageView by lazy { findViewById(R.id.movie_poster) }
    private val titleTextView: TextView by lazy { findViewById(R.id.title_text) }
    private val releaseTextView: TextView by lazy { findViewById(R.id.release_text) }
    private val overviewTextView: TextView by lazy { findViewById(R.id.overview_text) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // 1. FIX: Enable Back Button (Up Navigation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "LAB WEEK 12"

        // 2. Get Movie ID
        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId == -1) return

        // 3. Setup ViewModel
        val movieRepository = (application as MovieApplication).movieRepository
        val viewModel = ViewModelProvider(
            this,
            DetailsViewModel.Factory(movieRepository)
        )[DetailsViewModel::class.java]

        // 4. Observe Details and Update UI
        viewModel.movieDetails.observe(this) { details ->
            if (details != null) {
                bindMovieDetails(details)
            }
        }

        // 5. Fetch Data
        viewModel.fetchDetails(movieId)
    }

    // 6. FIX: Handle Back Button Click
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun bindMovieDetails(details: MovieDetails) {
        // Update Title
        titleTextView.text = details.title

        // Update Release Year
        releaseTextView.text = details.releaseYear

        // Update Overview
        overviewTextView.text = "Overview: ${details.overview ?: "No overview available."}"

        // 7. FIX: Load Poster Image using Glide
        val imageUrl = "https://image.tmdb.org/t/p/w185/"
        if (!details.posterPath.isNullOrEmpty()) {
            Glide.with(this)
                .load("$imageUrl${details.posterPath}")
                .placeholder(R.mipmap.ic_launcher)
                .into(posterImageView)
        }
    }
}