package com.merajavier.cineme.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id") val id: Int,
    @ColumnInfo(name="username") var username: String?,
    @ColumnInfo(name="movie_id") var movieId: Int?,
    @ColumnInfo(name="title") var title: String?,
    @ColumnInfo(name="overview") var overview: String?,
    @ColumnInfo(name="poster_path") var posterPath: String?,
    @ColumnInfo(name="release_date") var releaseDate: String?
)