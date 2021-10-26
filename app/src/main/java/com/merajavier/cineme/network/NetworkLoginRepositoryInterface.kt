package com.merajavier.cineme.network

import com.merajavier.cineme.login.GuestSessionResponse

interface NetworkLoginRepositoryInterface {

    suspend fun getGuestSession() : GuestSessionResponse
}

class NetworkLoginRepository(
    private val apiInterface : TMDBApiLoginInterface
) : NetworkLoginRepositoryInterface {

    override suspend fun getGuestSession(): GuestSessionResponse {
        return apiInterface.getGuestSession()
    }
}