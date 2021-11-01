package com.merajavier.cineme.network

import com.google.gson.Gson
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.login.authentication.*
import retrofit2.awaitResponse

interface NetworkAuthenticationRepositoryInterface {
    suspend fun createToken() : TMDBApiResult<*>
    suspend fun validateToken(request: ValidateTokenWithLoginRequest) : TMDBApiResult<*>
    suspend fun createSession(request: CreateSessionRequest) : TMDBApiResult<*>
    suspend fun deleteSession(request: DeleteSessionRequest) : TMDBApiResult<*>
}

class NetworkAuthenticationRepository(
    private val apiInterface: TMDBApiAuthenticationInterface
) : NetworkAuthenticationRepositoryInterface{

    override suspend fun createToken(): TMDBApiResult<*> {
        return try{
            val response = apiInterface.createRequestToken().awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), CreateTokenResponse::class.java))
            }
            else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun validateToken(request: ValidateTokenWithLoginRequest): TMDBApiResult<*> {
        return try{
            val response = apiInterface.validateTokenWithLogin(request).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), ValidateTokenWithLoginResponse::class.java))
            }
            else{
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                TMDBApiResult.Failure(errorResponse)
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun createSession(request: CreateSessionRequest): TMDBApiResult<*> {
        return try{
            val response = apiInterface.createSession(request).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), CreateSessionResponse::class.java))
            }
            else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun deleteSession(request: DeleteSessionRequest): TMDBApiResult<*> {
        return try{
            val response = apiInterface.deleteSession(request).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), DeleteSessionResponse::class.java))
            }
            else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }
}

