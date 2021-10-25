package com.merajavier.cineme.network

import com.merajavier.cineme.cast.ActorsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TMDBApiCastInterface {
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movieId: Int
    ) : ActorsResponse
}

