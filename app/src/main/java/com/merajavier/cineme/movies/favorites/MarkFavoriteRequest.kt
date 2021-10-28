package com.merajavier.cineme.movies.favorites

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarkFavoriteRequest(
    @Json(name="media_type") val mediaType: String,
    @Json(name="media_id") val mediaId: Int,
    val favorite: Boolean
) : Parcelable

