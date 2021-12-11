package com.merajavier.cineme.network.api

import com.merajavier.cineme.movies.rate.RateMovieRequest
import retrofit2.Call
import retrofit2.http.*

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

    @GET("movie/{movie_id}/account_states")
    fun getAccountState(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String
    ) : Call<String>

    @GET("movie/{movie_id}/reviews")
    fun getReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") pageNumber: Int
    ) : Call<String>

    @POST("movie/{movie_id}/rating")
    fun rate(
        @Path("movie_id") movieId: Int,
        @Body() rateMovieRequest: RateMovieRequest,
        @Query("session_id") guestSessionId: String,
    ) : Call<String>
}

