package com.merajavier.cineme.login.account

import android.os.Parcelable
import com.merajavier.cineme.movies.MovieDataItem
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteMoviesResponse(
    @Json(name = "page") val pageNumber: Int,
    @Json(name="results") val movies: List<MovieDataItem>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name="total_results") val totalResults: Int
) : Parcelable