package com.merajavier.cineme.data.local

import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.favorites.FavoriteMovieDataItem
import com.merajavier.cineme.movies.upcoming.UpcomingMovieDataItem

fun FavoriteMovieDataItem.toFavoriteMovieEntity() : FavoriteMovieEntity{
    return FavoriteMovieEntity(
        movieId = this.id,
        overview = this.overview,
        title = this.title,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate
    )
}

fun List<FavoriteMovieDataItem>.toFavoriteMovieEnties() : List<FavoriteMovieEntity>{

    return this.map { movie ->
        movie.toFavoriteMovieEntity()
    }
}

fun UpcomingMovieDataItem.toFavoriteMovieEntity() : FavoriteMovieEntity{
    return FavoriteMovieEntity(
        movieId = this.id,
        overview = this.overview,
        title = this.title,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate
    )
}