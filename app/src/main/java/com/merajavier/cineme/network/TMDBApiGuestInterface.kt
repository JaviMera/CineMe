package com.merajavier.cineme.network

import com.merajavier.cineme.login.GuestSessionResponse
import retrofit2.http.GET

interface TMDBApiGuestInterface{
    @GET("authentication/guest_session/new")
    suspend fun getSession() : GuestSessionResponse
}