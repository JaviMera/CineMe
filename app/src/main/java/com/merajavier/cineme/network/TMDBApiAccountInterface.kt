package com.merajavier.cineme.network

import com.merajavier.cineme.login.account.AccountDetailsResponse
import com.merajavier.cineme.movies.favorites.FavoriteMoviesResponse
import com.merajavier.cineme.movies.favorites.MarkFavoriteRequest
import com.merajavier.cineme.movies.favorites.MarkFavoriteResponse
import retrofit2.Call
import retrofit2.http.*

interface TMDBApiAccountInterface{
    @GET("account")
    fun getDetails(
        @Query("session_id") sessionId: String
    ) : Call<String>

    @POST("account/{account_id}/favorite")
    fun markFavorite(
        @Query("session_id") sessionId: String,
        @Body() markFavoriteRequest: MarkFavoriteRequest
    ) : Call<String>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("page") pageNumber: Int
    ) : Call<String>
}