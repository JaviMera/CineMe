package com.merajavier.cineme.movies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieReleaseDateDataItem(
    val certification: String
): Parcelable {

}