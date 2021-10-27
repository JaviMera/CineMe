package com.merajavier.cineme.login.account

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class AccountDetailsResponse(
    val id: Int,
    val username: String
) : Parcelable

@Parcelize
class TMDBResponseError(

    val success: Boolean,
    @Json(name="status_code") val statusCode: Int,
    @Json(name="status_message") val statusMessage: String
) : Parcelable