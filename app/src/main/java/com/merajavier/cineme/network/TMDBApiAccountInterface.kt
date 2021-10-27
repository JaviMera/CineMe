package com.merajavier.cineme.network

import com.merajavier.cineme.login.account.AccountDetailsResponse
import com.merajavier.cineme.login.account.MarkFavoriteRequest
import com.merajavier.cineme.login.account.MarkFavoriteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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
}