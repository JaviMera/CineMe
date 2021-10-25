package com.merajavier.cineme.cast

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrewDataItem(
    val id: Int,
    val job: String?,
    val department: String?,
    @Json(name="original_name") val name: String?
) : Parcelable