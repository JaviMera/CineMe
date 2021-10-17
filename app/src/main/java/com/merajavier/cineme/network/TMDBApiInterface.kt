package com.merajavier.cineme.network

import com.merajavier.cineme.movies.MovieDataItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiInterface {
    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ) : Call<MovieDataItem>
}