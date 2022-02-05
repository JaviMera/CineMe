package com.merajavier.cineme.movies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieReleaseDates(
    val results: List<MovieReleaseDateResultDataItem>
) : Parcelable {
}