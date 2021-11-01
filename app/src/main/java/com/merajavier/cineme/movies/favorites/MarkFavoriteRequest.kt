package com.merajavier.cineme.movies.favorites

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarkFavoriteRequest(
    val media_type: String,
    val media_id: Int,
    val favorite: Boolean
) : Parcelable

