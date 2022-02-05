package com.merajavier.cineme.movies

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieReleaseDateResultDataItem(
    @SerializedName("iso_3166_1") val country: String,
    @SerializedName("release_dates") val releaseDates: List<MovieReleaseDateDataItem>
) : Parcelable {

}