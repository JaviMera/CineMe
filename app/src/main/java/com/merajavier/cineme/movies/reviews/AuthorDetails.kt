package com.merajavier.cineme.movies.reviews

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorDetails(
    val name: String?,
    val username: String?,
    val rating: Double?
) : Parcelable