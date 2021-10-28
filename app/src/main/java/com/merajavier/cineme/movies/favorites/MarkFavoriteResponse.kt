package com.merajavier.cineme.movies.favorites

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarkFavoriteResponse(
    val success: Boolean,
    @Json(name="status_code") val statusCode: Int,
    @Json(name="status_message") val statusMessage: String
): Parcelable