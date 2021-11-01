package com.merajavier.cineme.network

import com.google.gson.Gson
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.upcoming.UpcomingMovieDataItem
import com.merajavier.cineme.movies.upcoming.UpcomingMovieResponse
import retrofit2.*

interface NetworkMoviesRepositoryInterface {

    suspend fun getNowPlaying(pageNumber: Int) : TMDBApiResult<*>
    suspend fun getDetails(movieId: Int) : TMDBApiResult<*>
    suspend fun  getUpcoming(pageNumber: Int) : TMDBApiResult<*>
}

class NetworkMoviesRepository(
    private val apiMoviesiInterface: TMDBApMoviesiInterface
    ) : NetworkMoviesRepositoryInterface {

    override suspend fun getNowPlaying(pageNumber: Int): TMDBApiResult<*> {
        return try {
            val response = apiMoviesiInterface.getNowPlayingMovies(pageNumber).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), UpcomingMovieResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun getDetails(movieId: Int): TMDBApiResult<*> {
        return try {
            val response = apiMoviesiInterface.getMovie(movieId).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), UpcomingMovieDataItem::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun getUpcoming(pageNumber: Int): TMDBApiResult<*> {
        return try {
            val response = apiMoviesiInterface.getUpcomingMovies(pageNumber).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), UpcomingMovieResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }
}

