package com.merajavier.cineme.movies

import com.squareup.moshi.Json

data class MovieDataItem(
    @Json(name = "original_title") val originalTitle: String,
    val title: String,
    val overview:String,
    val popularity: Double
)