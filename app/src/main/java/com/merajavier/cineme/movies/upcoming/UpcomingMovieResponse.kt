package com.merajavier.cineme.movies.upcoming

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.merajavier.cineme.movies.MovieDataItem
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpcomingMovieResponse(
    @SerializedName("page") val pageNumber: Int,
    @SerializedName("results") val movies: List<MovieDataItem>,
    @SerializedName("total_pages") val totalPages: Int
) : Parcelable

