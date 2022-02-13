package com.merajavier.cineme.network.repositories

import com.google.gson.Gson
import com.merajavier.cineme.cast.ActorDetailDataItem
import com.merajavier.cineme.cast.ActorsResponse
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.network.api.TMDBApiCastInterface
import retrofit2.awaitResponse

interface NetworkCastRepositoryInterface {
    suspend fun getActors(movieId: Int) : TMDBApiResult<*>
    suspend fun getCrew(movieId: Int) : TMDBApiResult<*>
    suspend fun getActorDetails(actorId: Int, appendToResponse: List<String>) : TMDBApiResult<*>
}

class NetworkMovieActorRepository(
    private val apiInterface: TMDBApiCastInterface
) : NetworkCastRepositoryInterface {

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

    override suspend fun getActorDetails(actorId: Int, appendToResponse: List<String>): TMDBApiResult<*> {
        return try{
            val response = apiInterface.getActorDetails(actorId, appendToResponse.joinToString()).awaitResponse()
            if(response.isSuccessful){
                val actorResponse = Gson().fromJson(response.body(), ActorDetailDataItem::class.java)
                TMDBApiResult.Success(actorResponse)
            }
            else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }
}

