package com.merajavier.cineme.account.guest

import android.os.Parcelable
import com.merajavier.cineme.movies.MovieDataItem
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GuestRatedMoviesResponse(
    @Json(name="page") val pageNumber: Int,
    @Json(name="results") val ratedMovies: List<GuestRatedMovieDataItem>,
    @Json(name="total_pages") val totalPages: Int,
    @Json(name="total_results") val totalResults: Int
) : Parcelable