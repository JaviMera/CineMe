package com.merajavier.cineme.genre

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreDataItem(
    val id: Int,
    val name: String
) : Parcelable