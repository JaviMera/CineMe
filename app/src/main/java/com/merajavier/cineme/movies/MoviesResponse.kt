package com.merajavier.cineme.movies

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviesResponse(
    @SerializedName("page") val pageNumber: Int,
    @SerializedName("results") val movies: List<MovieDataItem>,
    @SerializedName("total_pages") val totalPages: Int
) : Parcelable

