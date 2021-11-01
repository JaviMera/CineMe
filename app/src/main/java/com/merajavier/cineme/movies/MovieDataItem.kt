package com.merajavier.cineme.movies

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.merajavier.cineme.genre.GenreDataItem
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDataItem(
    val id: Int,
    @SerializedName( "original_title") val originalTitle: String,
    val title: String,
    val overview:String,
    val popularity: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val genres: List<GenreDataItem>?,
    @SerializedName("vote_count") val voteCount: Int
) : Parcelable