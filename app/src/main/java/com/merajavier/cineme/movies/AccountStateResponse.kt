package com.merajavier.cineme.movies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountStateResponse(
    val favorite: Boolean
) : Parcelable