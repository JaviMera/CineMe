package com.merajavier.cineme.network

import com.google.gson.Gson
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.login.account.AccountDetailsResponse
import com.merajavier.cineme.movies.favorites.FavoriteMoviesResponse
import com.merajavier.cineme.movies.favorites.MarkFavoriteRequest
import com.merajavier.cineme.movies.favorites.MarkFavoriteResponse
import retrofit2.awaitResponse

interface NetworkAccountRepositoryInterface {
    suspend fun getAccountDetails(sessionId: String) : TMDBApiResult<*>
    suspend fun markFavorite(sessionId: String, request: MarkFavoriteRequest) : TMDBApiResult<*>
    suspend fun getFavoriteMovies(accountId: Int, sessionId: String) : TMDBApiResult<*>
}

class NetworkAccountRepository(
    private val apiInterface: TMDBApiAccountInterface
) : NetworkAccountRepositoryInterface {

    override suspend fun getAccountDetails(sessionId: String): TMDBApiResult<*> {

        return try{
            val response = apiInterface.getDetails(sessionId).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), AccountDetailsResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun markFavorite(
        sessionId: String,
        request: MarkFavoriteRequest
    ): TMDBApiResult<*> {

        return try{
            val response = apiInterface.markFavorite(sessionId, request).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), MarkFavoriteResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun getFavoriteMovies(accountId: Int, sessionId: String): TMDBApiResult<*> {
        return try{
            val response = apiInterface.getFavoriteMovies(accountId, sessionId).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), FavoriteMoviesResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }
}