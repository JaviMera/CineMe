package com.merajavier.cineme.network

import com.merajavier.cineme.login.authentication.*

interface NetworkAuthenticationRepositoryInterface {
    suspend fun createToken() : CreateTokenResponse
    suspend fun validateToken(request: ValidateTokenWithLoginRequest) : ValidateTokenWithLoginResponse
    suspend fun createSession(request: CreateSessionRequest) : CreateSessionResponse
    suspend fun deleteSession(request: DeleteSessionRequest) : DeleteSessionResponse
}

class NetworkAuthenticationRepository(
    private val apiInterface: TMDBApiAuthenticationInterface
) : NetworkAuthenticationRepositoryInterface{

    override suspend fun createToken(): CreateTokenResponse {
        return apiInterface.createRequestToken()
    }

    override suspend fun validateToken(request: ValidateTokenWithLoginRequest): ValidateTokenWithLoginResponse {
        return apiInterface.validateTokenWithLogin(request)
    }

    override suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse {
        return apiInterface.createSession(request)
    }

    override suspend fun deleteSession(request: DeleteSessionRequest): DeleteSessionResponse {
        return apiInterface.deleteSession(request)
    }
}

