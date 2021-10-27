package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class CreateSessionRequest(
    @Json(name="request_token") val requestToken: String
):Parcelable
