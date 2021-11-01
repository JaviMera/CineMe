package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class CreateSessionRequest(
    val request_token: String
):Parcelable
