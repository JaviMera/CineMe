package com.merajavier.cineme.network

import com.merajavier.cineme.login.authentication.*
import retrofit2.http.*

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

    @HTTP( method="DELETE", hasBody = true, path = "authentication/session")
    suspend fun deleteSession(
        @Body deleteSessionRequest: DeleteSessionRequest
    ) : DeleteSessionResponse
}