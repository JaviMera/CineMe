package com.merajavier.cineme.network

import com.merajavier.cineme.login.account.AccountDetailsResponse
import retrofit2.Retrofit

interface NetworkAccountRepositoryInterface {
    suspend fun getAccountDetails(sessionId: String) : AccountDetailsResponse
}

class NetworkAccountRepository(
    private val apiInterface: TMDBApiAccountInterface
) : NetworkAccountRepositoryInterface {

    override suspend fun getAccountDetails(sessionId: String): AccountDetailsResponse {
        return apiInterface.getDetails(sessionId)
    }
}