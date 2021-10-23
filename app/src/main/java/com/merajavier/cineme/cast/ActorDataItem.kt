package com.merajavier.cineme.cast

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorDataItem(
    val id: Int,
    val name: String?,
    val character: String?,
    @Json(name="profile_path") val photo: String?
) : Parcelable