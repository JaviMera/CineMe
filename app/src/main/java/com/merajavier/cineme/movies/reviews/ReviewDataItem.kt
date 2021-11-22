package com.merajavier.cineme.movies.reviews

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewDataItem(
    val id: String,
    val author: String?,
    val content: String?,
    @SerializedName("author_details") val authorDetails: AuthorDetails?
) : Parcelable

