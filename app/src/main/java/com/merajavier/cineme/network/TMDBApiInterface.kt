package com.merajavier.cineme.network

import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiInterface {
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int
    ) : MovieDataItem

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") pageNumber: Int
    ) : MoviesResponse
}