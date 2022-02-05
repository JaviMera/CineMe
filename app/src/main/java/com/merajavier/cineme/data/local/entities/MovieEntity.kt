package com.merajavier.cineme.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "now_playing_movies")
data class MovieEntity(
    @PrimaryKey @ColumnInfo(name="movie_id") var movieId: Int,
    @ColumnInfo(name="title") var title: String,
    @ColumnInfo(name="overview") var overview: String,
    @ColumnInfo(name="poster_path") var posterPath: String,
    @ColumnInfo(name="release_date") var releaseDate: String,
    @ColumnInfo(name="original_title") var originalTitle: String,
    @ColumnInfo(name="popularity") var popularity: Double,
    @ColumnInfo(name="vote_count") var voteCount: Int,
    @ColumnInfo(name="backdrop_path") var backdropPath: String,
    @ColumnInfo(name="vote_average") var voteAverage: Double,
    @ColumnInfo(name="runtime") var runtime: Int
) {
}

