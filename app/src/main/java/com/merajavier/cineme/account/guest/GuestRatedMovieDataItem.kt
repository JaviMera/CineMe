package com.merajavier.cineme.account.guest

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuestRatedMovieDataItem (
    val id: Int,
    val rating: Double
) : Parcelable