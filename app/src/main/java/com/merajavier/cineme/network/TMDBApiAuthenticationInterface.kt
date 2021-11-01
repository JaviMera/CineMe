package com.merajavier.cineme.network

import com.merajavier.cineme.login.authentication.*
import retrofit2.Call
import retrofit2.http.*

interface TMDBApiAuthenticationInterface {
    @GET("authentication/token/new")
    fun createRequestToken()
    : Call<String>

    @POST("authentication/token/validate_with_login")
    fun validateTokenWithLogin(
        @Body validateTokenWithLoginRequest: ValidateTokenWithLoginRequest
    )
    : Call<String>

    @POST("authentication/session/new")
    fun createSession(
        @Body createSessionRequest: CreateSessionRequest
    ) : Call<String>

    @HTTP( method="DELETE", hasBody = true, path = "authentication/session")
    fun deleteSession(
        @Body deleteSessionRequest: DeleteSessionRequest
    ) : Call<String>
}