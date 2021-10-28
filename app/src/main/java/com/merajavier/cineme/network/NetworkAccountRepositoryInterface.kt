package com.merajavier.cineme.network

import com.merajavier.cineme.login.account.AccountDetailsResponse
import com.merajavier.cineme.login.account.FavoriteMoviesResponse
import com.merajavier.cineme.login.account.MarkFavoriteRequest
import com.merajavier.cineme.login.account.MarkFavoriteResponse
import retrofit2.Retrofit

interface NetworkAccountRepositoryInterface {
    suspend fun getAccountDetails(sessionId: String) : AccountDetailsResponse
    suspend fun markFavorite(sessionId: String, request: MarkFavoriteRequest) : MarkFavoriteResponse
    suspend fun getFavoriteMovies(accountId: Int, sessionId: String) : FavoriteMoviesResponse
}

class NetworkAccountRepository(
    private val apiInterface: TMDBApiAccountInterface
) : NetworkAccountRepositoryInterface {

    override suspend fun getAccountDetails(sessionId: String): AccountDetailsResponse {
        return apiInterface.getDetails(sessionId)
    }

    override suspend fun markFavorite(
        sessionId: String,
        request: MarkFavoriteRequest
    ): MarkFavoriteResponse {

        return apiInterface.markFavorite(sessionId, request)
    }

    override suspend fun getFavoriteMovies(accountId: Int, sessionId: String): FavoriteMoviesResponse {
        return apiInterface.getFavoriteMovies(accountId, sessionId)
    }
}