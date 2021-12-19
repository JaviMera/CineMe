package com.merajavier.cineme.movies.rate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountStateResultWithoutRate(
    val favorite: Boolean,
    val rated: Boolean
) : Parcelable