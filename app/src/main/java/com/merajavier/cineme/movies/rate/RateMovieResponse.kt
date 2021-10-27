package com.merajavier.cineme.movies.rate

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class RateMovieResponse(
    val success: Boolean,
    @Json(name="status_code") val statusCode: Int,
    @Json(name="status_message") val statusMessage: String
) : Parcelable