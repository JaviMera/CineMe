package com.merajavier.cineme.network

import com.merajavier.cineme.cast.ActorDataItem
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApMoviesiInterface {
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int
    ) : MovieDataItem

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") pageNumber: Int
    ) : MoviesResponse

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("page") pageNumber: Int
    ) : Call<String>
}