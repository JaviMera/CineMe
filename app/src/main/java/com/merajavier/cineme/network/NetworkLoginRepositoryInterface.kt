package com.merajavier.cineme.network

import com.merajavier.cineme.login.GuestSessionResponse

interface NetworkLoginRepositoryInterface {

    suspend fun getGuestSession() : GuestSessionResponse
}

class NetworkGuestSessionRepository(
    private val apiInterface : TMDBApiGuestInterface
) : NetworkLoginRepositoryInterface {

    override suspend fun getGuestSession(): GuestSessionResponse {
        return GuestSessionResponse(
            true,
            "asdasdasd",
            expirationDate = "2021-10-25"
        )
    }
}