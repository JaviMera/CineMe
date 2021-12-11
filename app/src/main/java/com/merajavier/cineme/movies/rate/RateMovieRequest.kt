package com.merajavier.cineme.movies.rate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RateMovieRequest(
    val value: Float
) : Parcelable