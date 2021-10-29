package com.merajavier.cineme.movies.favorites

import android.os.Parcelable
import com.merajavier.cineme.data.local.FavoriteMovieEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteMovieDataItem(
    val id: Int,
    val title: String,
    val overview:String,
    @Json(name="release_date") val releaseDate: String,
    @Json(name="poster_path") val posterPath: String?
) : Parcelable