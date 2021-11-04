package com.merajavier.cineme.movies.favorites

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteMovieDataItem(
    val id: Int,
    val title: String,
    val overview:String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_path") val posterPath: String?
) : Parcelable