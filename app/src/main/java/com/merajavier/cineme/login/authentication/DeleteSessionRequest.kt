package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeleteSessionRequest(
    val session_id: String
) : Parcelable

