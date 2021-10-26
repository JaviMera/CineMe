package com.merajavier.cineme.network

import com.merajavier.cineme.login.GuestSessionResponse
import retrofit2.http.DELETE
import retrofit2.http.GET

interface TMDBApiLoginInterface{
    @GET("authentication/guest_session/new")
    suspend fun getGuestSession() : GuestSessionResponse
}