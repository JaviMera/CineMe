package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateTokenWithLoginResponse(
    val success: Boolean,
    @Json(name="status_code") val statusCode: Int,
    @Json(name="status_message") val statusMessage: Int
) : Parcelable

