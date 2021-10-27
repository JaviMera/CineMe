package com.merajavier.cineme.network

import com.merajavier.cineme.cast.ActorDataItem
import com.merajavier.cineme.genre.RateMovieRequest
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesResponse
import com.merajavier.cineme.movies.rate.RateMovieResponse
import retrofit2.http.*

interface TMDBApiInterface {
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int
    ) : MovieDataItem

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") pageNumber: Int
    ) : MoviesResponse

    @POST("movie/{movie_id}/rating")
    suspend fun rateMovieAsGuest(
        @Path("movie_id") movieId: Int,
        @Query("guest_session_id") guestSessionId: String,
        @Body rateMovieRequest: RateMovieRequest
    ) : RateMovieResponse
}