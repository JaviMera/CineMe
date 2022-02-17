package com.merajavier.cineme.network.api

import com.merajavier.cineme.cast.ActorsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiCastInterface {
    @GET("movie/{movie_id}/credits")
    fun getMovieCast(
        @Path("movie_id") movieId: Int
    ) : Call<String>

    @GET("person/{person_id}")
    fun getActorDetails(
        @Path("person_id") actorId: Int,
        @Query("append_to_response") appendToResponse: String
    ) : Call<String>

    @GET("person/{person_id}/tagged_images")
    fun getActorTaggedImages(
        @Path("person_id") actorId: Int,
        @Query("page") pageNumber: Int
    ) : Call<String>
}

