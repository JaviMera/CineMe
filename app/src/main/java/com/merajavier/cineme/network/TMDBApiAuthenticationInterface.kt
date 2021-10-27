package com.merajavier.cineme.network

import com.merajavier.cineme.login.authentication.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TMDBApiAuthenticationInterface {
    @GET("authentication/token/new")
    suspend fun createRequestToken()
    : CreateTokenResponse

    @POST("authentication/token/validate_with_login")
    suspend fun validateTokenWithLogin(
        @Body validateTokenWithLoginRequest: ValidateTokenWithLoginRequest
    )
    : ValidateTokenWithLoginResponse

    @POST("authentication/session/new")
    suspend fun createSession(
        @Body createSessionRequest: CreateSessionRequest
    ) : CreateSessionResponse
}