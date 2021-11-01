package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateTokenWithLoginResponse(
    val success: Boolean,
    @SerializedName("expires_at") val expirationDate: String,
    @SerializedName("request_token") val requestToken: String
) : Parcelable

