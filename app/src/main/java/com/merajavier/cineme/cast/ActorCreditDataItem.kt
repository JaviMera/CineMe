package com.merajavier.cineme.cast

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorCreditDataItem(
    val id: Int,
    @SerializedName("poster_path") val posterPath: String?,
    val title: String,
    val order: Int,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String?,
    val character: String?
) : Parcelable