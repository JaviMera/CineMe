package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class CreateSessionResponse(
    val success: Boolean,
    @Json(name="session_id") val sessionId: String
) : Parcelable
