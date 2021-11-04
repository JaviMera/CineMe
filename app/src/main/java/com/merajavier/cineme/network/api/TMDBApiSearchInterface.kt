package com.merajavier.cineme.network.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiSearchInterface{
    @GET("search/movie")
    fun moviesByTitle(
        @Query("query") query: String,
        @Query("page") pageNumber: Int
    ) : Call<String>
}