package com.merajavier.cineme.network

import com.merajavier.cineme.account.guest.GuestRatedMoviesResponse

interface NetworkGuestRepositoryInterface {
    suspend fun getRatedMovies(sessionId: String) : GuestRatedMoviesResponse
}

class NetworkGuestRepository(
    private val apiInterface: TMDBApiGuestSessionInterface
) : NetworkGuestRepositoryInterface{

    override suspend fun getRatedMovies(sessionId: String) : GuestRatedMoviesResponse {
        return apiInterface.getRatedMovies(sessionId)
    }
}