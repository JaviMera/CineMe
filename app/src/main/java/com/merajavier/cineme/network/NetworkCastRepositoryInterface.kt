package com.merajavier.cineme.network

import com.google.gson.Gson
import com.merajavier.cineme.cast.ActorsResponse
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import retrofit2.awaitResponse

interface NetworkCastRepositoryInterface {
    suspend fun getActors(movieId: Int) : TMDBApiResult<*>
    suspend fun getCrew(movieId: Int) : TMDBApiResult<*>
}

class NetworkMovieActorRepository(
    private val apiInterface: TMDBApiCastInterface
) : NetworkCastRepositoryInterface{

    override suspend fun getActors(movieId: Int): TMDBApiResult<*> {

        return try{
            val response = apiInterface.getMovieCast(movieId).awaitResponse()
            if(response.isSuccessful){
                val actorsResponse = Gson().fromJson(response.body(), ActorsResponse::class.java)
                TMDBApiResult.Success(actorsResponse.cast.sortedBy { cast -> cast.order })
            }
            else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun getCrew(movieId: Int): TMDBApiResult<*> {
        return try{
            val response = apiInterface.getMovieCast(movieId).awaitResponse()
            if(response.isSuccessful){
                val actorsResponse = Gson().fromJson(response.body(), ActorsResponse::class.java)
                TMDBApiResult.Success(actorsResponse.crew)
            }
            else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }
}

