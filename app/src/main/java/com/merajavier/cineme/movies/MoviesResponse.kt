package com.merajavier.cineme.movies

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviesResponse(
    @Json(name = "page") val pageNumber: Int,
    @Json(name="results") val movies: List<MovieDataItem>,
    @Json(name = "total_pages") val totalPages: Int
) : Parcelable

