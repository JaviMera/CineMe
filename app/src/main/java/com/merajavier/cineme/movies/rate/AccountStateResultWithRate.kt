package com.merajavier.cineme.movies.rate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountStateResultWithRate(
    val favorite: Boolean,
    val rated: MovieRateDataItem
) : Parcelable