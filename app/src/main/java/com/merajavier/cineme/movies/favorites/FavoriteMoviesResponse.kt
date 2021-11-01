package com.merajavier.cineme.movies.favorites

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteMoviesResponse(
    @SerializedName( "page") val pageNumber: Int,
    @SerializedName("results") val movies: List<FavoriteMovieDataItem>,
    @SerializedName( "total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
) : Parcelable