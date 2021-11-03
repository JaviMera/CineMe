package com.merajavier.cineme.data.local

import androidx.paging.PagingSource
import com.merajavier.cineme.genre.GenreDataItem
import com.merajavier.cineme.movies.favorites.FavoriteMovieDataItem
import com.merajavier.cineme.movies.MovieDataItem

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

fun MovieDataItem.toFavoriteMovieEntity() : FavoriteMovieEntity{
    return FavoriteMovieEntity(
        movieId = this.id,
        overview = this.overview,
        title = this.title,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate
    )
}

fun MovieEntity.toMovieDataItem() : MovieDataItem {
    return MovieDataItem(
        id = this.movieId,
        overview = this.overview,
        popularity = this.popularity,
        voteAverage = this.voteAverage,
        backdropPath = this.backdropPath,
        originalTitle = this.originalTitle,
        voteCount = this.voteCount,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        genres = listOf(GenreDataItem(0, ""))
    )
}

fun List<MovieEntity>.toMovieDataItems() : List<MovieDataItem> {
    return this.map { movieEntity -> movieEntity.toMovieDataItem() }
}

fun List<MovieDataItem>.toMovieEntities() : List<MovieEntity>{
    return this.map { movie ->
        MovieEntity(
            movieId = movie.id,
            overview = movie.overview,
            title = movie.title,
            releaseDate = movie.releaseDate,
            posterPath = movie.posterPath!!,
            voteCount = movie.voteCount,
            voteAverage = movie.voteAverage,
            popularity = movie.popularity,
            originalTitle = movie.originalTitle,
            backdropPath = movie.backdropPath!!
        )
    }
}