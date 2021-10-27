package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateTokenResponse (
    val success: Boolean,
    @Json(name="expires_at") val expireDate: String,
    @Json(name="request_token") val requestToken: String
) : Parcelable

