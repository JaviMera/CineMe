package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeleteSessionRequest(
    @Json(name="session_id") val session_id: String
) : Parcelable

