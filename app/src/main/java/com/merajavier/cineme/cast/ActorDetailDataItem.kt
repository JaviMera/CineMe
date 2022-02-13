package com.merajavier.cineme.cast

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorDetailDataItem(
    val biography: String,
    val birthday: String,
    val deathDay: String,
    val gender: Int,
    val name: String,
    @SerializedName("place_of_birth") val placeOfBirth: String,
    val popularity: Double,
    val images: ActorProfileImagesResponse
) : Parcelable

