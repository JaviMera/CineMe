package com.merajavier.cineme.genre

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RateMovieRequest(
    val value: Double
) : Parcelable