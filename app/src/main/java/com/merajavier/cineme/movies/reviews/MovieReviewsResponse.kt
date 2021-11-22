package com.merajavier.cineme.movies.reviews

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieReviewsResponse(
    val id: Int,
    val page: Int,
    val results: List<ReviewDataItem>?,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
) : Parcelable