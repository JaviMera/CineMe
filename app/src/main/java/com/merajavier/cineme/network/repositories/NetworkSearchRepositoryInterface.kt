package com.merajavier.cineme.network.repositories

import com.google.gson.Gson
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MoviesResponse
import com.merajavier.cineme.network.api.TMDBApiSearchInterface
import retrofit2.awaitResponse

interface NetworkSearchRepositoryInterface {
    suspend fun searchMoviesByTitle(title: String, pageNumber: Int) : TMDBApiResult<*>
}

class NetworkSearchRepository(
    private val apiInterface: TMDBApiSearchInterface
) : NetworkSearchRepositoryInterface {

    override suspend fun searchMoviesByTitle(title: String, pageNumber: Int): TMDBApiResult<*> {
        return try{
            val response = apiInterface.moviesByTitle(title, pageNumber).awaitResponse()
            if(response.isSuccessful){
                val actorsResponse = Gson().fromJson(response.body(), MoviesResponse::class.java)
                TMDBApiResult.Success(actorsResponse)
            }
            else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }
}