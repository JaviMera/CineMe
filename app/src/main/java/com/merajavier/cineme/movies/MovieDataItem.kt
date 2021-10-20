package com.merajavier.cineme.movies

import com.squareup.moshi.Json

data class MovieDataItem(
    val id: Int,
    @Json(name = "original_title") val originalTitle: String,
    val title: String,
    val overview:String,
    val popularity: Double,
    @Json(name="release_date") val releaseDate: String,
    @Json(name="vote_average") val voteAverage: Double,
    @Json(name="poster_path") val posterPath: String
)