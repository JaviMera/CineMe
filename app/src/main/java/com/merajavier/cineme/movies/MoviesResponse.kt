package com.merajavier.cineme.movies

import com.squareup.moshi.Json

data class MoviesResponse(
    @Json(name = "page") val pageNumber: Int,
    @Json(name="results") val movies: List<MovieDataItem>,
    @Json(name = "total_pages") val totalPages: Int
)