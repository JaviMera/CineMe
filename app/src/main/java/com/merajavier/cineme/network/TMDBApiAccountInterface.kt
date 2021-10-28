package com.merajavier.cineme.network

import com.merajavier.cineme.login.account.AccountDetailsResponse
import com.merajavier.cineme.login.account.FavoriteMoviesResponse
import com.merajavier.cineme.login.account.MarkFavoriteRequest
import com.merajavier.cineme.login.account.MarkFavoriteResponse
import com.merajavier.cineme.movies.MoviesResponse
import retrofit2.http.*

interface TMDBApiAccountInterface{
    @GET("account")
    suspend fun getDetails(
        @Query("session_id") sessionId: String
    ) : AccountDetailsResponse

    @POST("account/{account_id}/favorite")
    suspend fun markFavorite(
        @Query("session_id") sessionId: String,
        @Body() markFavoriteRequest: MarkFavoriteRequest
    ) : MarkFavoriteResponse

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String
    ) : FavoriteMoviesResponse
}