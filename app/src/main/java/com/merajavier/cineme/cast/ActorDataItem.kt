package com.merajavier.cineme.cast

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorDataItem(
    val id: Int,
    val name: String?,
    val character: String?,
    @SerializedName("profile_path") var photo: String?,
    val order: Int
) : Parcelable

