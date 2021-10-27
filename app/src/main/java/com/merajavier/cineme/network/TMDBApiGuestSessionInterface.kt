package com.merajavier.cineme.network

import com.merajavier.cineme.account.guest.GuestRatedMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TMDBApiGuestSessionInterface{
    @GET("guest_session/{session_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("session_id") sessionId: String
    ) : GuestRatedMoviesResponse
}