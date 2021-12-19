package com.merajavier.cineme.movies.rate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieRateDataItem(
    val value: Double
) : Parcelable