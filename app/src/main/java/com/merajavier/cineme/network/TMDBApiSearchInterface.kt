package com.merajavier.cineme.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiSearchInterface{
    @GET("search/movie")
    fun moviesByTitle(
        @Query("query") query: String
    ) : Call<String>
}