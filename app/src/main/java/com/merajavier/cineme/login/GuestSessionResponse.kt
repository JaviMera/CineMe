package com.merajavier.cineme.login

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuestSessionResponse (
    val success: Boolean,
    @Json(name = "guest_session_id") val sessionId: String,
    @Json(name = "expires_at") val expirationDate: String
) : Parcelable