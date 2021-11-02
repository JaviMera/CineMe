package com.merajavier.cineme.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApMoviesiInterface {
    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Int
    ) : Call<String>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("page") pageNumber: Int
    ) : Call<String>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("page") pageNumber: Int
    ) : Call<String>
}

