package com.merajavier.cineme.movies

import android.os.Parcelable
import com.merajavier.cineme.genre.GenreDataItem
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDataItem(
    val id: Int,
    @Json(name = "original_title") val originalTitle: String,
    val title: String,
    val overview:String,
    val popularity: Double,
    @Json(name="release_date") val releaseDate: String,
    @Json(name="vote_average") val voteAverage: Double,
    @Json(name="poster_path") val posterPath: String?,
    @Json(name="backdrop_path") val backdropPath: String?,
    val genres: List<GenreDataItem>?,
    @Json(name="vote_count") val voteCount: Int
) : Parcelable