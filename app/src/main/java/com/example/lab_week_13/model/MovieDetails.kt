package com.example.lab_week_13.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "poster_path") val posterPath: String?
) {
    val releaseYear: String
        get() = releaseDate?.takeIf { it.length >= 4 }?.substring(0, 4) ?: ""
}