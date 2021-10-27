package com.merajavier.cineme.network

import com.merajavier.cineme.login.account.AccountDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiAccountInterface{
    @GET("account")
    suspend fun getDetails(
        @Query("session_id") sessionId: String
    ) : AccountDetailsResponse
}